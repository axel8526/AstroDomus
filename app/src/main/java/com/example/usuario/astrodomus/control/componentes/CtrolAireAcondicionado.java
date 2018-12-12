package com.example.usuario.astrodomus.control.componentes;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.ControlPerfil;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.dialogs.atributos.AireAcondicionadoDialog;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Atributo;
import com.example.usuario.astrodomus.models.Componente;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CtrolAireAcondicionado {

    private int[] capacidad={17,30};
    private int estadoTemp=20;
    private ConsumoServicios servicio;
    private Context context;
    private AireAcondicionadoDialog aaDialog;

    public CtrolAireAcondicionado(Context context){
        this.context=context;
        servicio=new ManagerRetrofit(context).getConsumoServicio();
    }
    public void cambiarEstadoAtritutoComponente(Ambiente ambiente, Componente componente, String idAtributo, String estado){
        Call<ResponseBody> res=servicio.enviarEstadoAtributo(estado,
                ambiente.getIdAmbiente(),
                componente.getId_ubi(),idAtributo
        );

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }


        });
    }
    public void updateEstadoAtributoPerfil(Atributo atributo, String estado){
        String[] datos=atributo.getIdAtributo().split("_");
        String idEstados=datos[1];

        Call<ResponseBody> res=servicio.update_perfil(idEstados,estado);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public AireAcondicionadoDialog getDialogAire() {
        return aaDialog;
    }

    public void abrirDialog(){
        aaDialog=new AireAcondicionadoDialog(context);
        aaDialog.showDialog();
    }

    public void listenerBtonMas(final Ambiente ambiente, final Componente componente, final Atributo atributo){

        estadoTemp=Integer.parseInt(atributo.getEstadoAtributo()==null?estadoTemp+"":atributo.getEstadoAtributo());
        estadoTemp=estadoTemp==1?17:estadoTemp;

        aaDialog.setTextTemperatura(estadoTemp+"");

        aaDialog.getBtonMas().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuevoEstado=estadoTemp+1;
                if(nuevoEstado<=capacidad[1]){
                    estadoTemp++;

                    aaDialog.setTextTemperatura(estadoTemp+"");

                    if(ambiente!=null) {
                        cambiarEstadoAtritutoComponente(ambiente, componente, atributo.getIdAtributo(), estadoTemp + "");
                    }else{
                        updateEstadoAtributoPerfil(atributo, estadoTemp+"");
                    }
                }
            }
        });
    }
    public void listenerBtonMenos(final Ambiente ambiente, final Componente componente, final Atributo atributo){
        aaDialog.getBtonMenos().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuevoEstado=estadoTemp-1;
                if(nuevoEstado>=capacidad[0]){
                    estadoTemp--;
                    aaDialog.setTextTemperatura(estadoTemp+"");

                    if(ambiente!=null) {
                        cambiarEstadoAtritutoComponente(ambiente, componente, atributo.getIdAtributo(), estadoTemp + "");
                    }else{
                        updateEstadoAtributoPerfil(atributo,estadoTemp+"");
                    }
                }
            }
        });

    }

    public void listenerSwicth(final Ambiente ambiente, final Componente componente, final Atributo atributo){

        //si el dato no existe en la base de datos el estado llega nulo
        if(atributo.getEstadoAtributo()!=null)
        aaDialog.getSwitchPercianas().setChecked(atributo.getEstadoAtributo().equals("0")?true:false);



        aaDialog.getSwitchPercianas().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(ambiente!=null) {
                    cambiarEstadoAtritutoComponente(ambiente, componente, atributo.getIdAtributo(),
                            b ? "0" : "1");
                }else{
                    updateEstadoAtributoPerfil(atributo,b?"0":"1");
                }
            }
        });
    }
    public void listenerBotonListoControles(final ControlAmbiente controlAmbiente, final Ambiente ambiente){
        aaDialog.getBtonListo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlAmbiente.consultarDatosComponentes(ambiente);
                aaDialog.animarSalida();
            }
        });


    }
    public void listenerBotonListoPerfiles(final ControlPerfil ctrolPerfil, final String user, final String jornada){
        aaDialog.getBtonListo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctrolPerfil.cargarPerfiles(user,jornada);
                aaDialog.animarSalida();
            }
        });
    }





}
