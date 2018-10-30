package club.contaniif.contaniff.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.PreguntasVo;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.UsuariosHolder> implements View.OnClickListener, View.OnFocusChangeListener {

    private int selectedPosition = -1;

    private final ArrayList<PreguntasVo> listaUsuarios;
    private final String[] listadoLetras = {"A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. "};
    private View.OnClickListener listener;

    public PreguntasAdapter(ArrayList<PreguntasVo> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;

    }

    @NonNull
    @Override
    public UsuariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_usuarios_adapter, null, false);
        view.setOnClickListener(this);
        return new UsuariosHolder(view);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull UsuariosHolder viewHolder) {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosHolder holder, int position) {
        holder.respuesta.setText(listadoLetras[position] + listaUsuarios.get(position).getOpciones());

        if (selectedPosition == position) {
            holder.respuesta.setBackgroundColor(Color.parseColor("#C4CDDA"));
        } else {
            holder.respuesta.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    public class UsuariosHolder extends RecyclerView.ViewHolder {
        final TextView respuesta;
        UsuariosHolder(View itemView) {
            super(itemView);
            respuesta = itemView.findViewById(R.id.respuestaaaa);
        }

    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
