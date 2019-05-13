package mx.itesm.alertify;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import mx.itesm.alertify.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuiaFrag extends Fragment {

    int contInicio,contAjustes,contMapa,contSeguridad,contReportes;
    TextView inicio1;
    TextView inicio2;
    TextView inicio3;
    TextView inicio4;
    TextView inicio5;
    TextView inicio6;
    TextView inicio7;
    TextView ajustes1;
    TextView ajustes2;
    TextView ajustes3;
    TextView ajustes4;
    TextView mapa1;
    TextView reportes1;
    TextView seguridad1;
    TextView seguridad2;
    TextView seguridad3;
    TextView seguridad4;
    TextView seguridad5;
    TextView seguridad6;

    Button inicio;
    Button ajustes;
    Button mapa;
    Button seguridad;
    Button reportes;

    public GuiaFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_guia, container, false);

        inicio1 = v.findViewById(R.id.inicioTV1);
        inicio2 = v.findViewById(R.id.inicioTV2);
        inicio3 = v.findViewById(R.id.inicioTV3);
        inicio4 = v.findViewById(R.id.inicioTV4);
        inicio5 = v.findViewById(R.id.inicioTV5);
        inicio6 = v.findViewById(R.id.inicioTV6);
        inicio7 = v.findViewById(R.id.inicioTV7);
        ajustes1 = v.findViewById(R.id.ajustesTV1);
        ajustes2 = v.findViewById(R.id.ajustesTV2);
        ajustes3 = v.findViewById(R.id.ajustesTV3);
        ajustes4 = v.findViewById(R.id.ajustesTV4);
        mapa1 = v.findViewById(R.id.mapaTV1);
        reportes1 = v.findViewById(R.id.reportesTV1);
        seguridad1 = v.findViewById(R.id.textView2);
        seguridad2 = v.findViewById(R.id.textView3);
        seguridad3 = v.findViewById(R.id.textView5);
        seguridad4 = v.findViewById(R.id.textView6);
        seguridad5 = v.findViewById(R.id.textView7);
        seguridad6 = v.findViewById(R.id.textView8);

        setAllInvisible();

        inicio = v.findViewById(R.id.inicio);
        ajustes = v.findViewById(R.id.ajustes);
        mapa = v.findViewById(R.id.mapa);
        seguridad = v.findViewById(R.id.seguridad);
        reportes = v.findViewById(R.id.reportes);

        contInicio=0;
        contAjustes=0;
        contMapa=0;
        contSeguridad=0;
        contReportes=0;

        Objects.requireNonNull(getActivity()).setTitle("Guía");

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contInicio==0){
                    inicio1.setVisibility(View.VISIBLE);
                    inicio2.setVisibility(View.VISIBLE);
                    inicio3.setVisibility(View.VISIBLE);
                    inicio4.setVisibility(View.VISIBLE);
                    inicio5.setVisibility(View.VISIBLE);
                    inicio6.setVisibility(View.VISIBLE);
                    inicio7.setVisibility(View.VISIBLE);
                    contInicio++;
                }

                else {
                    setInicioInvisible();
                    contInicio = 0;
                }
            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contAjustes==0){
                    ajustes1.setVisibility(View.VISIBLE);
                    ajustes2.setVisibility(View.VISIBLE);
                    ajustes3.setVisibility(View.VISIBLE);
                    ajustes4.setVisibility(View.VISIBLE);
                    contAjustes++;
                }

                else {
                    setAjustesInvisible();
                    contAjustes = 0;
                }
            }
        });

        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contMapa==0){
                    mapa1.setVisibility(View.VISIBLE);
                    contMapa++;
                }

                else {
                    setMapaInvisible();
                    contMapa = 0;
                }
            }
        });

        seguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contSeguridad==0){
                    seguridad1.setVisibility(View.VISIBLE);
                    seguridad2.setVisibility(View.VISIBLE);
                    seguridad3.setVisibility(View.VISIBLE);
                    seguridad4.setVisibility(View.VISIBLE);
                    seguridad5.setVisibility(View.VISIBLE);
                    seguridad6.setVisibility(View.VISIBLE);
                    contSeguridad++;
                }

                else {
                    setSeguridadInvisible();
                    contSeguridad = 0;
                }
            }
        });

        reportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contReportes==0){
                    reportes1.setVisibility(View.VISIBLE);
                    contReportes++;
                }

                else {
                    setReportesInvisible();
                    contReportes = 0;
                }
            }
        });

        return v;
    }

    private void setReportesInvisible() {
        reportes1.setVisibility(View.GONE);
    }

    private void setSeguridadInvisible() {
        seguridad1.setVisibility(View.GONE);
        seguridad2.setVisibility(View.GONE);
        seguridad3.setVisibility(View.GONE);
        seguridad4.setVisibility(View.GONE);
        seguridad5.setVisibility(View.GONE);
        seguridad6.setVisibility(View.GONE);
    }

    private void setMapaInvisible() {
        mapa1.setVisibility(View.GONE);
    }

    private void setAjustesInvisible() {
        ajustes1.setVisibility(View.GONE);
        ajustes2.setVisibility(View.GONE);
        ajustes3.setVisibility(View.GONE);
        ajustes4.setVisibility(View.GONE);
    }

    private void setInicioInvisible() {
        inicio1.setVisibility(View.GONE);
        inicio2.setVisibility(View.GONE);
        inicio3.setVisibility(View.GONE);
        inicio4.setVisibility(View.GONE);
        inicio5.setVisibility(View.GONE);
        inicio6.setVisibility(View.GONE);
        inicio7.setVisibility(View.GONE);
    }

    private void setAllInvisible() {
        inicio1.setVisibility(View.GONE);
        inicio2.setVisibility(View.GONE);
        inicio3.setVisibility(View.GONE);
        inicio4.setVisibility(View.GONE);
        inicio5.setVisibility(View.GONE);
        inicio6.setVisibility(View.GONE);
        inicio7.setVisibility(View.GONE);
        ajustes1.setVisibility(View.GONE);
        ajustes2.setVisibility(View.GONE);
        ajustes3.setVisibility(View.GONE);
        ajustes4.setVisibility(View.GONE);
        mapa1.setVisibility(View.GONE);
        reportes1.setVisibility(View.GONE);
        seguridad1.setVisibility(View.GONE);
        seguridad2.setVisibility(View.GONE);
        seguridad3.setVisibility(View.GONE);
        seguridad4.setVisibility(View.GONE);
        seguridad5.setVisibility(View.GONE);
        seguridad6.setVisibility(View.GONE);
    }

}
