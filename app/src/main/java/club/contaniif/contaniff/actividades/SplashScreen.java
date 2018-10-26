package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;


import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.registro.Registro;


public class SplashScreen extends AppCompatActivity {

    private boolean internet = false;
    private boolean registrado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(con).getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            internet = true;
            cargarCredenciales();
        } else {
            internet = false;
        }


        CountDownTimer tiempo = new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent miIntent;
                if (internet && registrado) {
                    miIntent = new Intent(SplashScreen.this, MainActivity.class);
                    Datos.actualizarPuntos = true;
                } else if (!registrado && internet) {
                    miIntent = new Intent(SplashScreen.this, Registro.class);
                } else {
                    miIntent = new Intent(SplashScreen.this, Conexion.class);
                }
                startActivity(miIntent);
                finish();
            }
        };
        tiempo.start();
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        //Toast.makeText(getApplicationContext(),"credenciales" + credenciales,Toast.LENGTH_SHORT).show();
//Intent intent =new Intent(SplashScreen.this,Registro.class);
//startActivity(intent);
//Toast.makeText(getApplicationContext(),"credenciales" + credenciales,Toast.LENGTH_SHORT).show();
//Intent intent =new Intent(SplashScreen.this,MainActivity.class);
//startActivity(intent);
        registrado = credenciales != "No existe el valor";


    }
}
