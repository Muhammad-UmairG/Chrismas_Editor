package com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import androidx.recyclerview.widget.RecyclerView;

import com.furiousapps.glitter.photo.frames.effects.filter.editor.R;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.ColorCircleDrawable;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.DisplayUtil;


public class ColorAdapter2 extends RecyclerView.Adapter<ColorAdapter2.ViewHolder> implements OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private int margin;
    private int width;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.item_sticker);
        }
    }

    public ColorAdapter2(String[] listItem, Context ct, int width) {
        this.listItem = listItem;
        context = ct;
        this.width = width;
        margin = DisplayUtil.dip2px(context, 10.0f);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        LayoutParams layoutParams = new LayoutParams(width - (margin * 2), width - (margin * 2));
        layoutParams.setMargins(margin, margin, 0, margin);
        viewHolder.image.setLayoutParams(layoutParams);
        if (VERSION.SDK_INT < 16) {
            viewHolder.image.setBackgroundDrawable(new ColorCircleDrawable(Color.parseColor(listItem[position])));
        } else {
            viewHolder.image.setBackground(new ColorCircleDrawable(Color.parseColor(listItem[position])));
        }
        viewHolder.itemView.setTag(listItem[position]);
    }

    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public int getItemCount() {
        return listItem.length;
    }
}
