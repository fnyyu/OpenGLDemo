package com.fny.program.opengldemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fny.program.opengldemo.render.CircleRenderer;
import com.fny.program.opengldemo.render.CubeRenderer;

/**
 * Created by cvter on 2017/8/3.
 */

public class DrawActivity extends AppCompatActivity {

    private MySurfaceView mMySurfaceView;
    private boolean isRendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMySurfaceView = new MySurfaceView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (!supportEs2) {
            Toast.makeText(this, getString(R.string.noSupport), Toast.LENGTH_SHORT).show();
            return;
        }

        mMySurfaceView.setEGLContextClientVersion(2);
        switch (getIntent().getStringExtra(getString(R.string.graphic))){
            case "Circle":
                mMySurfaceView.setRenderer(new CircleRenderer(this));
                break;
            case "Cube":
                mMySurfaceView.setRenderer(new CubeRenderer(this));
                break;
            case "Sphere":
                mMySurfaceView.setRenderer(new CircleRenderer(this));
                break;
            case "Cone":
                mMySurfaceView.setRenderer(new CircleRenderer(this));
                break;
            default:
                mMySurfaceView.setRenderer(new CircleRenderer(this));
                break;

        }

        isRendererSet = true;
        setContentView(mMySurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRendererSet) {
            mMySurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRendererSet ) {
            mMySurfaceView.onResume();
        }
    }
}
