package com.fny.program.opengldemo.objects;

import com.fny.program.opengldemo.util.Geometry.*;

import java.util.Random;

import static android.opengl.Matrix.*;

/**
 * Created by cvter on 2017/8/11.
 */

public class FountainShooter {

    private Point position;
    private Vector direction;
    private int color;

    private float angleVariance;
    private float speedVariance;

    private Random random = new Random();

    private float[] ratationMatrix = new float[16];
    private float[] directionVector = new float[4];
    private float[] resultVector = new float[4];

    public FountainShooter(Point position, Vector direction, int color, float angleVariance, float speedVariance) {
        this.color = color;
        this.direction = direction;
        this.position = position;
        this.angleVariance = angleVariance;
        this.speedVariance = speedVariance;

        directionVector[0] = direction.x;
        directionVector[1] = direction.y;
        directionVector[2] = direction.z;
    }

    public void addParticles(FountainSystem fountainSystem, float currentTime, int count) {
        for (int i = 0; i < count; i++) {
//            fountainSystem.addParticle(position, color, direction, currentTime);

            setRotateEulerM(ratationMatrix, 0,
                    (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance,
                    (random.nextFloat() - 0.5f) * angleVariance);

            multiplyMV(resultVector, 0, ratationMatrix, 0, directionVector, 0);

            float speedAdjustment = 1f + random.nextFloat() * speedVariance;

            Vector thisDirection = new Vector(
                    resultVector[0] *speedAdjustment,
                    resultVector[1] *speedAdjustment,
                    resultVector[2] *speedAdjustment
                    );

            fountainSystem.addParticle(position, color, thisDirection, currentTime);
        }
    }
}
