package com.example.usuario.astrodomus.control.componentes;

import android.content.Context;
import android.view.View;

import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.dialogs.atributos.VentiladorDialog;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Atributo;
import com.example.usuario.astrodomus.models.Componente;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CtrolVentilador {

    private int[] capacidad = {1, 3};
    private int estadoVent = 1;
    private ConsumoServicios servicio;
    private Context context;
    private VentiladorDialog aaDialog;

    public CtrolVentilador(Context context) {
        this.context = context;
        servicio = new ManagerRetrofit(context).getConsumoServicio();
    }

    public void cambiarEstadoAtritutoComponente(Ambiente ambiente, Componente componente, String idAtributo, String estado) {
        Call<ResponseBody> res = servicio.enviarEstadoAtributo(estado,
                ambiente.getIdAmbiente(),
                componente.getId_ubi(), idAtributo
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

    public void abrirDialog() {
        aaDialog = new VentiladorDialog(context);
        aaDialog.showDialog();
    }

    public VentiladorDialog getAaDialog() {
        return aaDialog;
    }

    public void listenerBtonMas(final Ambiente ambiente, final Componente componente, final Atributo atributo) {

        estadoVent = Integer.parseInt(atributo.getEstadoAtributo() == null ? estadoVent + "" : atributo.getEstadoAtributo());
        aaDialog.setTextTemperatura(estadoVent + "");

        aaDialog.getBtonMas().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuevoEstado = estadoVent + 1;
                if (nuevoEstado <= capacidad[1]) {
                    estadoVent++;

                    aaDialog.setTextTemperatura(estadoVent + "");

                    cambiarEstadoAtritutoComponente(ambiente, componente, atributo.getIdAtributo(), estadoVent + "");
                }
            }
        });
    }

    public void listenerBtonMenos(final Ambiente ambiente, final Componente componente, final Atributo atributo) {
        aaDialog.getBtonMenos().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nuevoEstado = estadoVent - 1;
                if (nuevoEstado >= capacidad[0]) {
                    estadoVent--;
                    aaDialog.setTextTemperatura(estadoVent + "");

                    cambiarEstadoAtritutoComponente(ambiente, componente, atributo.getIdAtributo(), estadoVent + "");
                }
            }
        });

    }


    public void listenerBotonListoControles(final ControlAmbiente controlAmbiente, final Ambiente ambiente) {
        aaDialog.getBtonListo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlAmbiente.consultarDatosComponentes(ambiente);
                aaDialog.animarSalida();
            }
        });


    }

    public void cerrarDialog(){
        aaDialog.animarSalida();
    }
}
