package com.fny.program.opengldemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.*;

/**
 * Created by cvter on 2017/7/26.
 */

public class TextureHelper {

    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, int resourceId, boolean isRepeat) {
        final int[] textureObjectId = new int[1];
        glGenTextures(1, textureObjectId, 0);   // 创建新的纹理对象

        if (textureObjectId[0] == 0){
            if (LoggerConfig.ON){
                Log.w(TAG, "Could not generate a new OpenGL texture object");
            }
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (bitmap == null){
            if (LoggerConfig.ON){
                Log.w(TAG, "Resource ID" + resourceId + "could not be decoded");
            }
            glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureObjectId[0]); //绑定二维纹理对象

        if(isRepeat){
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //缩小使用三线过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);  //放大使用双线过滤
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glGenerateMipmap(GL_TEXTURE_2D);

        bitmap.recycle();

        glBindTexture(GL_TEXTURE_2D, 0); //解除绑定

        return textureObjectId[0];
    }

    public static int loadCubeMap(Context context, int[] cubeResources){
        final int[] textureObjectId = new int[1];
        glGenTextures(1, textureObjectId, 0);

        if (textureObjectId[0] == 0){
            if (LoggerConfig.ON){
                Log.w(TAG, "Could not generate a new OpenGL texture object");
            }
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmap = new Bitmap[6];

        for (int i = 0; i < 6; i++){
            cubeBitmap[i] = BitmapFactory.decodeResource(context.getResources(), cubeResources[i], options);

            if (cubeBitmap[i] == null){
                if (LoggerConfig.ON){
                    Log.w(TAG, "Resource ID" + cubeResources[i] + "could not be decoded");
                }
                glDeleteTextures(1, textureObjectId, 0);
                return 0;
            }
        }

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectId[0]); //绑定立方体贴图纹理对象
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR); //缩小使用双线过滤
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);  //放大使用双线过滤

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmap[0], 0); //左
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmap[1], 0); //右
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmap[2], 0); //上
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmap[3], 0); //下
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmap[4], 0); //前
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmap[5], 0); //后

        glBindTexture(GL_TEXTURE_2D, 0); //解除绑定

        for (Bitmap bitmap : cubeBitmap){
            bitmap.recycle();
        }

        return textureObjectId[0];
    }
}
