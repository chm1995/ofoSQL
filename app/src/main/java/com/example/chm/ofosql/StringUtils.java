package com.example.chm.ofosql;

/**
 * 创建者:   陈海明
 * 创建时间:  2016/10/16 21:18
 * 描述：   String判断工具
 */
public class StringUtils {

    private static final String OFO_ID_REGEX = "^[0-9]{6}$";

    private static final String PASSWORD_REGEX = "^[0-9]{4}$";


    public static boolean checkID(String ofoid) {
        return ofoid.matches(OFO_ID_REGEX);
    }

    public static boolean checkPassword(String pwd) {
        return pwd.matches(PASSWORD_REGEX);
    }


}
