package club.contaniif.contaniff.actividades;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Dialog dialogoAyuda;
    Dialog dialogoCargando;
    boolean seleccionado = false;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    TextView puntosUsuario;
    String correo;
    Button salir;
    FloatingActionButton adelante;
    int imagen = 1;
    ImageView ayuda,fondoAyuda;
    String puntajeUrl, imagenUrl;
    ImageView medalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogoAyuda = new Dialog(this);
        dialogoCargando = new Dialog(this);

        puntosUsuario = findViewById(R.id.puntosUsuario);
        medalla=findViewById(R.id.tipodemedalla);
        cargarCredenciales();
        //ayuda = findViewById(R.id.btnAyuda);
/*        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogo();
            }
        });*/

    }

    private void cargarAyuda() {
        SharedPreferences preferences = getSharedPreferences("Ayuda", Context.MODE_PRIVATE);
        String ayuda = preferences.getString("ayuda", "no");
        if (ayuda.equalsIgnoreCase("no")) {
            showDialogo();
        }else {
            Toast.makeText(getApplicationContext(),"Ya se mostro la ventana de ayuda",Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogoCargando() {
        dialogoCargando.setContentView(R.layout.popup_cargando);
        dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCargando.show();
    }

    private void showDialogo() {

        dialogoAyuda.setContentView(R.layout.popup_ayuda);
/*        fondoAyuda = dialogoAyuda.findViewById(R.id.imgAyuda);
        adelante = dialogoAyuda.findViewById(R.id.fabAdelante);
        adelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen++;
                if (imagen > 3){
                    imagen = 3;
                }

                switch (imagen){
                    case 1:
                        fondoAyuda.setImageDrawable(getDrawable(R.drawable.num1));
                        break;
                    case 2:
                        fondoAyuda.setImageDrawable(getDrawable(R.drawable.num2));
                        break;
                    case 3:
                        fondoAyuda.setImageDrawable(getDrawable(R.drawable.num3));
                        break;

                }

                Toast.makeText(getApplicationContext(),"Valos numaro " + imagen,Toast.LENGTH_SHORT).show();
            }
        });*/

        dialogoAyuda.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoAyuda.show();
        guardarAyuda();
    }

    private void guardarAyuda() {
        SharedPreferences preferences = getSharedPreferences("Ayuda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ayuda","visto");
        editor.commit();
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        if (credenciales != "No existe el valor") {
            correo = credenciales;
            cargarAyuda();
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
        dialogoCargando();
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
            imagenUrl = response.optJSONArray("medalla").getJSONObject(0).optString("medalla");
            dialogoCargando.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mostrarImg(imagenUrl);
        puntosUsuario.setText(puntajeUrl);


    }

  private void mostrarImg(String imagenUrl) {
        String ip=getApplicationContext().getString(R.string.imgRendimiento);

        final String urlImagen="https://"+ip+"medallas/"+imagenUrl+".png";
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