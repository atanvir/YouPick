package com.youpic.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youpic.R;

public class OverlapDecoration extends RecyclerView.ItemDecoration
{
    private static  final int overlap=-40;
    private Drawable mDivider;

    public OverlapDecoration(Context context) {
       // mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }





    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == 0) {
            outRect.set(0, 0, 0, 0);
        }else{
            outRect.set(overlap, 0, 0, 0);
        }
       // outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());


    }
}
