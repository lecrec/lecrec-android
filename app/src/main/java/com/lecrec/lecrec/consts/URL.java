package com.lecrec.lecrec.consts;
import com.lecrec.lecrec.BuildConfig;


public class URL {
    public static final String GET_API_URL() {
        final String url = BuildConfig.DEBUG ? "http://localhost:8000/api/" : "http://www.lecrec.com/api/";
        return url;
    }
}
