package com.example.usuario.astrodomus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("nom1")
    @Expose
    private String nombre1;

    @SerializedName("nom2")
    @Expose
    private String nombre2;

    @SerializedName("apll1")
    @Expose
    private String apellido1;

    @SerializedName("apll2")
    @Expose
    private String apellido2;

    @SerializedName("clave")
    @Expose
    private String clave;

    @SerializedName("iduser")
    @Expose
    private String id;

    @SerializedName("nomrol")
    @Expose
    private String rol;

    @SerializedName("correo")
    @Expose
    private String correo;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("movil")
    @Expose
    private String movil;

    public Usuario(){

    }

    public Usuario(String nombre1, String nombre2, String apellido1, String apellido2, String clave, String id, String rol, String correo, String direccion, String movil) {
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.clave = clave;
        this.id = id;
        this.rol = rol;
        this.correo = correo;
        this.direccion = direccion;
        this.movil = movil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
