package com.lecrec.lecrec.utils;
import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;


public abstract class CallUtils<T> implements Callback<T> {
    private final String TAG = CallUtils.class.getSimpleName();
    private final Call<T> call;
    protected static final int TOTAL_RETRIES = 3;
    protected int retryCount = 0;

    public CallUtils(Call<T> call) {
        this.call = call;
    }

    public void onFailure(Throwable t) {
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "====== Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retry();
        } else {
            onComplete();
            Log.e(TAG, "====== Complete");
        }
        Log.d(TAG , "====== Retrofit log : " + t.getMessage());
    }

    private void retry() {
        call.clone().enqueue(this);
    }

    //not abstract! because onComplete is not required!!
    public void onComplete(){}
}