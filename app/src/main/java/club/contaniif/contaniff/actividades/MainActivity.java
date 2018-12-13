package club.contaniif.contaniff.actividades;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.IOException;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.eventos.EventosActivity;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    Dialog dialogoAyuda;
    Dialog dialogoCargando;
    boolean seleccionado = false;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    TextView puntosUsuario;
    String correo;
    Button btnSalirAyuda;
    FloatingActionButton adelante;
    int imagen = 0;
    ImageView ayuda, fondoAyuda;
    WebView fondo;
    String puntajeUrl, imagenUrl;
    ImageView medalla;
    ImageButton next, back;
    TextView notificacionevento, notificacionSabias, notificacionVideos, notificaionVersion;
    ImageView imagenNotificacionEvento, imagenNotificacionSabias, imagenNotificacionVideo;
    Drawable siguiente, notificacion;

    String version;
    int videos, eventos, sabias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogoAyuda = new Dialog(this);
        dialogoCargando = new Dialog(this);

        puntosUsuario = findViewById(R.id.puntosUsuario);
        medalla = findViewById(R.id.tipodemedalla);
        cargarCredenciales();
        cargarWebService();
        ayuda = findViewById(R.id.btnAyuda);
        notificacionevento = findViewById(R.id.txtnotificacionEventos);
        notificacionSabias = findViewById(R.id.txtnotificacionSabias);
        notificacionVideos = findViewById(R.id.txtnotificacionVideo);
        imagenNotificacionEvento = findViewById(R.id.notificacionEventos);
        imagenNotificacionSabias = findViewById(R.id.notificacionSabias);
        imagenNotificacionVideo = findViewById(R.id.notificacionVideo);
        notificaionVersion = findViewById(R.id.version);
        siguiente = getResources().getDrawable(R.drawable.logo_linea_siguiente);
        notificacion = getResources().getDrawable(R.drawable.notificacion);


        imagenNotificacionEvento.setImageDrawable(siguiente);
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogo();
            }
        });

    }

    private void cargarAyuda() {
        SharedPreferences preferences = getSharedPreferences("Ayuda", Context.MODE_PRIVATE);
        String ayuda = preferences.getString("ayuda", "no");
        if (ayuda.equalsIgnoreCase("no")) {
            showDialogo();
        } else {
            //Toast.makeText(getApplicationContext(),"Ya se mostro la ventana de ayuda",Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        } catch (Exception e) {
            Log.i("Error ", e.toString());
        }

    }

    private void showDialogo() {
        dialogoAyuda.setContentView(R.layout.popup_ayuda);
        fondo = dialogoAyuda.findViewById(R.id.imagenFondoAyuda);
        next = dialogoAyuda.findViewById(R.id.btnNextAyuda);
        back = dialogoAyuda.findViewById(R.id.btnBackAyuda);
        fondo.loadUrl("http://contaniif.club/img/ayuda/grendimiento.gif");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = imagen + 1;
                back.setVisibility(View.VISIBLE);
                if (imagen >= 4) {
                    next.setVisibility(View.INVISIBLE);
                }
                cambiarImagen(imagen);

            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen = imagen - 1;
                next.setVisibility(View.VISIBLE);
                if (imagen <= 1) {
                    back.setVisibility(View.INVISIBLE);
                }
                cambiarImagen(imagen);

            }
        });

        dialogoAyuda.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoAyuda.show();
        guardarAyuda();
    }

    private void cambiarImagen(int imagen) {
        switch (imagen) {
            case 1:
                fondo.loadUrl("http://contaniif.club/img/ayuda/grendimiento.gif");
                break;
            case 2:
                fondo.loadUrl("http://contaniif.club/img/ayuda/gf_rendimiento.gif");
                break;
            case 3:
                fondo.loadUrl("http://contaniif.club/img/ayuda/gif_ayuda.gif");
                break;
            case 4:
                fondo.loadUrl("http://contaniif.club/img/ayuda/gif_empezar.gif");
                break;

        }
    }

    private void guardarAyuda() {
        SharedPreferences preferences = getSharedPreferences("Ayuda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ayuda", "visto");
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
        String url = "http://" + getApplicationContext().getString(R.string.ip) + "puntaje.php?idusuario=" + correo;
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
            puntajeUrl = json.getJSONObject(0).optString("puntos");
            imagenUrl = response.optJSONArray("eventos").getJSONObject(0).optString("medalla");
            version = response.optJSONArray("eventos").getJSONObject(0).optString("version");
            sabias = response.optJSONArray("eventos").getJSONObject(0).optInt("sabias");
            eventos = response.optJSONArray("eventos").getJSONObject(0).optInt("eventos");
            videos = response.optJSONArray("eventos").getJSONObject(0).optInt("videos");

            notificaciones();

            dialogoCargando.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mostrarImg(imagenUrl);
        puntosUsuario.setText(puntajeUrl);

    }

    private void notificaciones() {
        if (videos != 0) {
            imagenNotificacionVideo.setImageDrawable(notificacion);
            notificacionVideos.setVisibility(View.VISIBLE);
            notificacionVideos.setText("" + videos);
        } else if (videos == 0) {
            imagenNotificacionVideo.setImageDrawable(siguiente);
            notificacionVideos.setVisibility(View.INVISIBLE);
        }

        if (eventos != 0) {
            imagenNotificacionEvento.setImageDrawable(notificacion);
            notificacionevento.setVisibility(View.VISIBLE);
            notificacionevento.setText("" + eventos);
        } else if (eventos == 0) {
            imagenNotificacionEvento.setImageDrawable(siguiente);
            notificacionevento.setVisibility(View.INVISIBLE);
        }

        if (sabias != 0) {
            imagenNotificacionSabias.setImageDrawable(notificacion);
            notificacionSabias.setVisibility(View.VISIBLE);
            notificacionSabias.setText("" + sabias);
        } else if (sabias == 0) {
            imagenNotificacionSabias.setImageDrawable(siguiente);
            notificacionSabias.setVisibility(View.INVISIBLE);
        }

        if (!version.equalsIgnoreCase("0")) {
            notificaionVersion.setVisibility(View.VISIBLE);
            notificaionVersion.setText(version);
        }else {
            notificaionVersion.setVisibility(View.INVISIBLE);
        }
    }

    private void mostrarImg(String imagenUrl) {
        String ip = getApplicationContext().getString(R.string.imgRendimiento);

        final String urlImagen = "http://" + ip + "medallas/" + imagenUrl + ".png";
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                medalla.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);


    }

}