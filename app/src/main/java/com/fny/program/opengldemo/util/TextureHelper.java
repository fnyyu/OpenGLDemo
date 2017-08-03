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

    public static int loadTexture(Context context, int resourceId) {
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

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //缩小使用三线过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);  //放大使用双线过滤
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glGenerateMipmap(GL_TEXTURE_2D);
        bitmap.recycle();
        glBindTexture(GL_TEXTURE_2D, 0); //解除绑定

        return textureObjectId[0];
    }
}
