package com.fny.program.opengldemo.objects;

import com.fny.program.opengldemo.util.Geometry.*;

import java.util.Random;

import static android.opengl.Matrix.*;

/**
 * Created by cvter on 2017/8/11.
 */

public class FountainShooter {

    private Point position; //位置
    private int color; //颜色

    private float angleVariance; //角度变化量
    private float speedVariance; //速度变化量

    private Random random = new Random();

    private float[] rotationMatrix = new float[16];
    private float[] directionVector = new float[4];
    private float[] resultVector = new float[4];

    public FountainShooter(Point position, Vector direction, int color, float angleVariance, float speedVariance) {
        this.color = color;
        this.position = position;
        this.angleVariance = angleVariance;
        this.speedVariance = speedVariance;

        directionVector[0] = direction.x;  //方向
        directionVector[1] = direction.y;
        directionVector[2] = direction.z;
    }

    public void addFountains(FountainSystem fountainSystem, float currentTime, int count) {
        for (int i = 0; i < count; i++) {
            setRotateEulerM(rotationMatrix, 0,          //创建旋转矩阵，用angleVariance的一个随机量改变发射角度
                    (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance);

            multiplyMV(resultVector, 0, rotationMatrix, 0, directionVector, 0);

            float speedAdjustment = 1f + random.nextFloat() * speedVariance;    //调整速度
            Vector thisDirection = new Vector(
                    resultVector[0] * speedAdjustment,
                    resultVector[1] * speedAdjustment,
                    resultVector[2] * speedAdjustment
            );

            fountainSystem.addParticle(position, color, thisDirection, currentTime);
        }
    }
}
