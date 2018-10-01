package club.contaniif.contaniff.interfaces;

import java.util.ArrayList;

public interface Puente {
    //public void pantalla(int numero);
    //public void acercade(int numero);
    public void numero(String tipo);
    public void sabias(String tipo);
    public void activos(String puntos);
    public void finaliza();
    public void reinciarRendimiento();
    public void reinciar(int numeroPregunta, int tipo, ArrayList<String> lista);
}
