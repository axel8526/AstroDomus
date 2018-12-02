package com.example.usuario.astrodomus.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.constantes.JornadasId;
import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.fragments.ControlFragment;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.utils.AnimDialog;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EscogePerfilDialog extends Dialog {

    private RelativeLayout contenedor;
    private ImageView btonMa,btonTarde,btonNoche;
    private AnimDialog animDialog;
    private Context context;
    private ConsumoServicios servicio;
    private TextView btonContinuar;


    public EscogePerfilDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        setContentView(R.layout.dialog_escojer_jornada_perfil);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews();
        animDialog=new AnimDialog(context,this);
        servicio =new ManagerRetrofit(context).getConsumoServicio();
    }
    private void findViews(){
        contenedor=findViewById(R.id.ll_dg_contenedor);
        btonMa=findViewById(R.id.dg_perfiles_bton_ma);
        btonNoche=findViewById(R.id.dg_perfiles_bton_noche);
        btonTarde=findViewById(R.id.dg_perfiles_bton_tarde);
        btonContinuar=findViewById(R.id.dg_perfiles_bton_continuar);
    }

    public void showDialog(){
        show();
        animDialog.animarEntrada(contenedor);
    }
    public void cerrar(){
        animDialog.animarSalida(contenedor);
    }

    public void cargarPerfil(final ControlAmbiente ctrolAmbiente, final String idUser, final Ambiente ambiente){
        btonMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargar(ctrolAmbiente,idUser,ambiente, JornadasId.MANANA);
                cerrar();
            }
        });
        btonTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargar(ctrolAmbiente,idUser,ambiente,JornadasId.TARDE);
                cerrar();
            }
        });
        btonNoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargar(ctrolAmbiente,idUser,ambiente,JornadasId.NOCHE);
                cerrar();
            }
        });

        listenerBtonContinuar(ctrolAmbiente,ambiente);

        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ctrolAmbiente.consultarDatosComponentes(ambiente);

            }
        });

    }

    private void cargar(final ControlAmbiente ctrolAmbiente, String idUser, final Ambiente ambiente, String idJornada){
        Call<ResponseBody> res=servicio.cargarPerfil(idUser,ambiente.getIdAmbiente(),idJornada);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ctrolAmbiente.consultarDatosComponentes(ambiente);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void listenerBtonContinuar(final ControlAmbiente controlAmbiente, final Ambiente ambiente){
        btonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlAmbiente.consultarDatosComponentes(ambiente);
                cerrar();
            }
        });
    }

}

