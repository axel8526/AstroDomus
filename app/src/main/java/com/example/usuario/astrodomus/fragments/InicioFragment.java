package com.example.usuario.astrodomus.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.astrodomus.R;
import com.example.usuario.astrodomus.activities.HomeActivity;
import com.example.usuario.astrodomus.interfaces.ComunicaFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {


    private LinearLayout btonUser;
    private LinearLayout[] botonesHome;
    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_inicio, container, false);
        findVies(view);

        if(getActivity()!=null){

            listenerBotones();
        }

        return view;
    }

    public void findVies(View v){
        botonesHome=new LinearLayout[5];
        botonesHome[0]=v.findViewById(R.id.home_bton_user);
        botonesHome[1]=v.findViewById(R.id.home_bton_control);
        botonesHome[2]=v.findViewById(R.id.home_bton_perfil);
        botonesHome[3]=v.findViewById(R.id.home_bton_componente);
        botonesHome[4]=v.findViewById(R.id.home_bton_reporte);


    }
    public void listenerBotones(){
        for(int i=0; i<botonesHome.length; i++){
            final int fragment=i+1;
            botonesHome[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), ""+fragment, Toast.LENGTH_SHORT).show();
                    ((ComunicaFragment)getActivity()).activarFragment(fragment);
                }
            });
        }
    }

}
