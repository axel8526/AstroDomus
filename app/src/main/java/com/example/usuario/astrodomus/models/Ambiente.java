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

    @SerializedName("id_ac")
    @Expose
    private String estado;

    @SerializedName("iduser")
    @Expose
    private String idUser;


    public Ambiente(String idAmbiente, String nombreAmbiente, String estado, String idUser) {
        this.idAmbiente = idAmbiente;
        this.nombreAmbiente = nombreAmbiente;
        this.estado = estado;
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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
