package com.fny.program.opengldemo.program;

import android.content.Context;

import com.fny.program.opengldemo.R;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/8/10.
 */

public class FountainShaderProgram extends ShaderProgram {

    private int uMatrixLocation;
    private int uTimeLocation;

    private int aPositionLocation;
    private int aColorLocation;
    private int aDirectionVectorLocation;
    private int aParticleStartTimeLocation;
    private int uTextureUnitLocation;

    public FountainShaderProgram(Context context) {
        super(context, R.raw.fountain_vertex_shader, R.raw.fountain_fragment_shader2);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        uTimeLocation = glGetUniformLocation(mProgram, U_TIME);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        aColorLocation = glGetAttribLocation(mProgram, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(mProgram, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = glGetAttribLocation(mProgram, A_PARTICLE_START_TIME);
        uTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

    }

    public void setUniforms(float[] matrix, float elapsedTime, int textureId) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uTimeLocation, elapsedTime);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }

    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
}
