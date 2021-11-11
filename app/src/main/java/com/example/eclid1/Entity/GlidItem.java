package com.example.eclid1.Entity;

public class GlidItem {
    private int iId;
    private String iName;

    public GlidItem() {
    }

    public GlidItem(int iId, String iName) {
        this.iId = iId;
        this.iName = iName;
    }

    public int getiId() {
        return iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
