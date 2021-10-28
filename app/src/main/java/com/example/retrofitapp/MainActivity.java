package com.example.retrofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.retrofitapp.Interfaces.UsuariosApi;
import com.example.retrofitapp.Interfaces.UsuariosApi;
import com.example.retrofitapp.models.Usuarios;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import  retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {

    ListView listapersonas;
    ArrayList<String> titulos = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    Integer id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obtenerListaPersona();

        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, titulos);
        listapersonas = (ListView) findViewById(R.id.listusers);
        listapersonas.setAdapter(arrayAdapter);
    }

    private void obtenerListaPersonas() {
        if(validarConexionRed()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);

            Call<List<Usuarios>> calllista = usuariosApi.getUsuarios();

            calllista.enqueue(new Callback<List<Usuarios>>() {
                @Override
                public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {
                    for (Usuarios usuarios : response.body()){
                        Log.i(usuarios.getTitle(), "onResponse");
                        titulos.add(usuarios.getTitle());

                        arrayAdapter.notifyDataSetChanged();

                    }

                }

                @Override
                public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                    t.getMessage();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "No hay conexion a internet", Toast.LENGTH_LONG).show();
        }


    }

    private void obtenerListaPersona() {
        if(validarConexionRed()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);

            Call<Usuarios> callPersona = usuariosApi.getUsuario(id);

            callPersona.enqueue(new Callback<Usuarios>() {
                @Override
                public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                    Usuarios persona = response.body();
                    Log.i(persona.getTitle(), "onResponse");
                    titulos.add(persona.getTitle());

                    arrayAdapter.notifyDataSetChanged();

                }


                @Override
                public void onFailure(Call<Usuarios> call, Throwable t) {
                    t.getMessage();
                }

            });
        }else{
            Toast.makeText(getApplicationContext(), "No hay conexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validarConexionRed() {
        ConnectivityManager administradorConectividad = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo informacionRed = administradorConectividad.getActiveNetworkInfo();
        return informacionRed != null;
    }
}