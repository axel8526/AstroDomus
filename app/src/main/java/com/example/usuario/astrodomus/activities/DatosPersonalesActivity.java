package com.example.usuario.astrodomus.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DatosPersonalesActivity extends Activity {

    private EditText editNom1, editNom2, editApll1, editApll2, editMovil, editDireccion,editPassword;
    private TextView textCorreo, textRol, textId;
    private String correo, rol, id;
    private int typeActivity;
    private ImageView viewIcono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.formulario_datos_personales);
        findViews();
        cargaDatosPrincipales();
        comprobarActividad();
        cerrarTeclado(editNom1);

    }
    private void comprobarActividad(){
        if(typeActivity==0){
            findViewById(R.id.dp_bton_back).setEnabled(false);
            findViewById(R.id.dp_bton_back).setVisibility(View.INVISIBLE);
        }else{
            cargarUsuario();
        }
    }
    public void onBackPressed(){
        if(typeActivity==0){
            finish();
        }else{
            inicioActivity();
        }
    }



    public void findViews(){
        editNom1=findViewById(R.id.dp_nom1);
        editNom2=findViewById(R.id.dp_nom2);
        editApll1=findViewById(R.id.dp_apll1);
        editApll2=findViewById(R.id.dp_apll2);
        editMovil=findViewById(R.id.dp_movil);
        editDireccion=findViewById(R.id.dp_direccion);
        editPassword=findViewById(R.id.dp_confirmar_clave);
        textCorreo=findViewById(R.id.dp_text_correo);
        textRol=findViewById(R.id.dp_text_rol);
        textId=findViewById(R.id.dp_text_cc);
        viewIcono=findViewById(R.id.inicio_icon_user);
    }
    public void cargaDatosPrincipales(){
        Bundle datos=getIntent().getExtras();
        if(datos!=null){
            correo=datos.getString(InicioSesionActivity.KEY_CORREO);
            rol=datos.getString(InicioSesionActivity.KEY_ROL);
            id=datos.getString(InicioSesionActivity.KEY_ID);
            typeActivity=datos.getInt(InicioSesionActivity.KEY_ACTIVITY);


            textCorreo.setText(correo);
            textId.setText(id);
            textRol.setText(rol);

            //Toast.makeText(this, correo, Toast.LENGTH_SHORT).show();

        }
    }


    public void botones(View v){
        switch (v.getId()){
            case R.id.dp_bton_back:
                inicioActivity();
                break;
            case R.id.dp_bton_guardar:

                if (comprobarDatos()){
                    comprobarClave();
                }
                break;
        }
    }

    private boolean comprobarDatos(){
        boolean comprobar=true;
        String mensaje="";

        if(editNom1.getText().toString().trim().equalsIgnoreCase("")){
            mensaje="El campo nombre 1 esta vacío";
            comprobar=false;
        }
        else if(editApll1.getText().toString().trim().equalsIgnoreCase("")){
            mensaje="El campo apellido 1 esta vacío";
            comprobar=false;
        }
        else if(editMovil.getText().toString().trim().equalsIgnoreCase("")){
            mensaje="El campo celular esta vacío";
            comprobar=false;
        }
        else if(editDireccion.getText().toString().trim().equalsIgnoreCase("")){
            mensaje="El campo direccion esta vacío";
            comprobar=false;
        }
        else if(editPassword.getText().toString().trim().equalsIgnoreCase("")){
            mensaje="El campo contraseña esta vacío";
            comprobar=false;
        }


        if(!comprobar){
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }
        return comprobar;

    }
    public void comprobarClave(){
        ConsumoServicios servicio=new ManagerRetrofit(this).getConsumoServicio();

        Call<List<Usuario>> res=servicio.getUser(correo,editPassword.getText().toString().trim());

        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.body()==null || response.body().size()==0){
                    Toast.makeText(DatosPersonalesActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }else{
                    actuzalizarDatos();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });

    }
    public void actuzalizarDatos(){
        ConsumoServicios servicio=new ManagerRetrofit(this).getConsumoServicio();
        Call<ResponseBody> res=servicio.datosPersonales(
                editNom1.getText().toString().trim(),
                editNom2.getText().toString().trim(),
                editApll1.getText().toString().trim(),
                editApll2.getText().toString().trim(),
                editMovil.getText().toString().trim(),
                editDireccion.getText().toString().trim(),
                correo,
                editPassword.getText().toString().trim()


        );

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                inicioActivity();

                Toast.makeText(DatosPersonalesActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
        }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @TargetApi(23)
    public void inicioActivity(){


        Intent intent=new Intent(this,InicioActivity.class);
        intent.putExtra(InicioSesionActivity.KEY_CORREO,correo);
        intent.putExtra(InicioSesionActivity.KEY_ID,id);
        intent.putExtra(InicioSesionActivity.KEY_ROL,rol);

        Pair pair=new Pair(viewIcono,"inicio_icon_user_t");

        ActivityOptions op=ActivityOptions.makeSceneTransitionAnimation(this,pair);

        startActivity(intent,op.toBundle());
        finish();
    }

    public void cargarUsuario(){
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);

        ConsumoServicios servicio=new ManagerRetrofit(this).getConsumoServicio();
        Call<List<Usuario>> res=servicio.getUser(correo,datos.getString(InicioSesionActivity.KEY_PASSWORD,""));

        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                llenarCampos(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }

    public void llenarCampos(Usuario user){
        editNom1.setText(user.getNombre1());
        editNom2.setText(user.getNombre2());
        editApll1.setText(user.getApellido1());
        editApll2.setText(user.getApellido2());
        editMovil.setText(user.getMovil());
        editDireccion.setText(user.getDireccion());
    }

    public void cerrarTeclado(EditText editText){
        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
}
