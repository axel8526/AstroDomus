package com.example.usuario.astrodomus.dialogs.atributos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.control.ControlAmbiente;
import com.example.usuario.astrodomus.control.componentes.CtrolLuzLed;
import com.example.usuario.astrodomus.interfaces.ListenerListaComponente;
import com.example.usuario.astrodomus.models.Componente;
import com.example.usuario.astrodomus.utils.AnimDialog;

public class LuzLedDialog extends Dialog{

    private Context context;
    private Animation animation;
    private SeekBar seekBar;
    private Button btonListo;
    private RelativeLayout contenedor;
    private TextView textPorcentaje;
    private AnimDialog animDialog;


    public LuzLedDialog (Context context){
        super(context);
        this.context=context;
        setContentView(R.layout.dialog_atributos_luz);

        animDialog=new AnimDialog(context,this);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        findViews();
    }


    private void findViews(){
        contenedor=this.findViewById(R.id.ll_dg_contenedor);
        seekBar=this.findViewById(R.id.ll_dg_seekbar);
        btonListo=this.findViewById(R.id.ll_dg_bton_listo);
        textPorcentaje=this.findViewById(R.id.dg_atributo_porcentaje);
    }

    public void showDialog(){
        animarEntrada();

        show();
    }

    private void animarEntrada(){
        animDialog.animarEntrada(contenedor);

    }
    public void animarSalida(){
        animDialog.animarSalida(contenedor);
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public Button getBtonListo() {
        return btonListo;
    }

    public void cargarPorcentaje(String pocentaje){
        textPorcentaje.setText(pocentaje+"%");
    }
}
