package club.contaniif.contaniff.interfaces;

import java.util.ArrayList;

public interface Puente {
    void numero(String tipo);
    void sabias(String tipo);
    void activos(String puntos);
    void finaliza();
    void reinciarRendimiento();
    void reinciar(int numeroPregunta, ArrayList<String> lista);
}
