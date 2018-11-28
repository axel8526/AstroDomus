package com.example.usuario.astrodomus.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.adapters.AdapterAmbientes;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerListaAmbiente;
import com.example.usuario.astrodomus.interfaces.ListenerListaUsuarios;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment implements ListenerListaAmbiente {


    private Dialog dgListaAmbientes;
    private String rol;

    public ControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_control, container, false);
        findViews(v);

        if(getArguments()!=null){
            rol=getArguments().getString(InicioSesionActivity.KEY_ROL);
        }

        if(getActivity()!=null){
            abrirListaAmbientes();
        }


        return v;
    }

    public void abrirListaAmbientes(){
        dgListaAmbientes=new Dialog(getActivity());
        dgListaAmbientes.setContentView(R.layout.dialog_mostar_ambientes);
        dgListaAmbientes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        consultarDatosAmbiente();

        dgListaAmbientes.show();

    }
    public void consultarDatosAmbiente(){
        ConsumoServicios servicio=new ManagerRetrofit(getActivity()).getConsumoServicio();

        Call<List<Ambiente>> res=servicio.getAmbientes(rol);

        res.enqueue(new Callback<List<Ambiente>>() {
            @Override
            public void onResponse(Call<List<Ambiente>> call, Response<List<Ambiente>> response) {
                cargarDatosAmbiente((ArrayList)response.body());
            }

            @Override
            public void onFailure(Call<List<Ambiente>> call, Throwable t) {

            }
        });
    }

    public void cargarDatosAmbiente(ArrayList<Ambiente> ambientes){
        AdapterAmbientes adapterAmbientes=new AdapterAmbientes(getActivity(),ambientes,rol,this);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());

        RecyclerView rv=dgListaAmbientes.findViewById(R.id.m_ambientes_recycler);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapterAmbientes);

    }

    public void findViews(View v){

    }


    @Override
    public void estadoAmbiente(boolean estado, Ambiente ambiente) {
        cambiarEstadoAmbiente(ambiente.getIdAmbiente(),estado?"1":"3");

    }

    @Override
    public void apagarAmbiente(Ambiente ambiente) {
        cambiarEstadoAmbiente(ambiente.getIdAmbiente(),"1");
        //Toast.makeText(getActivity(), "se apago "+ambiente.getNombreAmbiente(), Toast.LENGTH_SHORT).show();
    }

    public void cambiarEstadoAmbiente(String idAmbiente, String estadoAc){
        ConsumoServicios servicio=new ManagerRetrofit(getActivity()).getConsumoServicio();

        Call<ResponseBody> res=servicio.cambiarEstadoAmbiente(idAmbiente,estadoAc);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                     consultarDatosAmbiente();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

