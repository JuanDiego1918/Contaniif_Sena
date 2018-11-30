package club.contaniif.contaniff.miRendimiento;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.interfaces.Puente;

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

    private OnFragmentInteractionListener mListener;
    private Dialog dialogoCargando;
    private String credenciales;
    private String cojeComentario;
    private int cambioCanjes;
    private String puntosDisponibles;
    private String correo;
    private RequestQueue request;
    private StringRequest stringRequest;

    public RendimiendoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
    }

    private TextView txtPuntos;
    private TextView txtCambiados;
    private TextView txtDisponibles;
    private TextView txtPuntosCanjes;
    private TextView txtAccionDia;
    private TextView txtActivo;
    private TextView txtDescontados;
    private Dialog dialogCanjes;
    private Dialog ventanaComentarios;
    private Puente puente;
    private String monedasCanjeadas;
    private String nombre;


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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogoCargando = new Dialog(Objects.requireNonNull(this.getContext()));
        cargarCredenciales();
        request = Volley.newRequestQueue(getContext());
        Datos.actualizarPuntos = true;
        View view = inflater.inflate(R.layout.fragment_rendimiendo, container, false);
        LinearLayout comentarios = view.findViewById(R.id.btnComentarios);
        ventanaComentarios = new Dialog(getContext());
        txtPuntos = view.findViewById(R.id.puntosObtenidos);
        txtCambiados = view.findViewById(R.id.puntosCambiados);
        txtDisponibles = view.findViewById(R.id.puntosDisponibles);
        txtPuntosCanjes = view.findViewById(R.id.puntosObtenidosCanjes);
        txtAccionDia = view.findViewById(R.id.accion);
        txtActivo = view.findViewById(R.id.activos);
        txtDescontados = view.findViewById(R.id.puntosDescontados);
        LinearLayout canjes = view.findViewById(R.id.btnCanjes);
        LinearLayout activos = view.findViewById(R.id.btnActivos);
        dialogCanjes = new Dialog(getContext());

        cargarNombre();
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

        activos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cambiarVarible(monedasCanjeadas) != 0) {
                    puente.activos(monedasCanjeadas);
                } else {
                    Toast.makeText(getContext(), " En este momento no tiene monedas\n" +
                            "disponibles para realizar compra de activos.", Toast.LENGTH_LONG).show();
                }
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
                float numeroValor = cambiarVarible(puntosDisponibles);
                if (!campoCanjes.getText().toString().equals("")) {
                    try {
                        cambioCanjes = Integer.parseInt((campoCanjes.getText().toString()));

                        if (cambioCanjes < 1) {
                            Toast.makeText(getContext(), nombre + " Ingrese un valor mayor a 0", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cambioCanjes <= numeroValor) {
                                realizarCanje(cambioCanjes);
                                dialogCanjes.dismiss();
                            } else {
                                Toast.makeText(getContext(), nombre + ", Puntos insuficientes para realizar en canje", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        campoCanjes.setText(null);
                        Toast.makeText(getContext(), nombre + " Verifique el numero por favor", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), nombre + " Por favor ingrese un valor", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Objects.requireNonNull(dialogCanjes.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCanjes.show();
    }

    private void cargarNombre() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "No existe el valor");
        if (!Objects.equals(nombre, "No existe el valor")) {
            this.nombre = nombre;
        }
    }

    private void realizarCanje(final float numero) {
        String url;
        url = "https://" + Objects.requireNonNull(getContext()).getString(R.string.ip) + "guardamonedas.php?idusuario=" + correo + "&&puntos=" + String.valueOf(numero);
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("registra")) {
                    Toast.makeText(getContext(), "Realiza Cambios" + response, Toast.LENGTH_LONG).show();
                } else {
                    puente.reinciarRendimiento();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Verifique Conexion A Internet" + error.toString(), Toast.LENGTH_LONG).show();
            }

        });

        request.add(stringRequest);
    }


    private int cambiarVarible(String puntosDisponibles) {
        String cambio = puntosDisponibles.replaceAll(",", "");

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
                if (campoComentario.getText().toString().equals(null)) {
                    enviarDatosComentarios(cojeComentario);
                    obtenerFecha();
                    ventanaComentarios.dismiss();
                } else {
                    Toast.makeText(getContext(), "Escriba su comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Objects.requireNonNull(ventanaComentarios.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ventanaComentarios.show();
    }

    private void obtenerFecha() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date();
        String fecha = dateFormat.format(date);
    }


    private void cargarCredenciales() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        this.credenciales = credenciales;
        correo = credenciales;
    }

    private void enviarDatosComentarios(final String cojeComentario) {

        String url;
        url = Objects.requireNonNull(getContext()).getString(R.string.ipComentario);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")) {
                    Toast.makeText(getContext(), "Comentario enviado " + response, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Comentario no registrado " + response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo registrar el comentario" + error.toString(), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                String idusuario = credenciales;
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idusuario", idusuario);
                parametros.put("comentario", cojeComentario);
                return parametros;
            }
        };

        request.add(stringRequest);
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

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            puente = (Puente) activity;
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
        dialogoCargando();
        String url;
        url = "https://" + Objects.requireNonNull(getContext()).getString(R.string.ip) + "puntajes.php?correo=" + correo;
        com.android.volley.toolbox.JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
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

    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("puntos");
        try {
            JSONObject jsonObject = null;
            txtPuntos.setText("Puntos Obtenidos: " + json.getJSONObject(0).optString("obtenidos"));
            txtCambiados.setText("Puntos Cambiados: " + json.getJSONObject(1).optString("canjeados"));
            txtActivo.setText("Activos Adquiridos: " + json.getJSONObject(2).optString("activos"));
            txtDescontados.setText("Puntos Descontados: " + json.getJSONObject(3).optString("descontados"));
            txtDisponibles.setText("Puntos Disponibles: " + json.getJSONObject(4).optString("quedan"));
            puntosDisponibles = json.getJSONObject(4).optString("quedan");
            txtAccionDia.setText("Acción del día: " + json.getJSONObject(5).optString("accion"));
            monedasCanjeadas = response.optJSONArray("Criptoniif").getJSONObject(0).optString("monedas");
            txtPuntosCanjes.setText("Monedas Disponibles: " + monedasCanjeadas);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
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
        void onFragmentInteraction();
    }
}
