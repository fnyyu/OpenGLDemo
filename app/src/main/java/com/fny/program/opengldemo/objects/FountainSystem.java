package com.fny.program.opengldemo.objects;

import android.graphics.Color;
import android.opengl.GLES20;

import com.fny.program.opengldemo.data.VertexArray;
import com.fny.program.opengldemo.program.FountainShaderProgram;
import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.Geometry;

/**
 * Created by cvter on 2017/8/11.
 */

public class FountainSystem {

    private static final int POSITION_COMPONENT_COUNT = 3;   //位置
    private static final int COLOR_COMPONENT_COUNT = 3; //颜色
    private static final int VECTOR_COMPONENT_COUNT = 3;    //方向
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;  //创建时间

    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT
            + VECTOR_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT; //粒子分量计数

    private static final int STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTE_PRE_FLOAT;

    private float[] particles;  //存储粒子
    private VertexArray vertexArray;
    private int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;   //下一粒子编号

    public FountainSystem(int maxParticleCount) {
        this.maxParticleCount = maxParticleCount;
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
    }

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction, float particleStartTime) {
        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;
        int currentOffset = particleOffset;
        nextParticle++;

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }

        if (nextParticle == maxParticleCount) {
            nextParticle = 0;   //从0开始回收旧粒子
        }

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        particles[currentOffset++] = Color.red(color) / 255f;
        particles[currentOffset++] = Color.green(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;

        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;

        particles[currentOffset++] = particleStartTime;

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    public void bindData(FountainShaderProgram program) {
        int dataOffset = 0;

        vertexArray.setVertexAttributePointer(dataOffset,
                program.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset,
                program.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset,
                program.getDirectionVectorAttributeLocation(), VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttributePointer(dataOffset,
                program.getParticleStartTimeAttributeLocation(), PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);

    }

    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentParticleCount);
    }

}
