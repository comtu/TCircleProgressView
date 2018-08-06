package com.tu.tcircleprogressview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.tu.tcircleprogresslibrary.TCircleProgressView;

/**
 * 圆型进度条,仿支付宝人脸识别进度bar
 *
 * @author comtu
 * @version 1.0
 * @date 2018/7/31  16:13
 */
public class MainActivity extends AppCompatActivity {
    TCircleProgressView mTCPV_Demo_1;
    TCircleProgressView mTCPV_Demo_2;
    TCircleProgressView mTCPV_Demo_3;
    TCircleProgressView mTCPV_Demo_4;
    TCircleProgressView mTCPV_Demo_5;
    TCircleProgressView mTCPV_Demo_6;
    TCircleProgressView mTCPV_Demo_7;

    boolean isShowAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml配置demo
        mTCPV_Demo_1 = findViewById(R.id.tcpv_demo_1);
        mTCPV_Demo_2 = findViewById(R.id.tcpv_demo_2);
        mTCPV_Demo_3 = findViewById(R.id.tcpv_demo_3);
        mTCPV_Demo_4 = findViewById(R.id.tcpv_demo_4);
        mTCPV_Demo_5 = findViewById(R.id.tcpv_demo_5);
        mTCPV_Demo_6 = findViewById(R.id.tcpv_demo_6);
        mTCPV_Demo_7 = findViewById(R.id.tcpv_demo_7);
        //代码配置
        mTCPV_Demo_7.setText("代码配置");
        mTCPV_Demo_7.setBorderWidth(10f);//设置圆弧宽度
        mTCPV_Demo_7.setStartAngle(90f, 90f);//设置圆弧起始的角度位置以及空白区域角度
        mTCPV_Demo_7.setAnimationDuration(3000);//设置动画执行时间
        mTCPV_Demo_7.setTotalProgress(100);//设置总进度
        mTCPV_Demo_7.setGradualColors( new int[] { //设置进度渐变值
                Color.parseColor("#d3effe"),
                Color.parseColor("#cdeafb"),
                Color.parseColor("#94d3fa"),
                Color.parseColor("#61b9f5"),
                Color.parseColor("#2ba2f9"),
                Color.parseColor("#0b8eec"),
                Color.parseColor("#0179cf"),
                Color.parseColor("#0060a2")
        });//渐变进度条颜色
        mTCPV_Demo_7.setBackgroundColor(Color.parseColor("#0000ff"));//设置背景色
        mTCPV_Demo_7.setArcBackgroundColor(Color.parseColor("#000000"));//设置圆弧背景色
        mTCPV_Demo_7.setHintBackgroundColor(Color.parseColor("#5500FF00"));//设置圆弧背景色
        mTCPV_Demo_7.setHintTextColor(Color.parseColor("#ff0000"));//设置文字颜色
        mTCPV_Demo_7.setIsShowHint(true);//显示半圆与文字
        mTCPV_Demo_7.setSemicircleRate(0.5f);//半圆覆盖比率


        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowAnimation) {
                    mTCPV_Demo_1.setProgressByAnimation(0, 100);
                    mTCPV_Demo_2.setProgressByAnimation(0, 90);
                    mTCPV_Demo_3.setProgressByAnimation(0, 80);
                    mTCPV_Demo_4.setProgressByAnimation(0, 70);
                    mTCPV_Demo_5.setProgressByAnimation(0, 60);
                    mTCPV_Demo_6.setProgressByAnimation(0, 50);
                    mTCPV_Demo_7.setProgressByAnimation(0, 40);
                } else {
                    mTCPV_Demo_1.setProgress(0);
                    mTCPV_Demo_2.setProgress(0);
                    mTCPV_Demo_3.setProgress(0);
                    mTCPV_Demo_4.setProgress(0);
                    mTCPV_Demo_5.setProgress(0);
                    mTCPV_Demo_6.setProgress(0);
                    mTCPV_Demo_7.setProgress(0);
                }
                isShowAnimation = !isShowAnimation;
            }
        });

        SeekBar sb = findViewById(R.id.sb);
        sb.setMax(100);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float startProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTCPV_Demo_1.setIsShowHint(i > 20);
                mTCPV_Demo_1.setText("一二三四五" + i);
                mTCPV_Demo_1.setProgress(i);
                startProgress = i;

                mTCPV_Demo_2.setProgress(i);
                mTCPV_Demo_3.setProgress(i);
                mTCPV_Demo_4.setProgress(i);
                mTCPV_Demo_5.setProgress(i);
                mTCPV_Demo_6.setProgress(i);
                mTCPV_Demo_7.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTCPV_Demo_1.setProgressByAnimation(startProgress, mTCPV_Demo_2.getTotalProgress());
            }
        });

        mTCPV_Demo_1.setProgressByAnimation(0, 100);
        mTCPV_Demo_2.setProgressByAnimation(0, 100);
        mTCPV_Demo_3.setProgressByAnimation(0, 100);
        mTCPV_Demo_4.setProgressByAnimation(0, 100);
        mTCPV_Demo_5.setProgressByAnimation(0, 100);
        mTCPV_Demo_6.setProgressByAnimation(0, 100);
        mTCPV_Demo_7.setProgressByAnimation(0, 100);
    }
}
