package com.example.android.shopping.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.Suggestion;
import com.example.android.shopping.R;
import com.squareup.picasso.Picasso;



import java.io.IOException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SuggestionSingleView extends AppCompatActivity {
    Suggestion mySuggest=new Suggestion();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_single_view);
        Intent intent=getIntent();
        mySuggest=intent.getParcelableExtra("mySuggest");
//get whole data from intent which is filled from clicked recyclerview item
        UIHelper.setTvText(this,R.id.txtsuggestProductname,mySuggest.getProductName());
        UIHelper.setTvText(this,R.id.txtsuggestProductPrice,mySuggest.getProductPrice());
        UIHelper.setTvText(this,R.id.txtSuggestID,String.valueOf(mySuggest.getProductid()));
        UIHelper.setTvText(this,R.id.txtPorductQuantity,String.valueOf(mySuggest.getProductcounts()));
        UIHelper.setTvText(this,R.id.msgSuggestion,mySuggest.getSuggest());

        UIHelper.SetImage(R.id.productImage_Single_Suggest,this);
        Picasso.with(this)
                .load(Tags.Address+"shopping/uploads/Product/"+mySuggest.getProductid()+"/product.jpg")
                .into(UIHelper.getImage);

        CircleImageView circleImageView=(CircleImageView) findViewById(R.id.imgSuggestUserProfile);
        Picasso.with(this)
                .load(Tags.Address + "shopping/uploads/profile/"+mySuggest.getUserid()+"/temp.jpg")
                .resize(60,60)
                .noFade()
                .centerCrop()
                .into(circleImageView);

        UIHelper.CollapsingToolbarLayout(this,R.id.collapsingToolbar,mySuggest.getUserName()+" "+mySuggest.getUserLastName());



    }
    public void Onclick_sendEmail(View view)
    {
        if(!UIHelper.isNetworkAvailable(this)) {

            Toast.makeText(this,"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressBar pbbar=(ProgressBar)findViewById(R.id.suggestProgress);
        FloatingActionButton actionButton=(FloatingActionButton)findViewById(R.id.btnFloatAction);
        mySuggest.setSuggest(UIHelper.etgetText(this,R.id.edit_SendSuggestion));
        mySuggest.setStatus("verify");
//verify is just for php side to detect related behavior
        SuggestionNetworkTask suggestionNetworkTask=new SuggestionNetworkTask(pbbar,actionButton,view);
        suggestionNetworkTask.execute();
    }

    public class SuggestionNetworkTask extends AsyncTask<String, Void, String> {

        private OkHttpClient client=new OkHttpClient();
        String Result;
        View myView;

        ProgressBar mProgress;

        FloatingActionButton btnRegCard;


        public SuggestionNetworkTask(ProgressBar pbbar,FloatingActionButton actionButton,View view)
        {
            this.mProgress=pbbar;
            this.btnRegCard=actionButton;
            this.myView=view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mProgress!=null)
                mProgress.setVisibility(View.VISIBLE);
            if (btnRegCard!=null)
                btnRegCard.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
//sending request process
                RequestBody requestBody;
                requestBody=new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("action","verify")
                        .addFormDataPart("orderid",String.valueOf(mySuggest.getOrderid()))
                        .addFormDataPart("Email",mySuggest.getEmail())
                        .addFormDataPart("productname",mySuggest.getProductName())
                        .build();
                Request request=new Request.Builder()
                        .url(Tags.OrderAddress)
                        .post(requestBody)
                        .build();
                Response response=null;
                response = client.newCall(request).execute();
                Result=response.body().string().toString();
                Log.d("ReturnValues:", removeUselessChar(Result));
                return removeUselessChar(Result);

            }  catch (IOException e) {
                e.printStackTrace();
            }


            return  null;
        }






        public String removeUselessChar(String str)
        {
            //clean the data from all useless char, that's for making sure the data is what everything we
            //expected for
            Log.d("Result String is:",str);
            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(str);
            StringBuilder build = new StringBuilder();
            while (m.find()) {
                build.append(m.group());
            }
            return build.toString();
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            Log.d("valueOfo:", "" + o);
            String msg = null;
            if (o != null ) {
                if (o != null && o.trim().matches(".*\\d+.*")) {

                    switch (o.toString().trim().toString().trim()) {
                        case "0":
                            msg = "اطلاعات شما با موفقیت ثبت شد";
                            break;
                        case "1":
                            msg = "خطا در ثبت اطلاعات";
                            break;
                        case "2":
                            msg = "ایمیل در دیتابیس موجود می باشد";
                            break;
                        case "3":
                            msg = "کاربر مورد نظر موجود نمی باشد";
                            break;
                        case "4":
                            msg = "آپدیت با موفقیت انجام شد";
                            break;
                        default:
                            msg = "خطایی بس ناجوانمردانه";
                    }
                    Snackbar.make(myView,msg != null ? msg : "لطفا مجددا تلاش کنید",Snackbar.LENGTH_SHORT).show();
                    if(mProgress!=null)
                        mProgress.setVisibility(View.INVISIBLE);
                    btnRegCard.setEnabled(true);

                }
            }

        }


    }

}
