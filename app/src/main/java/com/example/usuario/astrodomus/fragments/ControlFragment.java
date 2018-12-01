package com.example.usuario.astrodomus.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.adapters.AdapterAmbientes;
import com.example.usuario.astrodomus.adapters.AdapterComponentes;
import com.example.usuario.astrodomus.constantes.AtributosId;
import com.example.usuario.astrodomus.constantes.ComponentesId;
import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.control.componentes.CtrolAireAcondicionado;
import com.example.usuario.astrodomus.control.componentes.CtrolLuzLed;
import com.example.usuario.astrodomus.control.componentes.CtrolVentilador;
import com.example.usuario.astrodomus.dialogs.atributos.LuzLedDialog;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerListaAmbiente;
import com.example.usuario.astrodomus.interfaces.ListenerListaComponente;
import com.example.usuario.astrodomus.interfaces.ListenerListaUsuarios;
import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Atributo;
import com.example.usuario.astrodomus.models.Componente;
import com.example.usuario.astrodomus.models.Usuario;
import com.example.usuario.astrodomus.utils.AnimDialog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment implements ListenerListaAmbiente, ListenerListaComponente {


    private Dialog dgListaAmbientes;
    private String rol;
    private Ambiente ambiente;
    private View viewFragment;
    private ControlAmbiente ctrolAmbiente;


    public static final String DISP="1";
    public static final String DEFEC="3";
    public static final String OCUP="2";


    public ControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewFragment=inflater.inflate(R.layout.fragment_control, container, false);


        if(getArguments()!=null){
            rol=getArguments().getString(InicioSesionActivity.KEY_ROL);
        }

        if(getActivity()!=null){
            abrirListaAmbientes();
            ctrolAmbiente=new ControlAmbiente(getActivity(),this,rol);
            //Toast.makeText(getActivity(), ""+rol, Toast.LENGTH_SHORT).show();
            ctrolAmbiente.consultarDatosAmbiente();

        }


        return viewFragment;
    }

    public void abrirListaAmbientes(){
        dgListaAmbientes=new Dialog(getActivity());
        dgListaAmbientes.setContentView(R.layout.dialog_mostar_ambientes);
        dgListaAmbientes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dgListaAmbientes.setCanceledOnTouchOutside(false);
        final AnimDialog animDialog=new AnimDialog(getActivity(),dgListaAmbientes);
        //consultarDatosAmbiente();

        animDialog.animarEntrada(dgListaAmbientes.findViewById(R.id.dg_cont_ctrol_ambientes));
        dgListaAmbientes.show();

        dgListaAmbientes.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                animDialog.animarSalida(dgListaAmbientes.findViewById(R.id.dg_cont_ctrol_ambientes));
                getActivity().onBackPressed();
            }
        });








    }

    @Override
    public void ambientes(ArrayList<Ambiente> ambientes) {
        AdapterAmbientes adapterAmbientes=new AdapterAmbientes(getActivity(),ambientes,rol,this);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());

        RecyclerView rv=dgListaAmbientes.findViewById(R.id.m_ambientes_recycler);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapterAmbientes);
    }

    @Override
    public void iniciarAmbiente(Ambiente ambiente) {
        this.ambiente=ambiente;
        if(ambiente!=null) {
            //metodo nuevo
            ctrolAmbiente.consultarDatosComponentes(ambiente);

            //metodo anterior comentado
            // consultarDatosComponentes();
            AnimDialog animDialog=new AnimDialog(getActivity(),dgListaAmbientes);
            animDialog.animarSalida(dgListaAmbientes.findViewById(R.id.dg_cont_ctrol_ambientes));

        }
    }

    @Override
    public void onOffAmbiente(Ambiente ambiente, boolean lista) {


        //metodo anterior comentado
        //cambiarEstadoAmbiente(ambiente);
        ctrolAmbiente.cambiarEstadoAmbiente(ambiente,lista);

        if(ambiente.getEstado().equalsIgnoreCase(OCUP)){
            iniciarAmbiente(ambiente);
        }else{
            //metodo parara agapar el ambiente desde el boton, se comenta porque tiene errores
            //apagarAmbiente();
            iniciarAmbiente(null);


        }

        //Toast.makeText(getActivity(), "se apago "+ambiente.getNombreAmbiente(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void componentes(ArrayList<Componente> componentes) {
        RecyclerView rv=viewFragment.findViewById(R.id.control_recycler);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        AdapterComponentes adapter=new AdapterComponentes(getActivity(),componentes,this);

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        listenerApagarAmbiente();

    }

    @Override
    public void componenteOnOff(Componente componente, boolean power) {

        //metodo comentado
        //changeStateAtriComp(power?"0":"1",ambiente,componente.getId_ubi(),"1");

        ctrolAmbiente.changeStateAtriComp(power?"0":"1",ambiente,componente.getId_ubi(),"1");
    }




    private void listenerApagarAmbiente(){
        viewFragment.findViewById(R.id.control_bton_apagar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambiente.setEstado(DISP);
                onOffAmbiente(ambiente,false);
            }
        });

    }



    @Override
    public void estadoAmbiente(boolean estado, Ambiente ambiente) {

        ambiente.setEstado(estado?DISP:DEFEC);
        ctrolAmbiente.cambiarEstadoAmbiente(ambiente,true);
        //metodo anterior comentado
        //cambiarEstadoAmbiente(ambiente);

    }

    @Override
    public void mostrarAtributos(Componente componente) {

        switch (componente.getId_ubi()){
            case ComponentesId.LUCES_LED:
                luzLed(componente);
                break;
            case ComponentesId.AIRE_ACONDICIONADO:
                aireAcondicionado(componente);
                break;
            case ComponentesId.VENTILADOR:
                ventilador(componente);
                break;
        }
        //Toast.makeText(getActivity(), ""+componente.getAtributos().get(0).getNombreAtributo(), Toast.LENGTH_SHORT).show();
    }




    ///metodos para el control de los atributos de cada componente

    private void luzLed(Componente componente){
        Atributo atributo=getAtributoComponente(componente,AtributosId.INTENCIDAD_LUZ);

        CtrolLuzLed ctrolLuzLed=new CtrolLuzLed(getActivity());
        ctrolLuzLed.abrirDialog();
        ctrolLuzLed.listenerBotonListoControles(ctrolAmbiente,ambiente);
        ctrolLuzLed.listenerSeekbar(ambiente,componente, atributo);
    }
    private void aireAcondicionado(Componente componente){

        CtrolAireAcondicionado ctrolAire=new CtrolAireAcondicionado(getActivity());
        ctrolAire.abrirDialog();

        ctrolAire.listenerBtonMas(ambiente, componente, getAtributoComponente(componente,AtributosId.TEMPERATURA));
        ctrolAire.listenerBtonMenos(ambiente,componente,getAtributoComponente(componente,AtributosId.TEMPERATURA));
        ctrolAire.listenerSwicth(ambiente,componente,getAtributoComponente(componente,AtributosId.PERCIANAS));
        ctrolAire.listenerBotonListoControles(ctrolAmbiente,ambiente);
    }
    private void ventilador(Componente componente){

        CtrolVentilador ctrolVentilador=new CtrolVentilador(getActivity());
        ctrolVentilador.abrirDialog();

        ctrolVentilador.listenerBtonMas(ambiente, componente, getAtributoComponente(componente,AtributosId.VELOCIDAD));
        ctrolVentilador.listenerBtonMenos(ambiente,componente,getAtributoComponente(componente,AtributosId.VELOCIDAD));
        ctrolVentilador.listenerBotonListoControles(ctrolAmbiente,ambiente);
    }




    public Atributo getAtributoComponente(Componente componente, String tipoAtributo){
        Atributo atributo=null;

        for(int i=0;i<componente.getAtributos().size();i++){
            if(componente.getAtributos().get(i).getIdAtributo().equals(tipoAtributo)){
                atributo=componente.getAtributos().get(i);
            }
        }
        return atributo;
    }




}

