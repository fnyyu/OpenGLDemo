package com.fny.program.opengldemo.util;

import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/7/25.
 */

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {  //编译顶点着色器
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {  //编译片段着色器
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int glVertexShader, String shaderCode) {  //编译着色器具体操作

        final int shaderObjectId = glCreateShader(glVertexShader);  //创建新的着色器对象，保存其ID
        if (shaderObjectId == 0) {   //返回0表示创建失败
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader");
            }
            return 0;
        }

        glShaderSource(shaderObjectId, shaderCode); //上传shaderCode源代码，将其与shaderObjectId所引用着色器关联
        glCompileShader(shaderObjectId);  //编译着色器源代码
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0); //读取编译状态
        if (LoggerConfig.ON) {
            Log.v(TAG, "Results of compiling source: \n" + shaderCode + "\n" + glGetShaderInfoLog(shaderObjectId));
        }
        if (compileStatus[0] == 0) {   //验证编译状态
            glDeleteShader(shaderObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed");
            }
            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderID, int fragmentShaderID) {   //着色器链接OpenGL程序
        final int programObjectId = glCreateProgram();  //创建新的程序，将其与programObjectId关联
        if (programObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }
        glAttachShader(programObjectId, vertexShaderID);  //附加着色器
        glAttachShader(programObjectId, fragmentShaderID);
        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        if (LoggerConfig.ON) {
            Log.v(TAG, "Results of linking program: \n" + glGetProgramInfoLog(programObjectId));
        }

        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            if (LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed");
            }
            return 0;
        }

        return programObjectId;

    }

    public static boolean validateProgram(int programID) {   //拼接成为OpenGL程序
        glValidateProgram(programID);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programID, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validate program: \n" + glGetProgramInfoLog(programID));
        return validateStatus[0] != 0;
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
        int program;

        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON){
            validateProgram(program);
        }

        return program;
    }

}
