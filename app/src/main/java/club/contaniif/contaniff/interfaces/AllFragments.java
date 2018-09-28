package club.contaniif.contaniff.interfaces;


import club.contaniif.contaniff.acercaDe.AcercaDeFragment;
import club.contaniif.contaniff.configuracion.Configuracion;
import club.contaniif.contaniff.configuracion.MiPerfil;
import club.contaniif.contaniff.grupos.Grupos;
import club.contaniif.contaniff.miRendimiento.RendimiendoFragment;
import club.contaniif.contaniff.principal.Pantalla_empezar;
import club.contaniif.contaniff.sabiasQue.CategoriasSabias;
import club.contaniif.contaniff.videos.CategoriasVideosFragment;

public interface AllFragments
        extends Pantalla_empezar.OnFragmentInteractionListener,
        AcercaDeFragment.OnFragmentInteractionListener,
        RendimiendoFragment.OnFragmentInteractionListener,
        CategoriasVideosFragment.OnFragmentInteractionListener,
        CategoriasSabias.OnFragmentInteractionListener,
        Configuracion.OnFragmentInteractionListener,
        Grupos.OnFragmentInteractionListener,
        MiPerfil.OnFragmentInteractionListener{
}
