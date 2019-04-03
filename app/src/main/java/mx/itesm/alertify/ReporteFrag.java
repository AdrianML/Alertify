package mx.itesm.alertify;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mx.itesm.alertify.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReporteFrag extends Fragment {

    private EditText etTitulo;
    private EditText etFecha;
    private EditText etHora;
    private EditText etDesc;
    private Button btnEnviar;

    public ReporteFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reporte, container, false);
        etTitulo = v.findViewById(R.id.etTitulo);
        etFecha = v.findViewById(R.id.etFecha);
        etHora = v.findViewById(R.id.etHora);
        etDesc = v.findViewById(R.id.etDesc);
        btnEnviar = v.findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirReporte();
                // Limpiar la pantalla después de subir el reporte.
                etTitulo.setText("");
                etFecha.setText("");
                etHora.setText("");
                etDesc.setText("");

                //Toast.makeText(LupaActiv.this, "¡Ganaste en " + movimientos + " movimientos!", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Reporte Enviado", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    public void subirReporte(){
        String titulo = etTitulo.getText().toString();
        String fecha = etFecha.getText().toString();
        String hora = etHora.getText().toString();
        String desc = etDesc.getText().toString();

        Report newReport = new Report(titulo, fecha, hora, desc);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ruta = database.getReference("Reporte/" + titulo + "/"); //Tabla
        ruta.setValue(newReport); //Contenido

    }
}