package com.example.usuario.astrodomus.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.adapters.AdapterComponentesPerfil;
import com.example.usuario.astrodomus.constantes.AtributosId;
import com.example.usuario.astrodomus.constantes.ComponentesId;
import com.example.usuario.astrodomus.control.ControlPerfil;
import com.example.usuario.astrodomus.control.componentes.CtrolAireAcondicionado;
import com.example.usuario.astrodomus.control.componentes.CtrolLuzLed;
import com.example.usuario.astrodomus.control.componentes.CtrolVentilador;
import com.example.usuario.astrodomus.interfaces.ListenerPerfil;
import com.example.usuario.astrodomus.models.Atributo;
import com.example.usuario.astrodomus.models.Componente;
import com.example.usuario.astrodomus.models.ComponentePerfil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment implements ListenerPerfil {

    private View viewFragment;
    private RecyclerView rv;
    private ImageView[] btones;
    private Activity activity;
    private String rol, iduser;
    private ControlPerfil ctrlPerfil;
    private int jornadaActiva;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewFragment=inflater.inflate(R.layout.fragment_perfil, container, false);
        btones=new ImageView[3];
        findViews();

        if(getActivity()!=null && getArguments()!=null){
            activity=getActivity();

            iduser=getArguments().getString(InicioSesionActivity.KEY_ID);
            rol=getArguments().getString(InicioSesionActivity.KEY_ROL);

            ctrlPerfil=new ControlPerfil(activity,this,rol);

            listenerBotones();
            cargarPerfil(1);
        }
        return viewFragment;
    }

    public void findViews(){
        rv=viewFragment.findViewById(R.id.rv_perfiles);
        btones[0]=viewFragment.findViewById(R.id.perfil_bton_ma);
        btones[1]=viewFragment.findViewById(R.id.perfil_bton_tarde);
        btones[2]=viewFragment.findViewById(R.id.perfil_bton_noche);


    }
    public void listenerBotones(){
        for(int i=0; i<btones.length;i++){
            final int position=i;
            btones[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cargarPerfil(position+1);

                    Toast.makeText(activity, getTextJornada(position), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getTextJornada(int i){
        switch (i){
            case 0: return "Mañana";
            case 1:return "Tarde";
            default:return "Noche";
        }
    }
    public void cargarPerfil(int idJornada){
        jornadaActiva=idJornada;
        ctrlPerfil.cargarPerfiles(iduser,idJornada+"");
        for(int i=0; i<btones.length;i++){
            btones[i].setBackgroundColor(activity.getResources().getColor(R.color.color_app1));

        }
        btones[idJornada-1].setBackgroundColor(activity.getResources().getColor(R.color.color_fondo_componente));

    }

    @Override
    public void componenteOnOff(ComponentePerfil componentePerfil, boolean estado) {
        Atributo atributo=getAtributo(componentePerfil, AtributosId.ON_OFF);
        String[] datoId=atributo.getIdAtributo().split("_");
        String idEstados=datoId[1];

        ctrlPerfil.actualizarComponente(idEstados,estado?"0":"1");

    }
    public Atributo getAtributo(ComponentePerfil componente, String idAtributo){
        ArrayList<Atributo> atributos=componente.getAtributos();

        for(Atributo atributo : atributos){
            String[] datoId=atributo.getIdAtributo().split("_");
            String idAtri=datoId[0];

            if(idAtri.equals(idAtributo)){
                return  atributo;
            }
        }
        return null;
    }

    @Override
    public void mostrarAtributos(ComponentePerfil componente) {
        switch (componente.getId_componente()){
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
    }

    @Override
    public void componentes(ArrayList<ComponentePerfil> componentes) {
        LinearLayoutManager llm=new LinearLayoutManager(activity);
        AdapterComponentesPerfil adapter=new AdapterComponentesPerfil(activity,componentes,this);

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    private void luzLed(ComponentePerfil componente){
        Atributo atributo=getAtributo(componente,AtributosId.INTENCIDAD_LUZ);

        CtrolLuzLed ctrolLuzLed=new CtrolLuzLed(activity);
        ctrolLuzLed.abrirDialog();
        //ventanasEmergentes.add(ctrolLuzLed.getLuzLedDialog());

        ctrolLuzLed.listenerBotonListoPerfiles(ctrlPerfil,iduser,jornadaActiva+"");
        ctrolLuzLed.listenerSeekbar(null,null, atributo);
    }
    private void aireAcondicionado(ComponentePerfil componente){

        CtrolAireAcondicionado ctrolAire=new CtrolAireAcondicionado(activity);
        ctrolAire.abrirDialog();
        //ventanasEmergentes.add(ctrolAire.getDialogAire());

        ctrolAire.listenerBtonMas(null, null, getAtributo(componente,AtributosId.TEMPERATURA));
        ctrolAire.listenerBtonMenos(null,null,getAtributo(componente,AtributosId.TEMPERATURA));
        ctrolAire.listenerSwicth(null,null,getAtributo(componente,AtributosId.PERCIANAS));
        ctrolAire.listenerBotonListoPerfiles(ctrlPerfil,iduser,jornadaActiva+"");
    }
    private void ventilador(ComponentePerfil componente){

        CtrolVentilador ctrolVentilador=new CtrolVentilador(activity);
        ctrolVentilador.abrirDialog();
        //ventanasEmergentes.add(ctrolVentilador.getAaDialog());

        ctrolVentilador.listenerBtonMas(null, null, getAtributo(componente,AtributosId.VELOCIDAD));
        ctrolVentilador.listenerBtonMenos(null,null,getAtributo(componente,AtributosId.VELOCIDAD));
        ctrolVentilador.listenerBotonListoPerfiles(ctrlPerfil,iduser,jornadaActiva+"");
    }


}
