package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Componente;

import java.util.ArrayList;

public interface ListenerListaComponente {

    void mostrarAtributos(Componente componente);
    void componenteOnOff(Componente componente, boolean power);
    void componentes(ArrayList<Componente> componentes);
}
