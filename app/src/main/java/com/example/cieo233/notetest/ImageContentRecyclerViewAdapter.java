package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageContentRecyclerViewAdapter extends RecyclerView.Adapter {
    private ImageFolder imageFolder;
    private Context context;
    private Interfaces.OnImageClickedListener onImageClickedListener;

    public ImageContentRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        Glide.with(context).load(imageFolder.get(position).getImageURL()).into(myViewHolder.getImageView());
        myViewHolder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickedListener.onImageClicked(imageFolder.get(position), myViewHolder.getCheckBox());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFolder.size();
    }


    // function to set the data source
    void setImageFolder(String folderName) {
        this.imageFolder = GlobalStorage.getInstance().getImageFolder(folderName);
    }

    // function to set the callback
    public void setOnImageClickedListener(Interfaces.OnImageClickedListener onImageClickedListener) {
        this.onImageClickedListener = onImageClickedListener;
    }

   // function to update data source
    public void updateDateSet(String currentFolder) {
        imageFolder = GlobalStorage.getInstance().getImageFolder(currentFolder);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            checkBox = (ImageView) itemView.findViewById(R.id.checkBox);
        }


        public ImageView getImageView() {
            return imageView;
        }

        public ImageView getCheckBox() {
            return checkBox;
        }
    }


}
