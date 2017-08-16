package com.fny.program.opengldemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.fny.program.opengldemo.render.FountainRender;

/**
 * Created by cvter on 2017/8/15.
 */

public class MyPaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    private class GLEngine extends Engine {

        private FountainRender mRender;
        private GLEngine.WallPaperGLSurfaceView mSurfaceView;
        private boolean isRendererSet = false;

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

            mRender = new FountainRender(MyPaperService.this);
            isRendererSet = true;
            mSurfaceView.setEGLContextClientVersion(2);
            mSurfaceView.setPreserveEGLContextOnPause(true); //保留EG上下文
            mSurfaceView.setRenderer(mRender);
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

        @Override
        public void onOffsetsChanged(final float xOffset, final float yOffset,
                                     float xOffsetStep, float yOffsetStep,
                                     int xPixelOffset, int yPixelOffset) {
            mSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRender.handleOffsetsChanged(xOffset, yOffset);
                }
            });
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
