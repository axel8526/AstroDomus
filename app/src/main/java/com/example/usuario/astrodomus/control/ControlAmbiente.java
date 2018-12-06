package com.example.usuario.astrodomus.control;

import android.content.Context;
import android.widget.Toast;

import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.fragments.ControlFragment;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerAmbiente;
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

    //contructor solo para cambiar el estado del ambiente
    public ControlAmbiente(Context context){
        servicio=new ManagerRetrofit(context).getConsumoServicio();
        this.context=context;
    }

    public void usuarioAlojado(String idUser){
        Call<Ambiente> res=servicio.usuarioAlojado(idUser);

        res.enqueue(new Callback<Ambiente>() {
            @Override
            public void onResponse(Call<Ambiente> call, Response<Ambiente> response) {

                ((ListenerAmbiente)object).ambienteAlojado(response.body());

            }

            @Override
            public void onFailure(Call<Ambiente> call, Throwable t) {

                ((ListenerAmbiente)object).ambienteAlojado(null);

            }
        });
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

        Call<ResponseBody> res=servicio.cambiarEstadoAmbiente(ambiente.getIdAmbiente(),ambiente.getEstado(),ambiente.getIdUser());

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


                /*Se comenta por que el codigo ya es obsoleto, lo que hacia es vefiricar si la lista estata
                desactivada para volver a cargar el fragment desde home, pero como se coloco un hilo, que esta verificando
                . Ese mismo hilo se encarga de abrir la lista*/
                /*if(!lista){
                    try {
                       // ((ComunicaFragment) context).activarFragment(HomeActivity.FRAG_CONTROL);
                    }catch(Exception e){
                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    }
                }*/

                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }




}
