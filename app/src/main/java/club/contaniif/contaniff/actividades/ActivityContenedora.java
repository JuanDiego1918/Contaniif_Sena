package club.contaniif.contaniff.actividades;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.acercaDe.AcercaDeFragment;
import club.contaniif.contaniff.configuracion.Configuracion;
import club.contaniif.contaniff.entidades.PreguntasVo;
import club.contaniif.contaniff.entidades.VolleySingleton;
import club.contaniif.contaniff.interfaces.AllFragments;
import club.contaniif.contaniff.interfaces.Puente;
import club.contaniif.contaniff.miRendimiento.RendimiendoFragment;
import club.contaniif.contaniff.principal.Pantalla_empezar;
import club.contaniif.contaniff.principal.Pantalla_empezar_drag;
import club.contaniif.contaniff.sabiasQue.CategoriasSabias;
import club.contaniif.contaniff.sabiasQue.SabiasActivity;
import club.contaniif.contaniff.videos.CategoriasVideosFragment;
import club.contaniif.contaniff.videos.VideosActivity;

public class ActivityContenedora extends AppCompatActivity implements AllFragments, Puente, Response.Listener<JSONObject>, Response.ErrorListener {

    private int pantalla;
    private boolean seleccionado = false;
    private Fragment miFragment = null;
    private PreguntasVo preguntas;
    private Dialog dialogoCargando;
    private ArrayList<String> listaImagenes;
    private ArrayList<String> listaPreguntas;
    private String credenciales;
    int tipo;
    private JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    private int numeroPreguntaGlobal;
    private ArrayList<String> listaGobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaGobal = new ArrayList<>();
        setContentView(R.layout.activity_contenedora);

        dialogoCargando = new Dialog(this);
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
                //seleccionado = true;
                //miFragment = new Pantalla_empezar_drag();
                reinciar(0, listaGobal);
                break;
            case 3:
                seleccionado = true;
                miFragment = new CategoriasVideosFragment();
                break;
            case 4:
                seleccionado = true;
                miFragment = new CategoriasSabias();
                break;
            case 5:
                seleccionado = true;
                miFragment = new Configuracion();
                break;
            case 6:
                seleccionado = true;
                miFragment = new AcercaDeFragment();
                break;
        }
        if (seleccionado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, miFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction() {

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
    public void reinciar(int numeroPregunta, ArrayList<String> lista) {
        numeroPreguntaGlobal = numeroPregunta;
        listaGobal = lista;
        cargarCredenciales();
        cargarWebservices();
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

    @Override
    public void onErrorResponse(VolleyError error) {
        dialogoCargando.dismiss();
        Toast.makeText(this, "En este momento las preguntas no estan disponibles", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onResponse(JSONObject response) {
        preguntas = null;
        JSONArray json = response.optJSONArray("pregunta");
        JSONObject jsonObject;
        listaPreguntas = new ArrayList<>();
        listaImagenes = new ArrayList<>();
        final ArrayList<String> lista = new ArrayList<>();

        try {

            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                preguntas = new PreguntasVo();
                preguntas.setId(jsonObject.getInt("id"));
                preguntas.setPregunta(jsonObject.getString("pregunta"));
                preguntas.setCategoria(jsonObject.getInt("categoria"));
                preguntas.setPuntaje(jsonObject.getInt("puntaje"));
                preguntas.setTiempoDemora(jsonObject.getInt("tiempo"));
                preguntas.setTipo(jsonObject.getInt("tipopregunta"));
                preguntas.setRespuesta(jsonObject.getString("respuesta"));
                preguntas.setRetobuena(jsonObject.getString("retrobuena"));
                preguntas.setRetromala(jsonObject.getString("retromala"));
                listaPreguntas.add(jsonObject.getString("opcion"));
                listaImagenes.add(preguntas.getOpciones());
            }

            Bundle datos = new Bundle();
            datos.putInt("numeroPregunta", numeroPreguntaGlobal);
            datos.putStringArrayList("color", listaGobal);
            Bundle objeto = new Bundle();
            objeto.putSerializable("Objeto", preguntas);
            Bundle miBundle = new Bundle();
            miBundle.putBundle("Todo", datos);
            miBundle.putBundle("BundleObjeto", objeto);
            miBundle.putStringArrayList("respuestas", listaPreguntas);

            if (preguntas.getTipo() == 1 || preguntas.getTipo() == 2 || preguntas.getTipo() == 3) {
                miFragment = new Pantalla_empezar();
            } else if (preguntas.getTipo() == 4) {
                miFragment = new Pantalla_empezar_drag();
            }
            miFragment.setArguments(miBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, miFragment).commit();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" + " " + response, Toast.LENGTH_LONG).show();
        }


        dialogoCargando.dismiss();
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = this.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        this.credenciales = preferences.getString("correo", "No existe el valor");
        //Toast.makeText(getContext(),"Credenciales = " + this.credenciales, Toast.LENGTH_SHORT).show();
    }

    private void dialogoCargando() {
        dialogoCargando.setContentView(R.layout.popup_cargando);
        Objects.requireNonNull(dialogoCargando.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCargando.show();
    }

    private void cargarWebservices() {
        dialogoCargando();
        String ip = getApplicationContext().getString(R.string.ip);
        String url = "https://" + ip + "/wsConsultaPreguntaPrueba1.php?estudiante=" + credenciales;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
}
