package com.example.usuario.astrodomus.control;

import android.content.Context;

import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerListaAmbiente;
import com.example.usuario.astrodomus.interfaces.ListenerListaComponente;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Componente;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlAmbiente {


    private ConsumoServicios servicio;
    private Context context;
    private String rol;
    private Object object;
    private Ambiente ambiente;


    public ControlAmbiente(Context context, Object clase,  String rol){
        servicio=new ManagerRetrofit(context).getConsumoServicio();
        this.context=context;
        this.rol=rol;
        this.object=clase;

    }


    public void consultarDatosAmbiente(){


        Call<List<Ambiente>> res=servicio.getAmbientes(rol);

        res.enqueue(new Callback<List<Ambiente>>() {
            @Override
            public void onResponse(Call<List<Ambiente>> call, Response<List<Ambiente>> response) {
                ((ListenerListaAmbiente)object).ambientes((ArrayList<Ambiente>)response.body());
            }

            @Override
            public void onFailure(Call<List<Ambiente>> call, Throwable t) {

            }
        });
    }

    public void consultarDatosComponentes(Ambiente ambiente){
        ConsumoServicios servicio=new ManagerRetrofit(context).getConsumoServicio();
        Call<List<Componente>> res=servicio.getComponentes(ambiente.getIdAmbiente());

        res.enqueue(new Callback<List<Componente>>() {
            @Override
            public void onResponse(Call<List<Componente>> call, Response<List<Componente>> response) {

                ((ListenerListaComponente)object).componentes((ArrayList)response.body());
            }

            @Override
            public void onFailure(Call<List<Componente>> call, Throwable t) {

            }
        });
    }

    public void cambiarEstadoAmbiente(final Ambiente ambiente, final boolean lista){
        ConsumoServicios servicio=new ManagerRetrofit(context).getConsumoServicio();

        Call<ResponseBody> res=servicio.cambiarEstadoAmbiente(ambiente.getIdAmbiente(),ambiente.getEstado());

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(lista){
                    consultarDatosAmbiente();
                    apagarComponentesAmbiente(ambiente,true);

                }else{
                    apagarComponentesAmbiente(ambiente,false);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void changeStateAtriComp(String estado, final Ambiente ambiente, String idComponente, String idAtributo){
        ConsumoServicios servicio=new ManagerRetrofit(context).getConsumoServicio();

        Call<ResponseBody> res=servicio.enviarEstadoAtributo(estado,ambiente.getIdAmbiente(),idComponente,idAtributo);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                consultarDatosComponentes(ambiente);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void apagarComponentesAmbiente(Ambiente ambiente, final boolean lista){

        Call<ResponseBody> res=servicio.apagarAmbiente(ambiente.getIdAmbiente());

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(!lista){
                    ((ComunicaFragment)context).activarFragment(HomeActivity.FRAG_CONTROL);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }




}
