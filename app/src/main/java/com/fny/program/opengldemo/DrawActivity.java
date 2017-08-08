package com.fny.program.opengldemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fny.program.opengldemo.render.BaseRender;
import com.fny.program.opengldemo.render.CircleRenderer;
import com.fny.program.opengldemo.render.ConeRenderer;
import com.fny.program.opengldemo.render.CubeRenderer;
import com.fny.program.opengldemo.render.EarthRenderer;

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

        MySurfaceView.InitRender<BaseRender> set;
        BaseRender render;

        switch (getIntent().getStringExtra(getString(R.string.graphic))) {
            case "Cube":
                render = new CubeRenderer(this);
                set = new MySurfaceView.InitRender<>(render);
                break;
            case "Earth":
                render = new EarthRenderer(this);
                set = new MySurfaceView.InitRender<>(render);
                break;
            case "Cone":
                render = new ConeRenderer(this);
                set = new MySurfaceView.InitRender<>(render);
                break;
            default:
                render = new CircleRenderer(this);
                set = new MySurfaceView.InitRender<>(render);
                break;

        }
        mMySurfaceView.setBaseRender(set.getObject());
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
        if (isRendererSet) {
            mMySurfaceView.onResume();
        }
    }
}
