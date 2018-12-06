package com.example.usuario.astrodomus.hilos;

import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.fragments.ControlFragment;

public class EstadoAmbienteHilo extends Thread {

    private boolean estado;
    private ControlAmbiente controlAmbiente;
    private String idUser;

    public EstadoAmbienteHilo(ControlAmbiente ctrolAmbient, String idUser){
        this.controlAmbiente=ctrolAmbient;
        this.idUser=idUser;
        estado=true;
    }
    @Override
    public void run() {
        while(estado){
            controlAmbiente.usuarioAlojado(idUser);

            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void terminar(){
        estado=false;

    }
}
