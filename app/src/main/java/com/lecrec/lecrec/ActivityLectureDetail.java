package com.lecrec.lecrec;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.lecrec.lecrec.consts.URL;
import com.lecrec.lecrec.custom.CustomActivityWithRecyclerView;
import com.lecrec.lecrec.models.Record;
import com.lecrec.lecrec.models.TextObject;
import com.lecrec.lecrec.utils.AppController;
import com.lecrec.lecrec.utils.CallUtils;
import com.lecrec.lecrec.utils.Utils;
import com.lecrec.lecrec.utils.adapter.RecyclerAdapterText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


@EActivity(R.layout.activity_lecture_detail)
public class ActivityLectureDetail extends CustomActivityWithRecyclerView {
    @ViewById
    TextView tvDurationCurrent, tvDurationMaximum;
    @ViewById
    ImageButton btnPlayPause;
    @ViewById
    ProgressBar progressBar;

    private Record item;
    private List<TextObject> textObjects = new ArrayList<>();
    private String recordId, shareURL;
    private RxAudioPlayer mRxAudioPlayer;

    private TimeZone tz = TimeZone.getTimeZone("UTC");
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private int elapsedTime = 0;
    private Timer timer;
    private File mAudioFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isShowDivider = false;
        isInfiniteScroll = false;

        super.onCreate(savedInstanceState);

        df.setTimeZone(tz);

        recordId = getIntent().getStringExtra("record_id");
        shareURL = URL.SITE_URL + "?" + recordId;

        if(adapter == null) {
            adapter = new RecyclerAdapterText(this, textObjects);
        }

        mRxAudioPlayer = RxAudioPlayer.getInstance();
    }

    @AfterViews
    void afterViews(){
        setToolbar("스크립트 보기", 0, R.drawable.ic_arrow_back_white_24dp, R.drawable.ic_search_white_24dp, 0);

        if(adapter == null)
            return;

        super.setupRecyclerView();

        loadData(0);
    }

    @Override
    protected void loadData(int page) {
        Call<Record> call = AppController.getRecordService().getRecord(AppController.USER_TOKEN, recordId);
        call.enqueue(new CallUtils<Record>(call) {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {
                if (response.isSuccessful() && response.body() != null) {
                    item = response.body();
                    try{
                        textObjects.addAll(item.getTextObjects());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    updateView();
                }
            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {
                Log.e("LECREC", "ERROR GET RECORD !!");
            }
        });
    }

    private void updateView() {
        adapter.notifyDataSetChanged();

        tvDurationMaximum.setText(item.getDuration());

        mAudioFile = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "lecrec" + File.separator + item.getFilename());

    }

    boolean isPlay = false;
    void convertedPlay() {
        if(mAudioFile.exists()) {
            if(mRxAudioPlayer.getMediaPlayer() == null) {
                if(isPlay) {
                    mRxAudioPlayer.stopPlay();
                } else {
                    mRxAudioPlayer.play(PlayConfig.file(mAudioFile).looping(false).build())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    audioStop();
                                }
                            });
                }
            } else {
                if(isPlay) {
                    mRxAudioPlayer.getMediaPlayer().pause();
                } else {
                    mRxAudioPlayer.getMediaPlayer().start();
                }
            }

            if(isPlay) {
                stopTimer();
            } else {
                startTimer();
            }

            isPlay = !isPlay;
        } else {
            Utils.showGlobalToast(ActivityLectureDetail.this, "음성 파일이 삭제되었습니다.");
        }
    }


    void audioStop() {
        isPlay = false;
        elapsedTime = 0;
        stopTimer();
        progressBar.setProgress(0);
        mRxAudioPlayer.stopPlay();
        try{
            tvDurationCurrent.setText("00:00:00");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onRefresh() {

    }

    @Override
    protected void onItemClick(View view, int position) {
        if(mRxAudioPlayer.getMediaPlayer() != null) {
            mRxAudioPlayer.getMediaPlayer().seekTo((int) Float.parseFloat(textObjects.get(position).getTime()) * 1000);
            elapsedTime = mRxAudioPlayer.getMediaPlayer().getCurrentPosition() / 1000;
        }
    }
    
    public void onClick(View v) throws KakaoParameterException {
        switch (v.getId()){
            case R.id.btnShare:
                makeLink();
                break;
            case R.id.btnLeft:
                finish();
                break;
            case R.id.btnRight:
                break;
            case R.id.btnPlayPause:
                convertedPlay();
                break;
            case R.id.btnStop:
                audioStop();
                break;
        }
    }

    void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                elapsedTime += 1;
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }

    void stopTimer() {
        if(timer != null) {
            timer.cancel();
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(mRxAudioPlayer.getMediaPlayer() != null) {
                int position = (int)((mRxAudioPlayer.getMediaPlayer().getCurrentPosition()/(float)mRxAudioPlayer.getMediaPlayer().getDuration())*100);
                progressBar.setProgress(position);
            }
            tvDurationCurrent.setText(df.format(new Date(elapsedTime  * 1000)));
        }
    };

    private void makeLink() throws KakaoParameterException {
        final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        kakaoTalkLinkMessageBuilder.addWebLink("[필기셔틀 레크레크]" + item.getTitle() + "\r\n" + "http://211.249.62.164/index.html?id=" + item.getId(), "http://211.249.62.164/index.html?id=" + item.getId());
        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        mRxAudioPlayer.stopPlay();
    }
}
