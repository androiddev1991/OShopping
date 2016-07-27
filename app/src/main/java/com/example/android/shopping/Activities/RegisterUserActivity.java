package com.example.android.shopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopping.Helper.NetworkCalls.Register.RegUserNetworkCallTask;
import com.example.android.shopping.Helper.ImagePicker;
import com.example.android.shopping.R;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.user;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegisterUserActivity extends AppCompatActivity {

    private  File f=null;

    private static final int PICK_IMAGE_ID = 234;
    TextInputLayout NameLayout;
    TextInputLayout LastNameLayout;
    TextInputLayout EmailLayout;
    TextInputLayout Password1Layout;
    TextInputLayout Password2Layout;
    TextView txtImage_Header;
    Button btn_register;
    ImageView imageView;
    public String FIleName = "images.jpg";
    public String FilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        uiCustomization();
        FilePath=getFilesDir().getParentFile().getPath() + "/files/";
        try {

            copyfile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        f=new File(FilePath+FIleName);
        Log.d("FileName:",f.getName());
    }
    // copy default file to phone which is helpful if user doesn't set image
    public void copyfile(Context context) throws IOException {

        Log.d("File Path:",FilePath);
        InputStream myInput=context.getAssets().open(FIleName);

        String outFileName=FilePath+FIleName;
        OutputStream myOutPut=new FileOutputStream(outFileName);
        byte[] buffer=new byte[1024];
        int length;
        while ((length=myInput.read(buffer))>0)
        {
            myOutPut.write(buffer,0,length);
        }
        myOutPut.flush();
        myOutPut.close();
        myInput.close();
    }


    private void uiCustomization() {
        imageView=(ImageView)findViewById(R.id.img_profile);
        NameLayout=(TextInputLayout)findViewById(R.id.valName);
        LastNameLayout=(TextInputLayout)findViewById(R.id.valLastName);
        EmailLayout=(TextInputLayout)findViewById(R.id.valEmail);
        Password1Layout=(TextInputLayout)findViewById(R.id.valPWD1);
        Password2Layout=(TextInputLayout)findViewById(R.id.valPWD2);
        txtImage_Header=(TextView)findViewById(R.id.txtImage_Header);
        //btn_register=(Button) findViewById(R.id.btn_register);
//set ui with byekan typeface
        UIHelper.etTypeFace(this,R.id.name);
        UIHelper.etTypeFace(this,R.id.LastName);
        UIHelper.etTypeFace(this,R.id.email);
        UIHelper.etTypeFace(this,R.id.password1);
        UIHelper.etTypeFace(this,R.id.password2);
        UIHelper.txtTypeFace(this,R.id.txtImage_Header);
        //UIHelper.btnTypeFace(this,R.id.btn_register);
    }

    public void btnBack(View view)
    {
        finish();
    }

    public void ivGetImage(View view)
    {
        //selectImage();
        Intent chooseImageIntent= ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent,PICK_IMAGE_ID);
    }

 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
        Log.d("ResultCode:",Integer.toString(resultCode));
        if(resultCode!=-1)
            return;
     switch(requestCode) {
         case PICK_IMAGE_ID:
             Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
             imageView.setImageBitmap(bitmap);
             File file= null;
             try {
                 file = savebitmap(bitmap);
                 Log.d("file:",file.getName() + "  |is the file exist: "+file.getAbsolutePath());
                 f=file;
             } catch (IOException e) {
                 e.printStackTrace();
             }

             break;
         default:
             super.onActivityResult(requestCode, resultCode, data);
             break;
     }
     Log.d("ImageGallary:",Integer.toString(requestCode));

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

// validate whole data
    private int ValidateInput() {

        boolean check=UIHelper.validateEmail(UIHelper.etgetText(this, R.id.email));

        if (UIHelper.etgetText(this, R.id.name).equals("")) {
            NameLayout.setError("لطفا نام خود را وارد نمایید");
            return Tags.NAME_EMPTY_INPUT;
        }else{NameLayout.setErrorEnabled(false);}
        if (UIHelper.etgetText(this, R.id.LastName).equals("")) {
            LastNameLayout.setError("لطفا نام خانوادگی خود را وارد نمایید");
            return Tags.LAST_EMPTY_INPUT;
        }else{LastNameLayout.setErrorEnabled(false);}
        if (UIHelper.etgetText(this, R.id.email).equals("")) {
            EmailLayout.setError("لطفا ایمیل خود را وارد نمایید");
            return Tags.EMAIL_EMPTY_INPUT;
        }else{EmailLayout.setErrorEnabled(false);}
        if (!check) {
            EmailLayout.setError("ایمیل وارده اشتباه می باشد");
            return Tags.EMAIL_INCORRECT_INPUT;
        }else{EmailLayout.setErrorEnabled(false);}
        if(UIHelper.etgetText(this,R.id.password1).equals("")) {
            Password1Layout.setError("لطفا پسورد خود را وارد نمایید");
            return Tags.PWD_EMPTY_INPUT;
        }else{Password1Layout.setErrorEnabled(false);}
        if(UIHelper.etgetText(this,R.id.password2).equals("")) {
            Password2Layout.setError("لطفا پسورد خود را مجددا وارد نماید");
            return Tags.PWD2_EMPTY_INPUT;
        }else{Password2Layout.setErrorEnabled(false);}
        if(!UIHelper.etgetText(this,R.id.password2).equals(UIHelper.etgetText(this,R.id.password1))) {
            Password2Layout.setError("پسورد مطابقت ندارد");
            return Tags.PWD2_EQUAL_INPUT;
        }else{Password2Layout.setErrorEnabled(false);}
        return Tags.SUCCESSFUL_INPUT;
    }

    public void btn_Register_Click(View view) throws JSONException {
        if(!UIHelper.isNetworkAvailable(this)) {
            Snackbar.make(view,"به اینترنت متصل نمی باشید",Snackbar.LENGTH_LONG).show();
            return;
        }
        if(ValidateInput()!=Tags.SUCCESSFUL_INPUT)
            return;
        user us=new user();
        Log.d("fileName",f.getName().toString());
//set status as add to help php side detect what behavior should have to done
        us.setStatus("add");
        us.setName(UIHelper.tvgetText(this,R.id.name).toString());
        us.setLastName(UIHelper.tvgetText(this,R.id.LastName).toString());
        us.setEmail(UIHelper.tvgetText(this,R.id.email));
        us.setPassword(UIHelper.tvgetText(this,R.id.password1));
        FloatingActionButton btn_register=(FloatingActionButton)findViewById(R.id.btnRegister);
        us.setBtnSend(btn_register);
        us.setPbar(UIHelper.returnPrograss(this,R.id.RegprogressBar));
//f is the image file which help us to send image file to server
        RegUserNetworkCallTask networkRequest=new RegUserNetworkCallTask(us,f,this,this);
        networkRequest.execute();


    }

}
