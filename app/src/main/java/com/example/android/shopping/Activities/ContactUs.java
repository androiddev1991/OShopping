package com.example.android.shopping.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.R;

import java.util.regex.Pattern;

public class ContactUs extends AppCompatActivity {
    // private EditText edMessage;
    private TextInputLayout txtNameLayout, txtEmailLayout,txtMessageLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        txtNameLayout =(TextInputLayout)findViewById(R.id.txtUserLayout);
        txtEmailLayout =(TextInputLayout)findViewById(R.id.txtPassLayout);
        txtMessageLayout=(TextInputLayout)findViewById(R.id.txtmessLayout);
    }
    public void validateAndLogin(View view)
    {
        //validation
        if(validateName()&& validateEmail() && validateMessage()  && validateRadiobtn())
        {
            //finished validation

            RadioButton opinion=(RadioButton)findViewById(R.id.opinion);
            RadioButton order=(RadioButton)findViewById(R.id.order);
            TextView message=(TextView)findViewById(R.id.message);

            String[] TO={""};
            String[] CC={""};
            //sending email
            Log.i("Sending Email","");
            if(opinion.isChecked())
                TO[0]="opinion@gmail.com";
            else if(order.isChecked())
                TO[0]="androiddev1991@gmail.com";


            Intent emailIntent=new Intent(Intent.ACTION_SEND);
            //configure email to intent
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
            emailIntent.putExtra(Intent.EXTRA_CC,CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,"انتقاد از اوشاپ");
            emailIntent.putExtra(Intent.EXTRA_TEXT,message.getText().toString()+"");

            try
            {

                startActivity(Intent.createChooser(emailIntent,"ارسال پیام ..."));
                finish();
                Log.i("Finished Email sent","");
            }catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(ContactUs.this,"ایمیل ارسال نشد لطفا به جی میل خود وارد شوید",Toast.LENGTH_SHORT).show();
            }
        }
        else
            return;
    }

 //-------------------validate section that validate all for data----------
    private boolean validateName() {

        if(UIHelper.etgetText(this,R.id.txtname).toString().isEmpty()) {
            txtNameLayout.setError("لطفا نام خود را وارد کنید");
            return  false;
        }
        else
        {
            txtNameLayout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateMessage() {

        if(UIHelper.etgetText(this,R.id.message).toString().isEmpty()) {
            txtMessageLayout.setError("لطفا پیام خود را وارد کنید");
            return  false;
        }
        else
        {
            txtMessageLayout.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {

        String email= UIHelper.etgetText(this,R.id.email).toString().toString().trim();
        if(email.length()<5) {
            txtEmailLayout.setError("لطفا امیل خود را وارد کنید");
            return false;
        }
        else if(!UIHelper.validateEmail(email))
        {
            txtEmailLayout.setError("ایمیل وارده اشتباه می باشد");
            return false;
        }
        else
        {
            txtEmailLayout.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateRadiobtn() {
        RadioButton opinion=(RadioButton)findViewById(R.id.opinion);
        RadioButton order=(RadioButton)findViewById(R.id.order);
        if(opinion.isChecked() ^ order.isChecked()) {
            txtNameLayout.setErrorEnabled(false);
            return true;
        }
        else
        {
            Toast.makeText(this,"لطفا نوع پیام خود را مشخص نمایید",Toast.LENGTH_SHORT).show();
            return  false;

        }
    }
}
