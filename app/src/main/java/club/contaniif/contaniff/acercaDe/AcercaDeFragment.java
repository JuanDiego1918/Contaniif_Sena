package club.contaniif.contaniff.acercaDe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

import club.contaniif.contaniff.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AcercaDeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AcercaDeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AcercaDeFragment extends Fragment {
    //Hola
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public AcercaDeFragment() {
        // Required empty public constructor
    }

    private LinearLayout desarrollo;
    private LinearLayout mision;
    private LinearLayout historia;
    private View view;
    private Activity activity;
    private Dialog ventanaInformacion;
    private Dialog ventanaInstru;
    private CircleImageView foto;
    private CircleImageView usuario1;
    private CircleImageView usuario2;
    private CircleImageView usuario3;
    private RequestQueue request;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcercaDeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcercaDeFragment newInstance(String param1, String param2) {
        AcercaDeFragment fragment = new AcercaDeFragment();
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
        view = inflater.inflate(R.layout.fragment_acerca_de, container, false);
        LinearLayout informacion = view.findViewById(R.id.btnInformacion);
        desarrollo = view.findViewById(R.id.btnDesarrollo);
        mision = view.findViewById(R.id.btnMision);
        historia = view.findViewById(R.id.btnHistoria);

        request = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        ventanaInformacion = new Dialog(getContext());
        ventanaInstru = new Dialog(getContext());

        informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInfor();
            }
        });

        desarrollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaDesa();
            }
        });

        mision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaMis();
            }
        });

        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaHist();
            }
        });


        return view;
    }

    private void ventanaMis() {
        ventanaInformacion.setContentView(R.layout.popup_mision);
        ventanaInformacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ventanaInformacion.show();
    }

    private void ventanaDesa() {
        TextView ver1, ver2, ver3;
        ventanaInformacion.setContentView(R.layout.popup_desarrollo);
        ventanaInformacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        usuario1 = ventanaInformacion.findViewById(R.id.imagenUsuario);
        usuario2 = ventanaInformacion.findViewById(R.id.imagenUsuario2);
        usuario3 = ventanaInformacion.findViewById(R.id.imagenUsuario3);
        ver1 = ventanaInformacion.findViewById(R.id.vermas1);
        ver2 = ventanaInformacion.findViewById(R.id.vermas2);
        ver3 = ventanaInformacion.findViewById(R.id.vermas3);
        cargarImgGeneral(1);
        cargarImgGeneral(2);
        cargarImgGeneral(3);
        ver1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInstructoras(1);
            }
        });
        ver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInstructoras(2);
            }
        });
        ver3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInstructoras(3);
            }
        });
        usuario1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInstructoras(1);
            }
        });
        usuario2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInstructoras(2);
            }
        });
        usuario3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInstructoras(3);
            }
        });

        ventanaInformacion.show();
    }

    private void cargarImgGeneral(int i) {
        String ip = getContext().getString(R.string.imgFotos);
        final String urlImagen;
        switch (i) {
            case 1:
                urlImagen = "https://" + ip + "3.jpg";
                ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        usuario1.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
                    }
                });
                request.add(imageRequest);
                break;
            case 2:
                urlImagen = "https://" + ip + "1.jpg";
                ImageRequest imageRequest2 = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        usuario2.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
                    }
                });
                request.add(imageRequest2);
                break;
            case 3:
                urlImagen = "https://" + ip + "2.jpg";
                ImageRequest imageRequest3 = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        usuario3.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
                    }
                });
                request.add(imageRequest3);
                break;
        }
    }

    private void ventanaInstructoras(int numero) {
        TextView titulo, descripcion;

        ventanaInstru.setContentView(R.layout.popup_detalles);
        ventanaInstru.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        foto = ventanaInstru.findViewById(R.id.imagenDetalleFinal);
        titulo = ventanaInstru.findViewById(R.id.tituloInstrupopup);
        descripcion = ventanaInstru.findViewById(R.id.informacionInstu);
        switch (numero) {
            case 1:
                cargarImg(3);
                titulo.setText("Sonia Cenelia León Forero");
                descripcion.setText(" Contadora Pública \n" +
                        "Magister en Educación, Diplomado en Normas Internacionales de información Financiera y Diplomado en Normas Internacionales de Auditoria. Instructora en el área de contabilidad y finanzas, con 18 años de experiencia en el sector público y privado asesorando en la ejecución de los procesos contables y la auditoria de los mismos. En el área académica cuento con experiencia en docencia universitaria y técnica tanto en las modalidades presenciales como a distancia. Actualmente me encuentro direccionada a la investigación a través del semillero de investigación APOLUNIOS, buscando con ello, alternativas innovadoras para fortalecer el programa de contabilidad y finanzas y otros programas de formación afines a las finanzas.\n" +
                        "\n" +
                        "Contador Público, Magister en Educación, Instructora Investigadora del Centro Comercio y Turismos Regional Quindío SENA, sleon@sena.edu.co \n");
                break;
            case 2:
                cargarImg(1);
                titulo.setText("Angela Rosa Amaya Ortiz");
                descripcion.setText("Contadora Publica, Especialista en Revisoría Fiscal y auditoria; Me he desempeñado como Instructora del área contable en el Servicio Nacional de aprendizaje Sena desde el año 2010, en las modalidades de Complementaria virtual y titulada. Profesional con cultura investigativa, liderazgo y capacidad de trabajo en equipo, capaz de interactuar con profesionales de otras disciplinas y poner a su disposición los conocimientos de su formación para propiciar un mejoramiento social y cultural. Alto grado de cumplimiento y responsabilidad ante los compromisos adquiridos.\n" +
                        "\n" +
                        "Contador Público, Especialista en Revisaría y Auditoria, Instructora Investigadora del Centro Comercio y Turismos Regional Quindío SENA, aamayao@sena.edu.co \n");
                break;
            case 3:
                cargarImg(2);
                titulo.setText("Angelica María Medina");
                descripcion.setText("Contadora Pública, egresada de la Universidad Libre de Pereira, con experiencia en asesoría y capacitación en Proyectos Productivos y trabajo social, y diplomado en el manejo administrativo y contable de Entidades sin Ánimo de Lucro en la universidad Cooperativa. \n" +
                        "Profesional con cultura investigativa, liderazgo y capacidad de trabajo en equipo capaz de interactuar con profesionales de otras disciplinas y poner a su disposición los conocimientos de su formación para propiciar un mejoramiento social y cultural. Alto grado de cumplimiento y responsabilidad ante los compromisos adquiridos. \n" +
                        "\n" +
                        "Contador Público, Instructora Investigadora del Centro Comercio y Turismos Regional Quindío SENA, amedinac@sena.edu.co \n");
                break;
        }
        ventanaInstru.show();
    }

    private void cargarImg(int i) {
        String ip = getContext().getString(R.string.imgFotos);

        final String urlImagen = "https://" + ip + i + ".jpg";
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                foto.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar la imagen" + urlImagen, Toast.LENGTH_LONG).show();
            }
        });
        request.add(imageRequest);
    }

    private void ventanaHist() {
        ventanaInformacion.setContentView(R.layout.popup_historia);
        ventanaInformacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ventanaInformacion.show();
    }


    private void ventanaInfor() {
        ventanaInformacion.setContentView(R.layout.popup_informacion);
        ventanaInformacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ventanaInformacion.show();
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
        void onFragmentInteraction();
    }
}
