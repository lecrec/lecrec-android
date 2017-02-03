package com.lecrec.lecrec;

import android.view.View;

import com.lecrec.lecrec.custom.CustomActivityWithToolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_lecture_detail)
public class ActivityLectureDetail extends CustomActivityWithToolbar{
    @AfterViews
    void afterViews(){
        setToolbar("스크립트 보기", 0, R.drawable.ic_arrow_back_white_24dp, R.drawable.ic_search_white_24dp, 0);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnLeft:
                finish();
                break;
            case R.id.btnRight:

                break;
        }
    }
}
