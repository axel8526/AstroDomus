package com.example.usuario.astrodomus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ambiente {


    @SerializedName("id_ambiente")
    @Expose
    private String idAmbiente;

    @SerializedName("nombre")
    @Expose
    private String nombreAmbiente;

    @SerializedName("estado")
    @Expose
    private String estado;


    public Ambiente(String idAmbiente, String nombreAmbiente, String estado) {
        this.idAmbiente = idAmbiente;
        this.nombreAmbiente = nombreAmbiente;
        this.estado = estado;
    }



    public String getIdAmbiente() {
        return idAmbiente;
    }

    public void setIdAmbiente(String idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public String getNombreAmbiente() {
        return nombreAmbiente;
    }

    public void setNombreAmbiente(String nombreAmbiente) {
        this.nombreAmbiente = nombreAmbiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
