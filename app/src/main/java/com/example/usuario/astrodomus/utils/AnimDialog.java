package com.example.usuario.astrodomus.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.usuario.astrodomus.R;

public class AnimDialog {

    private Animation animation;
    private Context context;
    private Dialog dialog;

    public AnimDialog(Context context, Dialog dialog){
        this.context=context;
        this.dialog=dialog;
    }
    public AnimDialog(Context contex){
        context=contex;
    }
    public void animarEntrada(View v){
        animation= AnimationUtils.loadAnimation(context, R.anim.aparecer_desde_centro);
        animation.setFillAfter(true);

        v.startAnimation(animation);
    }


    public void animarSalida(View v){
        animation=AnimationUtils.loadAnimation(context,R.anim.desaparecer_por_centro);
        animation.setFillAfter(true);

        v.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
