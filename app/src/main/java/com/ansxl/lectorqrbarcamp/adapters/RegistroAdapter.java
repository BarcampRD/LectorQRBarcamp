package com.ansxl.lectorqrbarcamp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ansxl.lectorqrbarcamp.R;
import com.ansxl.lectorqrbarcamp.entities.Registro;

import java.util.ArrayList;

public class RegistroAdapter extends RecyclerView.Adapter<RegistroAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Registro> registros;
    private OnRegistroClickListener onRegistroClickListener;

    public RegistroAdapter(Context context, ArrayList<Registro> registros) {
        this.context = context;
        this.registros = registros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.registros_list_item, parent, false);
        return new RegistroAdapter.ViewHolder(view);
    }

    public interface OnRegistroClickListener{
        void OnRegistroClick(Registro registro);
    }

    public void setOnRegistroClickListener(OnRegistroClickListener onRegistroClickListener) {
        this.onRegistroClickListener = onRegistroClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Registro registro = registros.get(position);
        holder.size.setText("Size: "+registro.getSize());
        holder.cedula.setText(registro.getCedula());
        holder.nombre.setText(registro.getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegistroClickListener.OnRegistroClick(registro);
            }
        });
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, cedula, size;
        ViewHolder(View itemView){
            super(itemView);
            nombre = itemView.findViewById(R.id.registro_nombre);
            cedula = itemView.findViewById(R.id.registro_cedula);
            size = itemView.findViewById(R.id.registro_size);
        }
    }
}
