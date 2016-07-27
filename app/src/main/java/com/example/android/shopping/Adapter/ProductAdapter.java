package com.example.android.shopping.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.shopping.Model.Products;
import com.example.android.shopping.R;
import com.example.android.shopping.Helper.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.UserViewHolder> implements RecyclerView.OnClickListener
{


    private List<Products> productsList;
    private static Context mContext;
//
    public static String productid;
    public static String productdesc;
    public static String ProductQuantity;

    public ProductAdapter(List<Products> productsList, Context context) {
        this.productsList = productsList;
        this.mContext=context;

    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.product_recycler_cardview, viewGroup, false);

        return new UserViewHolder(itemView);
    }
    @Override
    public int getItemCount() {

        return productsList.size();
    }

    @Override
    public void onBindViewHolder(UserViewHolder contactViewHolder, int i) {
        Products myproduct = productsList.get(i);
        productdesc=myproduct.getProductDesc();
         productid=myproduct.getProductId();
        ProductQuantity= myproduct.getQuantity();
        contactViewHolder.vName.setText(myproduct.getProductName());
        contactViewHolder.vPrice.setText(myproduct.getProductPrice());

        contactViewHolder.vQuantity.setText(String.valueOf(ProductQuantity));

        Picasso.with(mContext)
                .load(Tags.Address+"shopping/uploads/Product/"+myproduct.getProductId()+"/product.jpg")
                .centerCrop().resize(60,60)
                .into(contactViewHolder.vImage);
        Log.d("ProductImage",Tags.Address+"shopping/uploads/Product/"+myproduct.getProductId()+"/product.jpg");
    }

    @Override
    public void onClick(View v) {
        Log.d("clickedItems","my itemclicked");
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        protected TextView vName;
        protected TextView vPrice;
        protected TextView vQuantity;
        protected CircleImageView vImage;


        public UserViewHolder(View v) {
            super(v);

        vName =  (TextView) v.findViewById(R.id.txtproduct_name);
        vPrice = (TextView)  v.findViewById(R.id.txtproduct_price);
        vQuantity=(TextView)v.findViewById(R.id.txtProduct_Quantity) ;
        vImage = (CircleImageView) v.findViewById(R.id.img_product_list);

//        v.setOnClickListener(this);
    }


        @Override
        public void onClick(View v) {


        }
    }
}