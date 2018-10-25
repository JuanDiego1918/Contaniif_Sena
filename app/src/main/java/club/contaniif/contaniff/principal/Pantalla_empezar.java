package club.contaniif.contaniff.principal;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.PaginacionNumeroAdapter;
import club.contaniif.contaniff.adapter.PreguntasAdapter;
import club.contaniif.contaniff.adapter.PreguntasImagenesAdapter;
import club.contaniif.contaniff.adapter.PreguntasSeleccionMultiple;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.entidades.NumeroVo;
import club.contaniif.contaniff.entidades.PreguntasVo;
import club.contaniif.contaniff.entidades.RecyclerViewOnClickListener;
import club.contaniif.contaniff.entidades.VolleySingleton;
import club.contaniif.contaniff.interfaces.Puente;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pantalla_empezar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pantalla_empezar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pantalla_empezar extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int puntos;

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    private String credenciales;

    public String getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(String credenciales) {
        this.credenciales = credenciales;
    }

    private int tiempoCapturado;
    ArrayList<String> listaPre;
    private String retroBuena;

    int contador = 0;

    private int numeroPregunta;

    private boolean isCheked = false;

    private boolean getIsCheked() {
        return isCheked;
    }

    public void setIsCheked(boolean isCheked) {
        this.isCheked = isCheked;
    }

    private int getTipoPregunta() {
        return tipoPregunta;
    }

    private void setTipoPregunta(int tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    private int tipoPregunta;
    private String urlImagen;

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    private String retroMala;
    private String resultado;
    private int puntage;

    private int getPuntage() {
        return puntage;
    }

    private void setPuntage(int puntage) {
        this.puntage = puntage;
    }

    private String getRetroBuena() {
        return retroBuena;
    }

    private void setRetroBuena(String retroBuena) {
        this.retroBuena = retroBuena;
    }

    private String getRetroMala() {
        return retroMala;
    }

    private void setRetroMala(String retroMala) {
        this.retroMala = retroMala;
    }

    private String getResultado() {
        return resultado;
    }

    private void setResultado(String resultado) {
        this.resultado = resultado;
    }

    private OnFragmentInteractionListener mListener;

    private int i = reiniciar;
    ProgressDialog progreso;
    private static long START_TIME_IN_MILLIS;
    private static final int reiniciar = 0;
    private static android.os.CountDownTimer CountDownTimer;
    private long mTimeLeftInMillis;
    private ProgressBar mProgressBar;
    private long mInitialTime;

    private MediaPlayer respondeBien;
    private MediaPlayer terminaBien;
    private MediaPlayer terminaMal;
    private Dialog dialogoCargando;
    private ArrayList<String> listaImagenes;
    private PreguntasAdapter adapter;
    private PreguntasImagenesAdapter adapter2;
    private PreguntasSeleccionMultiple adapter3;
    private String nombre;
    private Puente puente;
    private Button btnContinuar;
    private Button btnContinuar2;
    private TextView pregunta;
    private String informacion;

    private RecyclerView recyclerViewUsuarios;
    private ArrayList<PreguntasVo> listaPreguntas;
    private ArrayList<String> listaRespuesta;
    private RequestQueue request;
    private Dialog myDialogBuena;
    private Dialog myDialogMala;
    private Dialog MyDialogFinal;
    private ArrayList<String> listaSeleccionada;
    private int correctoSeleccionMultiple = 0;
    private PreguntasVo preguntas;
    private ArrayList<String> listaColores;
    boolean clicRespuesta = false;

    public Pantalla_empezar() {
        // Required empty public constructor
    }


    private Map<String, String> parametros;
    private int idpregunta;
    private int tiempo;
    private int puntaje;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter instruuno.
     * @param param2 Parameter instrudos.
     * @return A new instance of fragment Pantalla_empezar.
     */
    // TODO: Rename and change types and number of parameters
    public static Pantalla_empezar newInstance(String param1, String param2) {
        Pantalla_empezar fragment = new Pantalla_empezar();
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
        View vista = inflater.inflate(R.layout.fragment_pantalla_empezar, container, false);
        cargarCredenciales();
        request = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        ScrollView miScroll = vista.findViewById(R.id.scroll);

        myDialogBuena = new Dialog(getContext());
        myDialogMala = new Dialog(getContext());
        MyDialogFinal = new Dialog(getContext());
        dialogoCargando = new Dialog(this.getContext());

        respondeBien = MediaPlayer.create(getContext(),R.raw.responde_bien);
        terminaBien = MediaPlayer.create(getContext(),R.raw.finalizar_bien);
        terminaMal = MediaPlayer.create(getContext(),R.raw.finalizar_mal);

        btnContinuar2 = vista.findViewById(R.id.btnContinuar2);
        btnContinuar = vista.findViewById(R.id.btnContinuar);
        btnContinuar.setVisibility(View.INVISIBLE);


        RecyclerView miRecyclerNumero = vista.findViewById(R.id.recyclerNumeros);
        miRecyclerNumero.setLayoutManager(new LinearLayoutManager(getContext()));
        miRecyclerNumero.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        mProgressBar = vista.findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comparar();
            }
        });

        pregunta = vista.findViewById(R.id.campoPregunta);
        recyclerViewUsuarios = vista.findViewById(R.id.recyclerPreguntasss);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewUsuarios.setHasFixedSize(true);
        //request = Volley.newRequestQueue(getContext());

        if (getIsCheked()) {
            btnContinuar.setVisibility(View.VISIBLE);
            btnContinuar2.setVisibility(View.INVISIBLE);
        }
        cargarNombre();

        listaColores = new ArrayList<>();
        Bundle todo;
        Bundle objeto;
        Bundle miBundle = getArguments();
        if (miBundle != null) {
            todo = miBundle.getBundle("Todo");
            objeto = miBundle.getBundle("BundleObjeto");
            preguntas = (PreguntasVo) Objects.requireNonNull(objeto).getSerializable("Objeto");
            listaRespuesta = (ArrayList<String>) Objects.requireNonNull(miBundle.getStringArrayList("respuestas")).clone();
            numeroPregunta = Objects.requireNonNull(todo).getInt("numeroPregunta");
            listaColores = todo.getStringArrayList("color");
        } else {
            numeroPregunta = 0;
        }
        ArrayList<NumeroVo> listanumero = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            NumeroVo miNumeroVo = new NumeroVo();
            miNumeroVo.setNumeroPagina(i);
            listanumero.add(miNumeroVo);
        }

        for (int i = 0; i < listaColores.size(); i++) {
            listanumero.get(i).setColor(listaColores.get(i));
        }
        PaginacionNumeroAdapter miNumeroAdapter = new PaginacionNumeroAdapter(listanumero, getContext());
        miRecyclerNumero.setAdapter(miNumeroAdapter);
        cargarDatos();
        return vista;
    }

    private void cargarDatos() {

        PreguntasVo lisPreguntasVo;
        listaPreguntas = new ArrayList<>();
        listaImagenes = new ArrayList<>();

        for (int i = 0; i < listaRespuesta.size(); i++) {
            lisPreguntasVo = new PreguntasVo();
            lisPreguntasVo.setOpciones(listaRespuesta.get(i));
            lisPreguntasVo.setRutaImagen(listaRespuesta.get(i));
            listaPreguntas.add(lisPreguntasVo);
        }

        setRetroMala(preguntas.getRetromala());

        setRetroBuena(preguntas.getRetobuena());

        setTipoPregunta(preguntas.getTipo());

        setPuntage(preguntas.getPuntaje());
        pregunta.setText(preguntas.getPregunta());
        informacion = preguntas.getRespuesta();

        if (numeroPregunta != 0)

        {
            resetTimer();
        }

        mInitialTime = 0L +
                0L +
                DateUtils.SECOND_IN_MILLIS * preguntas.getTiempoDemora();
        START_TIME_IN_MILLIS = preguntas.getTiempoDemora() * 1000;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;

        starTime();
        if (preguntas.getTipo() == 3) {
            adapter2 = new PreguntasImagenesAdapter(listaPreguntas, getContext());
            recyclerViewUsuarios.setAdapter(adapter2);
            recyclerViewUsuarios.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    btnContinuar.setVisibility(View.VISIBLE);
                    btnContinuar2.setVisibility(View.INVISIBLE);

                    String enviaPregunta = listaPreguntas.get(recyclerViewUsuarios.getChildAdapterPosition(view)).getOpciones();
                    String enviaRespuesta = informacion;

                    if (enviaPregunta.equalsIgnoreCase(informacion)) {
                        setResultado("correcto");
                    } else {
                        setResultado("incorrecto");
                    }

                    adapter2.setSelectedPosition(position);
                }
            }));

        } else if (preguntas.getTipo() == 1) {
            adapter = new PreguntasAdapter(listaPreguntas);
            recyclerViewUsuarios.setAdapter(adapter);
            recyclerViewUsuarios.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    btnContinuar.setVisibility(View.VISIBLE);
                    btnContinuar2.setVisibility(View.INVISIBLE);

                    String enviaPregunta = listaPreguntas.get(recyclerViewUsuarios.getChildAdapterPosition(view)).getOpciones();
                    String enviaRespuesta = informacion;

                    if (enviaPregunta.equalsIgnoreCase(informacion)) {
                        setResultado("correcto");
                    } else {
                        setResultado("incorrecto");
                    }

                    adapter.setSelectedPosition(position);
                }
            }));
        } else if (preguntas.getTipo() == 2) {
            final ArrayList<String> listaRespuesta = new ArrayList<>();
            String[] bar = preguntas.getRespuesta().split("#&");
            for (String foobar : bar) {
                listaRespuesta.add(String.format(foobar));
            }


            btnContinuar.setVisibility(View.VISIBLE);
            btnContinuar2.setVisibility(View.INVISIBLE);
            adapter3 = new PreguntasSeleccionMultiple(listaPreguntas);
            recyclerViewUsuarios.setAdapter(adapter3);
            listaSeleccionada = new ArrayList<>();
            btnContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuilder = new StringBuilder();

                    for (PreguntasVo preguntasVo : listaPreguntas) {
                        if (preguntasVo.isCheck()) {
                            if (stringBuilder.length() > 0)
                                stringBuilder.append(", ");
                            listaSeleccionada.add(preguntasVo.getOpciones());
                        }
                    }
                    if (listaSeleccionada.size() == listaRespuesta.size()) {
                        for (int i = 0; i < listaSeleccionada.size(); i++) {
                            if (listaRespuesta.contains(listaSeleccionada.get(i))) {
                                correctoSeleccionMultiple++;
                            }
                        }
                        if (correctoSeleccionMultiple == listaRespuesta.size()) {
                            setResultado("correcto");
                        } else {
                            setResultado("incorrecto");
                        }
                    } else {
                        setResultado("incorrecto");
                    }
                    comparar();
                }
            });
        }
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
        if (CountDownTimer != null) {
            CountDownTimer.cancel();
        }

        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        i = reiniciar;

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

    private void updateCountDownText() {
        Log.v("Log_tag", "Tick of Progress" + i + mInitialTime);
        i++;
        mProgressBar.setProgress((int) (i * 100 / (mInitialTime / 1000)));
    }

    private void cargarWebservices() {
        dialogoCargando();
        String ip = Objects.requireNonNull(getContext()).getString(R.string.ip);
        //String url = "http://" + ip + "wsPreguntasTipo1.php";
        //String url = "http://" + ip + "/multiples.php";
        String url = "https://" + ip + "/wsConsultaPreguntaPrueba1.php?estudiante=" + credenciales;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
//        request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }


    private void showPopup(String retorno) {
        respondeBien.start();
        final Button retroBuena;
        TextView txtRetroBuena;

        myDialogBuena.setContentView(R.layout.popup_rcorrecta);
        TextView puntajeRespuestaBuena = myDialogBuena.findViewById(R.id.campoPuntajeCorrecto);
        puntajeRespuestaBuena.setText("+" + String.valueOf(getPuntage()));
        txtRetroBuena = myDialogBuena.findViewById(R.id.campoRetroBuena);
        txtRetroBuena.setText(nombre + " \n " + retorno);

        retroBuena = myDialogBuena.findViewById(R.id.btnRetroBuena);
        retroBuena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosPuntaje();
                retroBuena.setEnabled(false);
                dialogoCargando();
            }
        });

        Objects.requireNonNull(myDialogBuena.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogBuena.show();

    }

    private void revisar(boolean revisar) {
        if (revisar) {
            int puntos = puntage;
            if (tiempoCapturado > preguntas.getTiempoDemora()) {
            }
            listaColores.add("#45cc28");
        } else {
            listaColores.add("#ed2024");
        }
        if (numeroPregunta < 10) {
            puente.reinciar(numeroPregunta, listaColores);
        } else {
            Button finalizar;
            TextView campoFinal;
            MyDialogFinal.setContentView(R.layout.popup_terminar_preguntas);
            //puntajeRespuestaMala.setText("+"+String.valueOf(getPuntage()));
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
                terminaBien.start();
            } else if (malas > buenas) {
                campoFinal.setText("¡¡Debes Repasar Mas!!");
                terminaMal.start();
            }

            finalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialogFinal.dismiss();
                    Datos.actualizarPuntos = true;
                    puente.finaliza();
                }
            });

            Objects.requireNonNull(MyDialogFinal.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            MyDialogFinal.show();
        }
        btnContinuar.setVisibility(View.INVISIBLE);
        btnContinuar2.setVisibility(View.VISIBLE);
    }


    ///////////////////////////////VICTOR/////////////////////////////////


    private void cargarCredenciales() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        this.credenciales = preferences.getString("correo", "No existe el valor");
    }

    private void cargarNombre() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        String nombre = preferences.getString("nombre", "No existe el valor");
        if (!nombre.equals("No existe el valor")) {
            this.nombre = nombre;
        }
    }


    private void enviarDatosPuntaje() {
        String url;
        url = Objects.requireNonNull(getContext()).getString(R.string.ipRegistroPuntaje);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parametros.clear();
                idpregunta = 0;
                tiempo = 0;
                puntaje = 0;
                //progreso.hide();
                response.trim();
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
            protected Map<String, String> getParams() {

                String idusuario = credenciales;
                idpregunta = preguntas.getId();
                tiempo = tiempoCapturado;
                puntaje = getPuntage();

                //Hola Mono
                parametros = new HashMap<>();
                parametros.put("idusuario", idusuario);
                parametros.put("idpregunta", Integer.toString(idpregunta));
                parametros.put("tiempo", Integer.toString(tiempo));
                parametros.put("puntos", Integer.toString(puntaje));
                System.out.println(parametros.toString());
                System.out.println("*******Parametros " + parametros.toString());
                Log.i("*******Parametros ", parametros.toString());
                return parametros;
            }
        };

        request.add(stringRequest);
    }


    private void showPopup2(String retorno) {
        Vibrator vibrator = (Vibrator) Objects.requireNonNull(getContext()).getSystemService(Context.VIBRATOR_SERVICE);
        Objects.requireNonNull(vibrator).vibrate(500);

        Button retroMala;
        TextView txtRetroMala;
        myDialogMala.setContentView(R.layout.popup_rincorrecta);
        TextView puntajeRespuestaMala = myDialogMala.findViewById(R.id.campoPuntajeIncorrecto);
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

        Objects.requireNonNull(myDialogMala.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogMala.show();


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
        try {
            CountDownTimer.cancel();
        } catch (Exception ignored) {

        }
        mListener = null;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {

        preguntas = null;
        JSONArray json = response.optJSONArray("pregunta");
        JSONObject jsonObject = null;
        listaPreguntas = new ArrayList<>();
        listaImagenes = new ArrayList<>();

        try {

            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                preguntas = new PreguntasVo();
                preguntas.setId(jsonObject.getInt("id"));
                preguntas.setPregunta(jsonObject.getString("pregunta"));
                preguntas.setCategoria(jsonObject.getInt("categoria"));
                preguntas.setPuntaje(jsonObject.getInt("puntaje"));
                preguntas.setTiempoDemora(jsonObject.getInt("tiempo"));
                preguntas.setTipo(jsonObject.getInt("tipopregunta"));
                preguntas.setOpciones(jsonObject.getString("opcion"));
                preguntas.setRespuesta(jsonObject.getString("respuesta"));
                preguntas.setRetobuena(jsonObject.getString("retrobuena"));
                preguntas.setRetromala(jsonObject.getString("retromala"));
                preguntas.setRutaImagen(jsonObject.getString("opcion"));

                listaImagenes.add(preguntas.getOpciones());
                listaPreguntas.add(preguntas);
            }
            setRetroMala(preguntas.getRetromala());
            setRetroBuena(preguntas.getRetobuena());
            setTipoPregunta(preguntas.getTipo());
            setPuntage(preguntas.getPuntaje());
            pregunta.setText(preguntas.getPregunta());
            informacion = preguntas.getRespuesta();
            String informacion2 = preguntas.getOpciones();

            if (numeroPregunta != 0) {
                resetTimer();
            }

            mInitialTime = 0L +
                    0L +
                    DateUtils.SECOND_IN_MILLIS * preguntas.getTiempoDemora();
            START_TIME_IN_MILLIS = preguntas.getTiempoDemora() * 1000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
            starTime();

        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" + " " + response, Toast.LENGTH_LONG).show();
        }

        if (getTipoPregunta() == 3) {
            adapter2 = new PreguntasImagenesAdapter(listaPreguntas, getContext());
            recyclerViewUsuarios.setAdapter(adapter2);
            recyclerViewUsuarios.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    btnContinuar.setVisibility(View.VISIBLE);
                    btnContinuar2.setVisibility(View.INVISIBLE);

                    String enviaPregunta = listaPreguntas.get(recyclerViewUsuarios.getChildAdapterPosition(view)).getOpciones();
                    String enviaRespuesta = informacion;

                    if (enviaPregunta.equalsIgnoreCase(informacion)) {
                        setResultado("correcto");
                    } else {
                        setResultado("incorrecto");
                    }

                    adapter2.setSelectedPosition(position);
                }
            }));

        } else if (getTipoPregunta() == 1) {
            adapter = new PreguntasAdapter(listaPreguntas);
            recyclerViewUsuarios.setAdapter(adapter);
            recyclerViewUsuarios.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    btnContinuar.setVisibility(View.VISIBLE);
                    btnContinuar2.setVisibility(View.INVISIBLE);

                    String enviaPregunta = listaPreguntas.get(recyclerViewUsuarios.getChildAdapterPosition(view)).getOpciones();
                    String enviaRespuesta = informacion;

                    if (enviaPregunta.equalsIgnoreCase(informacion)) {
                        setResultado("correcto");
                    } else {
                        setResultado("incorrecto");
                    }

                    adapter.setSelectedPosition(position);
                }
            }));
        } else if (getTipoPregunta() == 2) {
            final ArrayList<String> listaRespuesta = new ArrayList<>();
            String[] bar = preguntas.getRespuesta().split("#&");
            for (String foobar : bar) {
                listaRespuesta.add(String.format(foobar));
            }


            btnContinuar.setVisibility(View.VISIBLE);
            btnContinuar2.setVisibility(View.INVISIBLE);
            adapter3 = new PreguntasSeleccionMultiple(listaPreguntas);
            recyclerViewUsuarios.setAdapter(adapter3);
            listaSeleccionada = new ArrayList<>();
            btnContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuilder = new StringBuilder();

                    for (PreguntasVo preguntasVo : listaPreguntas) {
                        if (preguntasVo.isCheck()) {
                            if (stringBuilder.length() > 0)
                                stringBuilder.append(", ");
                            listaSeleccionada.add(preguntasVo.getOpciones());
                        }
                    }
                    if (listaSeleccionada.size() == listaRespuesta.size()) {
                        for (int i = 0; i < listaSeleccionada.size(); i++) {
                            if (listaRespuesta.contains(listaSeleccionada.get(i))) {
                                correctoSeleccionMultiple++;
                            }
                        }
                        if (correctoSeleccionMultiple == listaRespuesta.size()) {
                            setResultado("correcto");
                        } else {
                            setResultado("incorrecto");
                        }
                    } else {
                        setResultado("incorrecto");
                    }
                    comparar();
                }
            });
        } else if (getTipoPregunta() == 4) {
            //cargarWebservices();
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


    private void comparar() {
        if (getResultado().equalsIgnoreCase("correcto")) {
            showPopup(getRetroBuena());
        } else {
            showPopup2(getRetroMala());
        }
        tiempoCapturado = i;
        numeroPregunta++;
    }


}