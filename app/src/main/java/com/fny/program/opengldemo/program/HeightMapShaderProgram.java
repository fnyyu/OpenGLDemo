package com.fny.program.opengldemo.program;

import android.content.Context;

import com.fny.program.opengldemo.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by cvter on 2017/8/15.
 */

public class HeightMapShaderProgram extends ShaderProgram {

    private int uMatrixLocation;

    private int aPositionLocation;

    public HeightMapShaderProgram(Context context) {
        super(context, R.raw.height_map_vertex_shader, R.raw.height_map_fragment_shader);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
    }

    public void setUniforms(float[] matrix) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
