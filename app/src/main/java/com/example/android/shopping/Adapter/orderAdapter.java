package com.example.android.shopping.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.shopping.Activities.SuggestionSingleView;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Model.Suggestion;
import com.example.android.shopping.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Android on 7/19/2016.
 */
public class orderAdapter extends RecyclerView.Adapter<orderAdapter.OrderViewHolder> {

    private List<Suggestion> SuggestList;
    private Context mContext;
    private static Suggestion mySuggestion;

    public orderAdapter(List<Suggestion> SuggestList, Context context) {
        //set suggestlist that is list of data retrieved frm server
        this.SuggestList = SuggestList;
        this.mContext = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.review_suggestion_cardview, viewGroup, false);

        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Suggestion obSuggest = SuggestList.get(position);
        holder.txtUserName_suggest.setText(obSuggest.getUserName() + obSuggest.getUserLastName());
        holder.txtproductName_Suggested.setText(obSuggest.getProductName());
        holder.vsuggestID.setText(String.valueOf(obSuggest.getOrderid()));
        mySuggestion = obSuggest;
        Picasso.with(mContext)
                //.load(Tags.Address+"/uploads/Rooms/"+obChatroom.getProductId()+"/imgRoom.jpg")
                .load(Tags.Address+"shopping/uploads/profile/"+mySuggestion.getUserid()+"/temp.jpg")
                .centerCrop().resize(96, 96)
                .into(holder.vSuggestImage);
        Log.d("OrderSuggest",Tags.Address+"shopping/uploads/profile/"+mySuggestion.getUserid()+"/temp.jpg");
        Picasso.with(mContext)
                .load(Tags.Address+"shopping/uploads/Product/"+mySuggestion.getProductid()+"/product.jpg")
                .centerCrop().resize(96, 96)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return SuggestList.size();
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {


        protected TextView txtUserName_suggest;
        protected TextView txtproductName_Suggested;
        protected TextView vsuggestID;
        protected CircleImageView vSuggestImage;
        protected ImageView imgProduct;

        public OrderViewHolder(View v) {
            super(v);


            vSuggestImage = (CircleImageView) v.findViewById(R.id.imgUsersuggested);
            txtproductName_Suggested = (TextView) v.findViewById(R.id.txtproductName_Suggested);
            vsuggestID = (TextView) v.findViewById(R.id.txtProduct_id_suggested);
            imgProduct = (ImageView) v.findViewById(R.id.productImage_suggested);

            txtUserName_suggest = (TextView) v.findViewById(R.id.txtUserName_suggest);
            //v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
