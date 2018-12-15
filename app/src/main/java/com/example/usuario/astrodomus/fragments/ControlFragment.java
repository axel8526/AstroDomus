package com.example.usuario.astrodomus.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.usuario.astrodomus.control.NotificacionAmbiente;
import com.example.usuario.astrodomus.control.componentes.CtrolAireAcondicionado;
import com.example.usuario.astrodomus.control.componentes.CtrolLuzLed;
import com.example.usuario.astrodomus.control.componentes.CtrolVentilador;
import com.example.usuario.astrodomus.dialogs.EscogePerfilDialog;
import com.example.usuario.astrodomus.dialogs.atributos.LuzLedDialog;
import com.example.usuario.astrodomus.hilos.EstadoAmbienteHilo;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerAmbiente;
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
public class ControlFragment extends Fragment implements ListenerListaAmbiente, ListenerListaComponente,ListenerAmbiente {


    private Dialog dgListaAmbientes;
    private String rol, idUser, correo;
    private Ambiente ambiente, ambienteCargado;
    private View viewFragment;
    private TextView textAmbiente;
    private ControlAmbiente ctrolAmbiente;
    private EscogePerfilDialog dialogEscogePerfil;

    private ArrayList<Ambiente> ambientes;
    private ArrayList<Dialog> ventanasEmergentes;



    public static final String DISP="1";
    public static final String DEFEC="3";
    public static final String OCUP="2";

    private EstadoAmbienteHilo ambienteHilo;
    private Activity activity;
    private Button btonApagar;
    private ProgressBar progressBar;



    public ControlFragment() {
        // Required empty public constructor
        ventanasEmergentes=new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewFragment=inflater.inflate(R.layout.fragment_control, container, false);
        findViews();
        toolbar();

        bListaAmbiente=false;
        if(getArguments()!=null){
            rol=getArguments().getString(InicioSesionActivity.KEY_ROL);
            idUser=getArguments().getString(InicioSesionActivity.KEY_ID);
            correo=getArguments().getString(InicioSesionActivity.KEY_CORREO);
        }

        if(getActivity()!=null){
            activity=getActivity();
            ctrolAmbiente=new ControlAmbiente(getActivity(),this,rol);
            if(ambienteHilo!=null){
                ambienteHilo.terminar();
            }
            ambienteHilo=new EstadoAmbienteHilo(ctrolAmbiente,idUser);
            ambienteHilo.start();

           // ctrolAmbiente.usuarioAlojado(idUser);

                //abrirListaAmbientes();
                //ctrolAmbiente.consultarDatosAmbiente();
                //Toast.makeText(getActivity(), ""+rol, Toast.LENGTH_SHORT).show();


        }


        return viewFragment;
    }
    public void findViews(){
        btonApagar=viewFragment.findViewById(R.id.control_bton_apagar);
        progressBar=viewFragment.findViewById(R.id.control_progress_bar);
        textAmbiente=viewFragment.findViewById(R.id.control_text_ambiente);
    }
    public void toolbar(){
        Button back=viewFragment.findViewById(R.id.toolbar_bton_back);
        ImageView icono=viewFragment.findViewById(R.id.toobar_icono);
        TextView titulo=viewFragment.findViewById(R.id.toolbar_titulo);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        icono.setImageResource(R.drawable.icon_boton_home1);
        titulo.setText("Controles");
    }
    public void progressBarEstado(boolean estado){

            progressBar.setLayoutParams(estado ?
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) :
                    new LinearLayout.LayoutParams(0, 0));

    }
    public void onDestroyView() {
        super.onDestroyView();
        if(ambienteHilo!=null){
            ambienteHilo.terminar();
        }
    }

    private boolean bListaAmbiente, estadoLista;



    @Override
    public void ambienteAlojado(final Ambiente ambiente) {
            if(ambiente==null){


                if(!bListaAmbiente) {
                    cerrarNotificacion();
                    abrirListaAmbientes();
                    cerrarDialogs();

                    bListaAmbiente=true;

                }



                if(this.ambiente!=null && rol.equals(InicioSesionActivity.ADMINISTRADOR_ROL)){
                    textAmbiente.setText(ambiente!=null?ambiente.getNombreAmbiente():"AMBIENTE");
                    this.ambiente=actualizarAmbiente(this.ambiente);
                    //si entra en este condicional es porque el administrador entro a un ambiente que ya estaba ocupado
                    //Toast.makeText(activity, this.ambiente.getIdUser()+"//"+this.ambiente.getIdAmbiente(), Toast.LENGTH_SHORT).show();
                    if(this.ambiente.getEstado().equals(OCUP)) {
                        ctrolAmbiente.consultarDatosComponentes(this.ambiente);
                       // Toast.makeText(activity, "entro if", Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(activity, "entro else", Toast.LENGTH_SHORT).show();
                        this.ambiente=null;
                        bListaAmbiente=false;


                    }
                    //condicional que comprueba cuando el usuario cerro el ambiente, y el administrador quedo dentro
                    //lo que se hace es volver a abrir la lista de ambientes
                }

                ctrolAmbiente.consultarDatosAmbiente();


            }else{
                bListaAmbiente=false;
                textAmbiente.setText(ambiente.getNombreAmbiente());


                this.ambiente=ambiente;
                //Toast.makeText(getActivity(), ambiente.getNombreAmbiente(), Toast.LENGTH_SHORT).show();



                ctrolAmbiente.consultarDatosComponentes(ambiente);
                //Toast.makeText(activity, "padre else", Toast.LENGTH_SHORT).show();
            }
    }



    public void abrirListaAmbientes(){
        dgListaAmbientes=new Dialog(activity);
        dgListaAmbientes.setContentView(R.layout.dialog_mostar_ambientes);
        dgListaAmbientes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dgListaAmbientes.setCanceledOnTouchOutside(false);
        final AnimDialog animDialog=new AnimDialog(activity,dgListaAmbientes);
        //consultarDatosAmbiente();

        animDialog.animarEntrada(dgListaAmbientes.findViewById(R.id.dg_cont_ctrol_ambientes));
        dgListaAmbientes.show();

        dgListaAmbientes.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                animDialog.animarSalida(dgListaAmbientes.findViewById(R.id.dg_cont_ctrol_ambientes));

                ambienteHilo.terminar();
                activity.onBackPressed();


            }
        });

        progressBarEstado(false);


    }

    @Override
    public void ambientes(ArrayList<Ambiente> ambientes) {

        ambientesDisponibles(!ambientes.isEmpty());

        this.ambientes=ambientes;

        AdapterAmbientes adapterAmbientes=new AdapterAmbientes(activity,ambientes,rol,this);
        LinearLayoutManager llm=new LinearLayoutManager(activity);

        RecyclerView rv=dgListaAmbientes.findViewById(R.id.m_ambientes_recycler);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapterAmbientes);
    }

    public Ambiente actualizarAmbiente(Ambiente ambiente){
        if (ambientes!=null) {
            for (Ambiente ambienteActual : ambientes) {
                if (ambiente.getIdAmbiente().equals(ambienteActual.getIdAmbiente())) {
                    return ambienteActual;
                }
            }
        }
        return null;
    }

    @Override
    public void iniciarAmbiente(Ambiente ambiente, String estadoAnterior) {
        this.ambiente=ambiente;

        if(estadoAnterior.equals(ControlFragment.DISP)){
            ambienteCargado=ambiente;
            ambiente.setIdUser(idUser);
            ctrolAmbiente.cambiarEstadoAmbiente(ambiente,true);



            //el cambiarAmbiente se coloca aqui porque aqui es la unica parde donde le enviamos
            //el iduser al ambiente, de resto se envia null por si se apaga el ambienete
            /*cuando inicie el administrador cuando este ocupado, el estado anterior sera
            * OCU asi que no ingresara a este condicional donde se le asigna usuario al ambiente*/

            NotificacionAmbiente not=new NotificacionAmbiente(activity,ambiente);
            not.crearNotificacion(rol,idUser,correo);
            not.mostrar();
            //codigo para mostrar la notificacion cuando se inicia ambiente

        }else{

            ambienteCargado=null;
        }

        if(ambiente!=null) {

            //metodo nuevo... se comemnta para abrir el dialog de cargar perfil
           // ctrolAmbiente.consultarDatosComponentes(ambiente);

            //metodo anterior comentado
            // consultarDatosComponentes();
            AnimDialog animDialog=new AnimDialog(activity,dgListaAmbientes);
            animDialog.animarSalida(dgListaAmbientes.findViewById(R.id.dg_cont_ctrol_ambientes));





                dialogEscogePerfil = new EscogePerfilDialog(activity);
                dialogEscogePerfil.showDialog();
                dialogEscogePerfil.cargarPerfil(ctrolAmbiente, idUser, ambiente);
                ventanasEmergentes.add(dialogEscogePerfil);






        }
    }







    @Override
    public void onOffAmbiente(Ambiente ambiente, boolean lista) {


        //metodo anterior comentado
        //cambiarEstadoAmbiente(ambiente);


        if(ambiente.getEstado().equalsIgnoreCase(OCUP)){
            iniciarAmbiente(ambiente,DISP);
        }else{

            this.ambiente=null;
            ctrolAmbiente.cambiarEstadoAmbiente(ambiente,lista);
        }

        //Toast.makeText(getActivity(), "se apago "+ambiente.getNombreAmbiente(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void componentes(ArrayList<Componente> componentes) {
        RecyclerView rv=viewFragment.findViewById(R.id.control_recycler);
        LinearLayoutManager llm=new LinearLayoutManager(activity);
        AdapterComponentes adapter=new AdapterComponentes(activity,componentes,this);

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

       btonApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarEstado(true);
                if(rol.equals(InicioSesionActivity.ADMINISTRADOR_ROL) && !idUser.equals(ambiente.getIdUser())){
                    //entrara a este condicional siempre y cuando el id del usuario sea diference al id usuario del ambiente
                   // abrirListaAmbientes();
                    bListaAmbiente=false;
                }


               /*Se coloca condicional ya que si el usuario hace dos click rapidos en el boton de apagar, la segunda vez
               * el ambiente es nulo*/
               if(ambiente!=null){
                   ambiente.setEstado(DISP);
                   ambiente.setIdUser(null);
                   onOffAmbiente(ambiente,false);
               }





                NotificacionAmbiente not=new NotificacionAmbiente(activity,ambiente);
                not.cerrar();
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

        CtrolLuzLed ctrolLuzLed=new CtrolLuzLed(activity);
        ctrolLuzLed.abrirDialog();
        ventanasEmergentes.add(ctrolLuzLed.getLuzLedDialog());

        ctrolLuzLed.listenerBotonListoControles(ctrolAmbiente,ambiente);
        ctrolLuzLed.listenerSeekbar(ambiente,componente, atributo);
    }
    private void aireAcondicionado(Componente componente){

        CtrolAireAcondicionado ctrolAire=new CtrolAireAcondicionado(activity);
        ctrolAire.abrirDialog();
        ventanasEmergentes.add(ctrolAire.getDialogAire());

        ctrolAire.listenerBtonMas(ambiente, componente, getAtributoComponente(componente,AtributosId.TEMPERATURA));
        ctrolAire.listenerBtonMenos(ambiente,componente,getAtributoComponente(componente,AtributosId.TEMPERATURA));
        ctrolAire.listenerSwicth(ambiente,componente,getAtributoComponente(componente,AtributosId.PERCIANAS));
        ctrolAire.listenerBotonListoControles(ctrolAmbiente,ambiente);
    }
    private void ventilador(Componente componente){

        CtrolVentilador ctrolVentilador=new CtrolVentilador(activity);
        ctrolVentilador.abrirDialog();
        ventanasEmergentes.add(ctrolVentilador.getAaDialog());



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

    public void cerrarDialogs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Dialog dialog : ventanasEmergentes){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }

                }
                ventanasEmergentes.clear();


            }
        }).start();

    }

    public void cerrarNotificacion(){
        NotificacionAmbiente not=new NotificacionAmbiente(activity);
        not.cerrar();
    }

    public void ambientesDisponibles(boolean estado){
        LinearLayout contenedor=dgListaAmbientes.findViewById(R.id.m_ambientes_nada);
        LinearLayout.LayoutParams llp;

        if(estado){
            llp=new LinearLayout.LayoutParams(0,0);
        }else{

            llp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        contenedor.setLayoutParams(llp);
    }



}

