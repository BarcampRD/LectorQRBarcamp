package com.ansxl.lectorqrbarcamp.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.ansxl.lectorqrbarcamp.R;
import com.ansxl.lectorqrbarcamp.adapters.RegistroAdapter;
import com.ansxl.lectorqrbarcamp.entities.Registro;
import com.ansxl.lectorqrbarcamp.services.Service;
import com.ansxl.lectorqrbarcamp.services.ServiceGenerator;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    final Service service = ServiceGenerator.createService(Service.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registrosRecycler = findViewById(R.id.registrosRecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        registrosRecycler.setLayoutManager(linearLayoutManager);
        loadRegistros();
    }

    void loadRegistros(){
        Call<List<Registro>> registros1 = service.getRegistros();
        if(registros1!=null){
            registros1.enqueue(new Callback<List<Registro>>() {
                @Override
                public void onResponse(@NonNull Call<List<Registro>> call, @NonNull Response<List<Registro>> response) {
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
                        public void OnRegistroClick(final Registro registro) {
                            android.support.v7.app.AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                            }
                            builder.setTitle("Confirmar participante");
                            builder.setMessage("Desea confirmar al participante #" + registro.getId() + " "+registro.getNombre())
                                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                           Call<String> stringCall = service.confirmar(String.valueOf(registro.getId()));

                                           if(stringCall!=null){
                                               stringCall.enqueue(new Callback<String>() {
                                                   @Override
                                                   public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                                       if(response.code()==404){
                                                           toastee("404");
                                                       } else {
                                                           if(response.body()==null){
                                                               toastee("empty");
                                                           }else{
                                                              toastee(""+response.body());
                                                               int position = getPosition(registro);
                                                               if(position!=-1){
                                                                   registros.remove(position);
                                                                   registroAdapter.notifyDataSetChanged();
                                                               }
                                                           }
                                                       }
                                                   }
                                                   @Override
                                                   public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                       toastee(t.toString());
                                                   }
                                               });
                                           }
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
                public void onFailure(@NonNull Call<List<Registro>> call, @NonNull Throwable t) {
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

    public int getPosition(Registro registro){
        for(int i=0; i<registros.size();i++){
            if (registro.getId()==registros.get(i).getId()){
                return i;
            }
        }
        return -1;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_camera:
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
                return true;
            case R.id.action_sync:
                loadRegistros();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void search(final SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try{
                    registroAdapter.getFilter().filter(newText);
                    return true;
                } catch (Exception ignored){
                }
                return true;
            }
        });
    }

    void toastee(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String res[] = scanResult.getContents().split(";");
            String id = res[0];
            toastee(id);
            Call<Registro> registroCall = service.consultar(id);
            if(registroCall!=null){
                registroCall.enqueue(new Callback<Registro>() {
                    @Override
                    public void onResponse(@NonNull Call<Registro> call, @NonNull Response<Registro> response) {
                        if(response.code()==404){
                            toastee("404");
                        } else if(response.code()==400){
                            toastee("Este participante no existe");
                        } else {
                            if(response.body()==null){
                                toastee("empty");
                            }else{
                                toastee(""+response.code());

                                final Registro registro=response.body();
                                android.support.v7.app.AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog);
                                } else {
                                    builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                                }
                                builder.setTitle("Confirmar participante");
                                builder.setMessage("Desea confirmar al participante #" + registro.getId() + " "+registro.getNombre())
                                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Call<String> stringCall = service.confirmar(String.valueOf(registro.getId()));
                                                if(stringCall!=null){
                                                    stringCall.enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                                            if(response.code()==404){
                                                                toastee("404");
                                                            } else {
                                                                if(response.body()==null){
                                                                    toastee("empty");
                                                                }else{
                                                                    toastee(""+response.body());
                                                                    int position = getPosition(registro);
                                                                    if(position!=-1){
                                                                        registros.remove(position);
                                                                        registroAdapter.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                            toastee(t.toString());
                                                        }
                                                    });
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User cancelled the dialog
                                            }
                                        });
                                builder.show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Registro> call, @NonNull Throwable t) {
                        toastee(t.toString());
                    }
                });
            }

        }
    }

}
