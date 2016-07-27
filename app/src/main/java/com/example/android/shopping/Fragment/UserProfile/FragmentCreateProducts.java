package com.example.android.shopping.Fragment.UserProfile;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.shopping.Activities.UserProfileActivity;
import com.example.android.shopping.Helper.ImagePicker;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Model.Products;
import com.example.android.shopping.R;
import com.example.android.shopping.Helper.UIHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreateProducts extends Fragment {

    private static final int PICK_IMAGE_ID = 234;
    FloatingActionButton btnClick;
    ImageView ProductImage;
    public FragmentCreateProducts() {
        // Required empty public constructor
    }
    File UpdateImageFile;
    static View vew;
    static ImageView imgChatroom;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v=inflater.inflate(R.layout.fragment_create_product, container, false);

        vew=v;//getting view for GetProductInfo to help getting data in object
        imgChatroom=(ImageView)v.findViewById(R.id.imgProduct);
        btnClick=(FloatingActionButton)v.findViewById(R.id.btnFloatAction);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("validate",String.valueOf(ValidateProductInput(getView())));
                if(ValidateProductInput(getView())!=10)
                    return;
                if(!UIHelper.isNetworkAvailable(getActivity())) {

                    Toast.makeText(getActivity(),"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
                    return;
                }
                ProductNetworkCallTask networkCallTask=

                        new ProductNetworkCallTask(GetProductInfo(),UpdateImageFile,getActivity(),getView());
                networkCallTask.execute();
            }
        });
        ProductImage=(ImageView)v.findViewById(R.id.imgProduct);
        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent= ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent,PICK_IMAGE_ID);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    public static void setImage(Bitmap bm)
    {
        imgChatroom.setImageBitmap(bm);
    }

    public static Products GetProductInfo()
    {
        Products myProducts =new Products();
        myProducts.setProductName(UIHelper.etgetText(vew,R.id.txtProductName));
        myProducts.setProductPrice(UIHelper.etgetText(vew,R.id.txtProductPrice));
        myProducts.setProductDesc(UIHelper.etgetText(vew,R.id.txtProductDesc));
        myProducts.setQuantity(UIHelper.etgetText(vew,R.id.txtProductCount));
        Date date=new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyyMMddhhmmss");
        Log.d("date is",ft.format(date));
        myProducts.setDate(ft.format(date));
        myProducts.setStatus("addProduct");
        myProducts.setPbar(UIHelper.returnPrograss(vew,R.id.chatProgress));
        myProducts.setBtnSend((FloatingActionButton) UIHelper.btnGetImage(vew,R.id.btnFloatAction));
        return myProducts;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ResultCode:",Integer.toString(resultCode));
        if(resultCode!=-1)
            return;
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);

                    setImage(bitmap);

                File file= null;
                try {
                    file = UIHelper.savebitmap(bitmap);
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
// validate input product data
    public int ValidateProductInput(View view)
    {
        if (UIHelper.etgetText(getActivity(), R.id.txtProductName).equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.ProductNameLayout,"لطفا نام چت روم را وارد نمایید",true);
            return Tags.NAME_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.ProductNameLayout,"",false);
        }
        if (UIHelper.etgetText(getActivity(), R.id.txtProductPrice).equals("")) {
            UIHelper.SetErrorText(getActivity(),R.id.ProductDescLayout,"لطفا نام چت روم را وارد نمایید",true);
            return Tags.NAME_EMPTY_INPUT;
        }else{
            UIHelper.SetErrorText(getActivity(),R.id.ProductDescLayout,"",false);
        }

        UIHelper.SetImage(R.id.imgProduct,view);

        if( UpdateImageFile==null ) {

            Snackbar.make(view, "لطفا برای چت روم عکسی انتخاب کنید", Snackbar.LENGTH_SHORT).show();
            return Tags.CHATROOM_PIC_INPUT;
        }
        return Tags.SUCCESSFUL_INPUT;
    }
//network request section
    public class ProductNetworkCallTask extends AsyncTask<String, Void, String> {
        ProgressBar mProgress;
        FloatingActionButton btnRegister;
        View view;
        Context context;


        File imageFile;
        Products myProducts =new Products();
        public  String Result;
        private final MediaType MEDIA_TYPE = MediaType.parse("image/*");
        private static final String IMGUR_CLIENT_ID = "...";
        private OkHttpClient client=new OkHttpClient();

        public ProductNetworkCallTask(Products myProducts, File ImageFile, Context mconext, View view)
        {
            this.context=mconext;
            if(ImageFile!=null) {
                this.imageFile = ImageFile;
                Log.d("getrealfile:", "NEtwork:  " + imageFile.getName() + " And Absolute: " + imageFile.getAbsolutePath());
            }else
            {
                Log.d("getrealfile:","Image not set to update");
            }
            this.myProducts = myProducts;
            this.view=view;
            mProgress= myProducts.getPbar();
            btnRegister= myProducts.getBtnSend();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mProgress!=null)
                mProgress.setVisibility(View.VISIBLE);
            if (btnRegister!=null)
                btnRegister.setEnabled(false);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            Response response;

                try {
                    Log.d("FIlePath:", imageFile.getAbsolutePath());
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action","addProduct")
                            .addFormDataPart("name", myProducts.getProductName())
                            .addFormDataPart("productPrice", myProducts.getProductPrice())
                            .addFormDataPart("productdate", myProducts.getDate())
                                .addFormDataPart("productdesc", myProducts.getProductDesc())
                            .addFormDataPart("ProductCount", myProducts.getQuantity())
                            .addFormDataPart("Image", "imgRoom.jpg",
                                    RequestBody.create(MEDIA_TYPE, imageFile))

                            .build();

                    Request request = new Request.Builder()
                            .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                            .url(Tags.ProductAddress)
                            .post(requestBody)
                            .build();

                    response = client.newCall(request).execute();

                    Result = response.body().string().toString();
                    Log.d("resultValue:", Result);
                    Pattern p = Pattern.compile("-?\\d+");
                    Matcher m = p.matcher(Result);
                    StringBuilder build = new StringBuilder();
                    while (m.find()) {
                        build.append(m.group());
                    }


                    return build.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

      return null;
        }


        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Log.d("valueOfo:",""+o);
            String msg = null;
            if(mProgress!=null)
                mProgress.setVisibility(View.INVISIBLE);
            btnRegister.setEnabled(true);
            if(o != null && o.trim().matches(".*\\d+.*")){

                switch (o.toString().trim().toString().trim()) {
                    case "0":
                        msg = "اطلاعات شما با موفقیت ثبت شد";

                        break;
                    case "1":
                        msg = "خطا در ثبت اطلاعات";
                        break;
                    case "2":
                        msg = "محصول در پایگاه داده موجود می باشد";
                        break;
                    default:
                        msg="خطایی بس ناجوانمردانه";
                }

            }



            Snackbar.make(view, msg != null ? msg : "لطفا مجددا تلاس فرمایید",Snackbar.LENGTH_SHORT).show();



        }

    }


}
