package com.lecrec.lecrec.custom;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lecrec.lecrec.R;
import com.lecrec.lecrec.utils.decorator.RecyclerDividerDecoration;
import com.lecrec.lecrec.utils.listener.EndlessRecyclerOnScrollListener;
import com.lecrec.lecrec.utils.listener.RecyclerItemClickListener;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity
public abstract class CustomActivityWithRecyclerView extends CustomActivityWithToolbar{
    @ViewById
    protected RecyclerView recyclerView;
    @ViewById
    protected TextView tvMessage;
    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected RecyclerView.Adapter adapter;
    protected int page = 1;
    protected boolean isLoaded;
    protected boolean isInfiniteScroll = true;
    protected boolean isAddDefaultOnItemTouchListener = true;
    protected boolean isGridType;
    protected boolean hasGridTypeHeader;
    protected int gridColumnCount = 2; //if you are using grid type, then default column count is 2
    protected boolean isShowDivider = true;
    protected LinearLayoutManager mLinearLayoutManager = null;

    private OnScrollListener mOnScrollListener;
    private RecyclerDividerDecoration mRecyclerDividerDecoration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnScrollListener = new OnScrollListener();
        mRecyclerDividerDecoration = new RecyclerDividerDecoration(this);
    }


    @UiThread
    protected void setMessage(String msg){
        tvMessage.setText(msg);
    }

    protected abstract void loadData(int page);
    protected abstract void onRefresh();
    protected abstract void onItemClick(View view, int position);

    protected void setupRecyclerView() {
        if(isGridType){
            mLinearLayoutManager = new GridLayoutManager(this, gridColumnCount);
        }else{
            mLinearLayoutManager = new LinearLayoutManager(this);
        }

        if(isGridType && hasGridTypeHeader) {
            ((GridLayoutManager)mLinearLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) return gridColumnCount;
                    return 1;
                }
            });
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        if(isInfiniteScroll)
            recyclerView.setOnScrollListener(mOnScrollListener);

        if(isAddDefaultOnItemTouchListener)
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                    new OnItemClickListener()));

        if(isShowDivider)
            recyclerView.addItemDecoration(mRecyclerDividerDecoration);
    }

    protected void setupSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private class OnScrollListener extends EndlessRecyclerOnScrollListener {
        @Override
        public void onLoadMore(int page) {
            swipeRefreshLayout.setRefreshing(true);
            loadData(page);
        }
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh() {
            mOnScrollListener.reset();
            CustomActivityWithRecyclerView.this.onRefresh();
        }
    }

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener{
        @Override
        public void onItemClick(View view, int position) {
            CustomActivityWithRecyclerView.this.onItemClick(view, position);
        }
    }
}
