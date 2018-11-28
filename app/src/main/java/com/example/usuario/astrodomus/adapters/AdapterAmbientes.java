package com.example.usuario.astrodomus.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.dialogs.ConfirmarAccionDialog;
import com.example.usuario.astrodomus.holders.HolderAmbiente;
import com.example.usuario.astrodomus.interfaces.ListenerListaAmbiente;
import com.example.usuario.astrodomus.models.Ambiente;

import java.util.ArrayList;

public class AdapterAmbientes extends RecyclerView.Adapter<HolderAmbiente> {


    private Context context;
    private ArrayList<Ambiente> ambientes;
    private String rol;
    private Fragment fragment;

    public AdapterAmbientes(Context context, ArrayList<Ambiente> ambientes, String rol, Fragment fragment) {
        this.context = context;
        this.ambientes = ambientes;
        this.rol=rol;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public HolderAmbiente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_ambiente,parent,false);
        return new HolderAmbiente(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final HolderAmbiente holder, int position) {
        final Ambiente ambiente=ambientes.get(position);

        holder.getViewNombre().setText(ambiente.getNombreAmbiente());
        holder.getViewEstado().setText(ambiente.getEstado());


        if(ambiente.getEstado().equalsIgnoreCase("defectuoso")){
            holder.getBtonSwitch().setChecked(false);
            holder.getViewEstado().setTextColor(context.getResources().getColor(R.color.rojo1));
            holder.getBtonPower().setVisibility(View.INVISIBLE);
        }else if(ambiente.getEstado().equalsIgnoreCase("disponible")){
            holder.getBtonSwitch().setChecked(true);
            holder.getViewEstado().setTextColor(context.getResources().getColor(R.color.gris));
            holder.getBtonSwitch().setEnabled(true);
            holder.getBtonPower().setVisibility(View.INVISIBLE);

        }else{
            holder.getBtonSwitch().setChecked(true);
            holder.getViewEstado().setTextColor(context.getResources().getColor(R.color.gris));
            holder.getBtonSwitch().setEnabled(false);
            holder.getBtonPower().setVisibility(View.VISIBLE);
        }



        if(!rol.equalsIgnoreCase("ADMINISTRADOR")) {
            holder.getBtonPower().setVisibility(View.INVISIBLE);
            holder.getBtonSwitch().setVisibility(View.INVISIBLE);

            holder.getBtonSwitch().setEnabled(false);
            holder.getViewEstado().setHeight(0);

        }else{

            if(ambiente.getEstado().equalsIgnoreCase("ocupado")) {


                holder.getBtonPower().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final ConfirmarAccionDialog dialogConfirmar = new ConfirmarAccionDialog(
                                context,
                                "¿Apagar ambiente?",
                                "se apagaran todos los componentes en el ambiente"
                        );
                        dialogConfirmar.abrirDialog();

                        dialogConfirmar.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((ListenerListaAmbiente) fragment).apagarAmbiente(ambiente);
                                dialogConfirmar.cerrarDialog();
                            }
                        });

                        dialogConfirmar.cerrarDialog2();


                    }
                });
            }


            holder.getBtonSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    final boolean estado=b;
                    if(!estado) {
                        final ConfirmarAccionDialog dialogConfirmar = new ConfirmarAccionDialog(context,
                                "¿Ambiente defectuoso?",
                                "Se pondra el estado del ambiente en defectuoso");
                        dialogConfirmar.abrirDialog();
                        dialogConfirmar.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((ListenerListaAmbiente) fragment).estadoAmbiente(estado, ambiente);
                                dialogConfirmar.cerrarDialog();
                            }
                        });

                        dialogConfirmar.getBtonCancelar().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                holder.getBtonSwitch().setChecked(true);
                                dialogConfirmar.cerrarDialog();
                            }
                        });
                    }else{
                        ((ListenerListaAmbiente) fragment).estadoAmbiente(estado, ambiente);
                    }
                }
            });
        }



    }



    @Override
    public int getItemCount() {
        return ambientes.size();
    }
}
