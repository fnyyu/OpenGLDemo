package com.fny.program.opengldemo.data;

import android.opengl.GLES20;

import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.ThrowError;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/8/14.
 */

public class IndexBuffer {

    private final int bufferId;

    public IndexBuffer(short[] indexData) {
        int buffers[] = new int[1];
        glGenBuffers(buffers.length, buffers, 0);

        if (buffers[0] == 0) {
            throw new ThrowError("Could not create a new index buffer object.");
        }

        bufferId = buffers[0];

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);

        ShortBuffer indexArray = ByteBuffer
                .allocateDirect(indexData.length * Constants.BYTE_PRE_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
        indexArray.position(0);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity() * Constants.BYTE_PRE_SHORT,
                indexArray, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    public int getBufferId() {
        return bufferId;
    }
}
