package com.example.android.shopping.Activities;

import android.content.Intent;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.example.android.shopping.Fragment.UserProfile.FragmentSuggestionReview;

import com.example.android.shopping.Fragment.UserProfile.FragmentCreateProducts;
import com.example.android.shopping.Fragment.UserProfile.FragmentProfile;
import com.example.android.shopping.Fragment.UserProfile.Fragment_ProductList;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Model.Products;
import com.example.android.shopping.R;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.user;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static user CurrentUserID;
    private  String currentImage;


    public ImageView imgShopping;
    public boolean adminPrevilage=true;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DrawerLayout mDrawer;


    //public static List<Products> allProductList;
    public static List<Products> listOfProducts;
    private int[] tabIcons={
            R.drawable.product_add_icon,
            R.drawable.review_icon,
            R.drawable.product_icon,
            R.drawable.contacts_icon

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        //myUser is an method in UIHelper which help us to deserialize login user data in json format
        //and send back as object
        CurrentUserID=UIHelper.myUser(this);

        //CurrentUserID=feedUser();
        Log.d("AdminPreve","it is: "+CurrentUserID.getAdmin());

        //check if user is admin or not that display views base on user authorization
        if(CurrentUserID.getAdmin()==0) {
            Log.d("AdminPreve","it compared as zero");
            adminPrevilage = false;
            RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.nav_Modiriat_visibility);
            relativeLayout.setVisibility(View.GONE);
        }
        else if(CurrentUserID.getAdmin()==1) {
            Log.d("AdminPreve","it compared as one which is true");
            adminPrevilage = true;

        }
         mDrawer=(DrawerLayout)findViewById(R.id.drawerId);
        //view pages customization
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.getTabAt(0).select();



//fill data
        UIHelper.SetImage(R.id.imgUserProfile,this);
        UIHelper.setImagePicasso(CurrentUserID.getUsId(),this);
        UIHelper.setTvText(this,R.id.txtmainTopProfile_name,CurrentUserID.getName()+ " "+CurrentUserID.getLastName());
        CircleImageView profileImage=(CircleImageView)findViewById(R.id.nav_UserProfilePic);
        Log.d("ProfileImage",Tags.Address+"shopping/uploads/profile/"+CurrentUserID.getUsId()+"temp.jpg");
        Picasso.with(this)

                .load(Tags.Address+"shopping/uploads/profile/"+CurrentUserID.getUsId()+"/temp.jpg")

                .into(profileImage);

        UIHelper.setTvText(this,R.id.nav_txtUserEmail,CurrentUserID.getEmail());
    }


    public user feedUser()
    {
        user myus=new user();
        myus.setName("sara");
        myus.setUsId(75);
        myus.setGender("zan");
        myus.setBirthDay("221112");
        myus.setLastName("aple sara");
        myus.setEmail("Sara@YAHOO.COM");
        myus.setAboutMe("assdasd");
        myus.setAdmin(1);
        return myus;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null && item.getItemId() == R.id.btnMyMenu) {
            if (mDrawer.isDrawerOpen(Gravity.RIGHT)) {
                mDrawer.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }
        return false;
    }
    private void setupTabIcons() {
        if (adminPrevilage){
            tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //checking for user previlage
        if(adminPrevilage) {
            adapter.addFragment(new FragmentCreateProducts(), "ثبت");
            adapter.addFragment(new FragmentSuggestionReview(), "بازبینی");
        }
        adapter.addFragment(new Fragment_ProductList(), "محصولات");
        adapter.addFragment(new FragmentProfile(), "پروفایل ");
        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void PickDate(View view)
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                UserProfileActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        FragmentProfile.setText(Integer.toString(year)+"/"+Integer.toString(monthOfYear)+"/"+Integer.toString(dayOfMonth));
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }






    public void onClick_profileImage(View view)
    {
        currentImage="profileImage";
        imgShopping =FragmentProfile.getProfileImage();

    }

  public void openAcountManager(View view)
  {
      Intent intent=new Intent(this,ManageUser.class);
      startActivity(intent);
  }
    public void onclick_AboutUS(View view)
    {
        Intent intent=new Intent(this,AboutUs.class);
        startActivity(intent);
    }

    public void openTouristActivity(View view)
    {
        Intent intent=new Intent(this,ContactUs.class);
        startActivity(intent);
    }
    public void exitApp(View view)
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}
