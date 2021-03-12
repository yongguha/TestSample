package com.commax.ocf.server.app.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.commax.ocf.server.app.R;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;
    private Drawable mDivider;

    public RecyclerViewDecoration(Context context, int divHeight) {
        this.divHeight = divHeight;
        this.mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        /*
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for(int i=0;i<childCount;i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            //mDivider.setBounds(left, top, right, bottom);
            //mDivider.setBounds(left, divHeight/2, right, divHeight/2);
            mDivider.draw(c);
        }
        */
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top=divHeight/2;
        outRect.bottom=divHeight/2;

        //view.setBackgroundResource(R.color.listseletec);
        //parent.setBackgroundResource(R.color.listseletec);


    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
