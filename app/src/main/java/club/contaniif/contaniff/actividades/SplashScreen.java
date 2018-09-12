package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;


import com.felipecsl.gifimageview.library.GifImageView;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.registro.Registro;


public class SplashScreen extends AppCompatActivity {

    CountDownTimer tiempo;
    boolean internet = false;
    boolean registrado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectivityManager con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            internet = true;
            cargarCredenciales();
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
                if (internet == true && registrado == true) {
                    miIntent = new Intent(SplashScreen.this, MainActivity.class);
                    Datos.actualizarPuntos=true;
                } else if (registrado == false && internet == true){
                    miIntent =new Intent(SplashScreen.this,Registro.class);
                }else {
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
        if (credenciales !="No existe el valor") {
            //Toast.makeText(getApplicationContext(),"credenciales" + credenciales,Toast.LENGTH_SHORT).show();
            //Intent intent =new Intent(SplashScreen.this,Registro.class);
            //startActivity(intent);
            registrado = true;
        } else {
            //Toast.makeText(getApplicationContext(),"credenciales" + credenciales,Toast.LENGTH_SHORT).show();
            //Intent intent =new Intent(SplashScreen.this,MainActivity.class);
            //startActivity(intent);
            registrado = false;
        }


    }
}
