package com.lecrec.lecrec;

import android.Manifest;
import android.content.res.ColorStateList;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.lecrec.lecrec.custom.CustomActivityWithToolbar;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


@EActivity(R.layout.activity_record_voice)
public class ActivityRecordVoice extends CustomActivityWithToolbar implements AudioRecorder.OnErrorListener{
    @ViewById
    LinearLayout llBottomBar;
    @ViewById
    FloatingActionButton fabRec;
    @ViewById
    TextView tvComplete, tvDurationCurrent;
    @ViewById
    DonutProgress donutProgress;

    private TimeZone tz = TimeZone.getTimeZone("UTC");
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private int elapsedTime = 0;
    private Timer timer = new Timer();
    private AudioRecorder mAudioRecorder;
    private RxAudioPlayer mRxAudioPlayer;
    private File mAudioFile, mConvertedAudioFile;

    @AfterViews
    void afterViews(){
        setToolbar("강의녹음", 0, R.drawable.ic_arrow_back_white_24dp, 0, 0);

        df.setTimeZone(tz);

        mAudioRecorder = AudioRecorder.getInstance();
        mRxAudioPlayer = RxAudioPlayer.getInstance();
        mAudioRecorder.setOnErrorListener(this);

        boolean isPermissionsGranted = new RxPermissions(this)
                .isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                new RxPermissions(this)
                        .isGranted(Manifest.permission.RECORD_AUDIO);
        if (!isPermissionsGranted) {
            new RxPermissions(this)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            // not record first time to request permission
                            if (granted) {
                                Toast.makeText(getApplicationContext(), "Permission granted",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Permission not granted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } else {
            initRecorder();
        }
    }

    void initRecorder() {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "lecrec");
        if (!f.exists()) {
            f.mkdirs();
        }


    }

    boolean isInitRecorder, isRecording;

    @Click(R.id.fabRec)
    void clickRec() {
        if(!isInitRecorder) {
            isInitRecorder = true;
            mAudioFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "lecrec" + File.separator + System.nanoTime() + ".file.m4a");
            mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                    MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                    mAudioFile);
        }

        if(!isRecording) {
            isRecording = true;
            startTimer();
            mAudioRecorder.startRecord();
            fabRec.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            fabRec.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_stop_white_24dp));
        } else {
            isRecording = false;
            stopTimer();
            fabRec.setVisibility(View.INVISIBLE);
            tvComplete.setVisibility(View.VISIBLE);
            llBottomBar.setVisibility(View.VISIBLE);
            mAudioRecorder.stopRecord();
            convertToWav();
        }
    }

    void convertToWav() {
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                mConvertedAudioFile = convertedFile;
                Log.d("aaaaaaa", "success!!!!");
                Log.d("aaaaaaa", "success!!!!" + convertedFile.getAbsolutePath());
                Log.d("aaaaaaa", "success!!!!" + convertedFile.getName());
                convertedPlay();
            }
            @Override
            public void onFailure(Exception error) {
                Log.d("aaaaaaa", "fail!!!!");
            }
        };
        AndroidAudioConverter.with(this)
                .setFile(mAudioFile)
                .setFormat(AudioFormat.WAV)
                .setCallback(callback)
                .convert();
    }

    void convertedPlay() {
        mRxAudioPlayer.play(PlayConfig.file(mConvertedAudioFile).looping(false).build())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.d("AAAAAA","finish converted!!!!!");
                    }
                });

    }

    void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                elapsedTime += 1; //increase every sec
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }

    void stopTimer() {
        timer.cancel();
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            donutProgress.setProgress((float)(elapsedTime/7200.0*100 + 1));
            tvDurationCurrent.setText(df.format(new Date(elapsedTime  * 1000)));
        }
    };

    @Override
    public void onError(int error) {
        Toast.makeText(this, "Error code: " + error, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnLeft:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        mAudioRecorder.stopRecord();
        mRxAudioPlayer.stopPlay();
    }
 /*
    private void uploadFile(Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = AppController.getRecordService().createRecord(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }*/
}
