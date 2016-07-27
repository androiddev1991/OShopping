package com.example.android.shopping.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.Products;
import com.example.android.shopping.Model.user;
import com.example.android.shopping.R;
import com.example.android.shopping.Helper.Tags;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<user> UserList;
    private Context mContext;
    public UserAdapter(List<user> UserList, Context context) {
        this.UserList = UserList;
        this.mContext=context;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.user_recycler_cardview, viewGroup, false);

        return new UserViewHolder(itemView);
    }
    @Override
    public int getItemCount() {
        return UserList.size();
    }

    @Override
    public void onBindViewHolder(UserViewHolder userviewHolder, int i) {
        user obMyUser = UserList.get(i);
        userviewHolder.vUserGener.setText(obMyUser.getGender().toString());
        userviewHolder.vUserName.setText(obMyUser.getName()+" "+obMyUser.getLastName());
        userviewHolder.vUserEmail.setText(obMyUser.getEmail());
        if(obMyUser.getAdmin()==1)
            userviewHolder.vUserAdmin.setText("مدیر");
        else
            userviewHolder.vUserAdmin.setText("ثبت مدیر");
        Log.d("ImageProfile",Tags.Address+"shopping/uploads/profile/"+obMyUser.getUsId()+"/temp.jpg");
        Picasso.with(mContext)

                .load(Tags.Address+"shopping/uploads/profile/"+obMyUser.getUsId()+"/temp.jpg")
                .centerCrop().resize(70,70)
                .into(userviewHolder.vUserImage);
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {


        protected TextView vUserGener;
        protected TextView vUserName;
        protected TextView vUserEmail;
        protected TextView vUserAdmin;
        protected CircleImageView vUserImage;

        public UserViewHolder(View v) {
            super(v);
            vUserGener =  (TextView) v.findViewById(R.id.txtUserGener);
            vUserName = (TextView)  v.findViewById(R.id.txtUserName_lsUser);
            vUserEmail = (TextView)  v.findViewById(R.id.txtlstEmail);
            vUserAdmin = (TextView)  v.findViewById(R.id.txtMadeUserAdmin);
            vUserImage = (CircleImageView) v.findViewById(R.id.img_user_list);
        }
    }
}
