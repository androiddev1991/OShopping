package com.example.android.shopping.Model;


import android.support.design.widget.FloatingActionButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by Android on 7/12/2016.
 */
public class user {


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }



    public int getUsId() {
        return usId;
    }

    public void setUsId(int usId) {
        this.usId = usId;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFavFilm() {
        return favFilm;
    }

    public void setFavFilm(String favFilm) {
        this.favFilm = favFilm;
    }

    public String getFavColor() {
        return favColor;
    }

    public void setFavColor(String favColor) {
        this.favColor = favColor;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    private int usId;
    private String Name;
    private String LastName;
    private String Email;
    private String Password;
    private String Image;
    private String birthDay;
    private String gender;
    private String favFilm;
    private String favColor;
    private String aboutMe;
    private String status;
    private ProgressBar pbar;
    private FloatingActionButton btnSend;
    private int admin;
    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }



    public ImageButton getImgBtnSend() {
        return imgBtnSend;
    }

    public void setImgBtnSend(ImageButton imgBtnSend) {
        this.imgBtnSend = imgBtnSend;
    }

    private ImageButton imgBtnSend;

}
