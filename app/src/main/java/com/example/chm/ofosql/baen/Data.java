package com.example.chm.ofosql.baen;

/**
 * Created by asus1 on 2016/12/9.
 */

public class Data {
    String id;
    String pwd;

    public Data(String id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
