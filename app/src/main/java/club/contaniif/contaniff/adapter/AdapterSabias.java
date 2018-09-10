package club.contaniif.contaniff.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.SabiasVo;

public class AdapterSabias extends RecyclerView.Adapter<AdapterSabias.SabiasHolderView> implements View.OnClickListener {

    ArrayList<SabiasVo> listaSabias;
    View.OnClickListener listener;

    public AdapterSabias(ArrayList<SabiasVo> listaSabias) {
        this.listaSabias = listaSabias;
    }

    @NonNull
    @Override
    public SabiasHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_sabias, null, false);
        view.setOnClickListener(this);
        return new SabiasHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SabiasHolderView holder, int position) {
        holder.descripcionCorta.setText(listaSabias.get(position).getDescripcionCorta());
    }

    @Override
    public int getItemCount() {
        return listaSabias.size();
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }

    public class SabiasHolderView extends RecyclerView.ViewHolder {
        TextView descripcionCorta;

        public SabiasHolderView(View itemView) {
            super(itemView);

            descripcionCorta = itemView.findViewById(R.id.descripcionSabias);
        }
    }
}
