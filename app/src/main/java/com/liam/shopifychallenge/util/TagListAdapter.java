package com.liam.shopifychallenge.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liam.shopifychallenge.OnTagClickListener;
import com.liam.shopifychallenge.R;

import java.util.List;

/**
 * Adapter for tag list
 */

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagListViewHolder> {

    private List<String> tags;
    private OnTagClickListener onTagClickListener;

    public TagListAdapter(OnTagClickListener onTagClickListener){
        this.onTagClickListener = onTagClickListener;
    }

    public void setTagList(List<String> tags){
        this.tags = tags;
    }

    @Override
    public void onBindViewHolder(final TagListViewHolder viewHolder, int position){
        final String tagName = tags.get(position);
        viewHolder.tagButton.setText(tagName);

    }

    @Override
    public TagListViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tag_item_view, viewGroup, false);
        return new TagListViewHolder(view, onTagClickListener);
    }

    @Override
    public int getItemCount() {
        return (tags == null)? 0 : tags.size();
    }


    class TagListViewHolder extends RecyclerView.ViewHolder{
        private OnTagClickListener listener;
        private Button tagButton;
        TagListViewHolder(View view, final OnTagClickListener onTagClickListener){
            super(view);
            this.listener = onTagClickListener;
            tagButton = view.findViewById(R.id.tag_button);
            tagButton.setOnClickListener(v->
                    listener.onTagClicked(getAdapterPosition()));
        }
    }
}

