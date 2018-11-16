package com.ansxl.lectorqrbarcamp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.ansxl.lectorqrbarcamp.R;
import com.ansxl.lectorqrbarcamp.entities.Registro;

import java.util.ArrayList;

public class RegistroAdapter extends RecyclerView.Adapter<RegistroAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Registro> registros;
    private ArrayList<Registro> mFilteredList;
    private OnRegistroClickListener onRegistroClickListener;

    public RegistroAdapter(Context context, ArrayList<Registro> registros) {
        this.context = context;
        this.registros = registros;
        this.mFilteredList = registros;
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
        int i =0;
        try{
            i = mFilteredList.size();
        }catch (NullPointerException e){
            Toast.makeText(context, "Error interno, intentar mas tarde", Toast.LENGTH_LONG).show();
        }
        return i;
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

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase().trim();

                if (charString.isEmpty()) {
                    mFilteredList = registros;
                } else {

                    ArrayList<Registro> filteredList = new ArrayList<>();

                    for (Registro registro : registros) {

                        if ((registro.getNombre()!=null&&registro.getNombre().toLowerCase().contains(charSequence))){
                            filteredList.add(registro);
                        }
                    }

                    mFilteredList = new ArrayList<>(filteredList);
                }

                if ( mFilteredList == null )
                    mFilteredList = new ArrayList<>();

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                Log.e("FilterResults", mFilteredList+"");
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Registro>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }
}
