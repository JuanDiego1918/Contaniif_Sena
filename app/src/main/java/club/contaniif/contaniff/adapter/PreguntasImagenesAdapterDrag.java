package club.contaniif.contaniff.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

public class PreguntasImagenesAdapterDrag extends RecyclerView.Adapter<PreguntasImagenesAdapterDrag.ImagenesHolder> implements View.OnLongClickListener,View.OnClickListener {

    private final ArrayList<PreguntasVo> listaImagenes;
    private final Context context;
    private View.OnClickListener listener;
    private View.OnLongClickListener onLongClickListener;


    public PreguntasImagenesAdapterDrag(ArrayList<PreguntasVo> listaImagenes, Context context) {
        this.listaImagenes = listaImagenes;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagenesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_imagen, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnLongClickListener(this);
        return new ImagenesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenesHolder holder, int position) {
        if (listaImagenes.get(position).isMostrar()){
            if (listaImagenes.get(position).getRuta() != null) {
                cargarImagenWebService(listaImagenes.get(position).getRuta(), holder);
            } else {
                holder.imagen.setImageResource(R.drawable.punteadoc);
            }
        }else{
            holder.imagen.setImageResource(R.drawable.punteadoc);
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

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public  void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public boolean onLongClick(View v) {
        if (onLongClickListener != null) {
            onLongClickListener.onLongClick(v);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }


    public class ImagenesHolder extends RecyclerView.ViewHolder {
        final ImageView imagen;

        ImagenesHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenesRespuestaRelacion);
        }
    }
}
