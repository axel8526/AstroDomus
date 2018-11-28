package com.example.usuario.astrodomus.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.control.ManagerRetrofit;
import com.example.usuario.astrodomus.dialogs.ConfirmarAccionDialog;
import com.example.usuario.astrodomus.holders.HolderUser;
import com.example.usuario.astrodomus.interfaces.ConsumoServicios;
import com.example.usuario.astrodomus.interfaces.ListenerListaUsuarios;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterUsuarios extends RecyclerView.Adapter<HolderUser> {

    private List<Usuario> usuarios;
    private Context context;
    private Fragment fragment;

    public AdapterUsuarios(Context context, List<Usuario> usuarios, Fragment fragment){

        this.context=context;
        this.usuarios=usuarios;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public HolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.item_usuario,parent,false);

        return new HolderUser(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final HolderUser holder, int position) {
            final Usuario user=usuarios.get(position);



            if(user.getNombre1()==null || user.getNombre1().equalsIgnoreCase("")){
               holder.getIdUser().setText("[SIN REGISTRAR]");
            }else{
                holder.getIdUser().setText("");
            }

            holder.getNombreUser().setText(user.getCorreo());
            holder.getRolUser().setText(user.getRol());


            //eliminarUsuario(holder.getBotonElinimar(),user.getId());

            holder.getBotonElinimar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   eliminarUsuario2(user);
                }
            });

            clickUsuario(holder.getContenedor(),user);

    }
    public void clickUsuario(View view,final Usuario user){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListenerListaUsuarios)fragment).clickItem(user);
            }
        });
    }

    public void eliminarUsuario2(final Usuario usuario){
        final ConfirmarAccionDialog confirmarAccionDialog=new ConfirmarAccionDialog(context,
                "¿Eliminar usuario?",
                        "Esta acción eliminara esta cuenta");

        confirmarAccionDialog.abrirDialog();


        confirmarAccionDialog.getBtonConfirmar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar(usuario.getId());
                confirmarAccionDialog.cerrarDialog();
            }
        });
        confirmarAccionDialog.getBtonCancelar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarAccionDialog.cerrarDialog();


            }
        });
    }



    public void eliminarUsuario(ImageView view, final String id){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog_eliminar_user);

                dialog.findViewById(R.id.dg_bton_aceptar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eliminar(id);
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.dg_bton_cancelar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public void eliminar(String id){
        ConsumoServicios servicio =new ManagerRetrofit(context).getConsumoServicio();
        Call<ResponseBody> res=servicio.eliminarUsuario(id);
        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                ((ListenerListaUsuarios)fragment).actualizaLista();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }
}
