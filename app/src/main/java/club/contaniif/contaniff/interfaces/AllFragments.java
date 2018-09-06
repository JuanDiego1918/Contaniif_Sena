package club.contaniif.contaniff.interfaces;


import club.contaniif.contaniff.acercaDe.AcercaDeFragment;
import club.contaniif.contaniff.principal.Pantalla_empezar;
import club.contaniif.contaniff.videos.CategoriasVideosFragment;

public interface AllFragments
        extends Pantalla_empezar.OnFragmentInteractionListener,
        //,Pantalla_teoria.OnFragmentInteractionListener
        AcercaDeFragment.OnFragmentInteractionListener,
        //PantallaPrincipal.OnFragmentInteractionListener,
        //MisionVision.OnFragmentInteractionListener,
        //AcercaDeNosotros.OnFragmentInteractionListener,
        //SinConexionInternet.OnFragmentInteractionListener,
        CategoriasVideosFragment.OnFragmentInteractionListener
        //PrimerFragment.OnFragmentInteractionListener,
        //PantallaConfiguracion.OnFragmentInteractionListener,
        //MiRendimiento.OnFragmentInteractionListener,
        //Pantalla_empezar_drag.OnFragmentInteractionListener,
        //Registro.OnFragmentInteractionListener
{
}
