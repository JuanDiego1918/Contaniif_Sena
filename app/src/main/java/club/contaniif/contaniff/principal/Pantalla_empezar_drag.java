package club.contaniif.contaniff.principal;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.AdapterDrag;
import club.contaniif.contaniff.adapter.PaginacionNumeroAdapter;
import club.contaniif.contaniff.adapter.PreguntasImagenesAdapterDrag;
import club.contaniif.contaniff.entidades.Datos;
import club.contaniif.contaniff.entidades.NumeroVo;
import club.contaniif.contaniff.entidades.PreguntasVo;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public Pantalla_empezar_drag() {
        // Required empty public constructor
    }

    private Puente puente;
    private int numeroArray = 0;
    private int numero;
    private int correctas = 0;
    private Button btnContinuar2;
    private Button btnContinuar;
    private AdapterDrag miAdapter;
    private ArrayList<PreguntasVo> respuestaCompleta;
    private ArrayList<String> respuestaCorrecta;

    private PreguntasImagenesAdapterDrag miPreguntasImagenesAdapter;
    private ArrayList<PreguntasVo> list;
    private RecyclerView PreguntasTexto;
    private RecyclerView Imagenes;
    private PreguntasVo miVo;
    //////////////////////////////////////////////////////////////////////////
    private String retroBuena;
    private boolean isCheked = false;

    private boolean getIsCheked() {
        return isCheked;
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


    private int i = reiniciar;
    private static long START_TIME_IN_MILLIS;
    private static final int reiniciar = 0;
    private static android.os.CountDownTimer CountDownTimer;
    private long mTimeLeftInMillis;
    private ProgressBar mProgressBar;
    private long mInitialTime;

    private MediaPlayer respondeBien;
    private MediaPlayer terminaBien;
    private MediaPlayer terminaMal;
    private TextView pregunta;
    private RequestQueue request;
    private Dialog myDialogBuena;
    private Dialog myDialogMala;
    private Dialog MyDialogFinal;
    private String nombre;
    private ArrayList<String> listaColores;
    private ArrayList<String> listaRespuesta;
    private int numeroPregunta;
    private int tiempoCapturado;
    private String credenciales;
    private PreguntasVo preguntasVo;
    private Dialog dialogoCargando;
    private int clickChek = 0;
    private int puntajeFinal;
    private String urlMensaje;
    private TextView campoFinal;
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pantalla_empezar_drag, container, false);
        request = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        respuestaCorrecta = new ArrayList<>();
        respuestaCompleta = new ArrayList<>();
        list = new ArrayList<>();
        dialogoCargando = new Dialog(this.getContext());
        myDialogBuena = new Dialog(getContext());
        myDialogMala = new Dialog(getContext());
        MyDialogFinal = new Dialog(getContext());

        respondeBien = MediaPlayer.create(getContext(), R.raw.responde_bien);
        terminaBien = MediaPlayer.create(getContext(), R.raw.finalizar_bien);
        terminaMal = MediaPlayer.create(getContext(), R.raw.finalizar_mal);

        mProgressBar = view.findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);
        pregunta = view.findViewById(R.id.campoPregunta);
        Imagenes = view.findViewById(R.id.primero);
        PreguntasTexto = view.findViewById(R.id.segundo);
        btnContinuar2 = view.findViewById(R.id.btnContinuar2);
        btnContinuar = view.findViewById(R.id.btnContinuar);
        btnContinuar.setVisibility(View.INVISIBLE);
        RecyclerView miRecyclerNumero = view.findViewById(R.id.recyclerNumeros);
        miRecyclerNumero.setLayoutManager(new LinearLayoutManager(getContext()));
        miRecyclerNumero.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        PreguntasTexto.setLayoutManager(new LinearLayoutManager(getContext()));
        Imagenes.setLayoutManager(new LinearLayoutManager(getContext()));

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickChek == 1) {
                    Toast.makeText(getContext(), nombre + ", No debes hacer trampa", Toast.LENGTH_SHORT).show();
                    revisar(false);
                } else {
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
                    clickChek = 1;
                    comparar();
                }
            }
        });


        if (getIsCheked()) {
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
            miVo = (PreguntasVo) Objects.requireNonNull(objeto).getSerializable("Objeto");
            listaRespuesta = (ArrayList<String>) Objects.requireNonNull(miBundle.getStringArrayList("respuestas")).clone();
            cargarDatos();
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
        Datos.actualizarPuntos = true;
        return view;
    }

    private void cargarDatos() {
        list = new ArrayList<>();
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
        if (numeroPregunta != 0) {
            resetTimer();
        }

        mInitialTime = 0L +
                0L +
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
                    if (Objects.equals(list.get(i).getRuta(), list.get(PreguntasTexto.getChildAdapterPosition(view)).getImg())) {
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

                switch (dragEvent) {
                    case DragEvent.ACTION_DROP:
                        if (list.get(numero).isMostrar() && list.get(PreguntasTexto.getChildAdapterPosition(v)).getImg() == null) {
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

    private void showPopup(String retorno) {
        respondeBien.start();
        final Button retroBuena;
        TextView txtRetroBuena;

        myDialogBuena.setContentView(R.layout.popup_rcorrecta);
        TextView puntajeRespuestaBuena = myDialogBuena.findViewById(R.id.campoPuntajeCorrecto);
        puntaje();
        puntajeRespuestaBuena.setText("+" + puntajeFinal);
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

        Objects.requireNonNull(myDialogBuena.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogBuena.show();
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

    private void puntaje() {
        if (tiempoCapturado > preguntasVo.getTiempoDemora()) {
            puntajeFinal = (getPuntage() * 50) / 100;
        } else {
            puntajeFinal = getPuntage();
        }
    }

    private void revisar(boolean revisar) {
        if (revisar) {
            listaColores.add("#45cc28");
        } else {
            listaColores.add("#ed2024");
        }
        if (numeroPregunta < 10) {
            puente.reinciar(numeroPregunta, listaColores);
        } else {
            Button finalizar;

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
                urlMensaje = "https://contaniif.club/movil/retroalimentacion.php?codigo=" + buenas+"&idusuario="+credenciales;
                terminaBien.start();
            } else if (malas > buenas) {
                urlMensaje = "https://contaniif.club/movil/retroalimentacion.php?codigo=" + buenas+"&idusuario="+credenciales;
                terminaMal.start();
            } else if (malas == buenas) {
                urlMensaje = "https://contaniif.club/movil/retroalimentacion.php?codigo=" + buenas+"&idusuario="+credenciales;
                terminaBien.start();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlMensaje, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    campoFinal.setText(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No se pudo registrar el puntaje" + error.toString(), Toast.LENGTH_SHORT).show();
                }

            });
            request.add(stringRequest);


            finalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialogFinal.dismiss();

                    puente.finaliza();
                }
            });

            Objects.requireNonNull(MyDialogFinal.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            MyDialogFinal.show();
        }
        btnContinuar.setVisibility(View.INVISIBLE);
        btnContinuar2.setVisibility(View.VISIBLE);
    }


    private void enviarDatosPuntaje() {
        String url;
        url = Objects.requireNonNull(getContext()).getString(R.string.ipRegistroPuntaje);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                int idpregunta = preguntasVo.getId();
                int tiempo = tiempoCapturado;
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idusuario", idusuario);
                parametros.put("idpregunta", Integer.toString(idpregunta));
                parametros.put("tiempo", Integer.toString(tiempo));
                parametros.put("puntos", Integer.toString(puntajeFinal));
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

    private void cargarCredenciales() {
        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        this.credenciales = preferences.getString("correo", "No existe el valor");
    }

}
