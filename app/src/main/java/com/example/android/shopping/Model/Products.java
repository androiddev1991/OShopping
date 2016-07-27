package com.example.android.shopping.Model;

import android.support.design.widget.FloatingActionButton;
import android.widget.ProgressBar;

/**
 * Created by Android on 7/12/2016.
 */
public class Products {
    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        this.ProductId = productId;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        this.ProductDesc = productDesc;
    }

    public int getProductImage() {
        return ProductImage;
    }

    public void setProductImage(int productImage) {
        this.ProductImage = productImage;
    }

    private String ProductName;
    private String ProductId;
    private String ProductDesc;
    private int ProductImage;

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    private String Quantity;
    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    private String ProductPrice;

    private ProgressBar pbar;

    private String date;
    private FloatingActionButton btnSend;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    public FloatingActionButton getBtnSend() {
        return btnSend;
    }

    public void setBtnSend(FloatingActionButton btnSend) {
        this.btnSend = btnSend;
    }

    public ProgressBar getPbar() {
        return pbar;
    }

    public void setPbar(ProgressBar pbar) {
        this.pbar = pbar;
    }


}
