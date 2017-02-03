package com.lecrec.lecrec;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lecrec.lecrec.custom.CustomActivityWithRecyclerView;
import com.lecrec.lecrec.models.Record;
import com.lecrec.lecrec.utils.AppController;
import com.lecrec.lecrec.utils.adapter.RecyclerAdapterRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class ActivityMain extends CustomActivityWithRecyclerView implements NavigationView.OnNavigationItemSelectedListener {
    @ViewById
    DrawerLayout drawerLayout;
    @ViewById
    NavigationView navigationView;
    @ViewById
    LinearLayout llBgWrapper;
    @ViewById
    ImageView ivBgRecord;

    private int firstPosition = 0;
    private List<Record> items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isGridType = true;
        isShowDivider = false;

        super.onCreate(savedInstanceState);

        if(adapter == null) {
            adapter = new RecyclerAdapterRecord(this, items);
        }

        initTestRecords();
    }

    void initTestRecords() {
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, true, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, true, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, true, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, true, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, true, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, true, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", false, false, null));
        items.add(new Record("0", "모바일 UX 디자인 3주차", "mobile.m4a", "안녕하세요.", "44:21", "2017-02-04 12:00:12", "2017-02-04 12:00:12", true, false, null));
    }

    @AfterViews
    void afterViews(){
        setToolbar("레크레크", 0, R.drawable.ic_menu_white_24dp, R.drawable.ic_folder_open_white_24dp, 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMain.this, ActivityRecordVoice_.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        if(adapter == null)
            return;

        setupRecyclerView();
        super.setupSwipeRefreshLayout();

        if(items.size() == 0) {
            loadData(0);
        }
    }

    protected void setupRecyclerView(){
        super.setupRecyclerView();
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnLeft:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btnRight:
                break;
        }
    }

    @Override
    protected void loadData(int page) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        if(page == 0)
            items.clear();

        initTestRecords();
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onRefresh() {
        loadData(0);
    }

    @Override
    protected void onItemClick(View view, int position) {
        Intent intent = new Intent(ActivityMain.this, ActivityLectureDetail_.class);
        intent.putExtra("record_id", items.get(position).getId());
        startActivity(intent);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            View view = recyclerView.getChildAt(0);
            Log.d("aaaaaa", "top : " + view.getTop());
            Log.d("aaaaaa", "now : " + mLinearLayoutManager.findFirstVisibleItemPosition());
            firstPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (view != null && (firstPosition < 4 || (firstPosition == 4 && view.getTop() < -135))) {
                llBgWrapper.setVisibility(View.VISIBLE);
                llBgWrapper.setTranslationY(view.getTop() - llBgWrapper.getHeight() + (40 * AppController.SCREEN_DENSITY));
                ivBgRecord.setTranslationY((-(view.getTop() - llBgWrapper.getHeight()) / 2));
            } else {
                llBgWrapper.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
