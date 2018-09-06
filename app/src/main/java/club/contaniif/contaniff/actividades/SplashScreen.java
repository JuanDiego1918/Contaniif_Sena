package club.contaniif.contaniff.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import club.contaniif.contaniff.R;


public class SplashScreen extends AppCompatActivity {

    CountDownTimer tiempo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tiempo=new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent miIntent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(miIntent);
                finish();
            }
        };
        tiempo.start();
    }
}
