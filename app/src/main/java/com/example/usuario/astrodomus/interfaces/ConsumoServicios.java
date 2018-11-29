package com.example.usuario.astrodomus.interfaces;

import com.example.usuario.astrodomus.models.Ambiente;
import com.example.usuario.astrodomus.models.Componente;
import com.example.usuario.astrodomus.models.Rol;
import com.example.usuario.astrodomus.models.Usuario;

import java.util.List;

import javax.security.auth.callback.Callback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ConsumoServicios {

    @GET("inicio_sesion.php")
    Call<List<Usuario>> getUser(@Query("id")String id,@Query("clave")String clave);

    @GET("roles.php")
    Call<List<Rol>> getRol();

    @GET("get_users.php")
    Call<List<Usuario>> getUsuarios(@Query("admin")String id,
                                    @Query("busqueda")String busqueda);

    @GET("eliminar_usuario.php")
    Call<ResponseBody> eliminarUsuario(@Query("id_user") String id);

    @GET("crear_usuario.php")
    Call<ResponseBody> crearUsuario(@Query("codusuario")String codusuario,
                                    @Query("nomrol") String codrol,
                                    @Query("correo") String correo
    );

    @GET("comprobar_user.php")
    Call<List<Usuario>> comprobarUser(@Query("correo") String correo);

    @GET("datos_personales.php")
    Call<ResponseBody> datosPersonales(@Query("nom1") String nom1,
                                       @Query("nom2") String nom2,
                                       @Query("apll1") String apll1,
                                       @Query("apll2") String apll2,
                                       @Query("movil") String movil,
                                       @Query("direccion") String direccion,
                                       @Query("correo") String correo,
                                       @Query("clave") String clave);

    @GET("change_password.php")
    Call<ResponseBody> updatePassword(@Query("password") String password,
                                      @Query("correo") String correo);


    @GET("get_ambientes.php")
    Call<List<Ambiente>> getAmbientes(@Query("rol") String rol);

    @GET("cambiar_estado_ambientes.php")
    Call<ResponseBody> cambiarEstadoAmbiente(@Query("id_ambiente") String idAmbiente,
                                             @Query("estado_ac") String estadoAc);


    @GET("get_componentes.php")
    Call<List<Componente>> getComponentes(@Query("id_ambiente") String idAmbiente);

    @GET("get_atributos.php")
    Call<List<Componente>> getAtributos(@Query("id_componente") String idComponente);


    @GET("set_estados_componente.php")
    Call<ResponseBody> enviarEstadoAtributo(@Query("estado") String estado,
                                            @Query("id_ambiente") String idAmbiente,
                                            @Query("id_componente") String idComponente,
                                            @Query("id_atributo") String idAtributo);
}
