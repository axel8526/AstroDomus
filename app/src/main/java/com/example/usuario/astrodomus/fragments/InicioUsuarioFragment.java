package com.example.usuario.astrodomus.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioUsuarioFragment extends Fragment {


    LinearLayout btonControl, btonPerfil;
    public InicioUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v =inflater.inflate(R.layout.fragment_inicio_usuario, container, false);
        findVies(v);
        return v;
    }
    public void findVies(View v){
        listenerBotones((LinearLayout) v.findViewById(R.id.home_bton_control), HomeActivity.FRAG_CONTROL);
        listenerBotones((LinearLayout) v.findViewById(R.id.home_bton_perfil), HomeActivity.FRAG_PERFIL);

    }
    public void listenerBotones(LinearLayout linear,final int fragment){
        for(int i=0; i<2; i++){
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ComunicaFragment)getActivity()).activarFragment(fragment);
                }
            });
        }
    }

}
