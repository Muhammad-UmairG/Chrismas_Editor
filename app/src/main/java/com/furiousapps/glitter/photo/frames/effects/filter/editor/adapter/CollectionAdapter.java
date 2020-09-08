package com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.furiousapps.glitter.photo.frames.effects.filter.editor.R;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.SaveActivity;

import java.util.ArrayList;


public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> listItem;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.thumbImage);
        }
    }

    public CollectionAdapter(ArrayList<String> listItem, Context ct) {
        this.listItem = listItem;
        context = ct;

    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.galleryitem, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Bitmap myBitmap = BitmapFactory.decodeFile(listItem.get(position));

        viewHolder.image.setImageBitmap(myBitmap);

        viewHolder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, SaveActivity.class);
                i.putExtra("string",listItem.get(position));
                context.startActivity(i);
            }
        });



    }




    public int getItemCount() {
        return listItem.size();
    }
}
