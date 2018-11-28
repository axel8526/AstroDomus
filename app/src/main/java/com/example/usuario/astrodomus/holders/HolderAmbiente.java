package com.example.usuario.astrodomus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;

public class HolderAmbiente extends RecyclerView.ViewHolder {

    private TextView viewNombre, viewEstado;
    private ImageView btonPower;
    private Switch btonSwitch;
    private LinearLayout contenedor;

    public HolderAmbiente(View itemView) {
        super(itemView);
        viewNombre=itemView.findViewById(R.id.amb_nombre_ambiente);
        viewEstado=itemView.findViewById(R.id.amb_estado);
        btonSwitch=itemView.findViewById(R.id.amb_switch);
        btonPower=itemView.findViewById(R.id.amb_power);
        contenedor=itemView.findViewById(R.id.amb_contenedor_item);

    }

    public TextView getViewNombre() {
        return viewNombre;
    }

    public void setViewNombre(TextView viewNombre) {
        this.viewNombre = viewNombre;
    }

    public TextView getViewEstado() {
        return viewEstado;
    }

    public void setViewEstado(TextView viewEstado) {
        this.viewEstado = viewEstado;
    }

    public ImageView getBtonPower() {
        return btonPower;
    }

    public void setBtonPower(ImageView btonPower) {
        this.btonPower = btonPower;
    }

    public Switch getBtonSwitch() {
        return btonSwitch;
    }

    public void setBtonSwitch(Switch btonSwitch) {
        this.btonSwitch = btonSwitch;
    }

    public LinearLayout getContenedor() {
        return contenedor;
    }

    public void setContenedor(LinearLayout contenedor) {
        this.contenedor = contenedor;
    }
}
