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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.CategoriasAdapter;
import club.contaniif.contaniff.entidades.CategoriasVo;
import club.contaniif.contaniff.entidades.Datos;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RendimiendoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RendimiendoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RendimiendoFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
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
    int cambioCanjes;
    String valorCriptoniif,puntosDisponibles;
    String fecha;
    String correo;
    RequestQueue request;
    StringRequest stringRequest;

    public RendimiendoFragment() {
        // Required empty public constructor
    }

    View view;
    Activity activity;
    LinearLayout puntos, canjes, activos, comentarios;
    TextView txtPuntos, txtCambiados, txtDisponibles, txtPuntosCanjes, txtAccionDia, txtActivo, txtDescontados;
    Dialog dialogCanjes;
    Dialog ventanaComentarios;
    JsonObjectRequest JsonObjectRequest;

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
        Datos.actualizarPuntos = true;
        view = inflater.inflate(R.layout.fragment_rendimiendo, container, false);
        comentarios = view.findViewById(R.id.btnComentarios);
        ventanaComentarios = new Dialog(getContext());
        txtPuntos = view.findViewById(R.id.puntosObtenidos);
        txtCambiados = view.findViewById(R.id.puntosCambiados);
        txtDisponibles = view.findViewById(R.id.puntosDisponibles);
        txtPuntosCanjes = view.findViewById(R.id.puntosObtenidosCanjes);
        txtAccionDia = view.findViewById(R.id.accion);
        txtActivo = view.findViewById(R.id.activos);
        txtDescontados = view.findViewById(R.id.puntosDescontados);
        canjes=view.findViewById(R.id.btnCanjes);
        dialogCanjes=new Dialog(getContext());

        cargarDatos();

        comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaComen();
            }
        });

        canjes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaCanjes();
            }
        });

        return view;
    }

    private void ventanaCanjes() {
        Button cancelar, enviar;
        final EditText campoCanjes;

        dialogCanjes.setContentView(R.layout.popup_canjes);
        cancelar = dialogCanjes.findViewById(R.id.btnCancelarComentario);
        enviar = dialogCanjes.findViewById(R.id.enviar);
        campoCanjes = dialogCanjes.findViewById(R.id.campoCanjes);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCanjes.dismiss();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float numeroValor=cambiarVarible(puntosDisponibles);
                cambioCanjes = Integer.parseInt((campoCanjes.getText().toString()));
                if (cambioCanjes<=numeroValor){
                    realizarCanje(cambioCanjes);
                    dialogCanjes.dismiss();
                }else {
                    Toast.makeText(getContext(),"Te Pasas Wey",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogCanjes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCanjes.show();
    }

    private void realizarCanje(final float numero) {
        String url;
        url = "https://" + getContext().getString(R.string.ip)+"guardamonedas.php?idusuario="+correo+"&&nose="+String.valueOf(numero);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("registra")) {
                    Toast.makeText(getContext(), "Realiza Cambios" + response, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "no registrado " + response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo registrar el comentario" + error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        request.add(stringRequest);
    }

    private void realizarCanje1(final float numero) {
        String url;
        url = "https://" + getContext().getString(R.string.ip)+"guardamonedas.php?";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")) {
                    Toast.makeText(getContext(), "Realiza Cambios" + response, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "no registrado " + response, Toast.LENGTH_LONG).show();
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

                String idusuario = correo;
                String cambio = cojeComentario;

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idusuario", idusuario);
                parametros.put("nose", String.valueOf(numero));
                return parametros;
            }
        };

        request.add(stringRequest);
    }

    private int cambiarVarible(String puntosDisponibles) {
        String cambio=puntosDisponibles.replaceAll(",","");

        return Integer.parseInt(cambio);
    }


    private void ventanaComen() {
        Button cancelar, enviar;
        final EditText campoComentario;

        ventanaComentarios.setContentView(R.layout.popup_comentarios);
        cancelar = ventanaComentarios.findViewById(R.id.btnCancelarComentario);
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
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        this.credenciales = credenciales;
        correo = credenciales;
    }

    private void enviarDatosComentarios() {

        String url;
        url = "https://" + getContext().getString(R.string.ipComentario);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progreso.hide();
                if (response.trim().equalsIgnoreCase("registra")) {
                    Toast.makeText(getContext(), "Comentario enviado " + response, Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(),"Crede " + credenciales, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Comentario no registrado " + response, Toast.LENGTH_LONG).show();
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
                parametros.put("comentario", comentario);
                parametros.put("fecha", fecha);
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

    private void cargarDatos() {
        String url;
        url = "https://" + getContext().getString(R.string.ip) + "puntajes.php?correo=" + correo;
        JsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(JsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("puntos");
        try {
            JSONObject jsonObject = null;

            txtPuntos.setText("Puntos Obtenidos: " + json.getJSONObject(0).optString("obtenidos"));
            txtCambiados.setText("Puntos Cambiados: " + json.getJSONObject(1).optString("canjeados"));
            txtDescontados.setText("Puntos Descontados: " + json.getJSONObject(2).optString("descontados"));
            txtDisponibles.setText("Puntos Disponibles: " + json.getJSONObject(3).optString("quedan"));
            puntosDisponibles=json.getJSONObject(3).optString("quedan");
            txtAccionDia.setText("Accion del día: " + json.getJSONObject(4).optString("accion"));
            txtPuntosCanjes.setText("Monedas Canjeadas: " + response.optJSONArray("Criptoniif").getJSONObject(0).optString("monedas"));
        } catch (JSONException e) {
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
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
