package com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.furiousapps.glitter.photo.frames.effects.filter.editor.R;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap.BitmapLoader;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.DisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.ViewHolder> implements OnClickListener {
    private Context context;
    private List<String> listColor;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Typeface typeface;
    private int width;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public RelativeLayout rlcontext;
        public TextView text;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            rlcontext = view.findViewById(R.id.rlcontext);
            text = view.findViewById(R.id.text);
        }
    }

    public EffectAdapter(String[] listItem, Context ct, int width) {
        this.listItem = listItem;
        context = ct;
        width = width - DisplayUtil.dip2px(context, 15.0f);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        listColor = new ArrayList(Arrays.asList("#5508c8", "#3f97e9", "#e4c120", "#c82970", "#e49820", "#949494", "#e46f20", "#ba0bc0", "#3fa3c3", "#3fc3b2", "#3fc390", "#d2434d", "#7fa31e", "#3f6ec3", "#8f08c8", "#a3951e", "#c00b98", "#1ea340"));
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effect, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.image.setImageBitmap(BitmapLoader.loadFromAsset(context, new int[]{200, 200}, listItem[position]));
        LayoutParams layoutParams = new LayoutParams(width, width / 4);
        layoutParams.addRule(12);
        viewHolder.rlcontext.setLayoutParams(layoutParams);
        viewHolder.rlcontext.setBackgroundColor(Color.parseColor(listColor.get(position % listColor.size())));
        viewHolder.itemView.setTag(listItem[position]);
        if (position == 0) {
            viewHolder.text.setText("No");
        } else {
            viewHolder.text.setText("e" + position);
        }
        viewHolder.text.setTypeface(typeface);
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
