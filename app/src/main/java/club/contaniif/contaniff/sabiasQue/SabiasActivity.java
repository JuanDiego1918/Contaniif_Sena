package club.contaniif.contaniff.sabiasQue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.AdapterSabias;
import club.contaniif.contaniff.entidades.SabiasVo;

public class SabiasActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private ArrayList<SabiasVo> listaSabias;
    private RecyclerView recyclerView;
    private int categoria;
    private Dialog dialog;
    private Dialog dialogoCargando;
    private ImageView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabias);
        dialogoCargando = new Dialog(this);
        listaSabias = new ArrayList();
        recyclerView = findViewById(R.id.recyclerSabias);
        error = findViewById(R.id.errorSabiasQue);
        Bundle miBundle = this.getIntent().getExtras();
        categoria = Integer.parseInt(Objects.requireNonNull(miBundle).getString("id"));
        dialog = new Dialog(this);
        cargarWebService();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        RequestQueue request = Volley.newRequestQueue(getApplication());
        String url = "http://" + getApplicationContext().getString(R.string.ip) + "sabias.php?categoria=" + categoria;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            Objects.requireNonNull(dialogoCargando.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        } catch (Exception e) {
            Log.i("Error ", e.toString());
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplication(), "Estamos realizando ajustes, por favor verifique mas tarde", Toast.LENGTH_LONG).show();
        recyclerView.setVisibility(View.INVISIBLE);
        this.error.setVisibility(View.VISIBLE);
        dialogoCargando.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("sabias");
        try {
            JSONObject jsonObject;
            for (int i = 0; i < json.length(); i++) {
                SabiasVo sabiasVo = new SabiasVo();
                jsonObject = json.getJSONObject(i);
                sabiasVo.setTitulo(jsonObject.optString("titulo"));
                sabiasVo.setId(jsonObject.optInt("id"));
                sabiasVo.setTeoria(jsonObject.optString("teoria"));
                listaSabias.add(sabiasVo);
            }
            // Set up the ViewPager with the sections adapter
            AdapterSabias adapterSabias = new AdapterSabias(listaSabias);
            recyclerView.setAdapter(adapterSabias);
            adapterSabias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ventana(view);
                }
            });
        } catch (JSONException e) {
            Toast.makeText(getApplication(), "Estamos realizando ajustes, por favor verifique mas tarde", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(View.INVISIBLE);
            error.setVisibility(View.VISIBLE);
        }
        dialogoCargando.dismiss();

    }

    private void ventana(View view) {
        TextView titulo, teoria;
        dialog.setContentView(R.layout.popup_sabias);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        titulo = dialog.findViewById(R.id.tituloSabiaspopup);
        teoria = dialog.findViewById(R.id.teoriaSabias);
        titulo.setText("" + listaSabias.get(recyclerView.getChildAdapterPosition(view)).getTitulo());
        teoria.setText("" + listaSabias.get(recyclerView.getChildAdapterPosition(view)).getTeoria());
        dialog.show();

    }
}
