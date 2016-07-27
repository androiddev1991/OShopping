package com.example.android.shopping.Fragment.UserProfile;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping.Activities.ProductSingleView;
import com.example.android.shopping.Activities.UserProfileActivity;
import com.example.android.shopping.Helper.Tags;
import com.example.android.shopping.Helper.UIHelper;
import com.example.android.shopping.Model.Products;
import com.example.android.shopping.R;
import com.example.android.shopping.Adapter.ProductAdapter;
import com.example.android.shopping.interfaces.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ProductList extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

private   RecyclerView recList;
    public ProgressBar waiting;
    View myView;

    private List<Products> productsList=new ArrayList<>();//share list of product that getting from server
    public Fragment_ProductList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //productsList=UserProfileActivity.getALlProduct();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_product_list, container, false);





        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(!UIHelper.isNetworkAvailable(getActivity())) {

            Toast.makeText(getActivity(),"به اینترنت متصل نمی باشید",Toast.LENGTH_SHORT).show();
            return;
        }
        super.onViewCreated(view, savedInstanceState);
        //set and update recyclerview
        myView =view;
        waiting=(ProgressBar)view.findViewById(R.id.porductListWaiting);

        recList = (RecyclerView) myView.findViewById(R.id.recycerListUser);
        recList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),this));

        SelectAllProductNetworkTask AllProduct=new SelectAllProductNetworkTask();
        AllProduct.execute();

    }

    @Override
    public void onItemClick(View childView, int position) {
        //save clicked item in intent then sent to product single view
        Products product = productsList.get(position);
        Intent intent=new Intent(getActivity(), ProductSingleView.class);
        intent.putExtra("name",product.getProductName());
        intent.putExtra("price",product.getProductPrice());
        intent.putExtra("productId",product.getProductId());
        intent.putExtra("desc",product.getProductDesc());

        intent.putExtra("UserId", String.valueOf(UserProfileActivity.CurrentUserID.getUsId()));
        intent.putExtra("Quantity",product.getQuantity());

        getActivity().startActivity(intent);
//        getActivity().startActivity(new Intent(getActivity(),ProductSingleView.class)
//                .putExtra("Products",new Gson().toJson(Products))
//                .putExtra("userid", String.valueOf(UserProfileActivity.CurrentUserID.getUsId()))
//        );


    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }


    public class SelectAllProductNetworkTask extends AsyncTask<String,Void,String> {



        public  String Result;

        private OkHttpClient client=new OkHttpClient();




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waiting.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody requestBody;
                    requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("action","selectAll")

                            .build();

                    Request request = new Request.Builder()
                            .url(Tags.ProductAddress)
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();

                    Result=response.body().string();
                    return  Result;


            }  catch (IOException e) {
                e.printStackTrace();
            }


            return  null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            waiting.setVisibility(View.GONE);
            if(o!=null) {
//fill retrieved data from json to myobject,
                try {
                    JSONObject jsonObject = new JSONObject(Result);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jobject = jsonArray.getJSONObject(i);
                        Products myProducts = new Products();
                        myProducts.setProductId(String.valueOf(jobject.getInt("id")));
                        myProducts.setProductName(jobject.getString("name"));
                        myProducts.setProductDesc(jobject.getString("productdesc"));
                        myProducts.setProductPrice(jobject.getString("productPrice"));
                        myProducts.setDate(jobject.getString("productdate"));
                        myProducts.setQuantity(jobject.getString("productcount"));

                        productsList.add(myProducts);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





                recList.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);

                ProductAdapter ca = new ProductAdapter(productsList, getContext());
                recList.setAdapter(ca);
            }
            else
            {

            }
        }
    }

}
