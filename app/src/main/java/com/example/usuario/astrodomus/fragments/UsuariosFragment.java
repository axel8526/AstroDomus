package com.example.usuario.astrodomus.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.adapters.AdapterUsuarios;
import com.example.usuario.astrodomus.control.EnviarMail;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.dialogs.ConfirmarAccionDialog;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerListaUsuarios;
import com.example.usuario.astrodomus.interfaces.MensajeEnviado;
import com.example.usuario.astrodomus.models.Rol;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment implements ListenerListaUsuarios,MensajeEnviado {


    private Spinner sRol;
    private TextView linkCargaUsers;
    private EditText editId, editCorreo;
    private Button btonCrearUsuario;
    private Dialog dialogLoading, dgListUser;
    private String admin, busqueda;

    public UsuariosFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_crear_usuario, container, false);

        findViews(v);

        if(getArguments()!=null){
            admin=getArguments().getString(InicioSesionActivity.KEY_CORREO);
        }

        if(getActivity()!=null){
            setRoles();
            botones();

        }

        return v;
    }
    public void findViews(View v){
        sRol=v.findViewById(R.id.spinner_rol);
        linkCargaUsers=v.findViewById(R.id.link_lista_usuarios);
        editCorreo=v.findViewById(R.id.cu_correo);
        editId=v.findViewById(R.id.cu_id);
        btonCrearUsuario=v.findViewById(R.id.cu_bton_crear);
    }
    public void botones(){
        linkCargaUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dgListUser=new Dialog(getActivity());
                dgListUser.setContentView(R.layout.dialog_usuarios_registrados);
                rv=dgListUser.findViewById(R.id.rv_usuarios);
                busqueda="";
                abrirLista();

                final EditText editConsulta=dgListUser.findViewById(R.id.lu_edit_buscar);
                final TextView tClear=dgListUser.findViewById(R.id.lu_bton_clear);


                dgListUser.findViewById(R.id.lu_bton_buscar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tClear.setText("X");
                        busqueda=editConsulta.getText().toString().trim();
                        abrirLista();
                        cerrarTeclado(editConsulta);

                    }
                });

                tClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editConsulta.setText("");
                        busqueda="";
                        tClear.setText("");
                        abrirLista();

                    }
                });

                dgListUser.show();
            }
        });

        btonCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    if(verificarCampos()){
                        cerrarTeclado(editId);
                        comprobarUsuario(getUser());
                    }
            }
        });
    }
    public void vaciarCampos(){
        editCorreo.setText("");
        editId.setText("");
        sRol.setSelection(0);
    }

    public boolean verificarCampos(){
        String mensaje=null;
        boolean comprobar=true;

        if(editId.getText().toString().trim().equals("")){
            mensaje="El campo id esta vacío";
            comprobar=false;

        }
        if(editCorreo.getText().toString().trim().equals("")){
            mensaje="El campo correo esta vacío";
            comprobar=false;
        }else{
            String correo=editCorreo.getText().toString();
            int c=0;
            int punto=0;
            int posicion=0;

            for(int i=0; i<correo.length();i++){
                if(correo.charAt(i)=='@'){
                    c++;
                    posicion=i;
                }
                if(posicion!=0) {
                    //String nuevo=correo.substring(posicion, correo.length() - 1);
                    if (correo.charAt(i)=='.') {
                        punto++;
                    }
                }
            }

            if(c!=1 || punto==0){
                mensaje="Correo invalido";
                comprobar=false;
            }
        }

        if(mensaje!=null) Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();

          return comprobar;
    }
    public void mostrarDialogCargando(){
        dialogLoading=new Dialog(getActivity());
        dialogLoading.setContentView(R.layout.dialog_cargando_oscuroo);
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLoading.setCanceledOnTouchOutside(false);

        ProgressBar progressBar=dialogLoading.findViewById(R.id.dg_loading_o);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        dialogLoading.show();

    }
    public Usuario getUser(){
        Usuario usuario=new Usuario();

        usuario.setId(editId.getText().toString().trim());
        usuario.setCorreo(editCorreo.getText().toString().trim());
        usuario.setRol(sRol.getSelectedItem().toString());

        return usuario;
    }
    public void comprobarUsuario(final Usuario usuario)
    {
        mostrarDialogCargando();

        ConsumoServicios servicio=new ManagerRetrofit(getActivity()).getConsumoServicio();
        Call<List<Usuario>> res=servicio.comprobarUser(usuario.getCorreo());
        
        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.body()==null || response.body().size()==0){
                    guardarUsuario(usuario);
                }else{
                    dialogLoading.dismiss();
                    Toast.makeText(getActivity(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }


    public void guardarUsuario(final Usuario usuario){
        ConsumoServicios servicio=new ManagerRetrofit(getActivity()).getConsumoServicio();

        Call<ResponseBody> res=servicio.crearUsuario(
                usuario.getId(),
                usuario.getRol(),
                usuario.getCorreo()

        );
        final Fragment fragment=this;
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                EnviarMail enviarMail=new EnviarMail(fragment);
                enviarMail.setCorreoReceptor(usuario.getCorreo());
                enviarMail.enviar2();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
    public void cerrarTeclado(EditText editText){
        InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public void setRoles(){
        ManagerRetrofit mRetrofit=new ManagerRetrofit(getActivity());
        ConsumoServicios servicio=mRetrofit.getConsumoServicio();

        Call<List<Rol>> res=servicio.getRol();

        res.enqueue(new Callback<List<Rol>>() {
            @Override
            public void onResponse(Call<List<Rol>> call, Response<List<Rol>> response) {
                cargarRoles(response.body());
            }

            @Override
            public void onFailure(Call<List<Rol>> call, Throwable t) {

            }
        });

    }
    public void cargarRoles(List<Rol> roles){
        List<String> nombresRol=new ArrayList<>();
        for (int i=0; i<roles.size();i++){
            nombresRol.add(roles.get(i).getNomrol());
        }
        ArrayAdapter adapter=new ArrayAdapter(getActivity(),R.layout.spinner_rol_item,nombresRol);
        sRol.setAdapter(adapter);

    }

    public void cargarDatosUsuarios(){
        ConsumoServicios servicio=new ManagerRetrofit(getActivity()).getConsumoServicio();
        Call<List<Usuario>> res=servicio.getUsuarios(admin,busqueda);

        res.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                cargarLista(response.body());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });

    }
    private RecyclerView rv;
    public void cargarLista(List<Usuario> usuarios){
        AdapterUsuarios adapter=new AdapterUsuarios(getContext(),usuarios,this);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity().getApplicationContext());

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);


    }

    @Override
    public void actualizaLista() {
        cargarDatosUsuarios();
    }

    @Override
    public void clickItem(final Usuario usuario) {
        LinearLayout conInfo=dgListUser.findViewById(R.id.ui_contenedor_datos);
        TextView tNombre,tRol,tId, tCorreo, tMovil, tDireccion;

        tNombre=dgListUser.findViewById(R.id.ui_nombre);
        tRol=dgListUser.findViewById(R.id.ui_rol);
        tId=dgListUser.findViewById(R.id.ui_id);
        tCorreo=dgListUser.findViewById(R.id.ui_correo);
        tMovil=dgListUser.findViewById(R.id.ui_movil);
        tDireccion=dgListUser.findViewById(R.id.ui_direccion);


        String nombre="SIN REGISTRAR";
        if(usuario.getNombre1()!=null)
        nombre=
                usuario.getNombre1()+" "
                +(usuario.getNombre2().equalsIgnoreCase("")?"":usuario.getNombre2()+" ")
                +usuario.getApellido1()+" "
                +(usuario.getApellido2().equalsIgnoreCase("")?"":usuario.getApellido2()+" ");

        tNombre.setText(nombre);
        tRol.setText(usuario.getRol());
        tCorreo.setText(usuario.getCorreo());
        tMovil.setText(usuario.getMovil());
        tDireccion.setText(usuario.getDireccion());
        tId.setText(usuario.getId());



        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        conInfo.setLayoutParams(lp);

        dgListUser.findViewById(R.id.ui_bton_atras).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirLista();
            }
        });

        dgListUser.findViewById(R.id.ui_bton_borrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarUsuario(usuario.getId());

            }
        });
    }

    public void abrirLista(){
        cargarDatosUsuarios();
        LinearLayout conInfo=dgListUser.findViewById(R.id.ui_contenedor_datos);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        conInfo.setLayoutParams(lp);

    }


    @Override
    public void mensajeEnviado(boolean envio) {
        dialogLoading.dismiss();
        Toast.makeText(getActivity(), "Usuario creado", Toast.LENGTH_SHORT).show();
        vaciarCampos();
    }

    public void eliminarUsuario(final String id){

        final ConfirmarAccionDialog confirmarAccionDialog=new ConfirmarAccionDialog(getActivity(),
                "¿Eliminar usuario?","Esta acción eliminara esta cuenta",false);

        confirmarAccionDialog.abrirDialog();

        confirmarAccionDialog.getBtonCancelar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarAccionDialog.cerrarDialog();
            }
        });

        confirmarAccionDialog.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar(id);
                abrirLista();

                confirmarAccionDialog.cerrarDialog();
            }
        });

    }
    public void eliminar(String id){
        ConsumoServicios servicio =new ManagerRetrofit(getActivity()).getConsumoServicio();
        Call<ResponseBody> res=servicio.eliminarUsuario(id);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getActivity(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
                actualizaLista();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
