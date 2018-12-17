package com.example.usuario.astrodomus.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.NotificacionAmbiente;
import com.example.usuario.astrodomus.dialogs.ConfirmarAccionDialog;
import com.example.usuario.astrodomus.fragments.ControlFragment;
import com.example.usuario.astrodomus.fragments.InicioFragment;
import com.example.usuario.astrodomus.fragments.InicioUsuarioFragment;
import com.example.usuario.astrodomus.fragments.PerfilFragment;
import com.example.usuario.astrodomus.fragments.UsuariosFragment;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ListenerAmbiente;
import com.example.usuario.astrodomus.models.Ambiente;

public class InicioActivity extends AppCompatActivity implements ComunicaFragment, ListenerAmbiente {


    public static final int FRAG_REPORTE=5;
    public static final int FRAG_COMPONENE=4;
    public static final int FRAG_PERFIL=3;
    public static final int FRAG_CONTROL=2;
    public static final int FRAG_USER=1;
    public static final int FRAG_HOME=0;

    private int fragActivo;
    private String correo,id,rol, nombre;

    private TextView textBienvenido, textRol, textCC, textCorreo;
    private ImageView btonIcono, btonHome, btonPassword, btonInfo,btonSalir;
    private LinearLayout contenedorDatos, conDatosAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        findViews();
        datosUsuario();
        cargarDatosUsuario();

        comprobarAmbienteUsuario();
        activarFragment(FRAG_HOME);
        colorFondoBotones(btonHome,btonInfo,btonPassword);
    }

    public void onBackPressed(){
        if(fragActivo!=0){
            activarFragment(FRAG_HOME);
        }else{
            super.onBackPressed();
        }
    }

    public void findViews(){
        textBienvenido=findViewById(R.id.inicio_text_bienvenido);
        textCC=findViewById(R.id.inicio_text_cc);
        textCorreo=findViewById(R.id.inicio_text_correo);
        textRol=findViewById(R.id.inicio_text_rol);
        btonIcono=findViewById(R.id.inicio_icon_user);
        btonHome=findViewById(R.id.inicio_bton_home);
        btonInfo=findViewById(R.id.inicio_bton_info);
        btonPassword=findViewById(R.id.inicio_bton_clave);
        btonSalir=findViewById(R.id.inicio_bton_salir_app);
        contenedorDatos=findViewById(R.id.inicio_contenedor_datos_user);
        conDatosAnim=findViewById(R.id.id_datos_user);
    }
    public void cargarDatosUsuario(){
        textRol.setText(rol);
        textCorreo.setText(correo);
        textCC.setText(id);

        String noms[]=nombre.split(" ");
        textBienvenido.setText("Bienvenido\n"+nombreUser(noms[0]));
    }

    public String nombreUser(String nom){
        String n=nom.toLowerCase();
        return (n.charAt(0)+"").toUpperCase()+n.substring(1,n.length());
    }

    public void abrirDialogCerrarSesion(){
        String noms[]=nombre.split(" ");

        final ConfirmarAccionDialog dialog=new ConfirmarAccionDialog(this,
                "¿Cerrar sesion?","Se cerrara la sesion del usuario "+nombreUser(noms[0])+" "+nombreUser(noms[1]),false);

        dialog.abrirDialog();
        dialog.setTextBtonConfirmar("confirmar");
        dialog.setTheme(R.drawable.icon_dialog_bien,R.drawable.icon_user,R.color.colorPrimary);

        dialog.cerrarDialog2();

        dialog.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
                dialog.cerrarDialog();
            }
        });
    }

    @Override
    public void activarFragment(int fragment) {

            iniciarFragment(fragment);



    }

    public void iniciarFragment(int frag){
        fragActivo=frag;
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        Fragment fragment=getFragment(frag);

        Bundle datos=new Bundle();
        datos.putString(InicioSesionActivity.KEY_CORREO,correo);
        datos.putString(InicioSesionActivity.KEY_ID,id);
        datos.putString(InicioSesionActivity.KEY_ROL,rol);
        fragment.setArguments(datos);



        transaction.replace(R.id.contenedor_fragment,fragment);
        transaction.commit();
    }
    public void datosUsuario(){
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);
        //Bundle datos=getIntent().getExtras();
        if(datos!=null){
            correo=datos.getString(InicioSesionActivity.KEY_CORREO,"correo vacio");
            rol=datos.getString(InicioSesionActivity.KEY_ROL,"rol vacio");
            id=datos.getString(InicioSesionActivity.KEY_ID,"id vacio");
            nombre=datos.getString(InicioSesionActivity.KEY_NOMBRE, "nom vacio");


        }
    }

    public Fragment getFragment(int frag){
        boolean a=frag!=0?abrirFramentAstroDomus(false):abrirFramentAstroDomus(true);
        colorFondoBotones(btonHome,btonInfo,btonPassword);

        switch (frag){
            case FRAG_USER:return new UsuariosFragment();
            case FRAG_CONTROL:return new ControlFragment();
            case FRAG_PERFIL: return new PerfilFragment();
            case FRAG_COMPONENE: return new UsuariosFragment();
            case FRAG_REPORTE: return new UsuariosFragment();

            default:

                if(rol.equalsIgnoreCase(InicioSesionActivity.ADMINISTRADOR_ROL)) return new InicioFragment();
                else return new InicioUsuarioFragment();


        }
    }


    @Override
    public void ambienteAlojado(Ambiente ambiente) {
        if(ambiente!=null) {
            NotificacionAmbiente not = new NotificacionAmbiente(this,ambiente);
            not.crearNotificacion(rol,id,correo);
            not.mostrar();
        }
    }

    public void comprobarAmbienteUsuario(){
        ControlAmbiente ctrolAmb=new ControlAmbiente(getApplicationContext(),this, rol);
        ctrolAmb.usuarioAlojado(id);
    }

    public void botonesInicio(View v){
        switch (v.getId()){
            case R.id.inicio_icon_user:
                abrirActivity(DatosPersonalesActivity.class);
                break;
            case R.id.inicio_bton_home:
                colorFondoBotones(btonHome,btonInfo,btonPassword);
                activarFragment(FRAG_HOME);
                abrirFramentAstroDomus(true);

                break;
            case R.id.inicio_bton_clave:
                colorFondoBotones(btonPassword,btonInfo,btonHome);
                abrirActivity(ChangePasswordActivity.class);
                break;
            case R.id.inicio_bton_info:
                colorFondoBotones(btonInfo,btonHome,btonPassword);



                break;
            case R.id.inicio_bton_salir_app:
                abrirDialogCerrarSesion();
                break;

        }
    }
    public void colorFondoBotones(View bton1, View bton2, View bton3){

        if(fragActivo!=0){
            bton1.setBackgroundColor(getResources().getColor(R.color.tranparente));
        }else{
            bton1.setBackgroundColor(getResources().getColor(R.color.H_color_oscuro_link));
        }

        bton2.setBackgroundColor(getResources().getColor(R.color.tranparente));
        bton3.setBackgroundColor(getResources().getColor(R.color.tranparente));
    }



    public boolean abrirFramentAstroDomus(boolean home){


        final FrameLayout.LayoutParams llm;
        Animation animation;


        if(!home){
            llm=new FrameLayout.LayoutParams(0,0);
            animation= AnimationUtils.loadAnimation(this,R.anim.desaparecer_top);

        }else{
            int px=(int)(195*getResources().getDisplayMetrics().density);
            llm=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,px);
            animation=AnimationUtils.loadAnimation(this,R.anim.aparecer__top);
            contenedorDatos.setLayoutParams(llm);
        }

        animation.setFillAfter(true);
        contenedorDatos.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contenedorDatos.setLayoutParams(llm);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        return home;
    }


    public void cerrarSesion(){
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor guarda=datos.edit();

        guarda.putBoolean(InicioSesionActivity.KEY_RECORDAR,false);
        guarda.apply();


        NotificacionAmbiente not=new NotificacionAmbiente(this);
        not.cerrar();

        startActivity(new Intent(this,InicioSesionActivity.class));
        finish();
    }

    @TargetApi(23)
    public void abrirActivity(Class clase){
        Intent intent=new Intent(this,clase);
        intent.putExtra(InicioSesionActivity.KEY_CORREO,correo);
        intent.putExtra(InicioSesionActivity.KEY_ID,id);
        intent.putExtra(InicioSesionActivity.KEY_ROL,rol);
        intent.putExtra(InicioSesionActivity.KEY_ACTIVITY,1);


        Pair[] pairs=new Pair[2];


        pairs[0]=new Pair(conDatosAnim,"id_datos_user_t");
        pairs[1]=new Pair(btonIcono,"inicio_icon_user_t");

        abrirFramentAstroDomus(false);



        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent,op.toBundle());
        finish();

    }
}
