package com.lecrec.lecrec.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lecrec.lecrec.R;
import com.lecrec.lecrec.models.TextObject;

import java.util.List;


public class RecyclerAdapterText extends RecyclerView.Adapter<RecyclerAdapterText.ViewHolder> {
    private List<TextObject> mItems;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final LinearLayout llTitleContainer;
        public final TextView tvTitle;
        public final TextView tvDatetime;
        public final TextView tvText;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            llTitleContainer = (LinearLayout) view.findViewById(R.id.llTitleContainer);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvDatetime = (TextView) view.findViewById(R.id.tvDatetime);
            tvText = (TextView) view.findViewById(R.id.tvText);
        }
    }

    public RecyclerAdapterText(Context context, List<TextObject> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_recycler_text, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextObject item = mItems.get(position);

        holder.llTitleContainer.setVisibility(View.GONE);
        if(position == 0) {
            holder.llTitleContainer.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(item.getTitle());
            holder.tvDatetime.setText(item.getDatetime());
        } else {
            holder.tvText.setText(item.getText());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
