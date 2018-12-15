package com.example.usuario.astrodomus.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.NotificacionAmbiente;
import com.example.usuario.astrodomus.fragments.ControlFragment;
import com.example.usuario.astrodomus.fragments.InicioUsuarioFragment;
import com.example.usuario.astrodomus.fragments.PerfilFragment;
import com.example.usuario.astrodomus.fragments.UsuariosFragment;
import com.example.usuario.astrodomus.fragments.InicioFragment;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;
import com.example.usuario.astrodomus.interfaces.ListenerAmbiente;
import com.example.usuario.astrodomus.models.Ambiente;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ComunicaFragment, ListenerAmbiente{

    public static final int FRAG_REPORTE=5;
    public static final int FRAG_COMPONENE=4;
    public static final int FRAG_PERFIL=3;
    public static final int FRAG_CONTROL=2;
    public static final int FRAG_USER=1;
    public static final int FRAG_HOME=0;

    private int fragActivo;
    private Toolbar toolbar;
    private String correo,id,rol;
    private boolean salirApp;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.getMenu().removeItem(R.id.nav_controles);



        //TextView view=findViewById(R.id.nav_nombre_user);
        //view.setText("Alexander Casanova");
        //TextView viw=findViewById(R.id.nav_rol_user);
        //viw.setText("ADMINISTRADOR");

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrincipalOscuro));



        datosUsuario();
        comprobarAmbienteUsuario();
        activarFragment(FRAG_HOME);





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragActivo!=0){
                activarFragment(FRAG_HOME);
            }else {

                if(!salirApp){
                    Toast.makeText(this, "Otra vez para salir", Toast.LENGTH_SHORT).show();
                    salirApp=true;
                }else{
                    super.onBackPressed();
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        TextView textCorreo=drawer.findViewById(R.id.nav_rol_user);
        textCorreo.setText(correo);
        TextView textRol=drawer.findViewById(R.id.nav_nombre_user);
        textRol.setText(rol);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cerrar_sesion) {
            cerrarSesion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_usuarios) {
            activarFragment(FRAG_USER);
            // Handle the camera action
        } else if (id == R.id.nav_controles) {
            activarFragment(FRAG_CONTROL);

        } else if (id == R.id.nav_perfiles) {
            activarFragment(FRAG_PERFIL);

        } else if (id == R.id.nav_componentes) {
            activarFragment(FRAG_COMPONENE);

        } else if (id == R.id.nav_reportes) {
            activarFragment(FRAG_REPORTE);

        } else if (id == R.id.nav_datos_person) {
            abrirActivity(DatosPersonalesActivity.class);
        }else if(id == R.id.nav_seguridad){
            abrirActivity(ChangePasswordActivity.class);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }
    public void datosUsuario(){
        Bundle datos=getIntent().getExtras();
        if(datos!=null){
            correo=datos.getString(InicioSesionActivity.KEY_CORREO);
            rol=datos.getString(InicioSesionActivity.KEY_ROL);
            id=datos.getString(InicioSesionActivity.KEY_ID);

            iniciaMenu();

        }
    }
    public void onResume(){
        super.onResume();
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);



    }
    public boolean isAdmin(){
        if(rol.equalsIgnoreCase("administrador")){
            return true;
        }
        return false;
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
    public Fragment getFragment(int frag){
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
    public void activarFragment(int fragment) {
        iniciarFragment(fragment);
        navegationSelecionarItem(fragment);
    }
    public void navegationSelecionarItem(int frag){
        switch (frag){
            case FRAG_USER:
                navigationView.setCheckedItem(R.id.nav_usuarios);
                toolbar.setTitle("Usuarios");
            break;
            case FRAG_CONTROL:
                navigationView.setCheckedItem(R.id.nav_controles);
                toolbar.setTitle("Controles");
                break;
            case FRAG_PERFIL:
                navigationView.setCheckedItem(R.id.nav_perfiles);
                toolbar.setTitle("Perfiles");
                break;
            case FRAG_COMPONENE:
                navigationView.setCheckedItem(R.id.nav_componentes);
                toolbar.setTitle("Componentes");
                break;
            case FRAG_REPORTE:
                navigationView.setCheckedItem(R.id.nav_reportes);
                toolbar.setTitle("Reportes");
                break;

                default: iniciaMenu();

        }

    }


    public void iniciaMenu(){
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_home_drawer);
        toolbar.setTitle("");
        if(!isAdmin()){
            navigationView.getMenu().removeItem(R.id.nav_reportes);
            navigationView.getMenu().removeItem(R.id.nav_componentes);
            navigationView.getMenu().removeItem(R.id.nav_usuarios);
        }
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
    public void abrirActivity(Class clase){
        Intent intent=new Intent(this,clase);
        intent.putExtra(InicioSesionActivity.KEY_CORREO,correo);
        intent.putExtra(InicioSesionActivity.KEY_ID,id);
        intent.putExtra(InicioSesionActivity.KEY_ROL,rol);
        intent.putExtra(InicioSesionActivity.KEY_ACTIVITY,1);

        startActivity(intent);
        finish();

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

}
