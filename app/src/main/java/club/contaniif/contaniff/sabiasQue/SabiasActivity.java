package club.contaniif.contaniff.sabiasQue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.AdapterSabias;
import club.contaniif.contaniff.adapter.PaginacionNumeroAdapter;
import club.contaniif.contaniff.entidades.EventoVo;
import club.contaniif.contaniff.entidades.NumeroVo;
import club.contaniif.contaniff.entidades.SabiasVo;

public class SabiasActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    SabiasVo sabiasVo;
    ArrayList<SabiasVo> listaSabias;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    RecyclerView recyclerView;
    AdapterSabias adapterSabias;
    Bundle miBundle;
    int categoria;
    Dialog dialog;
    Dialog dialogoCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabias);
        dialogoCargando = new Dialog(this);
        listaSabias = new ArrayList();
        recyclerView = findViewById(R.id.recyclerSabias);
        miBundle = this.getIntent().getExtras();
        categoria = Integer.parseInt(miBundle.getString("id"));
        dialog = new Dialog(this);
        cargarWebService();

    }

    private void cargarWebService() {
        dialogoCargando();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        request = Volley.newRequestQueue(getApplication());
        String url = "https://" + getApplicationContext().getString(R.string.ip) + "sabias.php?categoria=" + categoria;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void dialogoCargando() {
        dialogoCargando.setContentView(R.layout.popup_cargando);
        dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCargando.show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplication(), "NO se pudo Consultar:" + error.toString(), Toast.LENGTH_LONG).show();
        Log.i("Error", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("sabias");
        try {
            JSONObject jsonObject = null;
            for (int i = 0; i < json.length(); i++) {
                sabiasVo = new SabiasVo();
                jsonObject = json.getJSONObject(i);
                sabiasVo.setTitulo(jsonObject.optString("titulo"));
                sabiasVo.setId(jsonObject.optInt("id"));
                sabiasVo.setTeoria(jsonObject.optString("teoria"));
                listaSabias.add(sabiasVo);
            }
            // Set up the ViewPager with the sections adapter
            adapterSabias = new AdapterSabias(listaSabias);
            recyclerView.setAdapter(adapterSabias);
            adapterSabias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ventana(view);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
        }
        dialogoCargando.dismiss();

    }

    private void ventana(View view) { TextView titulo, teoria;
        dialog.setContentView(R.layout.popup_sabias);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        titulo = dialog.findViewById(R.id.tituloSabiaspopup);
        teoria = dialog.findViewById(R.id.teoriaSabias);
        titulo.setText("" + listaSabias.get(recyclerView.getChildAdapterPosition(view)).getTitulo());
        teoria.setText("" + listaSabias.get(recyclerView.getChildAdapterPosition(view)).getTeoria());
        dialog.show();

    }
}
