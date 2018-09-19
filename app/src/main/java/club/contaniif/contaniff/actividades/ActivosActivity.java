package club.contaniif.contaniff.actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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
import club.contaniif.contaniff.adapter.AdapterSabias;
import club.contaniif.contaniff.entidades.SabiasVo;

public class ActivosActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    RecyclerView recyclerView;
    String credenciales,correo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos);


        cargarWebService();
    }

    private void cargarWebService() {
        cargarCredenciales();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //      recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        request = Volley.newRequestQueue(getApplication());
        String url = "https://" + getApplicationContext().getString(R.string.ip) + "activos.php?idusuario="+correo;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
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
        JSONArray json = response.optJSONArray("activos");
        try {
            JSONObject jsonObject = null;
            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                Toast.makeText(getApplication(), "" + jsonObject.optString("estado"), Toast.LENGTH_LONG).show();

            }
            // Set up the ViewPager with the sections adapter
//            adapterSabias = new AdapterSabias(listaSabias);
//            recyclerView.setAdapter(adapterSabias);
//            adapterSabias.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ventana(view);
//                }
//            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
        }
    }
}
