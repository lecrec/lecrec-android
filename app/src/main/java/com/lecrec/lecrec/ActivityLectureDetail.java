package com.lecrec.lecrec;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lecrec.lecrec.consts.URL;
import com.lecrec.lecrec.custom.CustomActivityWithRecyclerView;
import com.lecrec.lecrec.models.Record;
import com.lecrec.lecrec.models.TextObject;
import com.lecrec.lecrec.utils.AppController;
import com.lecrec.lecrec.utils.CallUtils;
import com.lecrec.lecrec.utils.adapter.RecyclerAdapterText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


@EActivity(R.layout.activity_lecture_detail)
public class ActivityLectureDetail extends CustomActivityWithRecyclerView{
    private Record item;
    private List<TextObject> textObjects = new ArrayList<>();
    private String recordId, shareURL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isShowDivider = false;
        isInfiniteScroll = false;

        super.onCreate(savedInstanceState);

        recordId = getIntent().getStringExtra("record_id");
        shareURL = URL.SITE_URL + "?" + recordId;

        if(adapter == null) {
            adapter = new RecyclerAdapterText(this, textObjects);
        }
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
    }

    @Override
    protected void onRefresh() {

    }

    @Override
    protected void onItemClick(View view, int position) {

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
