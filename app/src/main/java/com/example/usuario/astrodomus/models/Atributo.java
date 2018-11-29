package com.example.usuario.astrodomus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Atributo {

    @SerializedName("id_atributo")
    @Expose
    private String idAtributo;

    @SerializedName("nombre_atributo")
    @Expose
    private String nombreAtributo;


    public Atributo(String idAtributo, String nombreAtributo) {
        this.idAtributo = idAtributo;
        this.nombreAtributo = nombreAtributo;
    }

    public String getIdAtributo() {
        return idAtributo;
    }

    public void setIdAtributo(String idAtributo) {
        this.idAtributo = idAtributo;
    }

    public String getNombreAtributo() {
        return nombreAtributo;
    }

    public void setNombreAtributo(String nombreAtributo) {
        this.nombreAtributo = nombreAtributo;
    }
}
