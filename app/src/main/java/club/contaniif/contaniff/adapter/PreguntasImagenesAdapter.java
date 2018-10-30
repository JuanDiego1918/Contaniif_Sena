package club.contaniif.contaniff.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;


import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.PreguntasVo;
import club.contaniif.contaniff.entidades.VolleySingleton;

public class PreguntasImagenesAdapter extends RecyclerView.Adapter<PreguntasImagenesAdapter.ImagenesHolder> implements View.OnClickListener, View.OnFocusChangeListener {

    private int selectedPosition = -1;
    private View.OnClickListener listener;
    private final ArrayList<PreguntasVo> listaImagenes;
    private final Context context;


    public PreguntasImagenesAdapter(ArrayList<PreguntasVo> listaImagenes, Context context) {
        this.listaImagenes = listaImagenes;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagenesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_preguntas_imagenes, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnClickListener(this);
        return new ImagenesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenesHolder holder, int position) {
        if (listaImagenes.get(position).getRutaImagen() != null) {
            cargarImagenWebService(listaImagenes.get(position).getRutaImagen(), holder);
        } else {
            Toast.makeText(context, "esta mal", Toast.LENGTH_SHORT).show();
        }
        if (selectedPosition == position) {
            holder.imagen.setBackgroundColor(Color.parseColor("#C4CDDA"));
        } else {
            holder.imagen.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void cargarImagenWebService(String rutaImagen, final ImagenesHolder holder) {
        String ip = context.getString(R.string.imgPreguntas);
        String urlImagen = "http://" + ip + rutaImagen;
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al cargar la imagen" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    public static class ImagenesHolder extends RecyclerView.ViewHolder {
        final ImageView imagen;

        ImagenesHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgPregunta);
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

}