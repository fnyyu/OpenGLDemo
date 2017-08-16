package com.fny.program.opengldemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.fny.program.opengldemo.render.FountainRender;

/**
 * Created by cvter on 2017/8/15.
 */

public class MyPaperService extends WallpaperService {

    private GLEngine.WallPaperGLSurfaceView mSurfaceView;
    private boolean isRendererSet = false;

    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    public class GLEngine extends Engine {
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            mSurfaceView = new WallPaperGLSurfaceView(MyPaperService.this);

            final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
            final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

            if (!supportEs2) {
                Toast.makeText(MyPaperService.this, getString(R.string.noSupport), Toast.LENGTH_SHORT).show();
                return;
            }

            isRendererSet = true;
            mSurfaceView.setEGLContextClientVersion(2);
            mSurfaceView.setRenderer(new FountainRender(MyPaperService.this));
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (isRendererSet) {
                if (visible) {
                    mSurfaceView.onResume();
                } else {
                    mSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mSurfaceView.onWallPaperDestroy();
        }

        class WallPaperGLSurfaceView extends GLSurfaceView{

            public WallPaperGLSurfaceView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onWallPaperDestroy(){
                super.onDetachedFromWindow();
            }


        }

    }



}
