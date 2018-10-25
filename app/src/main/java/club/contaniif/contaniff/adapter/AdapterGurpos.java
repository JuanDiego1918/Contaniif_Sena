package club.contaniif.contaniff.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.GruposVo;

public class AdapterGurpos extends RecyclerView.Adapter<AdapterGurpos.GruposHolder> /*implements View.OnClickListener*/ {

    private final ArrayList<GruposVo>listaGrupos;
    //View.OnClickListener listener;

    public AdapterGurpos(ArrayList<GruposVo> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    @NonNull
    @Override
    public GruposHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_grupos,null,false);
        //view.setOnClickListener(this);
        return new GruposHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GruposHolder holder, int position) {
        holder.campoGrupo.setText(listaGrupos.get(position).getGrupo());
    }

    @Override
    public int getItemCount() {
        return listaGrupos.size();
    }
/*
    @Override
    public void onClick(View v) {
        *//*if (listener != null){
            listener.onClick(v);
        }*//*
    }*/

    public class GruposHolder extends RecyclerView.ViewHolder {
        final TextView campoGrupo;
        GruposHolder(View itemView) {
            super(itemView);
            campoGrupo = itemView.findViewById(R.id.campoGrupoModelo);
        }
    }
}
