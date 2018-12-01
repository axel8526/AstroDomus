package com.example.usuario.astrodomus.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.holders.HolderComponente;
import com.example.usuario.astrodomus.interfaces.ListenerListaComponente;
import com.example.usuario.astrodomus.models.Componente;

import java.util.ArrayList;

public class AdapterComponentes extends RecyclerView.Adapter<HolderComponente> {


    private Context context;
    private ArrayList<Componente> componentes;
    private Fragment fragment;

    private static final String LUCES_LED="luces led";
    private static final String AIRE_ACOM="aire acondicionado";
    private static final String VENTILADOR="ventilador";

    public AdapterComponentes(Context context, ArrayList<Componente> componentes, Fragment fragment){
        this.context=context;
        this.componentes=componentes;
        this.fragment=fragment;

    }
    @NonNull
    @Override
    public HolderComponente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.item_componente,parent,false);
        return new HolderComponente(v);
    }



    @Override
    public void onBindViewHolder(@NonNull HolderComponente holder, int position) {
        Componente componente=componentes.get(position);

        holder.getvNombre().setText(componente.getNombreComponente());
        holder.getvIcono().setImageResource(getIdDrawable(componente));
        holder.getContenedor().setBackgroundResource(deshabilitarComponente(componente)?R.color.comp_habilitado:R.color.comp_deshabilitado);

        listenerImage(holder.getvIcono(),componente);
        holder.getvSwitch().setChecked(componente.getPower().equals("0")?true:false);
        listenerSwicth(holder.getvSwitch(),componente);



    }

    public int getIdDrawable(Componente componente){
        String nombre=componente.getNombreComponente().toLowerCase();
        switch (nombre){
            case LUCES_LED:return R.drawable.icon_bombillo;
            case AIRE_ACOM:return R.drawable.icon_aire_acomd;
            default: return R.drawable.icon_ventilador;
        }
    }
    public boolean deshabilitarComponente(Componente componente){
        if(componente.getEstado().equalsIgnoreCase(AdapterAmbientes.DISPONIBLE)){
            return true;
        }
        return false;
    }
    public void listenerSwicth(Switch sw, final Componente componente){
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((ListenerListaComponente)fragment).componenteOnOff(componente,b);


            }
        });

    }
    public void listenerImage(View v, final Componente componente){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListenerListaComponente)fragment).mostrarAtributos(componente);
            }
        });
    }


    @Override
    public int getItemCount() {
        return componentes.size();
    }
}
