package com.example.usuario.astrodomus.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
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
    private String correo, id, rol;
    private int typeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        findViews();
        cargaDatosPrincipales();
        comprobarActividad();
    }
    private void comprobarActividad(){
        if(typeActivity==0){
            findViewById(R.id.cp_bton_back).setEnabled(false);
            findViewById(R.id.cp_bton_back).setVisibility(View.INVISIBLE);
            editPass1.setEnabled(false);
        }
    }

    public void findViews(){
        tCorreo=findViewById(R.id.cp_text_correo);
        tRol=findViewById(R.id.cp_text_rol);
        tId=findViewById(R.id.cp_text_cc);
        editPass1=findViewById(R.id.cp_password);
        editPass2=findViewById(R.id.cp_edit_clave2);
        editPass3=findViewById(R.id.cp_edit_clave3);
    }
    public void onBackPressed(){
        inicioActivity();
    }

    public void botones(View v){

        switch (v.getId()){
            case R.id.cp_bton_back:

                inicioActivity();
                break;
            case R.id.cp_bton_cambiar:

                if(comprobarDatos()){

                    if(typeActivity==0)
                        actualizaPassword();
                    else{
                        comprobarClave();
                    }
                }
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

    public void cargaDatosPrincipales(){
        Bundle datos=getIntent().getExtras();
        if(datos!=null){
            correo=datos.getString(InicioSesionActivity.KEY_CORREO);
            rol=datos.getString(InicioSesionActivity.KEY_ROL);
            id=datos.getString(InicioSesionActivity.KEY_ID);
            typeActivity=datos.getInt(InicioSesionActivity.KEY_ACTIVITY);


            tCorreo.setText(correo);
            tId.setText(id);
            tRol.setText(rol);

            //Toast.makeText(this, correo, Toast.LENGTH_SHORT).show();

        }
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


}
