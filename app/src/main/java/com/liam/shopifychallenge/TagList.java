package com.liam.shopifychallenge;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Liam on 2018-09-20.
 */

public class TagList extends RecyclerView {
    private final LinearLayoutManager linearLayoutManager;

    public TagList(final Context context, final AttributeSet attrs){
        super(context, attrs);
        linearLayoutManager = new LinearLayoutManager(context);
        setHasFixedSize(true);
        setLayoutManager(linearLayoutManager);
    }

    public LinearLayoutManager getLinearLayoutManager(){
        return linearLayoutManager;
    }
}
