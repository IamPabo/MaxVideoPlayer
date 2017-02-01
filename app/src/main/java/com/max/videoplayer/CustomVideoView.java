package com.max.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @auther MaxLiu
 * @time 2017/1/31
 */

public class CustomVideoView extends VideoView {

    private int defaultWidth = getResources().getDisplayMetrics().widthPixels;
    private int defaultHeight = getResources().getDisplayMetrics().heightPixels;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(defaultWidth,widthMeasureSpec);
        int height = getDefaultSize(defaultHeight,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }
}
