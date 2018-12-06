package com.example.usuario.astrodomus.control;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.DatosPersonalesActivity;
import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.activities.InicioSesionActivity;
import com.example.usuario.astrodomus.fragments.InicioUsuarioFragment;
import com.example.usuario.astrodomus.models.Ambiente;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificacionAmbiente {


    private Context context;
    private Ambiente ambiente;

    private NotificationCompat.Builder builder;
    public static final String APAGAR_="APAGAR";
    public static final String KEY_AMBIENTE="KEY_AMBI";

    public static final int ID_NOT=7;


    public NotificacionAmbiente(Context context, Ambiente ambiente){
        this.ambiente=ambiente;
        this.context=context;
         builder= new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);

    }
    public NotificacionAmbiente(Context context){
        this.context=context;
        builder= new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);
    }

    public void crearNotificacion(String rol, String id, String correo){
        Intent intent=new Intent(context, InicioSesionActivity.class);
        intent.setAction(APAGAR_);
        intent.putExtra(InicioSesionActivity.KEY_ROL,rol);
        intent.putExtra(InicioSesionActivity.KEY_CORREO,correo);
        intent.putExtra(InicioSesionActivity.KEY_ID,id);
        intent.putExtra(KEY_AMBIENTE,ambiente.getIdAmbiente());

        PendingIntent pI = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        builder.setContentText("Pulsar para apagar")
                .setContentTitle(ambiente.getNombreAmbiente())
                .setContentIntent(pI)
                .setContentInfo("Iniciado")
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.icon_control)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);


    }


    public void mostrar(){
        NotificationManager managerL = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        managerL.notify(ID_NOT, builder.build());

    }
    public void cerrar(){
        NotificationManager managerL = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        //managerL.notify(ID_NOT, builder.build());
        managerL.cancel(ID_NOT);
    }

}
