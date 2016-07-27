package com.example.android.shopping.Fragment.UserProfile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;


import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping.Helper.ImagePicker;
import com.example.android.shopping.Helper.NetworkCalls.Register.RegUserNetworkCallTask;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.R;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Activities.UserProfileActivity;
import com.example.android.shopping.Model.user;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends android.support.v4.app.Fragment  {
    private static final int PICK_IMAGE_ID = 234;
    static View v;
    public File UpdateImageFile;
    FloatingActionButton btnSend;
    ImageView frProfileImage;
public  static TextView textView;
    public FragmentProfile() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.fragment_profile, container, false);

        btnSend=(FloatingActionButton)v.findViewById(R.id.btnSendUser);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickSendUser();
            }
        });
        frProfileImage=(ImageView)v.findViewById(R.id.frProfileImage);

        frProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent= ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent,PICK_IMAGE_ID);
            }
        });
        textView = (TextView) v.findViewById(R.id.Datepicker);
        // Inflate the layout for this fragment
        return v;
    }

    public void onclickSendUser()
    {
        if(!UIHelper.isNetworkAvailable(getActivity())) {

            Toast.makeText(getActivity(),"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ValidateInput()!=10)
            return;

        user myUser=UpdateData();
        myUser.setUsId(Integer.parseInt(String.valueOf(UserProfileActivity.CurrentUserID.getUsId())));
        Log.d("UserID", String.valueOf(myUser.getUsId()));
        RegUserNetworkCallTask networkCallTask=
                new RegUserNetworkCallTask(myUser,UpdateImageFile,getContext(),getActivity());
        networkCallTask.execute();

    }
    public static ImageView getProfileImage()
    {
        return UIHelper.SetImage(R.id.frProfileImage,v);
    }
    public static void setProfileImage(Bitmap bm)
    {
        ImageView img =(ImageView)v.findViewById(R.id.frProfileImage);
        img.setImageBitmap(bm);
    }
    public void load_data(View v)
    {

       UIHelper.etSetText(R.id.txtname,v, UserProfileActivity.CurrentUserID.getName());
        UIHelper.etSetText(R.id.txtLastName,v, UserProfileActivity.CurrentUserID.getLastName());
        UIHelper.etSetText(R.id.txtEmail,v, UserProfileActivity.CurrentUserID.getEmail());
        UIHelper.etSetText(R.id.txtPw1,v, UserProfileActivity.CurrentUserID.getPassword());
        UIHelper.etSetText(R.id.txtPw2,v, UserProfileActivity.CurrentUserID.getPassword());
        UIHelper.setTvText(getActivity(),R.id.Datepicker, UserProfileActivity.CurrentUserID.getBirthDay(),v);
        UIHelper.etSetText(R.id.txtFavFilm,v, UserProfileActivity.CurrentUserID.getFavFilm());
        UIHelper.etSetText(R.id.txtFavcolor,v, UserProfileActivity.CurrentUserID.getFavColor());
        UIHelper.etSetText(R.id.txtAboutMe,v, UserProfileActivity.CurrentUserID.getAboutMe());

        UIHelper.setMaleFemale(R.id.rbMan,R.id.rbwoman,v, UserProfileActivity.CurrentUserID.getGender());
        UIHelper.SetImage(R.id.frProfileImage,v);

        UIHelper.setImagePicasso(UserProfileActivity.CurrentUserID.getUsId(),getContext());
    }
//getting updated data in object
    public user UpdateData()
    {
        user myUser=new user();
        myUser.setName(UIHelper.etgetText(v,R.id.txtname));
        myUser.setLastName(UIHelper.etgetText(v,R.id.txtLastName));
        myUser.setEmail(UIHelper.etgetText(v,R.id.txtEmail));
        myUser.setPassword(UIHelper.etgetText(v,R.id.txtPw1));
        myUser.setBirthDay(UIHelper.tvgetText(getActivity(),R.id.Datepicker,v));
        myUser.setGender(UIHelper.getSelectedRadio(R.id.groupGen,v));

        myUser.setFavFilm(UIHelper.etgetText(v,R.id.txtFavFilm));
        myUser.setFavColor(UIHelper.etgetText(v,R.id.txtFavcolor));
        myUser.setAboutMe(UIHelper.etgetText(v,R.id.txtAboutMe));
        myUser.setStatus("update");
        myUser.setPbar(UIHelper.returnPrograss(v,R.id.progressBar));
       FloatingActionButton floatButton=(FloatingActionButton)v.findViewById(R.id.btnSendUser);
        myUser.setBtnSend(floatButton);
        return myUser;
    }
    public static void setText(String text){

        textView.setText(text);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load_data(view);
    }
// validate input
    public int ValidateInput() {

        boolean check=UIHelper.validateEmail(UIHelper.etgetText(getActivity(), R.id.txtEmail));

        if (UIHelper.etgetText(getActivity(), R.id.txtname).equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.txtUserLayout,"لطفا نام خود را وارد نمایید",true);
            return Tags.NAME_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtUserLayout,"",false);
        }

        if (UIHelper.etgetText(getActivity(), R.id.txtLastName).equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.txtLastLayout,"لطفا نام خانوادگی خود را وارد نمایید",true);
            return Tags.LAST_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtLastLayout,"",false);
        }
        if (UIHelper.etgetText(getActivity(), R.id.txtEmail).equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.txtEmailLayout,"لطفا ایمیل خود را وارد نمایید",true);
            return Tags.EMAIL_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtEmailLayout,"",false);
        }

        if (!check) {
            UIHelper.SetErrorText(getActivity(),R.id.txtEmailLayout,"ایمیل وارده اشتباه می باشد",true);

            return Tags.EMAIL_INCORRECT_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtEmailLayout,"",false);
        }
        String PWD1,PWD2;
        PWD1=UIHelper.etgetText(getActivity(),R.id.txtPw1);
        PWD2=UIHelper.etgetText(getActivity(),R.id.txtPw2);
        if(PWD1.equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.txtPw1Layout,"لطفا پسورد خود را وارد نمایید",true);
            return Tags.PWD_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtPw1Layout,"",false);
        }


        if(PWD2.equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.txtPw2Layout,"لطفا پسورد خود را وارد نمایید",true);
            return Tags.PWD2_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtPw2Layout,"",false);
        }

        if(!PWD1.equals(PWD2)) {
            UIHelper.SetErrorText(getActivity(),R.id.txtPw2Layout,"پسورد مطابقت ندارد",true);
            return Tags.PWD2_EQUAL_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.txtPw2Layout,"",false);
        }
        return Tags.SUCCESSFUL_INPUT;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ResultCode:",Integer.toString(resultCode));
        if(resultCode!=-1)
            return;
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);



                 setProfileImage(bitmap);
                File file= null;
                try {
                    file = savebitmap(bitmap);
                    Log.d("file:",file.getName() + "  |is the file exist: "+file.getAbsolutePath());
                    UpdateImageFile=file;
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
}
