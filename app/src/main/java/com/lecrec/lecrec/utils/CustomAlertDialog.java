package com.lecrec.lecrec.utils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lecrec.lecrec.R;


public class CustomAlertDialog extends Dialog {

    private TextView mContentView;
    private Button mLeftButton, mRightButton, mOkButton;
    private String mContent;
    private String btnLeftText, btnRightText, btnOkText;
    private int openLinearId = 0;
    private Context mContext;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( !(mContext instanceof Activity) || ((Activity) mContext).isFinishing()){
            return;
        }

        // 다이얼로그 외부 화면 흐리게 표현 
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.7f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.layout_custom_alert_dialog);

        mContentView = (TextView) findViewById(R.id.txtContent);
        mContentView.setText(mContent);

        try {
            LinearLayout ll = (LinearLayout) findViewById(openLinearId);
            ll.setVisibility(View.VISIBLE);
            if (mLeftClickListener != null && mRightClickListener != null) { // confirm
                mLeftButton = (Button) findViewById(R.id.btnLeft);
                mRightButton = (Button) findViewById(R.id.btnRight);

                mLeftButton.setText(btnLeftText);
                mRightButton.setText(btnRightText);

                mLeftButton.setOnClickListener(mLeftClickListener);
                mRightButton.setOnClickListener(mRightClickListener);
            } else if (mLeftClickListener != null && mRightClickListener == null) { //single
                mOkButton = (Button) findViewById(R.id.btnOk);
                mOkButton.setText(btnOkText);
                mOkButton.setOnClickListener(mLeftClickListener);
            }
        }catch (Exception e){}
    }

    // single
    public CustomAlertDialog(Context context, String content, String okText,
                             View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
        this.mContent = content;
        this.mLeftClickListener = singleListener;
        this.openLinearId = R.id.llTypeOne;
        this.btnOkText = okText;
    }

    // double
    public CustomAlertDialog(Context context, String content, String leftText, String rightText,
                             View.OnClickListener leftListener,
                             View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
        this.openLinearId = R.id.llTypeTwo;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.btnLeftText = leftText;
        this.btnRightText = rightText;
    }
}