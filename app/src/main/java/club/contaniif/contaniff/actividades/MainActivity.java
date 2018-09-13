package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.PaginacionNumeroAdapter;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.entidades.EventoVo;
import club.contaniif.contaniff.entidades.NumeroVo;
import club.contaniif.contaniff.eventos.EventosActivity;
import club.contaniif.contaniff.interfaces.AllFragments;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    boolean seleccionado = false;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    TextView puntosUsuario;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puntosUsuario = findViewById(R.id.puntosUsuario);
        cargarCredenciales();

    }

    private void cargarCredenciales() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        if (credenciales != "No existe el valor") {
            correo = credenciales;
        }
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
                miIntent = new Intent(MainActivity.this, EventosActivity.class);
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
            miIntent = new Intent(MainActivity.this, ActivityContenedora.class);
            miIntent.putExtra("dato", miBundle);
        }
        startActivity(miIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Datos.actualizarPuntos == true) {
            Datos.actualizarPuntos = false;
            cargarWebService();
        }
    }

    private void cargarWebService() {
        request = Volley.newRequestQueue(getApplication());
        String url = "https://" + getApplicationContext().getString(R.string.ip) + "puntaje.php?idusuario="+correo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplication(), "No se pudo Consultar:" + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("puntaje");
        JSONObject jsonObject = null;

        try {
            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                puntosUsuario.setText("" + jsonObject.optString("puntos"));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplication(), "No :" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }
}