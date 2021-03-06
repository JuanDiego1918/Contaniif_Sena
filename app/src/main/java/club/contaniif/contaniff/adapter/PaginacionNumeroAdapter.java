package club.contaniif.contaniff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.NumeroVo;

public class PaginacionNumeroAdapter extends RecyclerView.Adapter<PaginacionNumeroAdapter.NumeroHolder> implements View.OnClickListener,View.OnTouchListener{

    private View.OnClickListener listener;
    private View.OnTouchListener touchListener;
    private final ArrayList<NumeroVo> listaNumero;

    public PaginacionNumeroAdapter(ArrayList<NumeroVo> listaNumero, Context context) {
        this.listaNumero = listaNumero;
        Context context1 = context;
    }

    @NonNull
    @Override
    public PaginacionNumeroAdapter.NumeroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_paginacion,null,false);
        view.setOnClickListener(this);
        view.setOnTouchListener(this);
        return new NumeroHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaginacionNumeroAdapter.NumeroHolder holder, final int position) {
        holder.numero.setText(String.valueOf(listaNumero.get(position).getNumeroPagina()));
        if (listaNumero.get(position).getColor()!=null){
            holder.numero.setTextColor(Color.parseColor(listaNumero.get(position).getColor()));
        }else{
            holder.numero.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return listaNumero.size();
    }
    public void setOnTouchListener(View.OnTouchListener touchListener){
        this.touchListener=touchListener;
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (touchListener!=null){
            touchListener.onTouch(v,event);
        }
        return false;
    }

    public class NumeroHolder extends RecyclerView.ViewHolder {
        final TextView numero;
        NumeroHolder(View itemView) {
            super(itemView);
            numero=itemView.findViewById(R.id.paginacion);

        }
    }
    public void setSelectedPosition() {
        //when item selected notify the adapter
        notifyDataSetChanged();
    }
}