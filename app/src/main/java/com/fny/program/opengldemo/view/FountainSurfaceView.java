package com.fny.program.opengldemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.fny.program.opengldemo.render.FountainRender;

/**
 * Created by cvter on 2017/8/10.
 */

public class FountainSurfaceView extends GLSurfaceView {

    private FountainRender mRender;

    public FountainSurfaceView(Context context) {
        super(context);
        mRender = new FountainRender(context);
        setEGLContextClientVersion(2);
        setRenderer(mRender);
    }
}
