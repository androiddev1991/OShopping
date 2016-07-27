package com.example.android.shopping.Helper.NetworkCalls.Register;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Model.user;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegUserNetworkCallTask extends AsyncTask<String, Void, String> {
    ProgressBar mProgress;
    FloatingActionButton btnRegister;
    user us;
    Context context;
    Activity ReciveActivity;
    File imageFile;

    public static String Result;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private static final String IMGUR_CLIENT_ID = "...";
    private OkHttpClient client=new OkHttpClient();
    public RegUserNetworkCallTask(user user, File ImageFile, Context mconext, Activity reciveActivity)
    {


        this.ReciveActivity=reciveActivity;

        this.context=mconext;
        if(ImageFile!=null) {
            this.imageFile = ImageFile;
            Log.d("getrealfile:", "NEtwork:  " + imageFile.getName() + " And Absolute: " + imageFile.getAbsolutePath());
        }else{Log.d("getrealfile:","Image not set to update");}
        this.us=user;

        mProgress= us.getPbar();
        btnRegister=us.getBtnSend();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mProgress!=null)
            mProgress.setVisibility(View.VISIBLE);
        if (btnRegister!=null)
            btnRegister.setEnabled(false);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            Log.d("JSON","Name: "+us.getName().toString());
            Log.d("JSON","LastName: "+us.getLastName().toString());
            RequestBody requestBody;
            if(imageFile==null) {
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("action", us.getStatus())
                        .addFormDataPart("userID", String.valueOf(us.getUsId()))
                        .addFormDataPart("Name", us.getName())
                        .addFormDataPart("LastName", us.getLastName())
                        .addFormDataPart("Email", us.getEmail())
                        .addFormDataPart("Password", us.getPassword())
                        .addFormDataPart("birthday", us.getBirthDay() + " ")
                        .addFormDataPart("gender", us.getGender())
                        .addFormDataPart("favFilm", us.getFavFilm() + " ")
                        .addFormDataPart("favColor", us.getFavColor() + " ")
                        .addFormDataPart("aboutme", us.getAboutMe() + " ")
                        .build();
            }
            else
            {
                 requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("action", us.getStatus())
                        .addFormDataPart("userID", String.valueOf(us.getUsId()))
                        .addFormDataPart("Name", us.getName())
                        .addFormDataPart("LastName", us.getLastName())
                        .addFormDataPart("Email", us.getEmail())
                        .addFormDataPart("Password", us.getPassword())
                        .addFormDataPart("imgProfile", "temp.jpg",
                                RequestBody.create(MEDIA_TYPE_PNG, imageFile))
                        .addFormDataPart("birthday", us.getBirthDay() + " ")
                        .addFormDataPart("gender", us.getGender()+ " ")
                        .addFormDataPart("favFilm", us.getFavFilm() + " ")
                        .addFormDataPart("favColor", us.getFavColor() + " ")
                        .addFormDataPart("aboutme", us.getAboutMe() + " ")
                        .build();
            }
            Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .url(Tags.UserAddress)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();

            Result=response.body().string().toString();
            Log.d("resultValue:",Result);
            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(Result);
            StringBuilder build = new StringBuilder();
            while (m.find()) { build.append(m.group());	}


            return build.toString();




        }  catch (IOException e) {
            e.printStackTrace();
        }


        return  null;
    }
public int val=0;
    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        Log.d("valueOfo:",""+o);
        String msg = null;
        if(o != null && o.trim().matches(".*\\d+.*")){

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
                    msg="کاربر مورد نظر موجود نمی باشد";
                    break;
                case "4":
                    msg="آپدیت با موفقیت انجام شد";
                    break;
                default:
                    msg="خطایی بس ناجوانمردانه";
            }

        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg != null ? msg : "لطفا مجددا تلاش کنید");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "دریافت شد",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(ReciveActivity.getTitle().toString().equals("RegisterUser"))
                            ReciveActivity.finish();
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
       alert11.show();
        if(mProgress!=null)
        mProgress.setVisibility(View.INVISIBLE);
        btnRegister.setEnabled(true);
    }
}


