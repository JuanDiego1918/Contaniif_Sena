package club.contaniif.contaniff.registro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
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
import java.util.Map;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.actividades.IngresaCodigoRegistro;
import club.contaniif.contaniff.actividades.MainActivity;
import club.contaniif.contaniff.entidades.VolleySingleton;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Registro extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    int posicion = 0, accion = 0;
    String departamento = "", genero = "", municipio = "";
    boolean seleccionaDepartamento = false, seleccionaGenero = false, seleccionaImagen = false, seleccionaMunicipio = false, seleccionaFecha = false;
    boolean seleccionaAnio = false,seleccionaMes = false,seleccionaDia = false;

    Spinner lisdaAnios, listaMeses, listaDias;
    ArrayList arrayAnios, arrayMeses, arrayDias;
    Dialog dialogoFecha;
    boolean permisoCamara = false;

    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    // Patrón para validar el email
    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    String dato;
    Dialog dialogoCargando;
    Dialog dialogoRegistrado;
    Dialog dialogoIngresaCorreo;
    Dialog dialogoTerminos;
    TextView campoTerminos;
    CheckBox checkTerminos;

    File fileImagen;
    Bitmap bitmap;
    Spinner listaDepartamentos, listaMunicipios, listaGenero;
    EditText campoNombre, campoApellido, campoCorreo;
    ImageView imagenCamara;
    TextView campoFecha;
    String fecha;
    Button btnRegistro, btnFecha;

    String anioLista,mesLista,diaLista;
    private int dia, mes, anio;
    ArrayList<String> ArrayDepartamentos;
    ArrayList<String> ArrayMunicipios;
    ArrayList<String> ArrayGenero;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registro);

        dialogoRegistrado = new Dialog(this);
        dialogoIngresaCorreo = new Dialog(this);
        dialogoFecha = new Dialog(this);
        dialogoCargando = new Dialog(this);
        dialogoTerminos = new Dialog(this);

        consultarCredenciales();
        obtenerFecha();
        llenarAnios();
        llenarMeses();
        llenarDias();


        if (solicitaPermisosVersionesSuperiores()) {
            permisoCamara = true;
        } else {
            permisoCamara = false;
        }

        request = Volley.newRequestQueue(getApplicationContext());

        ArrayGenero = new ArrayList<>();
        ArrayGenero.add("Seleccioar genero");
        ArrayGenero.add("Masculino");
        ArrayGenero.add("Femenino");

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

        campoTerminos = findViewById(R.id.campoTerminos);
        checkTerminos = findViewById(R.id.checkTerminos);
        //SpannableString mitextoU = new SpannableString("terminos y condiciones");
        //mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        campoTerminos.setPaintFlags(campoTerminos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        campoTerminos.setText("terminos y condiciones");
        campoTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupTerminos();
            }
        });

        //

        campoNombre = findViewById(R.id.campoNombreRegistro);
        campoApellido = findViewById(R.id.campoApellidosRegistro);
        campoCorreo = findViewById(R.id.campoCorreoRegistro);
        campoFecha = findViewById(R.id.campoFechaRegistro);
        campoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogoFecha();
            }
        });
        //btnFecha = findViewById(R.id.btnFechaRegistro);
        /*btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDialogoFecha();
            }
        });*/

        imagenCamara = findViewById(R.id.imagenUsuario);
        listaDepartamentos = findViewById(R.id.spinnerDepartamentoRegistro);
        listaMunicipios = findViewById(R.id.spinnerCuidadRegistro);
        listaGenero = findViewById(R.id.spinnerGeneroRegistro);

        ArrayAdapter<CharSequence> adapterDepartamentos = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, ArrayDepartamentos);
        listaDepartamentos.setAdapter(adapterDepartamentos);
        listaDepartamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    posicion = position;
                    departamento = ArrayDepartamentos.get(position);
                    cargarListaMunicipios();
                    seleccionaDepartamento = true;
                } else {
                    seleccionaDepartamento = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<CharSequence> adapterGenero = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, ArrayGenero);
        listaGenero.setAdapter(adapterGenero);
        listaGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    genero = ArrayGenero.get(i);
                    seleccionaGenero = true;
                } else {
                    seleccionaGenero = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imagenCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //opcionesCapturaFoto();
                //seleccionaImagen = false;
                //mostrarDialogOpciones();

                if (permisoCamara == false) {
                    Toast.makeText(Registro.this, "Debe aceptar los permisos para poder usar la camara, dirijase a configuracion de aplicaciones", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    //cargarDialogoRecomendacion();
                    //solicitarPermisosManual();
                    solicitaPermisosVersionesSuperiores();
                } else {
                    mostrarDialogOpciones();
                }

            }
        });

        btnRegistro = findViewById(R.id.btnRegistrar);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campoNombre.getText().equals("") || campoApellido.getText().equals("") || seleccionaGenero == false || seleccionaMunicipio == false || seleccionaDepartamento == false || seleccionaImagen == false || seleccionaFecha == false) {
                    Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkTerminos.isChecked()){
                        validaCorreo();
                    }else {
                        Toast.makeText(getApplicationContext(), "Debe aceptar los terminos y condiciones dela aplicacion", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        showPopupRegistrado();
    }

    private void validaCorreo() {
        // El email a validar
        String email = campoCorreo.getText().toString();
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            validaPermisoCamara();
        } else {
            Toast.makeText(this, "El email ingresado es inválido.", Toast.LENGTH_SHORT).show();
        }

    }

    private void validaPermisoCamara() {
        if (permisoCamara == false) {
            Toast.makeText(this, "Debe aceptar los permisos de camara, para poder registrarse ", Toast.LENGTH_LONG).show();
        } else {
            registrarUsuarios();
        }
    }

    private void showPopupRegistrado() {
        Button si, no;
        dialogoRegistrado.setContentView(R.layout.popup_pregunta_correo);
        si = dialogoRegistrado.findViewById(R.id.btnSiRegistrado);
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupIngresaCorreo();
                dialogoRegistrado.dismiss();
            }
        });

        no = dialogoRegistrado.findViewById(R.id.btnNoregistrado);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoRegistrado.dismiss();

            }
        });

        dialogoRegistrado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoRegistrado.show();

    }

    private void showPopupTerminos() {
        WebView webView;
        Button btnAceptar;

        dialogoTerminos.setContentView(R.layout.popup_terminos_condiciones);
        btnAceptar = dialogoTerminos.findViewById(R.id.btnAceptarTerminos);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoTerminos.hide();
                checkTerminos.setChecked(true);

            }
        });

        webView = dialogoTerminos.findViewById(R.id.webTerminos);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.contaniif.club/terminos.html");

        dialogoTerminos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoTerminos.show();

    }
    private void showPopupIngresaCorreo() {
        Button aceptar, cancelar;
        final EditText campoCorreo;


        dialogoIngresaCorreo.setContentView(R.layout.popup_correo);
        campoCorreo = dialogoIngresaCorreo.findViewById(R.id.campoCorreoValida);
        aceptar = dialogoIngresaCorreo.findViewById(R.id.btnEnviarCorreo);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = campoCorreo.getText().toString();
                Matcher mather = pattern.matcher(email);
                if (mather.find() == true) {
                    if (campoCorreo.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Registro.this, "Por favor ingrese el correo", Toast.LENGTH_SHORT).show();
                    } else {
                        dato = campoCorreo.getText().toString();
                        validarCorreo(dato);
                        dialogoIngresaCorreo.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El email ingresado es inválido.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        cancelar = dialogoIngresaCorreo.findViewById(R.id.btnCancelarCorreo);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoIngresaCorreo.dismiss();

            }
        });

        dialogoIngresaCorreo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoIngresaCorreo.show();
    }

    private void validarCorreo(String correo) {

        dialogoCargando();
        String url;
        java.lang.System.setProperty("https.protocols", "TLSv1");
        url = getApplicationContext().getString(R.string.ipValidaCorreo);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progreso.hide();
                if (response.trim().equalsIgnoreCase("NO")) {

                    Toast.makeText(getApplicationContext(), "No se encontro el usuario", Toast.LENGTH_LONG).show();
                    dialogoCargando.hide();
                    Log.i("********RESULTADO", "Respuesta server" + response);

                } else if (response.trim().equalsIgnoreCase("{\"codigo\":[\"Correo no existe\"]}")) {
                    Toast.makeText(getApplicationContext(), "El correo no se encuentra registrado", Toast.LENGTH_LONG).show();
                    dialogoCargando.hide();
                    showPopupIngresaCorreo();
                } else {

                    //Toast.makeText(getApplicationContext(), "Respuesta server =  " + response, Toast.LENGTH_LONG).show();
                    Log.i("********RESULTADO", "Respuesta server" + response);
                    //guardarNombre(response);
                    //guardarCredenciales(dato);
                    dialogoCargando.hide();
                    Bundle miBundle = new Bundle();
                    miBundle.putString("usuario", dato);
                    miBundle.putString("codigo", response);
                    Intent intent = new Intent(Registro.this, IngresaCodigoRegistro.class);
                    intent.putExtra("bundle", miBundle);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se pudo Registrar" + error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "NO SE REGISTRA" + error, Toast.LENGTH_LONG).show();
                Log.i("RESULTADO", "NO SE REGISTRA desde onError " + error.toString());
                Log.d("RESULT*****************", "NO SE REGISTRA desde onError " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String correo = dato;
                Map<String, String> parametros = new HashMap<>();
                parametros.put("correo", correo);
                Log.i("--------PARAMETROS ", parametros.toString());
                return parametros;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.add(stringRequest);
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
        arrayMeses.add("ENE");//31
        arrayMeses.add("FEB");//29
        arrayMeses.add("MAR");//31
        arrayMeses.add("ABR");//30
        arrayMeses.add("MAY");//31
        arrayMeses.add("JUN");//30
        arrayMeses.add("JUL");//31
        arrayMeses.add("AGO");//31
        arrayMeses.add("SEP");//30
        arrayMeses.add("OCT");//31
        arrayMeses.add("NOV");//30
        arrayMeses.add("DIC");//31
    }

    private void llenarDias(){
        arrayDias = new ArrayList();
        arrayDias.add("DIA");
        for (int i = 1; i <=31 ; i++) {
            arrayDias.add(i);
        }
    }

    private void cargarDialogoFecha() {
/*        final Calendar calendar = Calendar.getInstance();
        dia  = calendar.get(Calendar.DAY_OF_MONTH);
        mes  = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                campoFecha.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                fecha = ""+year+month+dayOfMonth;
                if (fecha != null) {
                    seleccionaFecha = true;
                }else {
                    seleccionaFecha = false;
                }
            }
        },dia,mes,anio);
        datePickerDialog.show();*/



        Button cancelar, enviar;
        dialogoFecha.setContentView(R.layout.popup_fecha);

        lisdaAnios = dialogoFecha.findViewById(R.id.spinnerAnio);
        listaMeses = dialogoFecha.findViewById(R.id.spinnerMes);
        listaDias = dialogoFecha.findViewById(R.id.spinnerDia);

        ArrayAdapter<CharSequence> adapterAnios = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arrayAnios);
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

        ArrayAdapter<CharSequence> adapterMeses = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arrayMeses);
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

        ArrayAdapter<CharSequence> adapterDias = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, arrayDias);
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
            }
        });

        enviar = dialogoFecha.findViewById(R.id.btnAceptarFecha);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccionaAnio == false || seleccionaMes == false || seleccionaDia == false){
                    seleccionaFecha = false;
                    campoFecha.setText("000-00-00");
                }else {
                    seleccionaFecha = true;
                    campoFecha.setText(anioLista+"-"+mesLista+"-"+diaLista);
                }


                dialogoFecha.dismiss();
            }
        });

        dialogoFecha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoFecha.show();

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

    private void consultarCredenciales() {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String credenciales = preferences.getString("correo", "No existe el valor");
    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    abrirCamara();
                } else {
                    if (opciones[i].equals("Elegir de Galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                        //seleccionaImagen = false;
                    }
                }
            }
        });
        builder.show();
    }

/*    private void opcionesCapturaFoto() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    seleccionaImagen = true;
                    abrirCamara();
                } else {
                    if (opciones[i].equals("Elegir de Galeria")) {
                        seleccionaImagen = true;
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                        seleccionaImagen = false;
                    }
                }
            }
        });

        if (permisoCamara == true) {
            builder.show();
        }

    }*/

    private void abrirCamara() {

        try {
            seleccionaImagen = true;
            File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
            boolean isCreada = miFile.exists();
            if (isCreada == false) {
                isCreada = miFile.mkdirs();//por si la variable no fue creada, se crea de nuevo
            }
            if (isCreada == true) {
                Long consecutivo = System.currentTimeMillis() / 1000;//aqui iba un 100, por si no funciona el codigo este es el error
                String nombre = consecutivo.toString() + ".jpg";

                path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN
                        + File.separator + nombre;//indicamos la ruta de almacenamiento

                fileImagen = new File(path);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

                ////
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String authorities = this.getPackageName() + ".provider";
                    Uri imageUri = FileProvider.getUriForFile(this, authorities, fileImagen);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
                }
                startActivityForResult(intent, COD_FOTO);
            }
        } catch (Exception e) {
            Toast.makeText(this, "No se puede abrir la camara, intente mas tarde", Toast.LENGTH_SHORT).show();
            seleccionaImagen = true;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        seleccionaImagen = true;
        try {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    imagenCamara.setImageURI(miPath);


                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                        bitmap = redimensionarImagen(bitmap, 200, 200);
                        imagenCamara.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Path", "" + path);
                                }
                            });

                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = redimensionarImagen(bitmap, 200, 200);
                    imagenCamara.setImageBitmap(bitmap);

                    break;
            }
            bitmap = redimensionarImagen(bitmap, 200, 200);
        } catch (Exception e) {
            seleccionaImagen = false;
            Toast.makeText(getApplicationContext(), "No se ha elegido ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        seleccionaImagen = true;
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


    //permisos
    ////////////////

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptadosss
        if ((getApplicationContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && getApplicationContext().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) || (shouldShowRequestPermissionRationale(CAMERA)))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }
        permisoCamara = false;
        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIS_PERMISOS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {//el dos representa los 2 permisos
                permisoCamara = true;
                Toast.makeText(getApplicationContext(), "Permisos aceptados", Toast.LENGTH_SHORT);
            }
        } else {
            solicitarPermisosManual();
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final android.support.v7.app.AlertDialog.Builder alertOpciones = new android.support.v7.app.AlertDialog.Builder(this);//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    permisoCamara = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    permisoCamara = false;
                }
            }
        });
        try{
            alertOpciones.show();
        }catch (Exception e){

        }

    }

    private void cargarDialogoRecomendacion() {
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        try {
            dialogo.show();
        } catch (Exception e) {

        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void cargarListaMunicipios() {
        String url = getApplicationContext().getString(R.string.ipTraerMunicipio) + posicion;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
        accion = 1;
    }

    private void registrarUsuarios() {
        dialogoCargando();
        String url;
        java.lang.System.setProperty("https.protocols", "TLSv1");
        url = getApplicationContext().getString(R.string.ipRegistro1);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.equals("com.android.volley.TimeoutError")){
                    dialogoCargando.hide();
                    Toast.makeText(Registro.this, "Por favor verificar la conexion a internet", Toast.LENGTH_SHORT).show();
                }

                if (response.trim().equalsIgnoreCase("registra")) {
                    Log.i("********RESULTADO", "Respuesta server" + response);
                    dialogoCargando.hide();
                    guardarCredenciales(campoCorreo.getText().toString());
                    guardarNombre(campoNombre.getText().toString());
                    Intent intent = new Intent(Registro.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else if (response.trim().equalsIgnoreCase("ya existe, datos no guardados")){
                    Toast.makeText(getApplicationContext(), "El usuario ya existe, por favor ingrese un correo distindo", Toast.LENGTH_LONG).show();
                    dialogoCargando.hide();
                }else {
                    Toast.makeText(getApplicationContext(), "Por el momento el usuario no se puede registrar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.equals("com.android.volley.TimeoutError")){
                    dialogoCargando.hide();
                    Toast.makeText(Registro.this, "Por favor verificar la conexion a internet", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "No se pudo Registrar" + error.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "NO SE REGISTRA" + error, Toast.LENGTH_LONG).show();
                Log.i("RESULTADO", "NO SE REGISTRA desde onError " + error.toString());
                Log.d("RESULT*****************", "NO SE REGISTRA desde onError " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombres = campoNombre.getText().toString();
                String apellidos = campoApellido.getText().toString();
                String generoo = genero;
                String correo = campoCorreo.getText().toString();
                String fechaNacimiento = campoFecha.getText().toString();
                String departamentoo = departamento;
                String municipioo = municipio;
                String rutaImagen = convertirImgString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombres", nombres);
                parametros.put("apellidos", apellidos);
                parametros.put("genero", generoo);
                parametros.put("correo", correo);
                parametros.put("fechaNacimiento", fechaNacimiento);
                parametros.put("departamento", departamentoo);
                parametros.put("municipio", municipioo);
                parametros.put("rutaImagen", rutaImagen);
                Log.i("--------PARAMETROS ", parametros.toString());
                return parametros;

            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.add(stringRequest);
        accion = (2);
    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }

    private void guardarCredenciales(String correo) {
        SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo", correo);
        editor.commit();
    }

    private void guardarNombre(String nombre) {
        SharedPreferences preferences = getSharedPreferences("Nombre", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombre", nombre);
        editor.commit();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (accion == 1) {
            Toast.makeText(getApplicationContext(), "Se produjo un error al cargar la lista de departamentos " + error.toString(), Toast.LENGTH_LONG).show();
        } else if (accion == 2) {
            Toast.makeText(getApplicationContext(), "No se pudo registrar " + error.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if (accion == 1) {
            JSONArray json = response.optJSONArray("usuario");
            JSONObject jsonObject = null;
            ArrayMunicipios = new ArrayList<String>();

            try {
                ArrayMunicipios.add("Seleccione su municipio");
                for (int i = 0; i < json.length(); i++) {
                    jsonObject = json.getJSONObject(i);
                    ArrayMunicipios.add(jsonObject.getString("municipio"));
                }
                ArrayAdapter<CharSequence> adapterMunicipios = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, ArrayMunicipios);
                listaMunicipios.setAdapter(adapterMunicipios);
                listaMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i != 0) {
                            municipio = ArrayMunicipios.get(i);
                            seleccionaMunicipio = true;
                        } else {
                            seleccionaMunicipio = false;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" + " " + response, Toast.LENGTH_LONG).show();

            }
        }
    }
}
