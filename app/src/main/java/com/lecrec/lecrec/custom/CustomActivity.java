package com.lecrec.lecrec.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.lecrec.lecrec.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class CustomActivity extends AppCompatActivity {
    protected boolean isAnimation = true;
    protected AlertDialog mAlertDialog;

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(isAnimation)
            this.overridePendingTransition(R.anim.animation_slide_cover_left_in, R.anim.animation_zoom_out);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();

        if(isAnimation)
            this.overridePendingTransition(R.anim.animation_zoom_in, R.anim.animation_slide_cover_right_out);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
