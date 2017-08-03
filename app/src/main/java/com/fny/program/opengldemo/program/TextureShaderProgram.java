package com.fny.program.opengldemo.program;

import android.content.Context;

import com.fny.program.opengldemo.R;
import static android.opengl.GLES20.*;


/**
 * Created by cvter on 2017/7/27.
 */

public class TextureShaderProgram extends ShaderProgram{

    private int uMatrixLocation;
    private int uTextUtilLocation;
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        uTextUtilLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId){
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);   //把活动的纹理单元设置为纹理单元0
        glBindTexture(GL_TEXTURE_2D, textureId); //将纹理绑定到该单元
        glUniform1i(uTextUtilLocation, 0);  //将选定的纹理单元传递给片段着色器中的u_TextureUnit
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
