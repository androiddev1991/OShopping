package com.example.android.shopping;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.shopping.Activities.LoginActivity;
import com.example.android.shopping.Activities.RegisterUserActivity;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.android.shopping.R.layout.activity_main);
    }
    public void clickLogin(View view)
    {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void RegForm(View view)
    {

        Intent intent=new Intent(this,RegisterUserActivity.class);
        startActivity(intent);

    }

}
