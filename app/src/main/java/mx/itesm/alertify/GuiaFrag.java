package mx.itesm.alertify;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import mx.itesm.alertify.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuiaFrag extends Fragment {


    public GuiaFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Objects.requireNonNull(getActivity()).setTitle("Guía");

        return inflater.inflate(R.layout.fragment_guia, container, false);
    }

}
