package club.contaniif.contaniff.ActivosCategoria;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.actividades.ActivosActivity;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class CategoriasActiosActivity extends AppCompatActivity {

    private String cantidadMonedas;
    private JustifyTextView comprar;
    private JustifyTextView adquirir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_actios);

        comprar=findViewById(R.id.activos);
        adquirir=findViewById(R.id.accion);

        comprar.setText("Lista de activos que se pueden canjear por CriptoNIIF, tenga presente la cantidad de puntos que descuenta diariamente el activo a adquirir.\n");
        adquirir.setText("Consulte los activos adquiridos hasta el momento, además observe cuantos puntos al día se le descuentan por los mismos como una de apreciación o administración de estos.\n");

        Bundle miBundle = getIntent().getExtras();
        if (miBundle != null) {
            cantidadMonedas = Objects.requireNonNull(miBundle.getString("puntos"));
        }

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

    public void eventoActivo(View view) {
        Bundle miBundle = new Bundle();
        int tipoActivo=0;
        switch (view.getId()) {
            case R.id.btnComprarActivos:
                tipoActivo=2;
                break;
            case R.id.btnMostrarActivos:
                tipoActivo=1;
                break;
        }
        miBundle.putInt("tipo",tipoActivo);
        miBundle.putString("puntos", cantidadMonedas);
        Intent miIntent = new Intent(getApplicationContext(), ActivosActivity.class);
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }
}
