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
import com.example.usuario.astrodomus.interfaces.ListenerPerfil;
import com.example.usuario.astrodomus.models.Componente;
import com.example.usuario.astrodomus.models.ComponentePerfil;

import java.util.ArrayList;


public class AdapterComponentesPerfil extends RecyclerView.Adapter<HolderComponente> {

    private Context context;
    private ArrayList<ComponentePerfil> componentes;
    private Fragment fragment;

    private static final String LUCES_LED="luces led";
    private static final String AIRE_ACOM="aire acondicionado";
    private static final String VENTILADOR="ventilador";

    public AdapterComponentesPerfil(Context context, ArrayList<ComponentePerfil> componentes, Fragment fragment) {
        this.context = context;
        this.componentes = componentes;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public HolderComponente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.item_componente,parent,false);
        return new HolderComponente(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderComponente holder, int position) {

        ComponentePerfil componente=componentes.get(position);

        holder.getvNombre().setText(componente.getNombreComponente());
        holder.getvIcono().setImageResource(getIdDrawable(componente));

        listenerImage(holder.getvIcono(),componente);
        holder.getvSwitch().setChecked(componente.getAtributos().get(0).getEstadoAtributo().equals("0")?true:false);
        listenerSwicth(holder.getvSwitch(),componente, holder);

        cambiarColorEstado(componente.getAtributos().get(0).getEstadoAtributo().equals("0")?true:false,holder);
    }
    public int getIdDrawable(ComponentePerfil componente){
        String nombre=componente.getNombreComponente().toLowerCase();
        switch (nombre){
            case LUCES_LED:return R.drawable.icon_bombillo;
            case AIRE_ACOM:return R.drawable.icon_aire_acomd;
            default: return R.drawable.icon_ventilador;
        }
    }

    public void cambiarColorEstado(boolean estado, HolderComponente holder){
        if(estado){
            holder.getvIcono().setColorFilter(context.getResources().getColor(R.color.componente_estado_encendio));
            holder.getvNombre().setTextColor(context.getResources().getColor(R.color.componente_estado_encendio));
        }else{
            holder.getvIcono().setColorFilter(context.getResources().getColor(R.color.titulos_letras_componente));
            holder.getvNombre().setTextColor(context.getResources().getColor(R.color.titulos_letras_componente));
        }
    }

    public void listenerSwicth(Switch sw, final ComponentePerfil componente, final HolderComponente holder){
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((ListenerPerfil)fragment).componenteOnOff(componente,b);
                cambiarColorEstado(b,holder);


            }
        });

    }
    public void listenerImage(View v, final ComponentePerfil componente){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListenerPerfil)fragment).mostrarAtributos(componente);
            }
        });
    }

    @Override
    public int getItemCount() {
        return componentes.size();
    }
}
