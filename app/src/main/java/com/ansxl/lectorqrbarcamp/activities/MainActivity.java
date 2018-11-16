package com.ansxl.lectorqrbarcamp.activities;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ansxl.lectorqrbarcamp.R;
import com.ansxl.lectorqrbarcamp.adapters.RegistroAdapter;
import com.ansxl.lectorqrbarcamp.entities.Registro;
import com.ansxl.lectorqrbarcamp.services.Service;
import com.ansxl.lectorqrbarcamp.services.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Registro> registros = new ArrayList<>();
    RecyclerView registrosRecycler;
    LinearLayoutManager linearLayoutManager;
    RegistroAdapter registroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayoutManager = new LinearLayoutManager(this);
        loadRegistros();
    }

    void loadRegistros(){
        Service service = ServiceGenerator.createService(Service.class);
        Call<List<Registro>> registros1 = service.getRegistros();
        if(registros1!=null){
            registros1.enqueue(new Callback<List<Registro>>() {
                @Override
                public void onResponse(Call<List<Registro>> call, Response<List<Registro>> response) {
                    if(response.code()==404){
                        Toast.makeText(getApplicationContext(), "404", Toast.LENGTH_LONG).show();
                    } else {
                        if(response.body()==null){
                            registros = new ArrayList<>();
                            Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_LONG).show();
                        }else{
                            registros = (ArrayList<Registro>) response.body();
                        }
                    }
                    registroAdapter = new RegistroAdapter(getApplicationContext(),registros);
                    registrosRecycler.invalidate();
                    registrosRecycler.setAdapter(null);
                    registrosRecycler.setLayoutManager(null);
                    registrosRecycler.setLayoutManager(linearLayoutManager);
                    registrosRecycler.setAdapter(registroAdapter);
                    registroAdapter.setOnRegistroClickListener(new RegistroAdapter.OnRegistroClickListener() {
                        @Override
                        public void OnRegistroClick(Registro registro) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
                            builder.setMessage("Desea confirmar al participante #" + registro.getId() + " "+registro.getNombre())
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            builder.show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Registro>> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else if(t instanceof Exception){
                        Toast.makeText(MainActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                }
            });
        }
    }
}
