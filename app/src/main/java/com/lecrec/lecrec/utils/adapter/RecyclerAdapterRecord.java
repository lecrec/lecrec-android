package com.lecrec.lecrec.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lecrec.lecrec.R;
import com.lecrec.lecrec.models.Record;

import java.util.List;


public class RecyclerAdapterRecord extends RecyclerView.Adapter<RecyclerAdapterRecord.ViewHolder> {
    private List<Record> mItems;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView tvFilename;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvFilename = (TextView) view.findViewById(R.id.tvFilename);
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

        holder.tvFilename.setText(item.getFilename());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
