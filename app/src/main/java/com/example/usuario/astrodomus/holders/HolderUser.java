package com.example.usuario.astrodomus.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;

public class HolderUser extends RecyclerView.ViewHolder {

    private TextView nombreUser, idUser, rolUser;
    private ImageView botonElinimar;
    private LinearLayout contenedor;

    public HolderUser(View itemView) {
        super(itemView);
        nombreUser=itemView.findViewById(R.id.h_nombre_user);
        idUser=itemView.findViewById(R.id.h_id_user);
        botonElinimar=itemView.findViewById(R.id.h_bton_eliminar);
        rolUser=itemView.findViewById(R.id.h_rol_user);
        contenedor=itemView.findViewById(R.id.h_contenedor_item);
    }

    public LinearLayout getContenedor() {
        return contenedor;
    }

    public void setContenedor(LinearLayout contenedor) {
        this.contenedor = contenedor;
    }

    public TextView getRolUser() {
        return rolUser;
    }

    public void setRolUser(TextView rolUser) {
        this.rolUser = rolUser;
    }

    public TextView getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(TextView nombreUser) {
        this.nombreUser = nombreUser;
    }

    public TextView getIdUser() {
        return idUser;
    }

    public void setIdUser(TextView idUser) {
        this.idUser = idUser;
    }

    public ImageView getBotonElinimar() {
        return botonElinimar;
    }

    public void setBotonElinimar(ImageView botonElinimar) {
        this.botonElinimar = botonElinimar;
    }
}
