package com.example.usuario.astrodomus.dialogs.atributos;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.usuario.astrodomus.R;

public class AireAcondicionadoDialog extends Dialog{

    private Context context;
    private Animation animation;
    private RelativeLayout contenedor;
    private Button btonMenos, btonMas,btonListo;
    private Switch switchPercianas;
    private TextView textTemperatura;

    public AireAcondicionadoDialog (Context context){
        super(context);
        this.context=context;


        setContentView(R.layout.dialog_atributos_aire_acondicionado);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews();
    }
    private void findViews(){
        contenedor=this.findViewById(R.id.ll_dg_contenedor);
        btonListo=this.findViewById(R.id.ll_dg_bton_listo);
        btonMas=this.findViewById(R.id.dg_atri_bton_mas);
        btonMenos=this.findViewById(R.id.dg_atri_bton_menos);
        switchPercianas=this.findViewById(R.id.dg_atri_switch1);
        textTemperatura=this.findViewById(R.id.dg_atri_text_temperatura);

    }

    public void showDialog(){
        animarEntrada();
        show();

        this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                animarSalida();
            }
        });
    }

    private void animarEntrada(){
        animation= AnimationUtils.loadAnimation(context,R.anim.aparecer_desde_centro);
        animation.setFillAfter(true);
        contenedor.startAnimation(animation);

    }
    public void animarSalida(){
        animation= AnimationUtils.loadAnimation(context,R.anim.desaparecer_por_centro);
        animation.setFillAfter(true);
        contenedor.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void setTextTemperatura(String text){
        textTemperatura.setText(text+"C°");
    }

    public Button getBtonMenos() {
        return btonMenos;
    }

    public Button getBtonMas() {
        return btonMas;
    }

    public Button getBtonListo() {
        return btonListo;
    }

    public Switch getSwitchPercianas() {
        return switchPercianas;
    }
}
