package club.contaniif.contaniff.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class AdapterActivos extends RecyclerView.Adapter<AdapterActivos.ActivosHolderView>{
    @NonNull
    @Override
    public ActivosHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivosHolderView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ActivosHolderView extends RecyclerView.ViewHolder {
        public ActivosHolderView(View itemView) {
            super(itemView);
        }
    }
}
