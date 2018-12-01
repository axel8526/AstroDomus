package com.example.usuario.astrodomus.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.EnviarMail;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.dialogs.ConfirmarAccionDialog;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.MensajeEnviado;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InicioSesionActivity extends AppCompatActivity implements MensajeEnviado {


    //el id era la cedula, se cambio al correo electronico
    private EditText id,password;
    private ProgressBar progressBar;
    private LinearLayout contenedorDatos;
    private CheckBox checkBox;
    private TextView textOlvidoPassword;
    private String codigoVerificar, textRol, textIdentificacion;



    public static final String ADMINISTRADOR_ROL="ADMINISTRADOR";
    public static final String KEY_CORREO="CORREO";
    public static final String KEY_ID="ID";
    public static final String KEY_ROL="ROL";
    public static final String KEY_ACTIVITY="ACT";
    public static final String KEY_PASSWORD="PASS";
    public static final String KEY_RECORDAR="RECO";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        findViews();

        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        mostrarDatos();


    }
    public void onResume(){
        super.onResume();

        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);

        if(datos.getBoolean(KEY_RECORDAR,false)){
            checkBox.setChecked(datos.getBoolean(KEY_RECORDAR,false));
            id.setText(datos.getString(KEY_CORREO,""));
            password.setText(datos.getString(KEY_PASSWORD,""));
            iniciarSesion(null);
        }
    }
    public void onPause(){
        super.onPause();

        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor guarda=datos.edit();

        guarda.putString(KEY_CORREO,textId);
        guarda.putString(KEY_PASSWORD,textPass);
        guarda.putBoolean(KEY_RECORDAR,checkBox.isChecked());

        guarda.apply();
    }




    public void findViews(){
        id=findViewById(R.id.is_id_user);
        password=findViewById(R.id.is_clave_user);
        progressBar=findViewById(R.id.is_progress_bar);
        contenedorDatos=findViewById(R.id.is_contenedor_dato);
        checkBox=findViewById(R.id.is_checkbox);
    }
    public void mostrarProgressBar(){

        LinearLayout.LayoutParams lm1=new LinearLayout.LayoutParams(100,100);
        progressBar.setLayoutParams(lm1);

        LinearLayout.LayoutParams lm=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        contenedorDatos.setLayoutParams(lm);

    }
    public void mostrarDatos(){
        LinearLayout.LayoutParams lm1=new LinearLayout.LayoutParams(0,0);
        progressBar.setLayoutParams(lm1);

        LinearLayout.LayoutParams lm=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        contenedorDatos.setLayoutParams(lm);
    }

    private String textId, textPass;

    public void iniciarSesion(View v){
         textId=id.getText().toString();
         textPass=password.getText().toString();

        if(textId.isEmpty()){
            Toast.makeText(this, "Ingrese identificacion", Toast.LENGTH_SHORT).show();
            return;
        }
        if (textPass.isEmpty()){
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarProgressBar();

        ManagerRetrofit mRetrofit=new ManagerRetrofit(this);
        ConsumoServicios servicio=mRetrofit.getConsumoServicio();

        Call<List<Usuario>> res=servicio.getUser(textId,textPass);

        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                verificar(response.body());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });

    }


    public void verificar(List<Usuario> usuario) {
        if (usuario == null || usuario.size() == 0) {
            Toast.makeText(this, "error en los datos", Toast.LENGTH_SHORT).show();
            mostrarDatos();

        } else {
            Intent intent=null;

           // Toast.makeText(this, "has iniciado sesion", Toast.LENGTH_SHORT).show();
            Usuario user = usuario.get(0);

            if(user.getNombre1()==null || user.getNombre1().equals("") ){
                intent=new Intent(this,DatosPersonalesActivity.class);

            }else {
                intent=new Intent(this,HomeActivity.class);

            }

            intent.putExtra(KEY_CORREO,user.getCorreo());
            intent.putExtra(KEY_ID,user.getId());
            intent.putExtra(KEY_ROL,user.getRol());
            intent.putExtra(KEY_ACTIVITY,0);

            startActivity(intent);
            finish();

        }
    }

    public void restablecerPassword(View v) {

        if (id.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Ingrese correo electronico", Toast.LENGTH_SHORT).show();
            return;
        }
        abrirDialogEnviarCorreo(id.getText().toString().trim());

    }
    public void abrirDialogEnviarCorreo(String correo){
        final ConfirmarAccionDialog dialog=new ConfirmarAccionDialog(this,
                "¿Enviar código?",correo,false);

        dialog.abrirDialog();
        dialog.setTextBtonConfirmar("enviar");
        dialog.setTheme(R.drawable.icon_dialog_bien,R.drawable.icon_correcto,R.color.colorPrimary);

        dialog.cerrarDialog2();

        dialog.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarCorreo();
                mostrarProgressBar();
                dialog.cerrarDialog();
            }
        });
    }

    public void comprobarCorreo() {


        ConsumoServicios servicio=new ManagerRetrofit(this).getConsumoServicio();
        Call<List<Usuario>> res=servicio.comprobarUser(id.getText().toString().trim());

        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.body()==null || response.body().size()==0){
                    mostrarDatos();
                    Toast.makeText(InicioSesionActivity.this, "Correo no registrado en AstroDomus", Toast.LENGTH_LONG).show();
                }else{

                    textRol=response.body().get(0).getRol();
                    textIdentificacion=response.body().get(0).getId();
                    enviarMensaje();

                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }


    public void enviarMensaje(){
        mostrarProgressBar();
        String randon=  new Random().nextInt(10000)*77+"";
        codigoVerificar=randon.substring(0,5);

        EnviarMail enviarMail=new EnviarMail(this);
        enviarMail.setCorreoReceptor(id.getText().toString());
        enviarMail.enviarCodigo(codigoVerificar);

        //posiblemente se podría quitar

    }


    @Override
    public void mensajeEnviado(boolean envio) {
        Toast.makeText(this, "Se ha enviado un codigo al correo", Toast.LENGTH_SHORT).show();
        mostrarDatos();
        abrirDialogCodigo();

    }
    public void abrirDialogCodigo(){

        final ConfirmarAccionDialog dialog=new ConfirmarAccionDialog(this,
                "Ingrese código","",true);

        dialog.cancelarAlTocarFuera(false);
        dialog.abrirDialog();
        dialog.setTextBtonConfirmar("aceptar");
        dialog.setTheme(R.drawable.icon_dialog_bien,R.drawable.icon_correcto,R.color.colorPrimary);


        dialog.cerrarDialog2();

        dialog.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.getEditText().getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(InicioSesionActivity.this, "Ingrese codigo", Toast.LENGTH_SHORT).show();

                }else if(dialog.getEditText().getText().toString().trim().equals(codigoVerificar)){
                    cambiarPassword();
                }else{
                    Toast.makeText(InicioSesionActivity.this, "Código incorrecto", Toast.LENGTH_SHORT).show();
                    dialog.cerrarDialog();
                }
            }
        });
    }

    public void cambiarPassword(){
        Intent intent=new Intent(this,ChangePasswordActivity.class);
        intent.putExtra(InicioSesionActivity.KEY_CORREO,id.getText().toString());
        intent.putExtra(InicioSesionActivity.KEY_ID,textIdentificacion);
        intent.putExtra(InicioSesionActivity.KEY_ROL,textRol);
        intent.putExtra(InicioSesionActivity.KEY_ACTIVITY,0);

        startActivity(intent);
        finish();
    }

    public void cambiarIp(View v){
        String ipText=id.getText().toString();
        if(!ipText.equalsIgnoreCase("")){
            String[] ipArray=ipText.split("_");

            if(ipArray.length==5){

                boolean comprueba=true;
                for(int i=0; i<ipArray.length;i++){
                    if(!isNumeric(ipArray[i])){
                        comprueba=false;
                    }
                }

                if(comprueba && ipArray[4].equals("8526")){
                    SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor guarda=datos.edit();

                    String ipM=ipArray[0]+"."+ipArray[1]+"."+ipArray[2]+"."+ipArray[3];
                    guarda.putString(ManagerRetrofit.KEY_IP,ipM);
                    guarda.apply();
                    Toast.makeText(this, ipM, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public boolean isNumeric(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
