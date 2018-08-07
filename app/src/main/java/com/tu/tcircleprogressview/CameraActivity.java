package com.tu.tcircleprogressview;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tu.tcircleprogresslibrary.TCircleProgressView;

/**
 * 模仿支付宝人脸识别
 *
 * @author comtu
 * @version 1.0
 * @date 2018/8/7  9:57
 */
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    // ===========================================================
    // Constants
    // ===========================================================
    public final static int REQUEST_CODE_CAMERA_OK = 1;

    // ===========================================================
    // Fields
    // ===========================================================
    private TCircleProgressView mTcpv;
    private TextView mTvProgress;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private int mCameraIndex;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SurfaceView surfaceView = findViewById(R.id.sv_camera);
        mSurfaceHolder = surfaceView.getHolder();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_OK);
        } else {
            showCamera();
        }

        mTvProgress = findViewById(R.id.tv_progress);


        mTcpv = findViewById(R.id.tcpv_dam_board);
        mTcpv.setTotalProgress(1000);
        mTcpv.setAnimationDuration(2000);
        mTcpv.setHintTextSize(15);
        //mTcpv.setTextPadding(8);
        mTcpv.setOnProgressListener(new TCircleProgressView.OnProgressListener() {
            @Override
            public void onProgressChanged(float progress) {
                mTvProgress.setText("进度:" + progress);
                mTcpv.setIsShowHint(progress > 300 && progress < 800);
            }
        });
        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            int mIndex = 0;

            @Override
            public void onClick(View view) {
                mTcpv.setProgress(0);
                mIndex = 0;
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        switch (mIndex) {
                            case 0:
                                mTcpv.setText("没有检测到脸");
                                mTcpv.setProgressByAnimation(0, 800);
                                break;
                            case 1:
                                mTcpv.setText("把脸移入框内");
                                mTcpv.setProgressByAnimation(800, 600);
                                break;
                            case 2:
                                mTcpv.setText("识别成功");
                                mTcpv.setProgressByAnimation(600, 1000);
                                break;
                            default:
                                break;
                        }
                        mIndex++;
                        if (mIndex < 3) {
                            mHandler.postDelayed(this, 2000);
                        }
                    }
                };
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 2000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCamera();
                    openCamera();
                } else {
                    Toast.makeText(this, "请手动打开相机权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void showCamera() {
        int cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//前置摄像头
                mCameraIndex = i;
                mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mSurfaceHolder.addCallback(this);
            }
        }
    }

    private void openCamera() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            mCameraIndex = mCameraIndex == 0 ? Camera.getNumberOfCameras() : mCameraIndex;
            mCamera = Camera.open(mCameraIndex);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            Camera.Parameters parameters = mCamera.getParameters();
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                parameters.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            if (mCamera != null) {
                mCamera.release();
            }
            finish();
        }
    }

}
