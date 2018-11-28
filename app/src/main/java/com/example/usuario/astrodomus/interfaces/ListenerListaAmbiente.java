package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.Ambiente;

public interface ListenerListaAmbiente {

    void estadoAmbiente(boolean estado,Ambiente ambiente);
    void apagarAmbiente(Ambiente ambiente);

}
