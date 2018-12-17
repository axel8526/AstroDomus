package com.example.usuario.astrodomus.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;

public class BienvenidaActivity extends AppCompatActivity {


    private LinearLayout conCorreo;
    private ImageView viewIcono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        findViews();
        animar();
        datosUsuario();
        cargarDatosUsuario();
        iniciarCronometro();
    }


    public void animar(){
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.aparecer_desvanecido);
        animation.setFillAfter(true);

        Animation animation2=AnimationUtils.loadAnimation(this,R.anim.transladar_desde_abajo);


        viewIcono.startAnimation(animation);
        textBienvenido.startAnimation(animation);
        conCorreo.startAnimation(animation);

        textCC.startAnimation(animation2);
        textRol.startAnimation(animation2);

    }
    public void iniciarCronometro(){
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                iniciarActividad();
            }
        }.start();
    }

    private TextView textBienvenido, textRol, textCC, textCorreo;
    public void cargarDatosUsuario(){
        textRol.setText(rol);
        textCorreo.setText(correo);
        textCC.setText(id);

        String noms[]=nombre.split(" ");
        textBienvenido.setText("Bienvenido\n"+nombreUser(noms[0]+" "+nombreUser(noms[1])));
    }
    public void findViews(){

        conCorreo=findViewById(R.id.id_con_correo);
        textBienvenido=findViewById(R.id.inicio_text_bienvenido);
        textCC=findViewById(R.id.inicio_text_cc);
        textCorreo=findViewById(R.id.inicio_text_correo);
        textRol=findViewById(R.id.inicio_text_rol);
        viewIcono=findViewById(R.id.inicio_icon_user);
    }
    public String nombreUser(String nom){
        String n=nom.toLowerCase();
        return (n.charAt(0)+"").toUpperCase()+n.substring(1,n.length());
    }

    private String correo, rol,id,nombre;
    public void datosUsuario(){
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        //Bundle datos=getIntent().getExtras();
        if(datos!=null){
            correo=datos.getString(InicioSesionActivity.KEY_CORREO,"correo vacio");
            rol=datos.getString(InicioSesionActivity.KEY_ROL,"rol vacio");
            id=datos.getString(InicioSesionActivity.KEY_ID,"id vacio");
            nombre=datos.getString(InicioSesionActivity.KEY_NOMBRE, "nom vacio");


        }
    }

    @TargetApi(23)
    public void iniciarActividad(){
        Intent intent=new Intent(this, InicioActivity.class);

        Pair[] pairs=new Pair[5];

        pairs[0]=new Pair(conCorreo,"id_con_correo_t");
        pairs[1]=new Pair(viewIcono,"inicio_icon_user_t");
        pairs[2]=new Pair(textRol,"inicio_text_rol_t");
        pairs[3]=new Pair(textCC,"inicio_text_cc");
        pairs[4]=new Pair(conCorreo,"id_con_correo_t");

        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent,op.toBundle());
        finish();
    }
}
