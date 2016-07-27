package com.example.android.shopping.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.android.shopping.Model.user;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Android on 6/28/2016.
 */
public class UIHelper  {
    private static  Typeface typeface;
    public  static ImageView getImage;
    private static  void settypeFace(Activity activity)
    {
        typeface = Typeface.createFromAsset(activity.getAssets(), "byekan.ttf");
    }
    public static ImageView SetImage(int i, View view)
    {
        ImageView img =(ImageView)view.findViewById(i);
        getImage=img;
        return img;
    }
    public static ImageView SetImage(int i, Activity activity)
    {
        ImageView img =(ImageView)activity.findViewById(i);
        getImage=img;
        return img;
    }
    public static void SetActionBar(int id, ActionBar actionBar)
    {
        actionBar.setHomeAsUpIndicator(id);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    public static void CollapsingToolbarLayout(Activity activity,int id,String title)
    {
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)activity.findViewById(id);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setTitle(title);
        
    }
//TextView Methods
    public static String tvgetText(Activity activity, int id) {
        TextView tv = (TextView) activity.findViewById(id);
        tv.setTypeface(typeface);
        return tv.getText().toString();
    }
    public static String tvgetText(Activity activity, int id,View view) {
        TextView tv = (TextView) view.findViewById(id);
        tv.setTypeface(typeface);
        return tv.getText().toString();
    }
    public static void setTvText(Activity activity, int id, String text) {
        TextView tv = (TextView) activity.findViewById(id);
        tv.setText(text);
        settypeFace(activity);
        tv.setTypeface(typeface);
    }
    public static void setTvText(Activity activity, int id, String text,View v) {
        TextView tv = (TextView) v.findViewById(id);
        tv.setText(text);
        settypeFace(activity);
        tv.setTypeface(typeface);
    }

    public static void txtTypeFace(Activity activity, int id) {
        TextView tv = (TextView) activity.findViewById(id);
        settypeFace(activity);
        tv.setTypeface(typeface);
    }
//EditText Methods
    public static String etgetText(View view, int id) {

        EditText et = (EditText) view.findViewById(id);
        et.setTypeface(typeface);
        return et.getText().toString();
    }
    public static String etgetText(Activity activity, int id) {

        EditText et = (EditText) activity.findViewById(id);
        et.setTypeface(typeface);
        return et.getText().toString();
    }
    public static void etSetText(int id,View view,String value)
    {
        EditText et = (EditText) view.findViewById(id);
        et.setTypeface(typeface);
        et.setEnabled(true);
        et.setText(value);
    }
    public static void etSetDeText(int id,View view,String value)
    {
        EditText et = (EditText) view.findViewById(id);
        et.setTypeface(typeface);
        et.setHint(value);
    }
    public static void etTypeFace(Activity activity, int id) {
        EditText tv = (EditText) activity.findViewById(id);
        settypeFace(activity);
        tv.setTypeface(typeface);
    }

//Button methods
    public static void btnTypeFace(Activity activity, int id) {
        Button tv = (Button) activity.findViewById(id);
        settypeFace(activity);

        tv.setTypeface(typeface);
    }
    public static ImageButton btnGetImage(Activity activity, int id) {
        ImageButton btn = (ImageButton) activity.findViewById(id);

        return btn;
    }
    public static ImageButton btnGetImage(View view, int id) {
        ImageButton btn = (ImageButton) view.findViewById(id);
        return btn;
    }
//Prograss Bar mthods
    public static ProgressBar returnPrograss(Activity activity,int id)
    {
        ProgressBar progressBar=(ProgressBar)activity.findViewById(id);
        return progressBar;
    }
    public static ProgressBar returnPrograss(View view,int id)
    {
        ProgressBar progressBar=(ProgressBar)view.findViewById(id);
        return progressBar;
    }
    //Check box methods
    public static boolean getCBChecked(Activity activity, int id) {
        CheckBox cb = (CheckBox) activity.findViewById(id);
        return cb.isChecked();
    }

    public static void setCBChecked(Activity activity, int id, boolean value) {
        CheckBox cb = (CheckBox) activity.findViewById(id);
        cb.setChecked(value);
    }
//Radio Button
    public static String getSelectedRadio(int id,View view)
    {
        RadioGroup radioGroup=(RadioGroup)view.findViewById(id);
        RadioButton radioButton=(RadioButton)view.findViewById( radioGroup.getCheckedRadioButtonId());
        return radioButton.getText().toString();
    }
    public static void setMaleFemale(int male,int female,View view,String gen)
    {
        if(gen.trim().equals("مرد"))
        {
            RadioButton radioButton=(RadioButton)view.findViewById(male);
            radioButton.setTypeface(typeface);
            radioButton.setChecked(true);
        }
        else if(gen.trim().equals("زن"))
        {
            RadioButton radioButton=(RadioButton)view.findViewById(female);
            radioButton.setTypeface(typeface);
            radioButton.setChecked(true);
        }

    }
//TextInputLayout Control
    public static void SetErrorText(View view,int Id,String Error,boolean EnableError)
    {
            TextInputLayout textInputLayout=(TextInputLayout)view.findViewById(Id);
            textInputLayout.setError(Error);
            textInputLayout.setErrorEnabled(EnableError);

    }
    public static void SetErrorText(Activity activity,int Id,String Error,boolean EnableError)
    {
        TextInputLayout textInputLayout=(TextInputLayout)activity.findViewById(Id);
        textInputLayout.setTypeface(typeface);
        textInputLayout.setError(Error);
        textInputLayout.setErrorEnabled(EnableError);

    }
  /*  public static boolean NetworkConnectivity(Context context)
    {

       ConnectivityManager connectivityManager=
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();

    }*/

    public static boolean isNetworkAvailable( Context context) {
         ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public static boolean validateEmail(String EmailAddress)
    {
          boolean check = Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(EmailAddress).matches();
        if(!check)
            return false;
        else
            return true;
    }

    public static void setImagePicasso(int id,Context context)
    {
        Picasso.with(context)
                .load(Tags.Address+"shopping/uploads/profile/"+id+"/temp.jpg")

                .into(getImage);

    }
    public static File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "testimage.jpg");
        file.createNewFile();
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(bytes.toByteArray());
        fo.close();
        return file;
    }

    public static user myUser(Context ctx)
    {
        String json = "";
        user myUser=new user();
        try {
            SharedPreferences prefs = ctx.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
            json = prefs.getString("UserID", "Error");

            Log.d("Preferneces values",json);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Log.d("JsonString","JsonIS: "+json.toString());
        String subString=json.substring(json.toString().indexOf("{"), json.lastIndexOf("}") + 1);

        try {
            JSONObject jsonResult=new JSONObject(subString);

            myUser.setUsId(Integer.parseInt(jsonResult.getString("id")));
            myUser.setName(jsonResult.getString("name"));
            myUser.setLastName(jsonResult.getString("lastname"));
            myUser.setEmail(jsonResult.getString("email"));
            myUser.setPassword(jsonResult.getString("password"));
            myUser.setImage(jsonResult.getString("image"));
            myUser.setBirthDay(jsonResult.getString("birthday"));
            myUser.setGender(jsonResult.getString("gender"));
            myUser.setFavFilm(jsonResult.getString("favFilm"));
            myUser.setFavColor(jsonResult.getString("favColor"));
            myUser.setAboutMe(jsonResult.getString("aboutme"));
            myUser.setAdmin(jsonResult.getInt("admin"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myUser;

    }
}
