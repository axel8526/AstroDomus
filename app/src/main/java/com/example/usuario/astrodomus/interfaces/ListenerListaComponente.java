package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.Componente;

public interface ListenerListaComponente {

    void mostrarAtributos(Componente componente);
    void componenteOnOff(Componente componente, boolean power);
}
