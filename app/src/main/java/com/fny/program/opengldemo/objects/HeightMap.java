package com.fny.program.opengldemo.objects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import com.fny.program.opengldemo.data.IndexBuffer;
import com.fny.program.opengldemo.data.VertexBuffer;
import com.fny.program.opengldemo.program.HeightMapShaderProgram;
import com.fny.program.opengldemo.util.ThrowError;

/**
 * Created by cvter on 2017/8/14.
 */

public class HeightMap {

    private static final int POSITION_COMPONENT_COUNT = 3;

    private int width;
    private int height;
    private int numElements;
    private VertexBuffer vertexBuffer;
    private IndexBuffer indexBuffer;

    public HeightMap(Bitmap bitmap) {
        width = bitmap.getWidth() / 3;
        height = bitmap.getHeight() / 3;

        if (width * height > 65536) {
            throw new ThrowError(width + "and" + height + "HeightMap is too large for the index buffer");
        }

        numElements = calculateNumElements();
        vertexBuffer = new VertexBuffer(loadBitmapData(bitmap));
        indexBuffer = new IndexBuffer(createIndexData());
    }

    //位图转换为高度图顶点数据
    private float[] loadBitmapData(Bitmap bitmap) {

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();

        float[] heightMapVertices = new float[width * height * POSITION_COMPONENT_COUNT];
        int offset = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                final float xPosition = ((float) col / (float) (width - 1)) - 0.5f;
                final float yPosition = (float) Color.red(pixels[(row * height) + col]) / (float) 255;
                final float zPosition = ((float) row / (float) (height - 1)) - 0.5f;

                heightMapVertices[offset++] = xPosition;
                heightMapVertices[offset++] = yPosition;
                heightMapVertices[offset++] = zPosition;
            }
        }

        return heightMapVertices;
    }

    //计算高度图索引数据大小
    private int calculateNumElements() {
        return (width - 1) * (height - 1) * 2 * 3;  //每四个顶点一组，每组需要两个三角形拼凑，每个三角形有三个索引
    }

    //生成高度图索引数据
    private short[] createIndexData() {
        short[] indexData = new short[numElements];
        int offset = 0;

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width - 1; col++) {
                short topLeftIndexNum = (short) (row * width + col);
                short topRightIndexNum = (short) (row * width + col + 1);
                short bottomLeftIndexNum = (short) ((row + 1) * width + col);
                short bottomRightIndexNum = (short) ((row + 1) * width + col + 1);

                indexData[offset++] = topLeftIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = topRightIndexNum;

                indexData[offset++] = topRightIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = bottomRightIndexNum;
            }
        }
        return indexData;
    }

    public void bindData(HeightMapShaderProgram program) {
        vertexBuffer.setVertexAttributePointer(0,
                program.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.getBufferId());
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numElements, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

}
