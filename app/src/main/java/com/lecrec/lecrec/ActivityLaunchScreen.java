package com.lecrec.lecrec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lecrec.lecrec.consts.CONST;
import com.lecrec.lecrec.custom.CustomActivity;
import com.lecrec.lecrec.models.User;
import com.lecrec.lecrec.utils.AppController;
import com.lecrec.lecrec.utils.CallUtils;
import com.lecrec.lecrec.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

@EActivity(R.layout.activity_launch_screen)
public class ActivityLaunchScreen extends CustomActivity {
    @ViewById
    LinearLayout llLoginWrapper;
    @ViewById
    ImageView ivLaunchLogoTitle;
    @ViewById
    LoginButton btnRegisterFacebook;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if(AppController.USER_ID != null && AppController.USER_TOKEN != null) {
            new CountDownTimer(1000, 100) {
                public void onTick(long millisUntilFinished) { }
                public void onFinish() {
                    goActivityMain();
                }
            }.start();
        } else {
            changeToLoginView();
        }

        pref = getSharedPreferences(CONST.PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    @AfterViews
    void init(){
        initSocialFacebook();
    }

    private void initSocialFacebook(){
        btnRegisterFacebook.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        btnRegisterFacebook.registerCallback(callbackManager, callback);
        btnRegisterFacebook.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    public void onResume(){
        super.onResume();
        facebookLogout();
    }

    private void facebookLogout(){
        try {
            LoginManager.getInstance().logOut();
        }catch (Exception e){ }
    }


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            String socialId = null, name = null;
                            try {
                                socialId = object.getString("id");
                                name = object.getString("name");
                            }catch (Exception e){}

                            register(socialId, name);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name");
            request.setParameters(parameters);
            request.executeAsync();
        }
        @Override
        public void onCancel() {
            Utils.showGlobalToast(ActivityLaunchScreen.this, "로그인을 취소 하였습니다!");
        }
        @Override
        public void onError(FacebookException e) {
            Utils.showGlobalToast(ActivityLaunchScreen.this, "소셜 로그인 오류입니다.");
        }
    };

    private void register(final String socialId, String name){
        Log.d("asdfsafasdf","asfasdf " + socialId + " / " + name);
        Call<User> call = AppController.getUserService().getUserOrCreate(socialId, name);
        call.enqueue(new CallUtils<User>(call) {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppController.USER_ID = response.body().getUserId();
                    AppController.USER_TOKEN = "JWT " + response.body().getToken();
                    AppController.USER_NAME = response.body().getUserName();

                    saveUserDataAndGoNext();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("LECREC", "ERROR SOCIAL LOGIN : " + t.getMessage());
                Log.e("LECREC", "ERROR SOCIAL LOGIN : " + t.getStackTrace());
            }
        });
    }

    private void saveUserDataAndGoNext(){
        editor.putString("user_id", AppController.USER_ID);
        editor.putString("user_token", AppController.USER_TOKEN);
        editor.putString("user_name", AppController.USER_NAME);
        editor.commit();

        goActivityMain();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void goActivityMain() {
        startActivity(new Intent(ActivityLaunchScreen.this, ActivityMain_.class));
        finish();
    }

    void changeToLoginView() {

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                ivLaunchLogoTitle.setVisibility(View.GONE);
                llLoginWrapper.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivLaunchLogoTitle.startAnimation(fadeOut);
            }
        }, 2000);
    }

    @Click(R.id.btnKakao)
    void clickKakao() {
        goActivityMain();
    }
}
