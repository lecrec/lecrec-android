package com.lecrec.lecrec.consts;
import com.lecrec.lecrec.BuildConfig;


public class URL {
    public static final String GET_API_URL() {
        final String url = BuildConfig.DEBUG ? "http://211.249.62.164/api/" : "http://211.249.62.164/api/";
        return url;
    }

    public static final String SITE_URL = "http://211.249.62.164/";
}
