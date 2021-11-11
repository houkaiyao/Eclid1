package com.example.eclid1.Bean;

import java.io.Serializable;

public class UploadBean implements Serializable {


    /**
     * code : 200
     * msg : OK
     * image_adress : https://api.sziep.xyz/image/Lost/17715501187/1632984440.jpg
     */

    private int code;
    private String msg;
    private String image_adress;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImage_adress() {
        return image_adress;
    }

    public void setImage_adress(String image_adress) {
        this.image_adress = image_adress;
    }
}
