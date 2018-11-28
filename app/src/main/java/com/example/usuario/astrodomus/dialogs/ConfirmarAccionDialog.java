package com.example.usuario.astrodomus.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;

public class ConfirmarAccionDialog {


    private Button btonConfirmar, btonCancelar;
    private TextView viewTitulo, viewSubtitulo;
    private String titulo, subtitulo;
    private Dialog dialog;
    private Context context;



    public ConfirmarAccionDialog(Context context, String titulo, String subtitulo){
        this.context=context;
        this.titulo=titulo;
        this.subtitulo=subtitulo;

    }

    public void abrirDialog(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_eliminar_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViews(dialog);
        cargarDatos();
        dialog.show();

    }
    private void findViews(Dialog dialog){
        btonCancelar=dialog.findViewById(R.id.dg_bton_cancelar);
        btonConfirmar=dialog.findViewById(R.id.dg_bton_aceptar);
        viewTitulo=dialog.findViewById(R.id.action_dg_text_accion);
        viewSubtitulo=dialog.findViewById(R.id.accion_dg_texto_compl);

    }

    public void cerrarDialog2(){
        btonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void cerrarDialog(){
        dialog.dismiss();
    }
    private void cargarDatos(){
        viewTitulo.setText(titulo);
        viewSubtitulo.setText(subtitulo);
    }



    public Button getBtonConfirmar() {
        return btonConfirmar;
    }

    public Button getBtonCancelar() {
        return btonCancelar;
    }
}
