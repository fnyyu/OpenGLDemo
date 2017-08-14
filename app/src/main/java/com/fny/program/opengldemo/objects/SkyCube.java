package com.fny.program.opengldemo.objects;

import android.opengl.GLES20;

import com.fny.program.opengldemo.data.VertexArray;
import com.fny.program.opengldemo.program.SkyCubeShaderProgram;

import java.nio.ByteBuffer;

/**
 * Created by cvter on 2017/8/14.
 */

public class SkyCube {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private VertexArray vertexArray;
    private ByteBuffer indexBuffer;

    public SkyCube() {
        vertexArray = new VertexArray(new float[]{
                -1, 1, 1,
                1, 1, 1,

                -1, -1, 1,
                1, -1, 1,

                -1, 1, -1,
                1, 1, -1,

                -1, -1, -1,
                1, -1, -1
        });

        indexBuffer = ByteBuffer.allocateDirect(6 * 6)   //索引数组，使用索引偏向值指向每个顶点
                .put(new byte[]{
                        1, 3, 0,
                        0, 3, 2,

                        4, 6, 5,
                        5, 6, 7,

                        0, 2, 4,
                        4, 2, 6,

                        5, 7, 1,
                        1, 7, 3,

                        5, 1, 4,
                        4, 1, 0,

                        6, 2, 7,
                        7, 2, 3
                });
        indexBuffer.position(0);
    }

    public void bindData(SkyCubeShaderProgram program) {
        vertexArray.setVertexAttributePointer(0, program.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void drawSelf(){
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexBuffer);
    }
}
