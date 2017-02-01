package com.max.videoplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    private static final String VIDEO_PATH =
            "http://mvideo.spriteapp.cn/video/2017/0124/58873eaee2838_wpc.mp4";
    private static final int UPDATE = 1;

    private boolean isFull = false;
    private int screenWidth;
    private int screenHeight;
    private RelativeLayout mVideoLayout;
    private CustomVideoView mVideoView;
    private ImageView mPlayBtn;
    private ImageView mFullBtn;
    private ImageView mBigPlayBtn;
    private SeekBar mLoading;
    private TextView startTime;
    private TextView endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mVideoLayout = (RelativeLayout) findViewById(R.id.layout_video);
        mVideoView = (CustomVideoView) findViewById(R.id.video_view);
        mVideoView.setOnClickListener(this);
        mPlayBtn = (ImageView) findViewById(R.id.btn_play_switch);
        mPlayBtn.setOnClickListener(this);
        mFullBtn = (ImageView) findViewById(R.id.btn_full_switch);
        mFullBtn.setOnClickListener(this);
        mBigPlayBtn = (ImageView) findViewById(R.id.btn_big_play);
        mBigPlayBtn.setOnClickListener(this);
        startTime = (TextView) findViewById(R.id.tv_time_start);
        endTime = (TextView) findViewById(R.id.tv_time_end);
        mLoading = (SeekBar) findViewById(R.id.sb_video);
        mLoading.setOnSeekBarChangeListener(this);
        // mLoading.setOnTouchListener(this);
        mVideoView.setVideoURI(Uri.parse(VIDEO_PATH));
//        mVideoView.start();
//        mHandler.sendEmptyMessage(UPDATE);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_switch:
                if (!mVideoView.isPlaying()) {
                    mPlayBtn.setImageResource(R.drawable.ic_video_pause);
                    //继续播放
                    mVideoView.start();
                    mBigPlayBtn.setVisibility(View.GONE);
                    mHandler.sendEmptyMessage(UPDATE);
                } else {
                    mPlayBtn.setImageResource(R.drawable.ic_video_play);
                    //暂停播放
                    mVideoView.pause();
                    mBigPlayBtn.setVisibility(View.VISIBLE);
                    mHandler.removeMessages(UPDATE);
                }
                break;
            case R.id.btn_full_switch:
                if (!isFull) {
                    mFullBtn.setImageResource(R.drawable.ic_video_fullscreen_exit_pressed);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    mFullBtn.setImageResource(R.drawable.ic_video_fullscreen_pressed);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.btn_big_play:
                mBigPlayBtn.setVisibility(View.GONE);
                mPlayBtn.setImageResource(R.drawable.ic_video_pause);
                //继续播放
                mVideoView.start();
                mHandler.sendEmptyMessage(UPDATE);
                break;
            case R.id.video_view:
                if (!mVideoView.isPlaying()) {
                    mPlayBtn.setImageResource(R.drawable.ic_video_pause);
                    //继续播放
                    mVideoView.start();
                    mBigPlayBtn.setVisibility(View.GONE);
                    mHandler.sendEmptyMessage(UPDATE);
                } else {
                    mPlayBtn.setImageResource(R.drawable.ic_video_play);
                    //暂停播放
                    mVideoView.pause();
                    mBigPlayBtn.setVisibility(View.VISIBLE);
                    mHandler.removeMessages(UPDATE);
                }
                break;
        }
    }

    /**
     * 设置Video的大小
     *
     * @param width  宽度
     * @param height 高度
     */
    private void setVideoScale(int width, int height) {
        ViewGroup.LayoutParams videoParams = mVideoView.getLayoutParams();
        videoParams.width = width;
        videoParams.height = height;
        mVideoView.setLayoutParams(videoParams);

        ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mVideoLayout.setLayoutParams(layoutParams);
    }

    /**
     * 更新时间
     *
     * @param textView 时间显示控件
     * @param time     毫秒数
     */
    private void updateTime(TextView textView, int time) {
        int total = time / 1000;
        int minute = total % 3600 / 60;
        int hours = total / 3600;
        int second = total % 60;
        StringBuilder sb;
        sb = new StringBuilder();
        if (hours != 0) {
            if (hours < 10) {
                sb.append("0");
            }
            sb.append(String.valueOf(hours)).append(":");
        }
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(minute)).append(":");
        if (second < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(second));
        textView.setText(sb.toString());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE) {
                //获取当前播放时间
                int currentTime = mVideoView.getCurrentPosition();
                //获取总共时间
                int totalTime = mVideoView.getDuration();
                //初始化时间
                updateTime(startTime, currentTime);
                updateTime(endTime, totalTime);
                //mVideoView.getBufferPercentage()
                //初始化SeekBar
                mLoading.setMax(totalTime);
                mLoading.setProgress(currentTime);
                mHandler.sendEmptyMessageDelayed(UPDATE, 500);
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mVideoView.seekTo(progress);
            updateTime(startTime, progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeMessages(UPDATE);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!seekBar.callOnClick()) mHandler.sendEmptyMessage(UPDATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(UPDATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(UPDATE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.sb_video) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeMessages(UPDATE);
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.sendEmptyMessage(UPDATE);
                    break;
                default:
                    mHandler.sendEmptyMessage(UPDATE);
                    break;
            }
        }
        return true;
    }

    /**
     * 监听屏幕方向的改变
     *
     * @param newConfig 屏幕配置信息
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoScale(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            isFull = true;
            //移除半屏状态
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mFullBtn.setImageResource(R.drawable.ic_video_fullscreen_exit_pressed);
        } else {//竖屏
            setVideoScale(ViewGroup.LayoutParams.MATCH_PARENT,pxFromDp(240));
            isFull = false;
            //恢复半屏状态
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            mFullBtn.setImageResource(R.drawable.ic_video_fullscreen_pressed);
        }
    }

    /**
     * dp转像素px
     *
     * @param dp 相转换的dp
     * @return 转换后的px
     */
    private int pxFromDp(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (metrics.density * dp + 0.5f);
    }
}
