package com.example.android.shopping.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import com.example.android.shopping.R;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences myPrefUser;
    private String email;
    private String password;
    private ProgressBar loginProgress;
    private Button btnLogin;
    private TextInputLayout emailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    //initialize variable
        loginProgress=(ProgressBar)findViewById(R.id.LoginProgressBar);
        btnLogin=(Button)findViewById(R.id.email_sign_in_button);
        emailLayout=(TextInputLayout)findViewById(R.id.emailLayout);
        myPrefUser=getPreferences(MODE_PRIVATE);

    }

    public void btnBack(View view)
    {finish();}


    //login process which first test for internet connection then if its true send data to use2.php

    public void loginOnClick(View view)
    {
        if(!UIHelper.isNetworkAvailable(this)) {
            Snackbar.make(view,"به اینترنت متصل نمی باشید",Snackbar.LENGTH_LONG).show();
            return;
        }

       email=UIHelper.etgetText(this,R.id.email);
        password=UIHelper.etgetText(this,R.id.password);
        loginNetworkTask loginNetworkTask=new loginNetworkTask();
        loginNetworkTask.execute();
    }
public class loginNetworkTask extends AsyncTask<String,Void,String>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loginProgress.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
    }

    OkHttpClient client=new OkHttpClient();
    @Override
    protected String doInBackground(String... params) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "login")
                .addFormDataPart("Email", email)
                .addFormDataPart("Password", password)
                .build();
          Request request = new Request.Builder()

                .url(Tags.UserAddress)
                .post(requestBody)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String result=response.body().string();


           /*     Pattern p = Pattern.compile("-?\\d+");
                Matcher m = p.matcher(result);
                StringBuilder builder = new StringBuilder();
                while (m.find()) {
                    builder.append(m.group());
                }*/

                return result.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("valueOfo:",""+s);
        try {

            JSONObject jsonResult = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
           // JSONObject jsonResult = new JSONObject(s);
//test if there is an error
        if(s != null && jsonResult.has("status")) {
            if(jsonResult.getBoolean("status")==true) {
                emailLayout.setError("ایمیل مورد نظر در دیتابیس نمی باشد");
                emailLayout.setErrorEnabled(true);

                loginProgress.setVisibility(View.INVISIBLE);
                btnLogin.setEnabled(true);
                return;
            }
        }
        //data is valid and can work with it
        else if(isValid(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1)))
        {
            Log.d("The Result:","The if Result is false");

//            SharedPreferences.Editor editor=myPrefUser.edit();
//            editor.putString("CurrrentUser","applemachinTage");
//            editor.commit();

            try {
                //to get ease of access we store data in sharedPreferences and
                //set it in currentUserID in userProfileActivity
                //be careful data deseriablize in UIHelper myUser method
                SharedPreferences.Editor editor = getSharedPreferences("AppPref", MODE_PRIVATE).edit();
                editor.putString("UserID",s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
                editor.commit();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

            Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
            intent.putExtra("Email", email);
            startActivity(intent);




        // Log.d("the Valid",String.valueOf(va));
        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setEnabled(true);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            Log.d("The value of EX1:",jse.getMessage());

            return false;
        }
    }

}

}
