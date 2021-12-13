package com.scrapper.i170303_i170364_project;

public class SearchModel
{
    private String header;
    private String desc;
    private String imgname;

    public SearchModel(String header, String desc, String imgname) {
        this.header = header;
        this.desc = desc;
        this.imgname = imgname;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }
}
