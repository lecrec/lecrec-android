package com.lecrec.lecrec.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lecrec.lecrec.R;
import com.lecrec.lecrec.models.Record;

import java.util.List;


public class RecyclerAdapterRecord extends RecyclerView.Adapter<RecyclerAdapterRecord.ViewHolder> {
    private List<Record> mItems;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final LinearLayout llRecordTop;
        public final LinearLayout llRecordBottom;
        public final TextView tvTitle;
        public final TextView tvCreated;
        public final TextView tvDuration;
        public final ImageView ivUpload;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            llRecordTop = (LinearLayout) view.findViewById(R.id.llRecordTop);
            llRecordBottom = (LinearLayout) view.findViewById(R.id.llRecordBottom);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvCreated = (TextView) view.findViewById(R.id.tvCreated);
            tvDuration = (TextView) view.findViewById(R.id.tvDuration);
            ivUpload = (ImageView) view.findViewById(R.id.ivUpload);
        }
    }

    public RecyclerAdapterRecord(Context context, List<Record> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_recycler_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Record item = mItems.get(position);

        holder.llRecordTop.setBackgroundResource(R.drawable.bg_record_corner_top);
        holder.llRecordBottom.setBackgroundResource(R.drawable.bg_record_corner_bottom);
        holder.ivUpload.setVisibility(View.VISIBLE);
        if(item.getUploaded()) {
            holder.llRecordTop.setBackgroundResource(R.drawable.bg_record_uploaded_corner_top);
            holder.llRecordBottom.setBackgroundResource(R.drawable.bg_record_uploaded_corner_bottom);
            holder.ivUpload.setVisibility(View.INVISIBLE);
        }

        holder.tvTitle.setText(item.getTitle());
        holder.tvCreated.setText(item.getDatetime());
        holder.tvDuration.setText(item.getDuration());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
