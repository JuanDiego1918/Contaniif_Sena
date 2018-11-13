package club.contaniif.contaniff.actividades;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.AdapterActivos;
import club.contaniif.contaniff.entidades.ActivosVo;

public class ActivosActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {


    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private RecyclerView obtenidos;
    private RecyclerView disponible;
    private String credenciales;
    private String correo;
    private ArrayList<ActivosVo> listActivos;
    private ArrayList<ActivosVo> listaDisponible;
    private ActivosVo activosVo;
    private Dialog dialogoCargando;
    private Dialog dialog;
    private Button comprar;
    private double cantidadMonedas;
    private ImageView Imgactivo;
    private TextView monedas;
    private AdapterActivos adapterActivos;
    private AdapterActivos adapterDisponible;
    private StringRequest stringRequest;
    private double valorObjeto;
    private String nombre;
    private int tipoActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos);
        dialogoCargando = new Dialog(this);
        obtenidos = findViewById(R.id.activosObtenidos);
        disponible = findViewById(R.id.activosDisponibles);
        monedas = findViewById(R.id.cantidadMonedasAc);
        dialog = new Dialog(this);
        listActivos = new ArrayList<>();
        listaDisponible = new ArrayList<>();
        cargarWebService();
        cargarNombre();
        Bundle miBundle = getIntent().getExtras();
        if (miBundle != null) {
            tipoActivo = miBundle.getInt("tipo");
            cantidadMonedas = cambiarVarible(Objects.requireNonNull(miBundle.getString("puntos")));
            monedas.setText("Monedas: " + miBundle.getString("puntos"));
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

    private void cargarWebService() {
        dialogoCargando();
        cargarCredenciales();
        request = Volley.newRequestQueue(getApplication());
        String url = "https://" + getApplicationContext().getString(R.string.ip) + "activos.php?idusuario=" + correo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void dialogoCargando() {
        dialogoCargando.setContentView(R.layout.popup_cargando);
        dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCargando.show();
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = this.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        this.credenciales = credenciales;
        correo = credenciales;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplication(), "NO se pudo Consultar:" + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        listaDisponible.clear();
        listActivos.clear();
        adapterActivos = null;
        adapterDisponible = null;
        JSONArray json = response.optJSONArray("activos");
        try {
            JSONObject jsonObject;
            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                activosVo = new ActivosVo();
                activosVo.setId(jsonObject.optString("id"));
                activosVo.setNombre(jsonObject.optString("nombre"));
                activosVo.setDescripcion(jsonObject.optString("descripcion"));
                activosVo.setValor(jsonObject.optString("valor"));
                activosVo.setDescuento(jsonObject.optString("descuento"));
                activosVo.setEstado(jsonObject.optString("estado"));
                if (jsonObject.optString("estado").equals("Tiene")) {
                    listActivos.add(activosVo);
                } else {
                    listaDisponible.add(activosVo);
                }
            }

            switch (tipoActivo) {
                case 1:
                    adapterActivos = new AdapterActivos(listActivos, getApplicationContext());
                    obtenidos.setAdapter(adapterActivos);
                    break;
                case 2:
                    adapterDisponible = new AdapterActivos(listaDisponible, getApplicationContext());
                    disponible.setAdapter(adapterDisponible);

                    adapterDisponible.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cargarVentana(listaDisponible.get(disponible.getChildAdapterPosition(view)));

                        }
                    });
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
        }
        dialogoCargando.dismiss();
    }

    private void cargarVentana(ActivosVo activos) {
        final ActivosVo vo = activos;
        TextView titulo, descrip, valor, descuento;
        Button cancelar;
        dialog.setContentView(R.layout.popup_activos);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        comprar = dialog.findViewById(R.id.btnComprar);
        cancelar = dialog.findViewById(R.id.btnCancel);
        titulo = dialog.findViewById(R.id.tituloPopupActivo);
        descrip = dialog.findViewById(R.id.descripActivoPopup);
        valor = dialog.findViewById(R.id.precioActivoPopup);
        descuento = dialog.findViewById(R.id.precioDescPopup);
        Imgactivo = dialog.findViewById(R.id.imagenPopupActivo);

        descuento.setText("Descuento diario: " + vo.getDescuento());
        titulo.setText("" + vo.getNombre());
        descrip.setText("" + vo.getDescripcion());
        valor.setText("" + vo.getValor());

        cargarImgGeneral(vo.getId());
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quitaPts = cambiarPts(vo.getValor());
                valorObjeto = cambiarVarible(quitaPts);
                if (valorObjeto > cantidadMonedas) {
                    Toast.makeText(ActivosActivity.this, nombre + ", Saldo Insuficiente", Toast.LENGTH_SHORT).show();
                } else {
                    comprar.setEnabled(false);
                    realizarComprar(vo);
                    dialog.dismiss();
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cargarNombre() {
        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "No existe el valor");
        if (!Objects.equals(nombre, "No existe el valor")) {
            this.nombre = nombre;
        }
    }

    private void realizarComprar(ActivosVo vo) {
        String url;
        url = "https://" + getApplicationContext().getString(R.string.ip) + "guardaactivos.php?idusuario=" + correo + "&&activo=" + vo.getId();
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("registra")) {
                    Toast.makeText(getApplicationContext(), "Realiza Cambios" + response, Toast.LENGTH_LONG).show();
                } else {
                    cantidadMonedas = cantidadMonedas - valorObjeto;
                    monedas.setText("Monedas: " + cantidadMonedas);
                    cargarWebService();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Verifique Conexion A Internet" + error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        request.add(stringRequest);
    }

    private void cargarImgGeneral(String rutaImagen) {
        String ip = getApplicationContext().getString(R.string.imgRendimiento);

        final String urlImagen = "https://" + ip + "activos/" + rutaImagen + ".png";
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Imgactivo.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    private double cambiarVarible(String puntosDisponibles) {
        String cambio = puntosDisponibles.replaceAll(",", "");
        Log.v("*******************", cambio);
        return Double.parseDouble(cambio);
    }

    private String cambiarPts(String puntosDisponibles) {
        return puntosDisponibles.replaceAll("CriptoNIIF.", "");
    }
}
