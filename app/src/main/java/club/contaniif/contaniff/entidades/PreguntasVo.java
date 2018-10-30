package club.contaniif.contaniff.entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class PreguntasVo implements Serializable {

    private String rutaImagen;

    private boolean isCheck;

    private ArrayList<String> listaSeleccionada;

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    private String pregunta;
    private int categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private int puntaje;
    private int tiempoDemora;
    private int tipo;

    private String arregloPregunta;

    private String opciones;
    private String respuesta;

    private String retobuena;
    private String retromala;


    private String palabra;
    private String ruta;
    private boolean mostrar;
    private String img;

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }


    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getTiempoDemora() {
        return tiempoDemora;
    }

    public void setTiempoDemora(int tiempoDemora) {
        this.tiempoDemora = tiempoDemora;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRetobuena() {
        return retobuena;
    }

    public void setRetobuena(String retobuena) {
        this.retobuena = retobuena;
    }

    public String getRetromala() {
        return retromala;
    }

    public void setRetromala(String retromala) {
        this.retromala = retromala;
    }

    public void setId() {
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean isMostrar() {
        return !mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
