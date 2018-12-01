package com.example.usuario.astrodomus.control.componentes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.SeekBar;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.dialogs.atributos.LuzLedDialog;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Atributo;
import com.example.usuario.astrodomus.models.Componente;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CtrolLuzLed {

    private ConsumoServicios servicio;
    private Context context;
    private LuzLedDialog luzLedDialog;

    public CtrolLuzLed (Context context){
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

    public void abrirDialog(){
        luzLedDialog=new LuzLedDialog(context);

        luzLedDialog.showDialog();
    }

    public void listenerSeekbar(final Ambiente ambiente, final Componente componente, final Atributo atributo){

        luzLedDialog.getSeekBar().setProgress(Integer.parseInt(atributo.getEstadoAtributo()));
        luzLedDialog.cargarPorcentaje(atributo.getEstadoAtributo());
        luzLedDialog.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(ambiente!=null) {
                    cambiarEstadoAtritutoComponente(ambiente, componente, atributo.getIdAtributo(), i + "");
                }else{
                    //codigo perfiles
                }
                luzLedDialog.cargarPorcentaje(i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void listenerBotonListoControles(final ControlAmbiente controlAmbiente, final Ambiente ambiente){
        luzLedDialog.getBtonListo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlAmbiente.consultarDatosComponentes(ambiente);
                luzLedDialog.animarSalida();
            }
        });


    }
    public void listenerBotonListoPerfiles(){
        luzLedDialog.getBtonListo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


}
