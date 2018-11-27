package com.example.usuario.astrodomus.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagerRetrofit {


    public static final String KEY_IP="IP";
    private static Retrofit retrofit=null;
    private String ip_pc;

    private Context context;



    public ManagerRetrofit(Context context){

        this.context=context;
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(context);
        ip_pc=datos.getString(KEY_IP,"10.103.68.103");

    }

    public ConsumoServicios getConsumoServicio(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl("http://"+ip_pc+"/astrodomus/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }

        return retrofit.create(ConsumoServicios.class);
    }


}
