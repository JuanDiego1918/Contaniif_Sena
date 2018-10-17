package club.contaniif.contaniff.configuracion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.UsuariosVo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MiPerfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MiPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiPerfil extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    int posicion;
    int accion;
    String generoo;
    String departamentoo;
    String id;
    String img;
    String municipioo;

    Dialog dialogoCargando;
    Spinner lisdaAnios, listaMeses, listaDias;
    ArrayList arrayAnios, arrayMeses, arrayDias;
    String anioLista,mesLista,diaLista;
    Dialog dialogoFecha;

    String rutaImagenn;
    String rutaImg;

    boolean seleccionaAnio = false,seleccionaMes = false,seleccionaDia = false;

    int daate;
    String date;

    public String getGeneroo() {
        return generoo;
    }

    public void setGeneroo(String generoo) {
        this.generoo = generoo;
    }

    public String getDepartamentoo() {
        return departamentoo;
    }

    public void setDepartamentoo(String departamentoo) {
        this.departamentoo = departamentoo;
    }

    public String getMunicipioo() {
        return municipioo;
    }

    public void setMunicipioo(String municipioo) {
        this.municipioo = municipioo;
    }

    public String getRutaImagenn() {
        return rutaImagenn;
    }

    public void setRutaImagenn(String rutaImagenn) {
        this.rutaImagenn = rutaImagenn;
    }

    public boolean isSeleccionaGenero() {
        return seleccionaGenero;
    }

    public void setSeleccionaGenero(boolean seleccionaGenero) {
        this.seleccionaGenero = seleccionaGenero;
    }

    public boolean isSeleccionaImagen() {
        return SeleccionaImagen;
    }

    public void setSeleccionaImagen(boolean seleccionaImagen) {
        SeleccionaImagen = seleccionaImagen;
    }

    public boolean isSeleccionaDepartamento() {
        return seleccionaDepartamento;
    }

    public void setSeleccionaDepartamento(boolean seleccionaDepartamento) {
        this.seleccionaDepartamento = seleccionaDepartamento;
    }

    boolean seleccionaGenero;
    boolean SeleccionaImagen;
    boolean seleccionaDepartamento;

    public boolean isSeleccionaImagenusuario() {
        return seleccionaImagenusuario;
    }

    public void setSeleccionaImagenusuario(boolean seleccionaImagenusuario) {
        this.seleccionaImagenusuario = seleccionaImagenusuario;
    }

    boolean seleccionaImagenusuario;

    String genero;
    String municipio;
    String departamento;
    int validacionGenero;
    int validacionMunicipio;
    int validacionDepartamento;
    int validacionFecha;
    int validacionImagenusuario1;
    String urlImagenUsuario;

    public String getUrlImagenUsuario() {
        return urlImagenUsuario;
    }

    public void setUrlImagenUsuario(String urlImagenUsuario) {
        this.urlImagenUsuario = urlImagenUsuario;
    }

    public int getValidacionImagenusuario1() {
        return validacionImagenusuario1;
    }

    public void setValidacionImagenusuario1(int validacionImagenusuario1) {
        this.validacionImagenusuario1 = validacionImagenusuario1;
    }

    public int getValidacionFecha() {
        return validacionFecha;
    }

    public void setValidacionFecha(int validacionFecha) {
        this.validacionFecha = validacionFecha;
    }

    String credenciales;

    public String getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(String credenciales) {
        this.credenciales = credenciales;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getValidacionGenero() {
        return validacionGenero;
    }

    public void setValidacionGenero(int validacionGenero) {
        this.validacionGenero = validacionGenero;
    }

    public int getValidacionMunicipio() {
        return validacionMunicipio;
    }

    public void setValidacionMunicipio(int validacionMunicipio) {
        this.validacionMunicipio = validacionMunicipio;
    }

    public int getValidacionDepartamento() {
        return validacionDepartamento;
    }

    public void setValidacionDepartamento(int validacionDepartamento) {
        this.validacionDepartamento = validacionDepartamento;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ///////
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    ///////-Elementos del layout
    Spinner listaDepartamentos, listaMunicipios, listaGenero;
    EditText campoNombre, campoApellido, campoCorreo;
    TextView campoMunicipio,campoDepartamento,campoGenero,campoFechaNacimiento;
    CircleImageView imagenUsuario, imagenCamara;
    String fecha;
    ImageView editarGenero,editarFecha,editarDepartamento,editarMunicipio;
    Button btnRegistro,btnFalso;
    //////-Listas
    ArrayList<String> ArrayDepartamentos;
    ArrayList<String> ArrayMunicipios;
    ArrayList<String> ArrayGenero;
    //////-Para pedir los datos a la base de datos
    RequestQueue request;
    RequestQueue request2;
    JsonObjectRequest jsonObjectRequest;
    JsonObjectRequest jsonObjectRequest2;
    JsonObjectRequest jsonObjectRequest3;
    StringRequest stringRequest;
    /////
    ProgressDialog progreso;
    /////
    String rutaImagen;
    /////
    String dato;

    public MiPerfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PantallaConfiguracion.
     */
    // TODO: Rename and change types and number of parameters
    public static MiPerfil newInstance(String param1, String param2) {
        MiPerfil fragment = new MiPerfil();
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
        View vista = inflater.inflate(R.layout.fragment_configuracion, container, false);
        request = Volley.newRequestQueue(getContext());
        request2 = Volley.newRequestQueue(getContext());
        dialogoCargando = new Dialog(this.getContext());
        dialogoFecha = new Dialog(this.getContext());
        obtenerFecha();
        llenarAnios();
        llenarMeses();
        llenarDias();

        cargarCredenciales();
        // myDialogFecha.setContentView(R.layout.popup_seleccionar_fecha);
        ArrayGenero = new ArrayList<>();
        ArrayGenero.add("Seleccioar genero");
        ArrayGenero.add("Masculino");
        ArrayGenero.add("Femenino");
        //
        ArrayDepartamentos = new ArrayList<>();
        ArrayDepartamentos.add("Seleccione su departamento");
        ArrayDepartamentos.add("Antioquia");
        ArrayDepartamentos.add("Atlántico");
        ArrayDepartamentos.add("Bogotá");
        ArrayDepartamentos.add("Bolívar");
        ArrayDepartamentos.add("Boyacá");
        ArrayDepartamentos.add("Caldas");
        ArrayDepartamentos.add("Caquetá");
        ArrayDepartamentos.add("Cauca");
        ArrayDepartamentos.add("Cesar");
        ArrayDepartamentos.add("Córdoba");
        ArrayDepartamentos.add("Cundinamarca");
        ArrayDepartamentos.add("Chocó");
        ArrayDepartamentos.add("Huila");
        ArrayDepartamentos.add("La Guajira");
        ArrayDepartamentos.add("Magdalena");
        ArrayDepartamentos.add("Meta");
        ArrayDepartamentos.add("Nariño");
        ArrayDepartamentos.add("Norte de Santander");
        ArrayDepartamentos.add("Quindío");
        ArrayDepartamentos.add("Risaralda");
        ArrayDepartamentos.add("Santander");
        ArrayDepartamentos.add("Sucre");
        ArrayDepartamentos.add("Tolima");
        ArrayDepartamentos.add("Valle del Cauca");
        ArrayDepartamentos.add("Arauca");
        ArrayDepartamentos.add("Casanare");
        ArrayDepartamentos.add("Putumayo");
        ArrayDepartamentos.add("San Andrés y Providencia");
        ArrayDepartamentos.add("Amazonas");
        ArrayDepartamentos.add("Guainía");
        ArrayDepartamentos.add("Guaviare");
        ArrayDepartamentos.add("Vaupés");
        ArrayDepartamentos.add("Vichada");


        btnRegistro = vista.findViewById(R.id.btnRegistrar);

        listaDepartamentos = vista.findViewById(R.id.spinnerDepartamentoConfig);
        ArrayAdapter<CharSequence> adapterDepartamentos = new ArrayAdapter(getContext(), R.layout.spinner_item, ArrayDepartamentos);
        listaDepartamentos.setAdapter(adapterDepartamentos);
        listaDepartamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    setValidacionDepartamento(2);
                    setPosicion(position);
                    setDepartamento(ArrayDepartamentos.get(position));
                    cargarListaMunicipios();
                    setDepartamentoo(ArrayDepartamentos.get(position));
                    setSeleccionaDepartamento(true);
                    //seleccionaDepartamento = true;
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listaMunicipios = vista.findViewById(R.id.spinnerMunicipioConfig);
        listaGenero = vista.findViewById(R.id.spinnerGeneroConfig);
        ArrayAdapter<CharSequence> adapterGenero = new ArrayAdapter(getContext(), R.layout.spinner_item, ArrayGenero);
        listaGenero.setAdapter(adapterGenero);
        listaGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    setGenero(ArrayGenero.get(i));
                    setValidacionGenero(2);
                    setGeneroo(ArrayGenero.get(i));
                    setSeleccionaGenero(true);
                    //seleccionaGenero=true;
                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        campoGenero = vista.findViewById(R.id.campoGeneroConfig);
        campoFechaNacimiento = vista.findViewById(R.id.campoFechaConfig);
        campoDepartamento = vista.findViewById(R.id.campoDepartamentoConfig);
        campoMunicipio = vista.findViewById(R.id.campoMunicipioConfig);

        editarGenero = vista.findViewById(R.id.imagenEditarGenero);
        editarGenero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campoGenero.setVisibility(View.INVISIBLE);
                listaGenero.setVisibility(View.VISIBLE);
                setValidacionGenero(10);
            }
        });

        editarFecha = vista.findViewById(R.id.imagenEditarFechaNacimiento);
        editarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValidacionFecha(10);
                cargarDialogoFecha();
            }
        });

        editarDepartamento = vista.findViewById(R.id.imagenEditarDepartamento);
        editarDepartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campoDepartamento.setVisibility(View.INVISIBLE);
                listaDepartamentos.setVisibility(View.VISIBLE);
                campoMunicipio.setVisibility(View.INVISIBLE);
                listaMunicipios.setVisibility(View.VISIBLE);
                setValidacionMunicipio(10);
            }
        });

        /*editarMunicipio = vista.findViewById(R.id.imagenEditarMunicipio);
        editarMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campoMunicipio.setVisibility(View.INVISIBLE);
                listaMunicipios.setVisibility(View.VISIBLE);
            }
        });*/

        campoNombre = vista.findViewById(R.id.campoNombreConfig);
        campoApellido = vista.findViewById(R.id.campoApellidoConfig);
        campoCorreo = vista.findViewById(R.id.campoCorreoConfig);
        campoFechaNacimiento = vista.findViewById(R.id.campoFechaConfig);
        //
        imagenUsuario = vista.findViewById(R.id.imagenUsuario);
        imagenUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionesCapturaFoto();
                setValidacionImagenusuario1(10);
                setSeleccionaImagenusuario(true);
                seleccionaImagenusuario = (true);
            }
        });
        imagenCamara = vista.findViewById(R.id.imagenCamara);
        imagenCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opcionesCapturaFoto();
                setValidacionImagenusuario1(10);
                setSeleccionaImagenusuario(true);
                seleccionaImagenusuario = (true);
            }
        });
        //
        btnRegistro = vista.findViewById(R.id.btnRegistrar);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getValidacionDepartamento()==10 || getValidacionFecha()==10 || getValidacionGenero()==10 || getValidacionMunicipio()==10){
                    Toast.makeText(getContext(),"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
                }else {
                    //setSeleccionaImagenusuario(false);
                    //setSeleccionaDepartamento(false);
                    //setSeleccionaGenero(false);
                    actualizarUsuarios();
                    guardarNombre(campoNombre.getText().toString());
                }


            }
        });


        //////////
        listaGenero.setVisibility(View.INVISIBLE);
        listaMunicipios.setVisibility(View.INVISIBLE);
        listaDepartamentos.setVisibility(View.INVISIBLE);
        //////////
        return vista;
    }

    private void obtenerFecha() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
        Date date = new Date();
        fecha = "20"+dateFormat.format(date);
    }

    private void llenarAnios(){
        arrayAnios = new ArrayList();
        arrayAnios.add("AÑO");
        int anio = Integer.parseInt(fecha) - 10;
        int restaAnio = anio -80;
        for (int i = anio; i >= restaAnio; i--) {
            arrayAnios.add(i);
        }
    }

    private void llenarMeses(){
        arrayMeses = new ArrayList();
        arrayMeses.add("MES");//31
        arrayMeses.add("ENERO");//31
        arrayMeses.add("FEBRERO");//29
        arrayMeses.add("MARZO");//31
        arrayMeses.add("ABRIL");//30
        arrayMeses.add("MAYO");//31
        arrayMeses.add("JUNIO");//30
        arrayMeses.add("JULIO");//31
        arrayMeses.add("AGOSTO");//31
        arrayMeses.add("SEPTIEMBRE");//30
        arrayMeses.add("OCTUBRE");//31
        arrayMeses.add("NOVIEMBRE");//30
        arrayMeses.add("DICIEMBRE");//31
    }

    private void llenarDias(){
        arrayDias = new ArrayList();
        arrayDias.add("DIA");
        for (int i = 1; i <=31 ; i++) {
            arrayDias.add(i);
        }
    }
    private void cargarListaMunicipios() {
        String url = getContext().getString(R.string.ipTraerMunicipio)+getPosicion();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
        setAccion(1);
    }


    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        }catch (Exception e){
            Log.i("Error " , e.toString());
        }

    }

    private void guardarNombre(String nombre) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombre", nombre);
        editor.commit();
    }

    private void cargarCredenciales() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credenciales",Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo","No existe el valor");
        this.credenciales = credenciales;
        dialogoCargando();
        cargarDatosPerfil();
        //Toast.makeText(getContext(),"credenciales:" + getCredenciales(),Toast.LENGTH_SHORT).show();

    }

    public void cargarDialogoFecha() {
        final Calendar calendar = Calendar.getInstance();
        Button cancelar, enviar;
        dialogoFecha.setContentView(R.layout.popup_fecha);

        lisdaAnios = dialogoFecha.findViewById(R.id.spinnerAnio);
        listaMeses = dialogoFecha.findViewById(R.id.spinnerMes);
        listaDias = dialogoFecha.findViewById(R.id.spinnerDia);

        ArrayAdapter<CharSequence> adapterAnios = new ArrayAdapter(this.getContext(), R.layout.spinner_item, arrayAnios);
        lisdaAnios.setAdapter(adapterAnios);
        lisdaAnios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    seleccionaAnio = true;
                    anioLista = arrayAnios.get(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionaAnio = false;
            }
        });

        ArrayAdapter<CharSequence> adapterMeses = new ArrayAdapter(this.getContext(), R.layout.spinner_item, arrayMeses);
        listaMeses.setAdapter(adapterMeses);
        listaMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    seleccionaMes = true;
                    mesLista = ""+position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionaMes = false;
            }
        });

        ArrayAdapter<CharSequence> adapterDias = new ArrayAdapter(this.getContext(), R.layout.spinner_item, arrayDias);
        listaDias.setAdapter(adapterDias);
        listaDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    seleccionaDia = true;
                    diaLista = arrayDias.get(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionaDia = false;
            }
        });

        cancelar = dialogoFecha.findViewById(R.id.btnCancelarFecha);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoFecha.dismiss();
                setValidacionFecha(2);
            }
        });

        enviar = dialogoFecha.findViewById(R.id.btnAceptarFecha);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccionaAnio == false || seleccionaMes == false || seleccionaDia == false){
                    setValidacionFecha(10);
                    campoFechaNacimiento.setText("000-00-00");
                }else {
                    setValidacionFecha(3);
                    campoFechaNacimiento.setText(anioLista+"-"+mesLista+"-"+diaLista);
                }


                dialogoFecha.dismiss();
            }
        });

        dialogoFecha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoFecha.show();

    }




    private void cargarDatosPerfil() {
        // progreso = new ProgressDialog(getApplicationContext());
        // progreso.setMessage("Consultando...");
        // progreso.show();

        String url = getContext().getString(R.string.ipMiPerfil)+getCredenciales();
        jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request2.add(jsonObjectRequest2);
        setAccion(2);
    }
/////////////////////////////////////////////////////////Capturar imagen

    private void opcionesCapturaFoto() {
        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    abrirCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){

                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();
        if (isCreada==false){
            isCreada=miFile.mkdirs();//por si la variable no fue creada, se crea de nuevo
        }
        if (isCreada==true){
            Long consecutivo= System.currentTimeMillis()/100;//aqui iba un 100, por si no funciona el codigo este es el error
            String nombre=consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre;//indicamos la ruta de almacenamiento

            fileImagen=new File(path);

            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(getContext(),authorities,fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent,COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    imagenUsuario.setImageURI(miPath);


                    try {
                        bitmap = redimensionarImagen(bitmap, 150, 150);
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), miPath);
                        imagenUsuario.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Path", "" + path);
                                }
                            });

                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = redimensionarImagen(bitmap, 150, 150);
                    imagenUsuario.setImageBitmap(bitmap);

                    break;
            }
            bitmap = redimensionarImagen(bitmap, 150, 150);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No se ha elegido ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        if (seleccionaImagenusuario==true){
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        }

        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }

/////////////////////////////////////////////////////////


    private void mostrarImg(String rutaImagen) {
        String ip=getContext().getString(R.string.ipImgsuario);
        img = ip;
        final String urlImagen=ip+id+".jpg";
        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imagenUsuario.setImageBitmap(response);
                dialogoCargando.hide();
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error al cargar la imagen" + error +  urlImagen, Toast.LENGTH_LONG).show();
                Log.i("Error img",error.toString() + urlImagen);
            }
        });
        setAccion(2);
        request.add(imageRequest);
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

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

        switch(getAccion()){
            case 1:
                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject =     null;
                ArrayMunicipios = new ArrayList<String>();
                try {
                    ArrayMunicipios.add("Seleccione su municipio");
                    for (int i = 0; i < json.length(); i++) {
                        jsonObject = json.getJSONObject(i);
                        //departamentos = new DepartamentosVo();
                        ArrayMunicipios.add(jsonObject.getString("municipio"));
                        //Toast.makeText(getContext(),"lista url" + response,Toast.LENGTH_LONG).show();
                    }
                    ArrayAdapter<CharSequence> adapterMunicipios=new ArrayAdapter(this.getContext(),R.layout.spinner_item,ArrayMunicipios);
                    listaMunicipios.setAdapter(adapterMunicipios);
                    listaMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i!=0){
                                setMunicipioo(ArrayMunicipios.get(i));
                                setMunicipio(ArrayMunicipios.get(i));
                                setValidacionMunicipio(2);
                            }else {

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" + " " + response, Toast.LENGTH_LONG).show();

                }
                break;
            case 2:
                UsuariosVo miUsuario = new UsuariosVo();
                JSONArray json2 = response.optJSONArray("usuario");
                JSONObject jsonObject2= null;

                try {
                    jsonObject2 = json2.getJSONObject(0);
                    miUsuario.setId(jsonObject2.optString("id"));
                    miUsuario.setNombres(jsonObject2.optString("nombres"));
                    miUsuario.setApellidos(jsonObject2.optString("apellidos"));
                    miUsuario.setGenero(jsonObject2.optString("genero"));
                    miUsuario.setCorreo(jsonObject2.optString("correo"));
                    miUsuario.setFechaNacimiento(jsonObject2.optString("fechaNacimiento"));
                    miUsuario.setDepartamento(jsonObject2.optString("departamento"));
                    miUsuario.setMunicipio(jsonObject2.optString("municipio"));
                    miUsuario.setRutaImagen(jsonObject2.optString("rutaImagen"));
///////////////////////////////////////////////////////////////////////////////777////77////77/////777777///7//////////////
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (seleccionaGenero==false){
                    setGeneroo(miUsuario.getGenero().toString());
                }

                if (seleccionaDepartamento==false){
                    setDepartamentoo(miUsuario.getDepartamento().toString());
                    setMunicipioo(miUsuario.getMunicipio().toString());
                }


                id = miUsuario.getId().toString();
                setUrlImagenUsuario(miUsuario.getRutaImagen().toString());
                mostrarImg(miUsuario.getRutaImagen().toString());
                campoNombre.setText(miUsuario.getNombres().toString());
                campoApellido.setText(miUsuario.getApellidos().toString());
                campoGenero.setText(miUsuario.getGenero().toString());
                campoCorreo.setText(miUsuario.getCorreo().toString());
                campoFechaNacimiento.setText(miUsuario.getFechaNacimiento().toString());
                campoDepartamento.setText(miUsuario.getDepartamento().toString());
                campoMunicipio.setText(miUsuario.getMunicipio().toString());

                if (seleccionaImagenusuario==true){


                }else {

                }
                break;
        }


    }

    private void actualizarUsuarios() {
        dialogoCargando();
        String url;
        if (seleccionaImagenusuario==true){
            url = getContext().getString(R.string.ipActualizarUsuario2);

        }else {
            url = getContext().getString(R.string.ipActualizarUsuario1);
        }



        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialogoCargando.hide();
                if (response.trim().equalsIgnoreCase("actualiza")){
                    listaGenero.setVisibility(View.INVISIBLE);
                    listaMunicipios.setVisibility(View.INVISIBLE);
                    listaDepartamentos.setVisibility(View.INVISIBLE);

                    campoMunicipio.setVisibility(View.VISIBLE);
                    campoDepartamento.setVisibility(View.VISIBLE);
                    campoGenero.setVisibility(View.VISIBLE);

                    cargarDatosPerfil();
                }else{
                    Toast.makeText(getContext(),"No se ha Actualizado",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                //pDialog.hide();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombres = campoNombre.getText().toString();
                String apellidos = campoApellido.getText().toString();
                String genero = generoo;
                String correo = getCredenciales();
                String fechaNacimiento = campoFechaNacimiento.getText().toString();
                String departamento = departamentoo;
                String municipio = municipioo;
                String rutaImagen = rutaImg=convertirImgString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombres", nombres);
                parametros.put("apellidos", apellidos);
                parametros.put("genero", genero);
                parametros.put("correo", correo);
                parametros.put("fechaNacimiento", fechaNacimiento);
                parametros.put("departamento", departamento);
                parametros.put("municipio", municipio);
                parametros.put("rutaImagen", rutaImagen);
                Log.i("*******Parametros",parametros.toString());
                return parametros;
            }
        };
        request.add(stringRequest);

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