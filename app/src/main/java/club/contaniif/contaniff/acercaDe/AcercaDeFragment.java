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
import android.widget.LinearLayout;

import club.contaniif.contaniff.R;

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
    Dialog ventanaInformacion;

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

        informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ventanaInfor();
            }
        });

        desarrollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
