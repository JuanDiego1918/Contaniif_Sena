package club.contaniif.contaniff.acercaDe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AcercaDeFragment() {
        // Required empty public constructor
    }

    LinearLayout informacion, desarrollo, mision, historia;
    View view;
    Activity activity;
    Dialog ventanaInformacion, ventanaInstru;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_acerca_de, container, false);
        informacion = view.findViewById(R.id.btnInformacion);
        desarrollo = view.findViewById(R.id.btnDesarrollo);
        mision = view.findViewById(R.id.btnMision);
        historia = view.findViewById(R.id.btnHistoria);

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
        CircleImageView usuario1, usuario2, usuario3;
        ventanaInformacion.setContentView(R.layout.popup_desarrollo);
        ventanaInformacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        usuario1 = ventanaInformacion.findViewById(R.id.imagenUsuario);
        usuario2 = ventanaInformacion.findViewById(R.id.imagenUsuario2);
        usuario3 = ventanaInformacion.findViewById(R.id.imagenUsuario3);

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

    private void ventanaInstructoras(int numero) {
        TextView titulo, descripcion;
        CircleImageView foto;
        ventanaInstru.setContentView(R.layout.popup_detalles);
        ventanaInstru.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        foto = ventanaInstru.findViewById(R.id.imagenDetalleFinal);
        titulo = ventanaInstru.findViewById(R.id.tituloInstrupopup);
        descripcion = ventanaInstru.findViewById(R.id.informacionInstu);
        switch (numero) {
            case 1:
                foto.setImageDrawable(getResources().getDrawable(R.drawable.medalla_gold));
                titulo.setText("Sonia Cenelia León Forero");
                descripcion.setText("Contadora Pública\n" +
                        "Magister en Educación, Diplomado en Normas Internacionales de información Financiera y Diplomado en Normas Internacionales de Auditoria. Instructora en el área de contabilidad y finanzas, con 18 años de experiencia en el sector público y privado asesorando en la ejecución de los procesos contables y la auditoria de los mismos. En el área académica cuento con experiencia en docencia universitaria y técnica tanto en las modalidades presenciales como a distancia. Actualmente me encuentro direccionada a la investigación a través del semillero de investigación APOLUNIOS, buscando con ello, alternativas innovadoras para fortalecer el programa de contabilidad y finanzas y otros programas de formación afines a las finanzas.\n" +
                        "\n" +
                        "Contador Público, Magister en Educación, Instructora Investigadora del Centro Comercio y Turismos Regional Quindío SENA, sleon@sena.edu.co");
                break;
            case 2:
                foto.setImageDrawable(getResources().getDrawable(R.drawable.medalla_silver));
                titulo.setText("Angela Rosa Amaya Ortiz");
                descripcion.setText("Contadora Publica, Especialista en Revisoría Fiscal y auditoria; Me he desempeñado como Instructora del área contable en el Servicio Nacional de aprendizaje Sena desde el año 2010, en las modalidades de Complementaria virtual y titulada. Profesional con cultura investigativa, liderazgo y capacidad de trabajo en equipo, capaz de interactuar con profesionales de otras disciplinas y poner a su disposición los conocimientos de su formación para propiciar un mejoramiento social y cultural. Alto grado de cumplimiento y responsabilidad ante los compromisos adquiridos.\n" +
                        "\n" +
                        "Contador Público, Especialista en Revisaría y Auditoria, Instructora Investigadora del Centro Comercio y Turismos Regional Quindío SENA, aamayao@sena.edu.co");
                break;
            case 3:
                foto.setImageDrawable(getResources().getDrawable(R.drawable.medalla_bronze));
                titulo.setText("Angelica María Medina");
                descripcion.setText("Contadora Pública, egresada de la Universidad Libre de Pereira, con experiencia en asesoría y capacitación en Proyectos Productivos y trabajo social, y diplomado en el manejo administrativo y contable de Entidades sin Ánimo de Lucro en la universidad Cooperativa.\n" +
                        "Profesional con cultura investigativa, liderazgo y capacidad de trabajo en equipo capaz de interactuar con profesionales de otras disciplinas y poner a su disposición los conocimientos de su formación para propiciar un mejoramiento social y cultural. Alto grado de cumplimiento y responsabilidad ante los compromisos adquiridos.\n" +
                        "Contador Público, Instructora Investigadora del Centro Comercio y Turismos Regional Quindío SENA, amedinac@sena.edu.co");
                break;
        }
        ventanaInstru.show();
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
