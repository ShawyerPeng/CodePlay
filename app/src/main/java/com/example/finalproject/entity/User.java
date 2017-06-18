package com.example.finalproject.entity;

public class User {
    private String faceurl;
    private String uname;
    private String urealname;
    private String uemail;
    private String utel;
    private String uidcard;
    private String usex;
    private String uindentity;
    private double uhonesty;
    private int uyear;
    private int umonth;
    private int uday;
    private boolean uadmin;

    public String getFaceurl() {
        return faceurl;
    }

    public void setFaceurl(String faceurl) {
        this.faceurl = faceurl;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUrealname() {
        return urealname;
    }

    public void setUrealname(String urealname) {
        this.urealname = urealname;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    public String getUidcard() {
        return uidcard;
    }

    public void setUidcard(String uidcard) {
        this.uidcard = uidcard;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getUindentity() {
        return uindentity;
    }

    public void setUindentity(String uindentity) {
        this.uindentity = uindentity;
    }

    public double getUhonesty() {
        return uhonesty;
    }

    public void setUhonesty(double uhonesty) {
        this.uhonesty = uhonesty;
    }

    public int getUyear() {
        return uyear;
    }

    public void setUyear(int uyear) {
        this.uyear = uyear;
    }

    public int getUmonth() {
        return umonth;
    }

    public void setUmonth(int umonth) {
        this.umonth = umonth;
    }

    public int getUday() {
        return uday;
    }

    public void setUday(int uday) {
        this.uday = uday;
    }

    public boolean isUadmin() {
        return uadmin;
    }

    public void setUadmin(boolean uadmin) {
        this.uadmin = uadmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "faceurl='" + faceurl + '\'' +
                ", uname='" + uname + '\'' +
                ", urealname='" + urealname + '\'' +
                ", uemail='" + uemail + '\'' +
                ", utel='" + utel + '\'' +
                ", uidcard='" + uidcard + '\'' +
                ", usex='" + usex + '\'' +
                ", uindentity='" + uindentity + '\'' +
                ", uhonesty=" + uhonesty +
                ", uyear=" + uyear +
                ", umonth=" + umonth +
                ", uday=" + uday +
                ", uadmin=" + uadmin +
                '}';
    }
}
