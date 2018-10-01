package club.contaniif.contaniff.actividades;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.acercaDe.AcercaDeFragment;
import club.contaniif.contaniff.configuracion.Configuracion;
import club.contaniif.contaniff.grupos.Grupos;
import club.contaniif.contaniff.interfaces.AllFragments;
import club.contaniif.contaniff.interfaces.Puente;
import club.contaniif.contaniff.miRendimiento.RendimiendoFragment;
import club.contaniif.contaniff.principal.Pantalla_empezar;
import club.contaniif.contaniff.sabiasQue.CategoriasSabias;
import club.contaniif.contaniff.sabiasQue.SabiasActivity;
import club.contaniif.contaniff.videos.CategoriasVideosFragment;
import club.contaniif.contaniff.videos.VideosActivity;

public class ActivityContenedora extends AppCompatActivity implements AllFragments ,Puente{

    int pantalla;
    boolean seleccionado = false;
    Fragment miFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedora);

        Bundle miBundle = getIntent().getBundleExtra("dato");
        pantalla = miBundle.getInt("pantalla");
        pantalla();
    }

    private void pantalla() {
        switch (pantalla) {
            case 1:
                seleccionado = true;
                miFragment = new RendimiendoFragment();
                break;
            case 2:
                seleccionado = true;
                miFragment = new Pantalla_empezar();
                break;
            case 3:
                seleccionado = true;
                miFragment=new CategoriasVideosFragment();
                break;
            case 4:
                seleccionado = true;
                miFragment=new CategoriasSabias();
                break;
            case 5:
                seleccionado = true;
                miFragment = new Configuracion();
                break;
            case 6:
                seleccionado = true;
                miFragment=new AcercaDeFragment();
                break;
        }
        if (seleccionado == true) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, miFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void numero(String tipo) {
        Bundle miBundle = new Bundle();
        miBundle.putString("id", tipo);
        Intent miIntent = new Intent(getApplicationContext(), VideosActivity.class);
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }

    @Override
    public void sabias(String tipo) {
        Bundle miBundle = new Bundle();
        miBundle.putString("id", tipo);
        Intent miIntent = new Intent(getApplicationContext(), SabiasActivity.class);
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }

    @Override
    public void activos(String puntos) {
        Bundle miBundle = new Bundle();
        miBundle.putString("puntos", puntos);
        Intent miIntent = new Intent(getApplicationContext(), ActivosActivity.class);
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }

    @Override
    public void reinciar(int numeroPregunta, int tipo, ArrayList<String> lista) {
        Bundle datos = new Bundle();
        Bundle miBundle=new Bundle();
        datos.putInt("numeroPregunta", numeroPregunta);
        datos.putStringArrayList("color",lista);
        miBundle.putBundle("Todo",datos);
        miFragment = new Pantalla_empezar();
        miFragment.setArguments(miBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, miFragment).commit();
    }

    @Override
    public void finaliza() {
        finish();
    }

    @Override
    public void reinciarRendimiento() {
        miFragment = new RendimiendoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, miFragment).commit();
    }
}
