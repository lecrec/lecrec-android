package com.lecrec.lecrec.custom;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.lecrec.lecrec.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public abstract class CustomActivityWithToolbar extends CustomActivity{
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected ImageButton btnLeft, btnRight, btnRightSide;
    @ViewById
    protected TextView tvToolbarTitle;

    @AfterViews
    protected void initToolbar(){
        btnLeft.setImageDrawable(null);
        btnRight.setImageDrawable(null);
        btnRightSide.setImageDrawable(null);
    }

    protected void setToolbar(String title, int backgroundColor, int left, int right, int rightSide){
        btnLeft.setImageResource(0);
        btnRight.setImageResource(0);
        btnRightSide.setImageResource(0);

        if(backgroundColor == 0){
            backgroundColor = R.color.colorPrimary;
        }

        toolbar.setBackgroundResource(backgroundColor);

        if(left > 0) {
            btnLeft.setVisibility(View.VISIBLE);
            btnLeft.setImageResource(left);
        }
        if(right > 0) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setImageResource(right);
        }

        if(rightSide > 0) {
            btnRightSide.setVisibility(View.VISIBLE);
            btnRightSide.setImageResource(rightSide);
        }

        if(title != null){
            tvToolbarTitle.setText(title);
            if(backgroundColor == R.color.colorPrimary){
                tvToolbarTitle.setTextColor(getResources().getColor(R.color.white));
            }else{
                tvToolbarTitle.setTextColor(getResources().getColor(R.color.dark_gray));
            }
        }
    }
}
