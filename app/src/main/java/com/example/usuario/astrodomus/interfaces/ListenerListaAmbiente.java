package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.Ambiente;

import java.util.ArrayList;

public interface ListenerListaAmbiente {

    void estadoAmbiente(boolean estado,Ambiente ambiente);
    void onOffAmbiente(Ambiente ambiente, boolean lista);
    void iniciarAmbiente(Ambiente ambiente);
    void ambientes(ArrayList<Ambiente> ambientes);

}
