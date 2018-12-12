package com.example.usuario.astrodomus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ComponentePerfil {

    @SerializedName("id_componente")
    @Expose
    private String id_componente;

    @SerializedName("nombre_comp")
    @Expose
    private String nombreComponente;

    @SerializedName("atributos")
    @Expose
    private ArrayList<Atributo> atributos;

    public ComponentePerfil(String id_componente, String nombreComponente, ArrayList<Atributo> atributos) {
        this.id_componente = id_componente;
        this.nombreComponente = nombreComponente;
        this.atributos = atributos;
    }

    public String getId_componente() {
        return id_componente;
    }

    public void setId_componente(String id_componente) {
        this.id_componente = id_componente;
    }

    public String getNombreComponente() {
        return nombreComponente;
    }

    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }
}
