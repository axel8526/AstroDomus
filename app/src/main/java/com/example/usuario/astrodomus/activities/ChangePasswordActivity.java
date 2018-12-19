package com.example.usuario.astrodomus.activities;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.control.NotificacionAmbiente;
import com.example.usuario.astrodomus.dialogs.ConfirmarAccionDialog;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView tCorreo, tRol, tId;
    private EditText editPass1, editPass2, editPass3;
    private String correo, id, rol, nombre;

    private ImageView btonIcono, btonHome, btonPassword, btonInfo,btonSalir;
    private int typeActivity;
    private LinearLayout conCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        findViews();
        datosUsuario();
        comprobarActividad();
        colorFondoBotones(btonPassword,btonInfo,btonHome);
    }
    private void comprobarActividad(){
        if(typeActivity==0){
            //findViewById(R.id.cp_bton_back).setEnabled(false);
            //findViewById(R.id.cp_bton_back).setVisibility(View.INVISIBLE);
            editPass1.setEnabled(false);
        }
    }

    public void findViews(){
        tCorreo=findViewById(R.id.inicio_text_correo);

        editPass1=findViewById(R.id.cp_password);
        editPass2=findViewById(R.id.cp_edit_clave2);
        editPass3=findViewById(R.id.cp_edit_clave3);
        btonHome=findViewById(R.id.inicio_bton_home);
        btonInfo=findViewById(R.id.inicio_bton_info);
        btonPassword=findViewById(R.id.inicio_bton_clave);
        btonSalir=findViewById(R.id.inicio_bton_salir_app);
        conCorreo=findViewById(R.id.id_con_correo);
        btonIcono=findViewById(R.id.inicio_icon_user);
    }
    public void onBackPressed(){
        inicioActivity();
    }

    public void botones(View v){

        switch (v.getId()){

            case R.id.cp_bton_cambiar:

                if(comprobarDatos()){

                    if(typeActivity==0)
                        actualizaPassword();
                    else{
                        comprobarClave();
                    }
                }
                break;

            case R.id.toolbar_bton_back:
                onBackPressed();
                break;

        }

    }

    public void inicioActivity(){

        Intent intent;
        if(typeActivity==0){
            intent=new Intent(this,InicioSesionActivity.class);
        }else{
            intent=new Intent(this,InicioActivity.class);
        }


        intent.putExtra(InicioSesionActivity.KEY_CORREO,correo);
        intent.putExtra(InicioSesionActivity.KEY_ID,id);
        intent.putExtra(InicioSesionActivity.KEY_ROL,rol);
        startActivity(intent);
        finish();
    }




    private boolean comprobarDatos(){
        boolean comprobar=true;
        String mensaje="";
        String pass1, pass2;

        pass1=editPass2.getText().toString();
        pass2=editPass3.getText().toString();

        if(pass1.equalsIgnoreCase("")){
            mensaje="El campo nueva contraseña esta vacío";
            comprobar=false;
        }
        else if(pass2.equalsIgnoreCase("")){
            mensaje="El campo confirmar contraseña esta vacío";
            comprobar=false;
        }
        else if(!pass1.equals(pass2)){
            mensaje="las contraseñas no coinciden";
            comprobar=false;
        }

        if(!comprobar){
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }
        return comprobar;

    }

    public void comprobarClave(){
        ConsumoServicios servicio=new ManagerRetrofit(this).getConsumoServicio();

        Call<List<Usuario>> res=servicio.getUser(correo,editPass1.getText().toString().trim());

        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.body()==null || response.body().size()==0){
                    Toast.makeText(ChangePasswordActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }else{
                    actualizaPassword();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });

    }
    public void actualizaPassword(){

        ConsumoServicios servicio=new ManagerRetrofit(this).getConsumoServicio();

        Call<ResponseBody> res=servicio.updatePassword(editPass2.getText().toString(),correo);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ChangePasswordActivity.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                guardarPassword();
                inicioActivity();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
    public void guardarPassword(){
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor guarda=datos.edit();

        guarda.putString(InicioSesionActivity.KEY_PASSWORD,editPass2.getText().toString());

        guarda.apply();
    }



    public void botonesInicio(View v){
        switch (v.getId()){
            case R.id.inicio_icon_user:

                break;
            case R.id.inicio_bton_home:

               abrirActivity(InicioActivity.class);

                break;
            case R.id.inicio_bton_clave:


                break;
            case R.id.inicio_bton_info:
                colorFondoBotones(btonInfo,btonHome,btonPassword);
                abrirActivity(DatosPersonalesActivity.class);



                break;
            case R.id.inicio_bton_salir_app:
                abrirDialogCerrarSesion();
                break;

        }
    }
    public void colorFondoBotones(View bton1, View bton2, View bton3){

        bton1.setBackgroundColor(getResources().getColor(R.color.H_color_oscuro_link));
        bton2.setBackgroundColor(getResources().getColor(R.color.tranparente));
        bton3.setBackgroundColor(getResources().getColor(R.color.tranparente));
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

    @TargetApi(23)
    public void abrirActivity(Class clase){

        Intent intent=new Intent(this,clase);


        Pair[] pairs=new Pair[1];


        pairs[0]=new Pair(btonIcono,"inicio_icon_user_t");


        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent,op.toBundle());
        finish();

    }
    public void datosUsuario(){
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);
        //Bundle datos=getIntent().getExtras();
        if(datos!=null){
            correo=datos.getString(InicioSesionActivity.KEY_CORREO,"correo vacio");
            rol=datos.getString(InicioSesionActivity.KEY_ROL,"rol vacio");
            id=datos.getString(InicioSesionActivity.KEY_ID,"id vacio");
            nombre=datos.getString(InicioSesionActivity.KEY_NOMBRE, "nom vacio");
            typeActivity=datos.getInt(InicioSesionActivity.KEY_ACTIVITY,1);

            tCorreo.setText(correo);



        }
    }
    public String nombreUser(String nom){
        String n=nom.toLowerCase();
        return (n.charAt(0)+"").toUpperCase()+n.substring(1,n.length());
    }
}
