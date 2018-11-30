package club.contaniif.contaniff.grupos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.AdapterGurpos;
import club.contaniif.contaniff.entidades.GruposVo;
import club.contaniif.contaniff.entidades.RecyclerViewOnClickListener;
import club.contaniif.contaniff.entidades.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Grupos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Grupos#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("EqualsBetweenInconvertibleTypes")
public class Grupos extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerGrupos;
    private ArrayList<GruposVo> listaGrupos;
    private Dialog dialogoCargando;
    private Dialog dialogGrupo;
    private String nombre;
    private RequestQueue request;
    private String dato;
    private String esta;
    private String dato2;
    private TextView campoGrupoP;
    private String idusuario;

    private String sinGrupo = "\nEn ContaNIIF los instructores o profesores pueden crear grupos de estudio, en este momento pertenece al grupo: _______, toque sobre un grupo para unirse a él.\n";

    public Grupos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Grupos.
     */
    // TODO: Rename and change types and number of parameters
    public static Grupos newInstance(String param1, String param2) {
        Grupos fragment = new Grupos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        cargarCredenciales();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_grupos, container, false);
        request = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        dialogoCargando = new Dialog(this.getContext());
        dialogGrupo = new Dialog(this.getContext());
        campoGrupoP = vista.findViewById(R.id.campoGrupoP);

        recyclerGrupos = vista.findViewById(R.id.recyclerGrupos);
        cargarWebservices();
        cargarNombre();
        return vista;
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        idusuario = preferences.getString("correo", "No existe el valor");
    }

    private void cargarWebservices() {
        dialogoCargando();
        String url = this.getString(R.string.ipGrupos) + idusuario;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
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

    private void cargarNombre() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "No existe el valor");
        if (!Objects.equals(nombre, "No existe el valor")) {
            this.nombre = nombre;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("grupos");
        JSONObject jsonObject;
        listaGrupos = new ArrayList<>();
        GruposVo gruposVo;
        String conGrupo = sinGrupo;

        try {

            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                gruposVo = new GruposVo();
                gruposVo.setEsta(jsonObject.getString("esta"));
                gruposVo.setGrupo(jsonObject.getString("grupo"));
                gruposVo.setCodigo(jsonObject.getString("codigo"));

                listaGrupos.add(gruposVo);
                esta = gruposVo.getEsta();
                conGrupo="\nEn ContaNIIF los instructores o profesores pueden crear grupos de estudio, en este momento pertenece al grupo: "+gruposVo.getEsta() + ", toque sobre un grupo para unirse a él. \n";
            }

            if (esta.equals("Ninguno")) {
                campoGrupoP.setText(sinGrupo);
            } else {
                campoGrupoP.setText(conGrupo);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" + " " + response, Toast.LENGTH_LONG).show();
        }

        AdapterGurpos adapter = new AdapterGurpos(listaGrupos);
        recyclerGrupos.setAdapter(adapter);
        recyclerGrupos.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dato = listaGrupos.get(recyclerGrupos.getChildAdapterPosition(view)).getCodigo();
                dato2 = listaGrupos.get(recyclerGrupos.getChildAdapterPosition(view)).getGrupo();
                showPopup();
            }
        }));

        dialogoCargando.dismiss();
    }

    private void showPopup() {

        Button aceptar, cancelar;
        TextView campoNombre, campoGrupo;
        try {
            dialogGrupo.setContentView(R.layout.popup_grupos);
            Objects.requireNonNull(dialogGrupo.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogGrupo.show();
        } catch (Exception e) {
            Log.i("Error ", e.toString());
        }

        campoNombre = dialogGrupo.findViewById(R.id.campoNombreGrupo);
        campoGrupo = dialogGrupo.findViewById(R.id.campoGrupo);

        campoNombre.setText(nombre);
        campoGrupo.setText("Esta seguro que desea unirse al grupo " + dato2);

        aceptar = dialogGrupo.findViewById(R.id.btnContinuarGrupo);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarGrupo();
                dialogGrupo.hide();
            }
        });

        cancelar = dialogGrupo.findViewById(R.id.btnCancelarGrupo);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGrupo.hide();
                Toast.makeText(getContext(), "No ha elegido ningun grupo ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registrarGrupo() {
        dialogoCargando();
        String url;
        java.lang.System.setProperty("https.protocols", "TLSv1");
        url = Objects.requireNonNull(getContext()).getString(R.string.ipRegistraGrupo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("asignado")) {
                    Log.i("********RESULTADO", "Respuesta server" + response);
                    dialogoCargando.hide();
                    Toast.makeText(getContext(), "Se a unido al grupo " + dato2, Toast.LENGTH_SHORT).show();
                    cargarWebservices();

                } else {
                    Toast.makeText(getContext(), "Por el momento el usuario no se puede registrar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.equals("com.android.volley.TimeoutError")) {
                    dialogoCargando.hide();
                    Toast.makeText(getContext(), "Por favor verificar la conexion a internet", Toast.LENGTH_SHORT).show();
                }

                Log.i("RESULTADO", "NO SE REGISTRA desde onError " + error.toString());
                Log.d("RESULT*****************", "NO SE REGISTRA desde onError " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String idUsuaio = idusuario;
                String curso = dato;

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idusuario", idUsuaio);
                parametros.put("curso", curso);
                Log.i("--------PARAMETROS ", parametros.toString());
                return parametros;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.add(stringRequest);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
