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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.ActivosVo;
import club.contaniif.contaniff.entidades.VolleySingleton;

public class AdapterActivos extends RecyclerView.Adapter<AdapterActivos.ActivosHolderView> implements View.OnClickListener {

    private final ArrayList<ActivosVo> listaActivos;
    private View.OnClickListener listener;
    private final Context context;

    public AdapterActivos(ArrayList<ActivosVo> listaActivos, Context context) {
        this.listaActivos = listaActivos;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivosHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_activos, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnClickListener(this);
        return new ActivosHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivosHolderView holder, int position) {
        if (listaActivos.get(position).getId() != null) {
            cargarImagenWebService(listaActivos.get(position).getId(), holder);
        }
        holder.nombre.setText(listaActivos.get(position).getNombre());
        holder.precio.setText(listaActivos.get(position).getValor());
        if (listaActivos.get(position).getEstado().equals("Tiene")) {
            holder.contenido.setBackgroundColor(Color.parseColor("#fff9c342"));
            holder.descrip.setText(listaActivos.get(position).getDescripcion());
            holder.descrip.setVisibility(View.VISIBLE);
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(listaActivos.get(position).getDescuento());
            holder.siguienteActivo.setVisibility(View.INVISIBLE);
        } else {
            holder.contenido.setBackgroundColor(Color.parseColor("#ff00cc86"));
            holder.descrip.setVisibility(View.INVISIBLE);
            holder.desc.setVisibility(View.INVISIBLE);
            holder.siguienteActivo.setVisibility(View.VISIBLE);
        }
    }

    private void cargarImagenWebService(String rutaImagen, final ActivosHolderView holder) {
        String ip = context.getString(R.string.imgRendimiento);
        String urlImagen = "https://" + ip + "activos/" + rutaImagen + ".png";
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
        return listaActivos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ActivosHolderView extends RecyclerView.ViewHolder {
        final TextView nombre;
        final TextView precio;
        final TextView descrip;
        final TextView desc;
        final ImageView imagen;
        final ImageView siguienteActivo;
        final LinearLayout contenido;

        ActivosHolderView(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreActivo);
            precio = itemView.findViewById(R.id.precioActivo);
            imagen = itemView.findViewById(R.id.imagenActivos);
            contenido = itemView.findViewById(R.id.contenido);
            descrip = itemView.findViewById(R.id.descripActivo);
            desc = itemView.findViewById(R.id.descActivo);
            siguienteActivo=itemView.findViewById(R.id.siguienteActivo);
        }
    }
}
