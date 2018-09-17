package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.eventos.EventosActivity;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    boolean seleccionado = false;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    TextView puntosUsuario;
    String correo;
    String puntajeUrl, imagenUrl;
    ImageView medalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puntosUsuario = findViewById(R.id.puntosUsuario);
        medalla=findViewById(R.id.tipodemedalla);
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

        try {
            puntajeUrl =json.getJSONObject(0).optString("puntos");
            imagenUrl = json.getJSONObject(1).optString("medalla");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mostrarImg(imagenUrl);
        puntosUsuario.setText(puntajeUrl);



    }

    private void mostrarImg(String imagenUrl) {
        String ip=getApplicationContext().getString(R.string.imgRendimiento);

        final String urlImagen="https://"+ip+imagenUrl+".png";
        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                medalla.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);


    }
}