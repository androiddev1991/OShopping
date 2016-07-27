package com.example.android.shopping.Fragment.UserProfile;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.shopping.Activities.SuggestionSingleView;

import com.example.android.shopping.Adapter.orderAdapter;

import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.Suggestion;
import com.example.android.shopping.R;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSuggestionReview extends Fragment  implements RecyclerItemClickListener.OnItemClickListener  {
    private ProgressBar SuggestionprogressBar;
    public    RecyclerView recSuggestList;

    public List<Suggestion> SuggestList=new ArrayList<>();//getting list of suggestion from server

    public FragmentSuggestionReview() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!UIHelper.isNetworkAvailable(getActivity())) {

            Toast.makeText(getActivity(),"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
            return;
        }
        SuggestionprogressBar=(ProgressBar)view.findViewById(R.id.SuggestionProgressBar);

        recSuggestList = (RecyclerView) view.findViewById(R.id.recyclerSuggestion);
        recSuggestList.setHasFixedSize(true);
        recSuggestList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),this));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recSuggestList.setLayoutManager(llm);

        SuggestionNetworkTask suggestionNetworkTask=new SuggestionNetworkTask();
        suggestionNetworkTask.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_suggestion_review, container, false);


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onItemClick(View childView, int position) {
        Suggestion mySuggestion=SuggestList.get(position);
            Intent intent = new Intent(childView.getContext(), SuggestionSingleView.class);
            intent.putExtra("mySuggest",mySuggestion);
            startActivity(intent);
    }



    public class SuggestionNetworkTask extends AsyncTask<String, Void, String> {

        private OkHttpClient client=new OkHttpClient();

        private String Result;




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SuggestionprogressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {


            RequestBody requestBody;
            requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action","selectAll")

                    .build();
            Request request=new Request.Builder()
                    .url(Tags.OrderAddress)
                    .post(requestBody)
                    .build();
            Response response=null;
            try {
                response = client.newCall(request).execute();
                Result = response.body().string().toString();
                return Result;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }




        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            SuggestionprogressBar.setVisibility(View.GONE);

            TextView Error=(TextView)getActivity().findViewById(R.id.txtError);
            String msg = null;
            if (o != null && isJSONValid(o)) {
                recSuggestList.setVisibility(View.VISIBLE);
                Error.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(o);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");
//conver json array to array object
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jobject =  jsonArray.getJSONObject(i);
                        Suggestion mySuggest=new Suggestion();
                        mySuggest.setProductName(jobject.getString("productname"));
                        mySuggest.setProductcounts(jobject.getInt("productcount"));
                        mySuggest.setProductid(jobject.getInt("productid"));
                        mySuggest.setProductPrice(jobject.getString("productPrice"));

                        mySuggest.setVarify(jobject.getInt("verify"));
                        mySuggest.setOrderid(jobject.getInt("orderid"));
                        mySuggest.setSuggest(jobject.getString("Suggest"));

                        mySuggest.setUserName(jobject.getString("name"));
                        mySuggest.setUserid(jobject.getInt("userid"));
                        mySuggest.setUserLastName(jobject.getString("lastname"));
                        mySuggest.setEmail(jobject.getString("email"));
                        SuggestList.add(mySuggest);
                    }



                    Log.d("RecSuggestListSize:",String.valueOf(SuggestList.size()));
                    orderAdapter ca = new orderAdapter(SuggestList,getContext());
                    recSuggestList.setAdapter(ca);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {

             //   try {
                    Error.setVisibility(View.VISIBLE);
               // } catch (Exception e) {
                //    e.printStackTrace();
                //}
            }





        }

        public boolean isJSONValid(String test) {
            try {
                new JSONObject(test);
            } catch (JSONException ex) {
                // edited, to include @Arthur's comment
                // e.g. in case JSONArray is valid as well...
                try {
                    new JSONArray(test);
                } catch (JSONException ex1) {
                    return false;
                }
            }
            return true;
        }

    }


}
