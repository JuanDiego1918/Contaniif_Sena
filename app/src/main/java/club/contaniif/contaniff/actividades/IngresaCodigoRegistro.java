package club.contaniif.contaniff.actividades;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.contaniif.contaniff.R;
public class IngresaCodigoRegistro extends AppCompatActivity {

    private EditText campoCodigo;
    private String usuario;
    private String codigoCampo;
    private Dialog dialogoCargando;
    private RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa_codigo_registro);
        dialogoCargando = new Dialog(this);
        request = Volley.newRequestQueue(this);
        Bundle miBundle = this.getIntent().getBundleExtra("bundle");
        String codigoJson = miBundle.getString("codigo");
        usuario = miBundle.getString("usuario");

        campoCodigo = findViewById(R.id.campoCodigoValidacion);
        Button btnCodigo = findViewById(R.id.btnRegistrarCodigo);
        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campoCodigo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(IngresaCodigoRegistro.this, "Por favor ingrese el codigo", Toast.LENGTH_SHORT).show();
                } else {
                    codigoCampo = campoCodigo.getText().toString();
                    enviaCodigo();
                }

            }
        });
    }

    private void dialogoCargando() {
        dialogoCargando.setContentView(R.layout.popup_cargando);
        Objects.requireNonNull(dialogoCargando.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCargando.show();
    }

    private void guardarCredenciales(String correo) {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo", correo);
        editor.commit();
    }

    private void guardarNombre(String nombre) {
        SharedPreferences preferences = getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombre",nombre);
        editor.commit();
    }

    private void enviaCodigo() {
        dialogoCargando();
        String url;
        java.lang.System.setProperty("https.protocols", "TLSv1");
        url = getApplicationContext().getString(R.string.ipEnviaCodigo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("Codigo invalido o vencido")) {
                    dialogoCargando.hide();
                    Toast.makeText(IngresaCodigoRegistro.this, "Codigo invalido o vencido", Toast.LENGTH_LONG).show();
                    Log.i("********RESULTADO", "Respuesta server" + response);
                } else {
                    Log.i("********RESULTADO", "Respuesta server" + response);
                    guardarNombre(response);
                    guardarCredenciales(usuario);
                    dialogoCargando.hide();
                    Intent intent = new Intent(IngresaCodigoRegistro.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("RESULT*****************", "NO SE REGISTRA " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("codigo", codigoCampo);
                parametros.put("correo", usuario);
                Log.i("--------PARAMETROS ", parametros.toString());
                return parametros;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.add(stringRequest);
    }


}
