package com.example.usuario.astrodomus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;

public class HolderComponente extends RecyclerView.ViewHolder {

    private TextView vNombre;
    private ImageView vIcono;
    private Switch vSwitch;
    private LinearLayout contenedor;


    public HolderComponente(View itemView) {
        super(itemView);
        vNombre=itemView.findViewById(R.id.comp_nombre_componente);
        vSwitch=itemView.findViewById(R.id.comp_switch);
        vIcono=itemView.findViewById(R.id.comp_icono);
        contenedor=itemView.findViewById(R.id.comp_contenedor_item);

    }

    public LinearLayout getContenedor() {
        return contenedor;
    }

    public void setContenedor(LinearLayout contenedor) {
        this.contenedor = contenedor;
    }

    public ImageView getvIcono() {
        return vIcono;
    }

    public void setvIcono(ImageView vIcono) {
        this.vIcono = vIcono;
    }

    public TextView getvNombre() {
        return vNombre;
    }

    public void setvNombre(TextView vNombre) {
        this.vNombre = vNombre;
    }

    public Switch getvSwitch() {
        return vSwitch;
    }

    public void setvSwitch(Switch vSwitch) {
        this.vSwitch = vSwitch;
    }
}
