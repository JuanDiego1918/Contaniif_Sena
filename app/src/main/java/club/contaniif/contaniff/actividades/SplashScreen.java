package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import club.contaniif.contaniff.R;


public class SplashScreen extends AppCompatActivity {

    CountDownTimer tiempo;
    boolean internet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            internet = true;
        } else {
            internet = false;
        }


        tiempo = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent miIntent = null;
                if (internet == true) {
                    miIntent=new Intent(SplashScreen.this,MainActivity.class);
                } else {
                    miIntent=new Intent(SplashScreen.this,Conexion.class);
                }
                startActivity(miIntent);
                finish();
            }
        };
        tiempo.start();
    }
}
