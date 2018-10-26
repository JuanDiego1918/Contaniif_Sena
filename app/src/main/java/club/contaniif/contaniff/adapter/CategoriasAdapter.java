package club.contaniif.contaniff.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.CategoriasVo;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaHolder> implements  View.OnClickListener {

    private View.OnClickListener listener;
    private final ArrayList<CategoriasVo> listaCategorias;

    public CategoriasAdapter(ArrayList<CategoriasVo> listaCategorias) {
        this.listaCategorias = listaCategorias;

    }


    @NonNull
    @Override
    public CategoriaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_categorias_adapter,null,false);
        view.setOnClickListener(this);
        return new CategoriaHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriaHolder holder, int position) {
        holder.palabra.setText(listaCategorias.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
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

    public class CategoriaHolder extends RecyclerView.ViewHolder {
        final TextView palabra;
        CategoriaHolder(View itemView) {
            super(itemView);
            palabra=itemView.findViewById(R.id.palabra);
        }
    }
    public void setSelectedPosition() {
        //when item selected notify the adapter
        notifyDataSetChanged();
    }
}
