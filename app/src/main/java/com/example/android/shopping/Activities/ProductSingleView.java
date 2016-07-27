package com.example.android.shopping.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.Products;
import com.example.android.shopping.Model.Suggestion;
import com.example.android.shopping.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductSingleView extends AppCompatActivity {

    private Suggestion mySuggestion = new Suggestion();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_single_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get clicked product from listview which is in ProductList fragment
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String productId = intent.getStringExtra("productId");
        String quantity=intent.getStringExtra("Quantity");
        String desc=intent.getStringExtra("desc");
        int userId = Integer.parseInt(intent.getStringExtra("UserId"));
        //addOrder is used as form action which help php to run addOrder related process
        mySuggestion.setStatus("addOrder");
        mySuggestion.setUserid(userId);
        mySuggestion.setProductid(Integer.parseInt(productId));

        //fill whole single product view with data
        UIHelper.SetImage(R.id.productImage_Single_Order, this);
        Log.d("the Id Is:",productId);
        Picasso.with(this)
                .load(Tags.Address+"shopping/uploads/Product/"+mySuggestion.getProductid()+"/product.jpg")
                .into(UIHelper.getImage);

        UIHelper.CollapsingToolbarLayout(this, R.id.ProductcollapsingToolbar, name);
        UIHelper.setTvText(this,R.id.txtProductOrderID, productId);
        UIHelper.setTvText(this, R.id.txtOrderProductPrice, price);
        UIHelper.setTvText(this,R.id.txtPorductQuantity,quantity);
        UIHelper.setTvText(this,R.id.txtPorductDesc,desc);
    }

    public void addToCard_Click(View view) {
        if(!UIHelper.isNetworkAvailable(this)) {

            Toast.makeText(this,"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProductOrderProgress);
        mySuggestion.setSuggest(UIHelper.etgetText(this,R.id.edit_SendSuggestion));
        FloatingActionButton btnFloat = (FloatingActionButton) findViewById(R.id.btnFloatAction);
    //get floatAction btn and progresss to make them deactive during server process
        SuggestionNetworkTask suggest = new SuggestionNetworkTask(btnFloat,progressBar,view);
        suggest.execute();

    }


    public class SuggestionNetworkTask extends AsyncTask<String, Void, String> {

        private OkHttpClient client = new OkHttpClient();
        String Result;

        ProgressBar mProgress;

        FloatingActionButton btnRegCard;
        View myView;

        public SuggestionNetworkTask(FloatingActionButton btnRegCard,ProgressBar mProgress,View myView)
        {
            this.myView=myView;
            this.mProgress=mProgress;
            this.btnRegCard=btnRegCard;
        }




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgress != null)
                mProgress.setVisibility(View.VISIBLE);
            if (btnRegCard != null)
                btnRegCard.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                RequestBody requestBody;
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("action", mySuggestion.getStatus())
                        .addFormDataPart("productid", String.valueOf(mySuggestion.getProductid()))
                        .addFormDataPart("userid", String.valueOf(mySuggestion.getUserid()))
                        .addFormDataPart("Suggest", String.valueOf(mySuggestion.getSuggest()))
                        .build();

                Request request = new Request.Builder()
                        .url(Tags.OrderAddress)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                Result = response.body().string().toString();

                Log.d("resultValue:", Result);
                return removeUselessChar(Result);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        public String removeUselessChar(String str) {
            Log.d("Result String is:", str);
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
                    Snackbar.make(myView, msg != null ? msg : "لطفا مجددا تلاش کنید", Snackbar.LENGTH_SHORT).show();
                    if (mProgress != null)
                        mProgress.setVisibility(View.INVISIBLE);
                    btnRegCard.setEnabled(true);

                }
            }
        }



    }



