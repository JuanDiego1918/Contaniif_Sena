package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import club.contaniif.contaniff.R;

public class IngresaCodigoRegistro extends AppCompatActivity {

    EditText campoCodigo;
    Button btnCodigo;
    String usuario;
    String codigoJson;
    String codigoCampo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa_codigo_registro);

        Bundle miBundle = this.getIntent().getBundleExtra("bundle");
        codigoJson = miBundle.getString("codigo");
        usuario = miBundle.getString("usuario");

        campoCodigo = findViewById(R.id.campoCodigoValidacion);
        btnCodigo = findViewById(R.id.btnRegistrarCodigo);
        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoCodigo.getText().toString() != null){
                    codigoCampo = campoCodigo.getText().toString();
                    validarCodigo();
                }else {
                    Toast.makeText(IngresaCodigoRegistro.this, "Por favor ingrese el codigo", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void validarCodigo() {
        if (codigoJson.equalsIgnoreCase(codigoCampo)){
            guardarCredenciales(usuario);
            Toast.makeText(this, "El codigo es incorrecto o ya ha expirado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(IngresaCodigoRegistro.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void guardarCredenciales(String correo) {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo",correo);
        editor.commit();
    }
}
