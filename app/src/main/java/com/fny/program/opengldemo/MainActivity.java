package com.fny.program.opengldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        intent = new Intent(this, DrawActivity.class);
    }

    @OnClick({R.id.bt_circle, R.id.bt_cube, R.id.bt_cone, R.id.bt_sphere,
            R.id.bt_earth, R.id.bt_dog, R.id.bt_fountain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_circle:
                intent.putExtra(getString(R.string.graphic), getString(R.string.circle));
                startActivity(intent);
                break;
            case R.id.bt_cube:
                intent.putExtra(getString(R.string.graphic), getString(R.string.cube));
                startActivity(intent);
                break;
            case R.id.bt_cone:
                intent.putExtra(getString(R.string.graphic), getString(R.string.cone));
                startActivity(intent);
                break;
            case R.id.bt_sphere:
                Intent intentSphere = new Intent(this, BallActivity.class);
                startActivity(intentSphere);
                break;
            case R.id.bt_earth:
                intent.putExtra(getString(R.string.graphic), getString(R.string.earth));
                startActivity(intent);
                break;
            case R.id.bt_dog:
                intent.putExtra(getString(R.string.graphic), getString(R.string.dog));
                startActivity(intent);
                break;
            case R.id.bt_fountain:
                Intent intentFountain = new Intent(this, FountainActivity.class);
                startActivity(intentFountain);
                break;
            default:
                break;
        }

    }
}
