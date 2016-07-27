package com.example.android.shopping.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Android on 7/19/2016.
 */
public class Suggestion implements Parcelable {
    private int cardid;
    private int productid;
    private  String ProductName;
    private int Varify;
    private int orderid;
    private String UserName;
    private String UserLastName;
    private String Email;
    private String Status;
    private String Suggest;
    private int productcounts;
    private int userid;

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public static Creator<Suggestion> getCREATOR() {
        return CREATOR;
    }

    private String productPrice;
    public Suggestion(){}
    public Suggestion(Parcel in) {
        cardid = in.readInt();
        productid = in.readInt();
        ProductName = in.readString();
        Varify = in.readInt();
        orderid = in.readInt();
        UserName = in.readString();
        UserLastName = in.readString();
        Email = in.readString();
        Status = in.readString();
        Suggest = in.readString();
        productcounts = in.readInt();
        userid = in.readInt();
        productPrice=in.readString();
    }

    public static final Creator<Suggestion> CREATOR = new Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getVarify() {
        return Varify;
    }

    public void setVarify(int varify) {
        Varify = varify;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserLastName() {
        return UserLastName;
    }

    public void setUserLastName(String userLastName) {
        UserLastName = userLastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }



    public String getSuggest() {
        return Suggest;
    }

    public void setSuggest(String suggest) {
        Suggest = suggest;
    }



    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public int getCardid() {
        return cardid;
    }

    public void setCardid(int cardid) {
        this.cardid = cardid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getProductcounts() {
        return productcounts;
    }

    public void setProductcounts(int productcounts) {
        this.productcounts = productcounts;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cardid);
        dest.writeInt(productid);
        dest.writeString(ProductName);
        dest.writeInt(Varify);
        dest.writeInt(orderid);
        dest.writeString(UserName);
        dest.writeString(UserLastName);
        dest.writeString(Email);
        dest.writeString(Status);
        dest.writeString(Suggest);
        dest.writeInt(productcounts);
        dest.writeInt(userid);
        dest.writeString(productPrice);
    }
}
