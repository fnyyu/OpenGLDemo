package com.fny.program.opengldemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.fny.program.opengldemo.view.FountainSurfaceView;

/**
 * Created by cvter on 2017/8/10.
 */

public class FountainActivity extends Activity {

    private FountainSurfaceView mSurfaceView;
    private boolean isRendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSurfaceView = new FountainSurfaceView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (!supportEs2) {
            Toast.makeText(this, getString(R.string.noSupport), Toast.LENGTH_SHORT).show();
            return;
        }

        isRendererSet = true;
        setContentView(mSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRendererSet) {
            mSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRendererSet) {
            mSurfaceView.onResume();
        }
    }
}
