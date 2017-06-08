package com.example.finalproject;

public class User {
    private String uindentity;
    private String usex;
    private String uname;
    private String uhonesty;
    private String uemail;

    public String getUindentity() {
        return uindentity;
    }

    public void setUindentity(String uindentity) {
        this.uindentity = uindentity;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUhonesty() {
        return uhonesty;
    }

    public void setUhonesty(String uhonesty) {
        this.uhonesty = uhonesty;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    @Override
    public String toString() {
        return "User{" +
                "uindentity='" + uindentity + '\'' +
                ", usex='" + usex + '\'' +
                ", uname='" + uname + '\'' +
                ", uhonesty='" + uhonesty + '\'' +
                ", uemail='" + uemail + '\'' +
                '}';
    }
}
