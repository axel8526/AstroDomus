package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.ComponentePerfil;

import java.util.ArrayList;

public interface ListenerPerfil {

    void componenteOnOff(ComponentePerfil componentePerfil, boolean estado);
    void mostrarAtributos(ComponentePerfil componentePerfil);
    void componentes(ArrayList<ComponentePerfil> componentes);
}
