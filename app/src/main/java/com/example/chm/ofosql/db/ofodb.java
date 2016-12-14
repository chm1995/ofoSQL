package com.example.chm.ofosql.db;

import org.litepal.crud.DataSupport;

/**
 * Created by asus1 on 2016/12/8.
 */

public class ofodb extends DataSupport {

    private String ofoid;
    private String pwd;


    public String getOfoid() {
        return ofoid;
    }

    public void setOfoid(String ofoid) {
        this.ofoid = ofoid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


}
