package com.example.usuario.astrodomus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rol {


    @SerializedName("codrol")
    @Expose
    private String codrol;

    @SerializedName("nomrol")
    @Expose
    private String nomrol;

    public Rol(String codrol, String nomrol) {
        this.codrol = codrol;
        this.nomrol = nomrol;
    }

    public String getCodrol() {
        return codrol;
    }

    public void setCodrol(String codrol) {
        this.codrol = codrol;
    }

    public String getNomrol() {
        return nomrol;
    }

    public void setNomrol(String nomrol) {
        this.nomrol = nomrol;
    }
}
