package com.lecrec.lecrec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.lecrec.lecrec.utils.AppController;
import com.lecrec.lecrec.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Arrays;

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
    private String imageUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if(AppController.USER_ID != null && AppController.USER_TOKEN != null) {
            goActivityMain();
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
                            String socialId = null, name = null, accessToken = null;
                            try {
                                socialId = object.getString("id");
                                name = object.getString("name");
                                accessToken = object.getString("access_token");
                            }catch (Exception e){}

                            imageUrl = "http://graph.facebook.com/" + socialId + "/picture?type=large";

                            if(accessToken == null)
                                accessToken = socialId;

                            register(
                                    socialId,
                                    name,
                                    "FB",
                                    accessToken
                            );
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
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

    private void register(final String socialId, String name, String socialType, final String accessToken){
//
//        Call<User> call = AppController.getAccountService().register(socialId, name, accessToken + "_p", "AN", socialId, socialType, accessToken, false);
//        call.enqueue(new CallUtils<User>(call, this, getResources().getString(R.string.msgErrorCommon)) {
//            @Override
//            public void onResponse(Response<User> response) {
//                if (response.isSuccess() && response.body() != null && response.body().getResult() != null) {
//                    AppController.AUTHORIZATION = "JWT " + response.body().getToken();
//
//                    editor.putString("authorization", response.body().getToken());
//                    editor.putString("email", socialId);
//                    editor.putString("password", accessToken + "_p");
//                    editor.putBoolean("is_social", true);
//                    editor.apply();
//
//                    if (response.body().getType().equals("login") || response.body().getType().equals("registration")) {
//                        if (pref.getBoolean(CONST.PREF_AGREE + socialId, false)) {
//                            getUser(GO_MAIN);
//                        } else {
//                            getUser(GO_AGREEMENT);
//                        }
//                    } else {
//                        super.showSingleDialog(msg);
//                    }
//                } else {
//                    facebookLogout();
//                    super.showSingleDialog(msg);
//                }
//            }
//        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void goActivityMain() {
        new CountDownTimer(1000, 100) {
            public void onTick(long millisUntilFinished) {
                //nothing
            }

            public void onFinish() {
                startActivity(new Intent(ActivityLaunchScreen.this, ActivityMain_.class));
                finish();
            }
        }.start();

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
        startActivity(new Intent(ActivityLaunchScreen.this, ActivityMain_.class));
        finish();
    }
}
