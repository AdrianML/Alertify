package mx.itesm.alertify;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReporteFrag extends Fragment {

    private EditText etTitulo;
    private EditText etFecha;
    private EditText etHora;
    private EditText etDesc;
    private Button btnEnviar;

    private int idReporte = 1;

    private double latitude;
    private double longitude;

    //SharedPreferences Manager
    TinyDB tinyDB = new TinyDB(getContext());

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

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("aviso", "Click");
                subirReporte();
            }
        });

        return v;
    }

    public void subirReporte() {
        Log.i("aviso", "estoy en subirReporte");
        String titulo = etTitulo.getText().toString();
        String fecha = etFecha.getText().toString();
        String hora = etHora.getText().toString();
        String desc = etDesc.getText().toString();

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Log.i("aviso", "Going in");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("aviso", "if 1");
            // TO DO
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Log.i("aviso", "if 2");
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!= null){
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
        }

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        Log.i("aviso", "salí");
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


        if(!titulo.isEmpty() && !fecha.isEmpty() && !hora.isEmpty() && !desc.isEmpty()){
            Report newReport = new Report(idReporte,titulo, fecha, hora, desc, latitude, longitude);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ruta = database.getReference("Reporte/" + idReporte + "/"); //Tabla
            ruta.setValue(newReport); //Contenido

            limpiarPantalla();

        }
        else {
            Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
        }

    }

    public void limpiarPantalla(){
        tinyDB.putInt("idReporte", idReporte);
        idReporte++;
        etTitulo.setText("");
        etFecha.setText("");
        etHora.setText("");
        etDesc.setText("");

        Toast.makeText(getActivity(), "Reporte Enviado", Toast.LENGTH_LONG).show();
    }

}