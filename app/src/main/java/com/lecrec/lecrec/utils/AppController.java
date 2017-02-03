package com.lecrec.lecrec.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.lecrec.lecrec.R;
import com.lecrec.lecrec.consts.CONST;
import com.lecrec.lecrec.consts.URL;
import com.lecrec.lecrec.models.User;
import com.lecrec.lecrec.services.UserService;
import com.lecrec.lecrec.utils.helper.PrimitiveConverterFactory;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppController extends Application {
    private static volatile AppController instance = null;
    private static volatile Activity currentActivity = null;

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    public static float SCREEN_DENSITY = 0;
    public static User USER = null;
    public static String USER_ID = null;
    public static String USER_MY_LIST_ID = null;
    public static String USER_TOKEN = null;

    // retrofit and interface
    private static Retrofit retrofit;
    private static UserService userService;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        if (USER_ID == null) {
            USER_ID = getSharedPreferences(CONST.PREF_NAME, MODE_PRIVATE).getString("user_id", null);
            USER_TOKEN = getSharedPreferences(CONST.PREF_NAME, MODE_PRIVATE).getString("user_token", null);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        SCREEN_DENSITY = displayMetrics.density;

        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(Exception error) {
                // FFmpeg is not supported by device
            }
        });

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    static{
        USER = new User();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL.GET_API_URL())
                .addConverterFactory(PrimitiveConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
    }

    public static UserService getUserService(){ return userService; }

    private static class KakaoSDKAdapter extends KakaoAdapter {
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Activity getTopActivity() {
                    return AppController.getCurrentActivity();
                }

                @Override
                public Context getApplicationContext() {
                    return AppController.getGlobalApplicationContext();
                }
            };
        }
    }

    public static AppController getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }
    public static void setCurrentActivity(Activity currentActivity) {
        AppController.currentActivity = currentActivity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
