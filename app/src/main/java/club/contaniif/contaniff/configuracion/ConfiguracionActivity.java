package club.contaniif.contaniff.configuracion;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.grupos.Grupos;
import club.contaniif.contaniff.interfaces.AllFragments;

public class ConfiguracionActivity extends AppCompatActivity implements AllFragments{

    Fragment miFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        Bundle miBundle = this.getIntent().getBundleExtra("dato");

        switch (miBundle.getInt("pantalla")){

            case 1:
                miFragment = new Grupos();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedorConfig,miFragment).commit();
                break;

            case 2:
                miFragment = new MiPerfil();
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedorConfig,miFragment).commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
