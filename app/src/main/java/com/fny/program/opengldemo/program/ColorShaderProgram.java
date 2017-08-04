package com.fny.program.opengldemo.program;

import android.content.Context;

import com.fny.program.opengldemo.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by cvter on 2017/7/27.
 */

public class ColorShaderProgram extends ShaderProgram{

    private int uMatrixLocation;
    private int aColorLocation;
    private int aPositionLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        aColorLocation = glGetUniformLocation(mProgram, A_COLOR);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
    }

    public void setUniforms(float[] matrix){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
