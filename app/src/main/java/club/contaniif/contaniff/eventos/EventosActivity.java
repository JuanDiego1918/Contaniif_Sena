package club.contaniif.contaniff.eventos;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.PaginacionNumeroAdapter;
import club.contaniif.contaniff.entidades.EventoVo;
import club.contaniif.contaniff.entidades.NumeroVo;

public class EventosActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static ArrayList<EventoVo> listaEventos;
    private static ArrayList<NumeroVo> listaNumero;
    private Dialog dialogoCargando;

    private static RecyclerView recyclerViewNumero;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        dialogoCargando = new Dialog(this);
        listaEventos = new ArrayList<>();
        listaNumero = new ArrayList<>();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        recyclerViewNumero = findViewById(R.id.numeroPaginacion);
        error=findViewById(R.id.errorEventos);

        cargarWebService();
        ///////////////////////
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

///////////////////////

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }
    private void cargarWebService() {
        dialogoCargando();
        recyclerViewNumero.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewNumero.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        RequestQueue request = Volley.newRequestQueue(getApplication());
        String url = "http://" + getApplicationContext().getString(R.string.ip) + "eventos.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            Objects.requireNonNull(dialogoCargando.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        }catch (Exception e){
            Log.i("Error " , e.toString());
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        for (int i=0;i<5;i++){
            Toast.makeText(getApplication(), "En este momento no hay eventos programados, si sabe de alguno y quiere que aparezca en esta sección favor escribir al correo: administrador@contaniif.club ", Toast.LENGTH_LONG).show();
        }

        mViewPager.setVisibility(View.INVISIBLE);
        this.error.setVisibility(View.VISIBLE);
        dialogoCargando.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("eventos");
        try {
            JSONObject jsonObject;
            for (int i = 0; i < json.length(); i++) {
                EventoVo miEventoVo = new EventoVo();
                NumeroVo miNumeroVo = new NumeroVo();
                miNumeroVo.setNumeroPagina(i + 1);
                jsonObject = json.getJSONObject(i);
                miEventoVo.setNombre(jsonObject.optString("nombre"));
                miEventoVo.setFecha(jsonObject.optString("fecha"));
                miEventoVo.setLugar(jsonObject.optString("lugar"));
                miEventoVo.setDescripcion(jsonObject.optString("descripcion"));
                miEventoVo.setImage(jsonObject.optString("id"));
                listaEventos.add(miEventoVo);
                listaNumero.add(miNumeroVo);
            }
            mViewPager.setAdapter(mSectionsPagerAdapter);

            PaginacionNumeroAdapter miNumeroAdapter = new PaginacionNumeroAdapter(listaNumero, getApplicationContext());
            recyclerViewNumero.setAdapter(miNumeroAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplication(), "En este momento no hay eventos programados, si sabe de alguno y quiere que aparezca en esta sección favor escribir al correo: administrador@contaniif.club ", Toast.LENGTH_LONG).show();
        }

        dialogoCargando.dismiss();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        TextView fecha, descripcion, lugar, nombre;
        ImageView img;
        RequestQueue request;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            funciona();
            return fragment;
        }

        private static void funciona() {
            Log.v("hola", "ENTRA");
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);

            nombre = rootView.findViewById(R.id.nombreEvento);
            fecha = rootView.findViewById(R.id.fechaEvento);
            descripcion = rootView.findViewById(R.id.descripEvento);
            img = rootView.findViewById(R.id.ImagenEvento);
            lugar = rootView.findViewById(R.id.lugarEvento);
            request = Volley.newRequestQueue(Objects.requireNonNull(getContext()));


            mostrarImg(listaEventos.get(Objects.requireNonNull(getArguments()).getInt(ARG_SECTION_NUMBER)).getImage());
            nombre.setText(listaEventos.get(getArguments().getInt(ARG_SECTION_NUMBER)).getNombre());
            descripcion.setText(listaEventos.get(getArguments().getInt(ARG_SECTION_NUMBER)).getDescripcion()+" \n ");
            fecha.setText(listaEventos.get(getArguments().getInt(ARG_SECTION_NUMBER)).getFecha());
            lugar.setText(listaEventos.get(getArguments().getInt(ARG_SECTION_NUMBER)).getLugar());

            return rootView;
        }

        private void mostrarImg(String rutaImagen) {
            String ip = Objects.requireNonNull(getContext()).getString(R.string.ipImg);

            final String urlImagen = "http://" + ip + rutaImagen + ".jpg";
            ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    img.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
                }
            });
            request.add(imageRequest);
        }

        class SectionsPagerAdapter extends FragmentPagerAdapter {

            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(final int position) {
                // getItem is called to instantiate the fragment for the given page.

                // Return a PlaceholderFragment (defined as a static inner class below).
                return PlaceholderFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                // Show 3 total pages.
                return listaNumero.size();
            }
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            // getItem is called to instantiate the fragment for the given page.

            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return listaNumero.size();
        }
    }
}