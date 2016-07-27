package com.example.android.shopping.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping.Adapter.UserAdapter;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.user;
import com.example.android.shopping.R;
import com.example.android.shopping.interfaces.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//using interface to implement onlongpress and onclick with position
public class ManageUser extends AppCompatActivity  implements RecyclerItemClickListener.OnItemClickListener {

    final int GET_ALL_USER=0;
    final int SET_ADMIN=1;
    final int SEARCH_PHRASE=2;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    public int UserId;
    public  TextView Error;
    public int admin;
    public String PhraseToSearch;

    List<user> listOfUser=new ArrayList<>();//==> get all users and send to recyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
    //Initialize variable
        Error=(TextView)findViewById(R.id.txtError);
        progressBar=(ProgressBar)findViewById(R.id.UserProgress);
        recyclerView=(RecyclerView)findViewById(R.id.recviewUser);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,this));

        DoAdmin(GET_ALL_USER);
//impletemt onpress key for EditText to execute search in another ways
        ((EditText)findViewById(R.id.email)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (!event.isShiftPressed()) {
                                // the user is done typing.
                                PhraseToSearch=UIHelper.etgetText(v,R.id.email);
                                DoAdmin(SEARCH_PHRASE);
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                });
    }


    @Override
    public void onItemClick(View childView, int position) {

    }
   //Set admin
    @Override
    public void onItemLongPress(final View childView, final int position) {
        user us=listOfUser.get(position);
        UserId=us.getUsId();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(" بعنوان مدیر انتخاب شود "+us.getEmail());
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        admin=1;
                        DoAdmin(SET_ADMIN);
                        dialog.cancel();

                    }
                });

        builder1.setNegativeButton(
                "مخالف",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        admin=0;
                        DoAdmin(SET_ADMIN);
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();

        alert11.show();


    }
//get 3 Aurgment which pass to network task and help us to detect Search phrase, Get all user or Set Admin
    public void DoAdmin(int task)
    {
        if(!UIHelper.isNetworkAvailable(this)) {

            Toast.makeText(this,"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
            return;
        }
        SelectAllUserNetworkTask GetAllUser=new SelectAllUserNetworkTask(this,task);
        GetAllUser.execute();
    }

    public class SelectAllUserNetworkTask extends AsyncTask <String,Void,String>{

        int myTask;
        Context myContext;

        List<user> userarray =new ArrayList<>();
        private OkHttpClient client=new OkHttpClient();
        public  String Result;
        public SelectAllUserNetworkTask(Context context,int myTask)
        {
            this.myTask=myTask;
            this.myContext=context;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                RequestBody requestBody=null;
                if(myTask==0) {//send all user
                    requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action", "select")

                            .build();
                } else if(myTask==1)//change to admin
                {
                    requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action", "setAdmin")
                            .addFormDataPart("userid", String.valueOf(UserId))
                            .addFormDataPart("admin", String.valueOf(admin))
                            .build();
                }else if(myTask==2) //get search Result
                {
                    Log.d("value of Phrase",PhraseToSearch);
                    requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action", "SearchOne")
                            .addFormDataPart("KeySearch", PhraseToSearch)
                            .build();
                }

                Request request = new Request.Builder()
                        .url(Tags.UserAddress)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                Result=response.body().string().toString();
                String str=Result.substring(Result.indexOf("{"), Result.lastIndexOf("}")+1);
                JSONObject jsonObject=new JSONObject(str);


                if(str != null && jsonObject.has("status")) {
                    if(jsonObject.getBoolean("status")==true) {
                        return "Error";
                    }}

                JSONArray jsonArray = jsonObject.getJSONArray("result");
//fill the myUser object with json and finally pass all of them as userArray then pass them to listOf User
//finally list of  users to recycerview Adapter to show
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jobject =  jsonArray.getJSONObject(i);
                    user myUser =new user();

                    myUser.setUsId(jobject.getInt("id"));
                    myUser.setName(jobject.getString("name"));
                    myUser.setLastName(jobject.getString("lastname"));
                    myUser.setEmail(jobject.getString("email"));
                    myUser.setPassword(jobject.getString("password"));
                    myUser.setImage(jobject.getString("image"));
                    myUser.setGender(jobject.getString("gender"));
                    myUser.setBirthDay(jobject.getString("birthday"));
                    myUser.setFavFilm(jobject.getString("favFilm"));
                    myUser.setFavColor(jobject.getString("favColor"));
                    myUser.setAboutMe(jobject.getString("aboutme"));
                    myUser.setAdmin(jobject.getInt("admin"));
                    userarray.add(myUser);
                }


                listOfUser = userarray;
                return "Valid";

            }  catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return  null;
        }




        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            if(o.equals("Valid")) {
                //json has valid key which sent from server
                recyclerView.setVisibility(View.VISIBLE);
                Error.setVisibility(View.GONE);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(myContext);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                //set list of user to show in adapter
                UserAdapter ca = new UserAdapter(listOfUser, myContext);
                recyclerView.setAdapter(ca);
            }
            else if(o.equals("Error"))
            {
                Error.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.GONE);
        }
    }

}
