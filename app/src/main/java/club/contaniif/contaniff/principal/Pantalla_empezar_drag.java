package club.contaniif.contaniff.principal;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.AdapterDrag;
import club.contaniif.contaniff.adapter.PaginacionNumeroAdapter;
import club.contaniif.contaniff.adapter.PreguntasAdapter;
import club.contaniif.contaniff.adapter.PreguntasImagenesAdapter;
import club.contaniif.contaniff.adapter.PreguntasImagenesAdapterDrag;
import club.contaniif.contaniff.adapter.PreguntasSeleccionMultiple;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.entidades.GestionPreguntas;
import club.contaniif.contaniff.entidades.NumeroVo;
import club.contaniif.contaniff.entidades.PreguntasVo;
import club.contaniif.contaniff.entidades.VolleySingleton;
import club.contaniif.contaniff.interfaces.Puente;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pantalla_empezar_drag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pantalla_empezar_drag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pantalla_empezar_drag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Pantalla_empezar_drag() {
        // Required empty public constructor
    }

    View view;
    Activity activity;
    Puente puente;

    int numeroArray = 0;
    int numero;
    int correctas = 0;
    Button btnContinuar2, btnContinuar;
    AdapterDrag miAdapter;
    ArrayList<PreguntasVo> respuestaCompleta;
    ArrayList<String> respuestaCorrecta;

    PreguntasImagenesAdapterDrag miPreguntasImagenesAdapter;
    ArrayList<PreguntasVo> list;
    RecyclerView PreguntasTexto, Imagenes;
    PreguntasVo miVo;
    HashMap copia;
    JsonObjectRequest jsonObjectRequest;
    //////////////////////////////////////////////////////////////////////////
    String retroBuena;
    boolean isCheked = false;

    public boolean getIsCheked() {
        return isCheked;
    }

    public void setIsCheked(boolean isCheked) {
        this.isCheked = isCheked;
    }

    public int getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(int tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    int tipoPregunta;
    String urlImagen;

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    String retroMala;
    String resultado;
    int puntage;

    public int getPuntage() {
        return puntage;
    }

    public void setPuntage(int puntage) {
        this.puntage = puntage;
    }

    public String getRetroBuena() {
        return retroBuena;
    }

    public void setRetroBuena(String retroBuena) {
        this.retroBuena = retroBuena;
    }

    public String getRetroMala() {
        return retroMala;
    }

    public void setRetroMala(String retroMala) {
        this.retroMala = retroMala;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }


    int i = reiniciar;
    ProgressDialog progreso;
    private static long START_TIME_IN_MILLIS;
    private static final int reiniciar = 0;
    public static android.os.CountDownTimer CountDownTimer;
    private long mTimeLeftInMillis;
    ProgressBar mProgressBar;
    long mInitialTime;

    ArrayList<NumeroVo> listanumero;
    NumeroVo miNumeroVo;
    //int numero = 3;
    RecyclerView miRecyclerNumero;

    TextView pregunta;
    TextView puntajeRespuestaBuena;
    TextView puntajeRespuestaMala;
    String informacion;
    String informacion2;
    RequestQueue request;
    Dialog myDialogBuena;
    Dialog myDialogMala, MyDialogFinal;
    String nombre;
    ArrayList<String> listaColores;
    ArrayList<String> listaRespuesta;
    int numeroPregunta;
    int tiempoCapturado;
    String credenciales;
    StringRequest stringRequest;
    PreguntasVo preguntasVo;
    Dialog dialogoCargando;
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pantalla_empezar_drag.
     */
    // TODO: Rename and change types and number of parameters
    public static Pantalla_empezar_drag newInstance(String param1, String param2) {
        Pantalla_empezar_drag fragment = new Pantalla_empezar_drag();
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

        view = inflater.inflate(R.layout.fragment_pantalla_empezar_drag, container, false);
        request = Volley.newRequestQueue(getContext());
        respuestaCorrecta = new ArrayList<>();
        respuestaCompleta = new ArrayList<>();
        list = new ArrayList<>();
        dialogoCargando = new Dialog(this.getContext());
        myDialogBuena = new Dialog(getContext());
        myDialogMala = new Dialog(getContext());
        MyDialogFinal = new Dialog(getContext());
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);
        pregunta = view.findViewById(R.id.campoPregunta);
        Imagenes = view.findViewById(R.id.primero);
        PreguntasTexto = view.findViewById(R.id.segundo);
        btnContinuar2 = view.findViewById(R.id.btnContinuar2);
        btnContinuar = view.findViewById(R.id.btnContinuar);
        btnContinuar.setVisibility(View.INVISIBLE);
        miRecyclerNumero = view.findViewById(R.id.recyclerNumeros);
        miRecyclerNumero.setLayoutManager(new LinearLayoutManager(getContext()));
        miRecyclerNumero.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        PreguntasTexto.setLayoutManager(new LinearLayoutManager(getContext()));
        Imagenes.setLayoutManager(new LinearLayoutManager(getContext()));

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] bar = list.get(0).getRespuesta().split("&&");
                for (String foobar : bar) {
                    respuestaCorrecta.add(foobar);
                }
                for (int i = 0; i < respuestaCompleta.size(); i++) {
                    if (respuestaCorrecta.contains(respuestaCompleta.get(i).getRespuesta())) {
                        correctas++;
                    }
                }
                if (correctas == 4) {
                    setResultado("correcto");
                } else {
                    setResultado("incorrecto");
                }
                comparar();
            }
        });


        if (getIsCheked() == true) {
            btnContinuar.setVisibility(View.VISIBLE);
            btnContinuar2.setVisibility(View.INVISIBLE);
        }
        cargarCredenciales();
        cargarNombre();

        listaColores = new ArrayList<>();
        Bundle todo;
        Bundle objeto;
        Bundle miBundle = getArguments();
        if (miBundle != null) {
            todo = miBundle.getBundle("Todo");
            objeto = miBundle.getBundle("BundleObjeto");
            miVo = (PreguntasVo) objeto.getSerializable("Objeto");
            listaRespuesta = (ArrayList<String>) miBundle.getStringArrayList("respuestas").clone();
            cargarDatos();
            numeroPregunta = todo.getInt("numeroPregunta");
            listaColores = todo.getStringArrayList("color");
        } else {
            numeroPregunta = 0;
        }
        listanumero = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            miNumeroVo = new NumeroVo();
            miNumeroVo.setNumeroPagina(i);
            listanumero.add(miNumeroVo);
        }

        for (int i = 0; i < listaColores.size(); i++) {
            listanumero.get(i).setColor(listaColores.get(i));
        }
        PaginacionNumeroAdapter miNumeroAdapter = new PaginacionNumeroAdapter(listanumero, getContext());
        miRecyclerNumero.setAdapter(miNumeroAdapter);
        return view;
    }

    private void cargarDatos() {
        list = new ArrayList<>();

        JSONObject jsonObject = null;
        final ArrayList<String> lista = new ArrayList<>();

        for (int i = 0; i < listaRespuesta.size(); i++) {
            preguntasVo = new PreguntasVo();
            lista.add(listaRespuesta.get(i));
            preguntasVo.setRespuesta(miVo.getRespuesta());
            preguntasVo.setOpciones(listaRespuesta.get(i));
            preguntasVo.setId(miVo.getId());
            preguntasVo.setPregunta(miVo.getPregunta());
            preguntasVo.setPuntaje(miVo.getPuntaje());
            preguntasVo.setTiempoDemora(miVo.getTiempoDemora());
            preguntasVo.setRespuesta(miVo.getRespuesta());
            preguntasVo.setRetobuena(miVo.getRetobuena());
            preguntasVo.setRetromala(miVo.getRetromala());
            list.add(preguntasVo);
        }

        for (int i = 0; i < list.size(); i++) {
            String[] bar = lista.get(i).split("&%");
            for (String foobar : bar) {
                list.get(i).setRuta(String.format(foobar));
                list.get(i).setPalabra(bar[0]);
            }
        }


        setRetroMala(preguntasVo.getRetromala());
        setRetroBuena(preguntasVo.getRetobuena());
        setTipoPregunta(preguntasVo.getTipo());
        setPuntage(preguntasVo.getPuntaje());
        pregunta.setText(preguntasVo.getPregunta());
        informacion = preguntasVo.getRespuesta();
        informacion2 = preguntasVo.getOpciones();
        if (numeroPregunta != 0)

        {
            resetTimer();
        }

        mInitialTime = DateUtils.DAY_IN_MILLIS * 0 +
                DateUtils.HOUR_IN_MILLIS * 0 +
                DateUtils.MINUTE_IN_MILLIS * 0 +
                DateUtils.SECOND_IN_MILLIS * preguntasVo.getTiempoDemora();
        START_TIME_IN_MILLIS = preguntasVo.getTiempoDemora() * 1000;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        starTime();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        miAdapter = new AdapterDrag(list, getContext());
        miPreguntasImagenesAdapter = new PreguntasImagenesAdapterDrag(list, getContext());
        Imagenes.setAdapter(miPreguntasImagenesAdapter);
        PreguntasTexto.setAdapter(miAdapter);

        miPreguntasImagenesAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                numero = -1;
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, dragShadowBuilder, view, 0);
                numero = Imagenes.getChildAdapterPosition(view);
                return true;
            }
        });

        miAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getRuta() == list.get(PreguntasTexto.getChildAdapterPosition(view)).getImg()) {
                        list.get(i).setMostrar(false);
                        respuestaCompleta.remove(list.get(PreguntasTexto.getChildAdapterPosition(view)));
                        Imagenes.setAdapter(miPreguntasImagenesAdapter);
                    }
                }
                list.get(PreguntasTexto.getChildAdapterPosition(view)).setImg(null);
                PreguntasTexto.setAdapter(miAdapter);
            }
        });

        miAdapter.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int dragEvent = event.getAction();
                final View view = (View) event.getLocalState();

                switch (dragEvent) {
                /*case DragEvent.ACTION_DRAG_ENTERED:
                    Toast.makeText(getApplicationContext(), "ENTERED" + list.get(PreguntasTexto.getChildAdapterPosition(view)).getPalabra(), Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Toast.makeText(getApplicationContext(), "EXITED" + list.get(PreguntasTexto.getChildAdapterPosition(view)).getPalabra(), Toast.LENGTH_SHORT).show();
                    break;*/
                    case DragEvent.ACTION_DROP:
                        if (list.get(numero).isMostrar() == false && list.get(PreguntasTexto.getChildAdapterPosition(v)).getImg() == null ) {
                            list.get(PreguntasTexto.getChildAdapterPosition(v)).setImg(list.get(numero).getRuta());
                            btnContinuar.setVisibility(View.VISIBLE);
                            btnContinuar2.setVisibility(View.INVISIBLE);
                            /////////////////////////////////////////////////////////////////////////////////////////////////////
                            miVo = new PreguntasVo();
                            miVo.setId(numeroArray);
                            miVo.setPalabra(list.get(PreguntasTexto.getChildAdapterPosition(v)).getPalabra());
                            miVo.setRuta(list.get(numero).getRuta());
                            miVo.setRespuesta(list.get(PreguntasTexto.getChildAdapterPosition(v)).getPalabra() + "&%" + list.get(numero).getRuta());
                            respuestaCompleta.add(miVo);
                            numeroArray++;
                            ////////////////////////////////////////////////////////////////////////////////////////////////////
                            PreguntasTexto.setAdapter(miAdapter);
                            list.get(numero).setMostrar(true);
                            Imagenes.setAdapter(miPreguntasImagenesAdapter);
                        } else {
                            Toast.makeText(getContext(), "No se puede cambiar", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void cargarNombre() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "No existe el valor");
        if (nombre != "No existe el valor") {
            this.nombre = nombre;
        }
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
        try {
            CountDownTimer.cancel();
        } catch (Exception e) {

        }
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

    private void starTime() {
        CountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                i++;
                mProgressBar.setProgress(100);
            }
        }.start();
    }

    private void resetTimer() {
        CountDownTimer.cancel();
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        i = reiniciar;

    }

    private void updateCountDownText() {
        Log.v("Log_tag", "Tick of Progress" + i + mInitialTime);
        i++;
        mProgressBar.setProgress((int) (i * 100 / (mInitialTime / 1000)));
    }

    private void comparar() {
        if (getResultado().equalsIgnoreCase("correcto")) {
            showPopup(getRetroBuena());
        } else {
            showPopup2(getRetroMala());
        }
        tiempoCapturado = i;
        numeroPregunta++;
    }


    public void showPopup(String retorno) {
        final Button retroBuena;
        TextView txtRetroBuena;

        myDialogBuena.setContentView(R.layout.popup_rcorrecta);
        puntajeRespuestaBuena = myDialogBuena.findViewById(R.id.campoPuntajeCorrecto);
        puntajeRespuestaBuena.setText("+" + String.valueOf(getPuntage()));
        txtRetroBuena = myDialogBuena.findViewById(R.id.campoRetroBuena);
        txtRetroBuena.setText(nombre + " \n " + retorno);

        retroBuena = myDialogBuena.findViewById(R.id.btnRetroBuena);
        retroBuena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosPuntaje();
                retroBuena.setEnabled(true);
                dialogoCargando();
            }
        });

        myDialogBuena.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogBuena.show();
    }

    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        }catch (Exception e){
            Log.i("Error " , e.toString());
        }

    }


    private void revisar(boolean revisar) {
        if (revisar == true) {
            int puntos = puntage;
            if (tiempoCapturado > preguntasVo.getTiempoDemora()) {
                puntos = (puntage * 75) / 100;
            }
            listaColores.add("#45cc28");
        } else {
            listaColores.add("#ed2024");
        }
        if (numeroPregunta < 10) {
            puente.reinciar(numeroPregunta, 1, listaColores);
        } else {
            Button finalizar;
            TextView campoFinal;
            MyDialogFinal.setContentView(R.layout.popup_terminar_preguntas);
            campoFinal = MyDialogFinal.findViewById(R.id.textoFinal);
            finalizar = MyDialogFinal.findViewById(R.id.btnFinal);
            int buenas = 0, malas = 0;
            for (int i = 0; i < listaColores.size(); i++) {
                if (listaColores.get(i).equals("#45cc28")) {
                    buenas++;
                } else if (listaColores.get(i).equals("#ed2024")) {
                    malas++;
                }
            }
            if (buenas > malas) {
                campoFinal.setText("Felicitaciones");
            } else if (malas > buenas) {
                campoFinal.setText("¡¡Debes Repasar Mas!!");
            }

            finalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialogFinal.dismiss();
                    Datos.actualizarPuntos = true;
                    puente.finaliza();
                }
            });

            MyDialogFinal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            MyDialogFinal.show();
        }
        btnContinuar.setVisibility(View.INVISIBLE);
        btnContinuar2.setVisibility(View.VISIBLE);
    }


    private void enviarDatosPuntaje() {
        String url;
        url = getContext().getString(R.string.ipRegistroPuntaje);
        stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registra")) {
                   // Toast.makeText(activity, ""+response, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(activity, ""+response, Toast.LENGTH_SHORT).show();
                }
                myDialogBuena.dismiss();
                revisar(true);
                dialogoCargando.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se pudo registrar el puntaje" + error.toString(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String idusuario = credenciales;
                int idpregunta = preguntasVo.getId();
                int tiempo = tiempoCapturado;
                int puntaje = getPuntage();
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idusuario", idusuario);
                parametros.put("idpregunta", Integer.toString(idpregunta));
                parametros.put("tiempo", Integer.toString(tiempo));
                parametros.put("puntos", Integer.toString(puntaje));
                Log.i("*******Parametros ", parametros.toString());
                return parametros;
            }
        };

        request.add(stringRequest);
    }


    public void showPopup2(String retorno) {
        Button retroMala;
        TextView txtRetroMala;
        myDialogMala.setContentView(R.layout.popup_rincorrecta);
        puntajeRespuestaMala = myDialogMala.findViewById(R.id.campoPuntajeIncorrecto);
        //puntajeRespuestaMala.setText("+"+String.valueOf(getPuntage()));
        txtRetroMala = myDialogMala.findViewById(R.id.campoRetroMala);
        txtRetroMala.setText(retorno);
        retroMala = myDialogMala.findViewById(R.id.btnRetroMala);


        retroMala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogMala.dismiss();
                revisar(false);
            }
        });

        myDialogMala.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogMala.show();


    }

    private void cargarCredenciales() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
        this.credenciales = credenciales;
        //Toast.makeText(getContext(),"Credenciales = " + this.credenciales, Toast.LENGTH_SHORT).show();
    }

}
