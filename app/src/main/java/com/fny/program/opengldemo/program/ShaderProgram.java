package com.fny.program.opengldemo.program;

import android.content.Context;

import com.fny.program.opengldemo.util.ResourceReader;
import com.fny.program.opengldemo.util.ShaderHelper;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/7/27.
 */

public class ShaderProgram {


    static final String U_TIME = "u_Time";
    static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";


    static final String U_MATRIX = "u_Matrix";
    static final String U_TEXTURE_UNIT = "u_TextureUnit";

    static final String A_POSITION= "a_Position";
    static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    static final String A_COLOR = "a_Color";

    int mProgram;

    ShaderProgram(Context context, int vertexShaderResourceID, int fragShaderResourceID){

        mProgram = ShaderHelper.buildProgram(ResourceReader.readFileFromResource(context, vertexShaderResourceID),
                ResourceReader.readFileFromResource(context, fragShaderResourceID));
    }

    public void useProgram(){
        glUseProgram(mProgram);
    }

}
