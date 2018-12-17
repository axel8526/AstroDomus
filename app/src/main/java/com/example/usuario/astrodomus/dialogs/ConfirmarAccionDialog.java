package com.example.usuario.astrodomus.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.utils.AnimDialog;

public class ConfirmarAccionDialog {


    private Button btonConfirmar, btonCancelar;
    private TextView viewTitulo, viewSubtitulo, viewBarraSuperior,viewBarraInferior;
    private String titulo, subtitulo;
    private Dialog dialog;
    private ImageView viewIcono;
    private Context context;
    private EditText editText;
    private RelativeLayout contenedor;
    private Animation animation;
    private AnimDialog animDialog;



    public ConfirmarAccionDialog(Context context, String titulo, String subtitulo,boolean mostrarEditText){
        this.context=context;
        this.titulo=titulo;
        this.subtitulo=subtitulo;

        dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_eliminar_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        findViews(dialog);
        mostrarEditText(mostrarEditText);
        animDialog=new AnimDialog(context,dialog);

    }
    private void animarEntrada(){
        animDialog.animarEntrada(contenedor);
    }
    private void animarSalida(){
        animDialog.animarSalida(contenedor);
    }


    public void abrirDialog(){
        cargarDatos();
        animarEntrada();
        dialog.show();

    }
    private void findViews(Dialog dialog){
        btonCancelar=dialog.findViewById(R.id.dg_bton_cancelar);
        btonConfirmar=dialog.findViewById(R.id.dg_bton_aceptar);
        viewTitulo=dialog.findViewById(R.id.action_dg_text_accion);
        viewSubtitulo=dialog.findViewById(R.id.accion_dg_texto_compl);

        viewBarraInferior=dialog.findViewById(R.id.action_dg_barra_inferior);
        viewBarraSuperior=dialog.findViewById(R.id.accion_dg_barra_superior);
        viewIcono=dialog.findViewById(R.id.accion_dg_icono_grande);
        editText=dialog.findViewById(R.id.accion_dg_edittext);
        contenedor=dialog.findViewById(R.id.accion_dg_contenedor);
    }
    private void mostrarEditText(boolean mostrar){

        LinearLayout.LayoutParams params0=new LinearLayout.LayoutParams(0,0);

        if (mostrar){
            viewSubtitulo.setLayoutParams(params0);

        }else{
            editText.setLayoutParams(params0);


        }
    }

    public void cerrarDialog2(){
        btonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animarSalida();
            }
        });
    }

    public void cerrarDialog(){
        animarSalida();
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


    public void setTextBtonConfirmar(String text){
        btonConfirmar.setText(text);
    }
    public void setTextBtonCancelar(String text){
        btonCancelar.setText(text);
    }

    public void setTheme(int fondoIcono,int icon, int color){
        viewIcono.setBackgroundResource(fondoIcono);
        viewIcono.setImageResource(icon);



        viewBarraSuperior.setBackgroundColor(context.getResources().getColor(color));
        viewBarraInferior.setBackgroundColor(context.getResources().getColor(color));
        btonConfirmar.setBackgroundColor(context.getResources().getColor(color));
    }

    public void cancelarAlTocarFuera(boolean e){
        dialog.setCanceledOnTouchOutside(e);
    }

    public EditText getEditText() {
        return editText;
    }
}
