package club.contaniif.contaniff.actividades;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.interfaces.AllFragments;
import club.contaniif.contaniff.videos.CategoriasVideosFragment;

public class ActivityContenedora extends AppCompatActivity implements AllFragments {

    int pantalla;
    boolean seleccionado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedora);

        Bundle miBundle = getIntent().getBundleExtra("dato");
        pantalla = miBundle.getInt("pantalla");
        pantalla();
    }

    private void pantalla() {
        Fragment miFragment = null;
        switch (pantalla) {
            case 1:
                seleccionado = true;
                Toast.makeText(getApplicationContext(), 1 + "pantalla " + pantalla, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                seleccionado = true;
                Toast.makeText(getApplicationContext(), 2 + "pantalla " + pantalla, Toast.LENGTH_SHORT).show();
                break;
            case 3:
                seleccionado = true;
                miFragment=new CategoriasVideosFragment();
                break;
            case 4:
                seleccionado = true;
                Toast.makeText(getApplicationContext(), 4 + "pantalla " + pantalla, Toast.LENGTH_SHORT).show();
                break;
            case 5:
                seleccionado = true;
                Toast.makeText(getApplicationContext(), 5 + "pantalla " + pantalla, Toast.LENGTH_SHORT).show();
                break;
            case 6:
                seleccionado = true;
                Toast.makeText(getApplicationContext(), 6 + "pantalla " + pantalla, Toast.LENGTH_SHORT).show();
                break;
        }
        if (seleccionado == true) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, miFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
