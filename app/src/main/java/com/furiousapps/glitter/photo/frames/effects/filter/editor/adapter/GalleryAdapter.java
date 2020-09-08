package com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.furiousapps.glitter.photo.frames.effects.filter.editor.R;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap.BitmapLoader;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements OnClickListener {
    private Context context;
    private String[] listItem;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String str);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.item_sticker);
        }
    }

    public GalleryAdapter(String[] listItem, Context ct) {
        this.listItem = listItem;
        context = ct;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageBitmap(BitmapLoader.loadFromAsset(context, new int[]{200, 200}, listItem[position]));
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
