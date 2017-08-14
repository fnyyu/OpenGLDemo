package com.fny.program.opengldemo.program;

import android.content.Context;

import com.fny.program.opengldemo.R;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/8/14.
 */

public class SkyCubeShaderProgram extends ShaderProgram{
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int aPositionLocation;

    public SkyCubeShaderProgram(Context context) {
        super(context, R.raw.sky_vertex_shader,
                R.raw.sky_fragment_shader);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
    }

    public void setUniforms(float[] matrix, int textureId) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);   //立方体贴图纹理
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
