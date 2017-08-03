package com.fny.program.opengldemo.objects;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cvter on 2017/7/27.
 */

public class Mesh {

    private FloatBuffer floatBuffer;
    private ShortBuffer indicesBuffer;
    private int numOfIndices = -1;
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private FloatBuffer colorBuffer;
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float rx = 0f;
    public float ry = 0f;
    public float rz = 0f;

    public void draw(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
