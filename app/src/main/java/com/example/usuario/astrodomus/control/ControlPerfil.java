package com.example.usuario.astrodomus.control;

import android.content.Context;

import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerPerfil;
import com.example.usuario.astrodomus.models.ComponentePerfil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlPerfil {

    private ConsumoServicios servicio;
    private Context context;
    private String rol;
    private Object object;


    public ControlPerfil(Context context, Object clase,  String rol){
        servicio=new ManagerRetrofit(context).getConsumoServicio();
        this.context=context;
        this.rol=rol;
        this.object=clase;

    }

    public void cargarPerfiles(String iduser, String idJornada){

        Call<List<ComponentePerfil>> res=servicio.getComponentesPerfil(iduser,idJornada);

        res.enqueue(new Callback<List<ComponentePerfil>>() {
            @Override
            public void onResponse(Call<List<ComponentePerfil>> call, Response<List<ComponentePerfil>> response) {
                ((ListenerPerfil)object).componentes((ArrayList<ComponentePerfil>)response.body());
            }

            @Override
            public void onFailure(Call<List<ComponentePerfil>> call, Throwable t) {

            }
        });

    }

    public void actualizarComponente(String idEstados, String estado){
        Call<ResponseBody> res=servicio.update_perfil(idEstados,estado);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

