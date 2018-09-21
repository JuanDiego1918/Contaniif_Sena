package club.contaniif.contaniff.grupos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import club.contaniif.contaniff.adapter.AdapterGurpos;
import club.contaniif.contaniff.entidades.GruposVo;
import club.contaniif.contaniff.entidades.PreguntasVo;
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
public class Grupos extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerGrupos;
    ArrayList<GruposVo>listaGrupos;
    RequestQueue request;
    String dato;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progreso;
    AdapterGurpos adapter;
    String idusuario;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_grupos, container, false);
        request = Volley.newRequestQueue(getContext());
        recyclerGrupos = vista.findViewById(R.id.recyclerGrupos);
        cargarWebservices();


        return vista;
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        idusuario = credenciales;
    }

    private void cargarWebservices() {
        progreso = new ProgressDialog(this.getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url = this.getString(R.string.ipGrupos)+idusuario;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void opciones() {
        final CharSequence[] opciones={"Aceptar","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("¿ Esta seguro que desea unirse a el grupo " + dato +  " ? ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Aceptar")){
                    //Se envian los datos de la persona y a que grupo va a pertenecer
                }else{

                }
            }
        });
        builder.show();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        JSONObject jsonObject = null;
        listaGrupos = new ArrayList<>();
        GruposVo gruposVo;

        try {

            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                gruposVo = new GruposVo();
                gruposVo.setEsta(jsonObject.getString("esta"));
                gruposVo.setGrupo(jsonObject.getString("grupo"));
                gruposVo.setCodigo(jsonObject.getString("codigo"));

                listaGrupos.add(gruposVo);
                progreso.hide();
            }

        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" + " " + response, Toast.LENGTH_LONG).show();
            progreso.hide();
        }

        adapter = new AdapterGurpos(listaGrupos);
        recyclerGrupos.setAdapter(adapter);
        recyclerGrupos.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               dato = listaGrupos.get(recyclerGrupos.getChildAdapterPosition(view)).getGrupo();
               opciones();
               //Toast.makeText(getContext(),"Codigo " + dato,Toast.LENGTH_SHORT).show();
            }
        }));
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
        void onFragmentInteraction(Uri uri);
    }
}
