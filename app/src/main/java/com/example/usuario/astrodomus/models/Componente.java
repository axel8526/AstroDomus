package com.example.usuario.astrodomus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Componente {

    @SerializedName("id_componente")
    @Expose
    private String id_ubi;

    @SerializedName("nombre")
    @Expose
    private String nombreAmbiente;

    @SerializedName("nombre_comp")
    @Expose
    private String nombreComponente;

    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("power")
    @Expose
    private String power;


    public Componente(String id_ubi, String nombreAmbiente, String nombreComponente, String estado, String power) {
        this.id_ubi = id_ubi;
        this.nombreAmbiente = nombreAmbiente;
        this.nombreComponente = nombreComponente;
        this.estado = estado;
        this.power = power;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getId_ubi() {
        return id_ubi;
    }

    public void setId_ubi(String id_ubi) {
        this.id_ubi = id_ubi;
    }

    public String getNombreAmbiente() {
        return nombreAmbiente;
    }

    public void setNombreAmbiente(String nombreAmbiente) {
        this.nombreAmbiente = nombreAmbiente;
    }

    public String getNombreComponente() {
        return nombreComponente;
    }

    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
