package club.contaniif.contaniff.sabiasQue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import club.contaniif.contaniff.adapter.CategoriasAdapter;
import club.contaniif.contaniff.entidades.CategoriasVo;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.interfaces.Puente;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoriasSabias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoriasSabias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriasSabias extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoriasSabias() {
        // Required empty public constructor
    }

    View view;
    RecyclerView recyclerView;
    RequestQueue request;
    Puente puente;
    Activity activity;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<CategoriasVo> listasCategorias;
    Dialog dialogoCargando;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriasSabias.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriasSabias newInstance(String param1, String param2) {
        CategoriasSabias fragment = new CategoriasSabias();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categorias_sabias, container, false);
        dialogoCargando = new Dialog(this.getContext());
        recyclerView = view.findViewById(R.id.categoriasSabias);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        request = Volley.newRequestQueue(getContext());
        cargarWebservices();
        return view;
    }

    private void cargarWebservices() {
        dialogoCargando();
        String url = "https://" + getContext().getString(R.string.ip) + "VideosCategorias.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void dialogoCargando() {
        dialogoCargando.setContentView(R.layout.popup_cargando);
        dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCargando.show();
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

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            puente = (Puente) this.activity;
        }

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
        Toast.makeText(getContext(), "No se pudo Consultar:" + error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        listasCategorias = new ArrayList<>();
        JSONArray json = response.optJSONArray("categorias");
        try {
            JSONObject jsonObject = null;
            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                CategoriasVo categoriasVo = new CategoriasVo();
                categoriasVo.setId(jsonObject.optString("id"));
                categoriasVo.setNombre(jsonObject.optString("nombre"));
                listasCategorias.add(categoriasVo);
            }
            CategoriasAdapter miCategoriasAdapter = new CategoriasAdapter(listasCategorias);
            recyclerView.setAdapter(miCategoriasAdapter);
            miCategoriasAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    puente.sabias(listasCategorias.get(recyclerView.getChildAdapterPosition(v)).getId());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión" +
                    " " + response, Toast.LENGTH_LONG).show();
        }
        dialogoCargando.dismiss();
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
