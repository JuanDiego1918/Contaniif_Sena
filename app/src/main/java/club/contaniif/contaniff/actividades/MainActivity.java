package club.contaniif.contaniff.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.interfaces.AllFragments;

public class MainActivity extends AppCompatActivity {

    boolean seleccionado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent miIntent = null;
        Bundle miBundle = new Bundle();
        switch (view.getId()) {
            case R.id.btnMiRendimiento:
                miBundle.putInt("pantalla", 1);
                seleccionado = true;
                break;
            case R.id.btnEmpezar:
                miBundle.putInt("pantalla", 2);
                seleccionado = true;
                break;
            case R.id.btnVideos:
                miBundle.putInt("pantalla", 3);
                seleccionado = true;
                break;
            case R.id.btnEventos:
                //miIntent=new Intent(MainActivity.this,ActivityContenedora.class);
                seleccionado = false;
                break;
            case R.id.btnSabias:
                miBundle.putInt("pantalla", 4);
                seleccionado = true;
                break;
            case R.id.btnConfiguracion:
                miBundle.putInt("pantalla", 5);
                seleccionado = true;
                break;
            case R.id.btnAcerca:
                miBundle.putInt("pantalla", 6);
                seleccionado = true;
                break;
        }
        if (seleccionado == true) {
            miIntent=new Intent(MainActivity.this,ActivityContenedora.class);
            miIntent.putExtra("dato", miBundle);
        }
        startActivity(miIntent);
    }
}