package club.contaniif.contaniff.miRendimiento;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.Datos;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RendimiendoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RendimiendoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RendimiendoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    String credenciales;
    String cojeComentario;
    String fecha;
    LinearLayout comentario;
    RequestQueue request;
    StringRequest stringRequest;

    public RendimiendoFragment() {
        // Required empty public constructor
    }

    View view;
    Activity activity;
    LinearLayout puntos, canjes, activos, comentarios;
    Dialog ventanaComentarios;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RendimiendoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RendimiendoFragment newInstance(String param1, String param2) {
        RendimiendoFragment fragment = new RendimiendoFragment();
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
        cargarCredenciales();
        request = Volley.newRequestQueue(getContext());
        Datos.actualizarPuntos=true;
        view = inflater.inflate(R.layout.fragment_rendimiendo, container, false);
        comentarios = view.findViewById(R.id.btnComentarios);
        ventanaComentarios = new Dialog(getContext());
        comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaComen();
            }
        });

        return view;
    }

    private void ventanaComen() {
        Button cancelar,enviar;
        final EditText campoComentario;

        ventanaComentarios.setContentView(R.layout.popup_comentarios);
        cancelar=ventanaComentarios.findViewById(R.id.btnCancelarComentario);
        enviar = ventanaComentarios.findViewById(R.id.enviar);
        campoComentario = ventanaComentarios.findViewById(R.id.campoComentario);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaComentarios.dismiss();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cojeComentario = (campoComentario.getText().toString());
                enviarDatosComentarios();
                obtenerFecha();
                ventanaComentarios.dismiss();

            }
        });

        ventanaComentarios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ventanaComentarios.show();
    }

    private void obtenerFecha() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date();
        fecha = dateFormat.format(date);
    }


    private void cargarCredenciales() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credenciales",Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo","No existe el valor");
        this.credenciales = credenciales;

    }

    private void enviarDatosComentarios() {

        String url;
        url = "http://"+getContext().getString(R.string.ipComentario);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progreso.hide();
                if (response.trim().equalsIgnoreCase("registra")) {
                    Toast.makeText(getContext(), "Comentario enviado " + response, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(),"Crede " + credenciales, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),"Comentario no registrado " + response, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(),"Crede " + credenciales, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo registrar el comentario" + error.toString(), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String idusuario = credenciales;
                String comentario = cojeComentario;

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idusuario", idusuario);
                parametros.put("comentario",comentario);
                parametros.put("fecha",fecha);
                return parametros;
            }
        };

        request.add(stringRequest);
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
