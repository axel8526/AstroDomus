package com.example.usuario.astrodomus.control;

import android.content.Context;

import com.example.usuario.astrodomus.interfaces.ConsumoServicios;

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
}

