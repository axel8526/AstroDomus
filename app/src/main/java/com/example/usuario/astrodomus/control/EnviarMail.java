package com.example.usuario.astrodomus.control;

import android.app.Application;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.usuario.astrodomus.interfaces.MensajeEnviado;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviarMail {

    private Session session;
    private static final String CORREO="astrodomus.adsi@gmail.com";
    private static final String PASSWORD="axel1998";
    private Object clase;

    public EnviarMail(Object clase){
        this.clase=clase;
    }

    private String correoReceptor;

    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    private String getMensajeCuentaCreada(){
        return "Bienvenido, se ha creado una cuenta en la plataforma de AstroDomus. Para iniciar sesion podrá hacerlo " +
                "con este correo electronico o su identificación.<br><br>" +
                "Correo: "+correoReceptor+"<br> Contraseña: astrodomus<br><br>" +
                "NOTA: Al iniciar sesion en el aplicativo le mostrara un formulario donde debera completar con sus datos personales para poder" +
                "acceder a las funciones de la aplicación.";
    }
    private class EnviarCorreo extends AsyncTask<String,Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {

            String mensaje = strings[0];

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
            StrictMode.setThreadPolicy(policy);
            Properties properties = new Properties();

            properties.put("mail.smtp.host", "smtp.googlemail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");


            try {
                session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(CORREO, PASSWORD);
                    }
                });

                if (session != null) {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(CORREO));
                    message.setSubject("Cuenta AstroDomus");
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoReceptor));
                    message.setContent(mensaje, "text/html; charset=utf-8");
                    Transport.send(message);
                    return true;
                }

            } catch (Exception e) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ((MensajeEnviado)clase).mensajeEnviado(aBoolean);
        }
    }
    public void enviar2(){
        EnviarCorreo enviarCorreo=new EnviarCorreo();
        enviarCorreo.execute(getMensajeCuentaCreada());

    }

    public void enviarCodigo(String codigo){
        EnviarCorreo enviarCorreo=new EnviarCorreo();
        enviarCorreo.execute(getMensajeRestablecerPassword(codigo));

    }

    public String getMensajeRestablecerPassword(String codigo){

        return "Se ha solicitado un cogido para restablecar contraseña. <br><br> CODIGO: "+codigo+"<br>" +
                "<br>Si usted no solicito este cambio, ignore el correo";
    }

}
