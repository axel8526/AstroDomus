package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.Ambiente;

public interface ListenerListaAmbiente {

    void estadoAmbiente(boolean estado,Ambiente ambiente);
    void onOffAmbiente(Ambiente ambiente, String estadoAc);
    void iniciarAmbiente(Ambiente ambiente);

}
