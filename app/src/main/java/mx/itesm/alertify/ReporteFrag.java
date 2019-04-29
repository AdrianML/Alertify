package mx.itesm.alertify;


import android.Manifest;
import android.app.Activity;
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

    private TinyDB tinyDB;
    private int idReporte;

    private double latitude;
    private double longitude;
    private Context mContext;

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

        idReporte = tinyDB.getInt("idReporte");

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirReporte();
            }
        });

        return v;
    }

    public void subirReporte() {
        String titulo = etTitulo.getText().toString();
        String fecha = etFecha.getText().toString();
        String hora = etHora.getText().toString();
        String desc = etDesc.getText().toString();

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


        if(!titulo.isEmpty() && !fecha.isEmpty() && !hora.isEmpty() && !desc.isEmpty()){
            Report newReport = new Report(idReporte,titulo, fecha, hora, desc, latitude, longitude);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            String email = tinyDB.getString("path");
            String path = "";

            for(int c = 0; c < email.length(); c++){
                if(email.charAt(c) != '.'){
                    path += email.charAt(c);
                }
            }
            Log.i("email", email);
            Log.i("path", path);
            tinyDB.putInt("idReporte", idReporte);
            DatabaseReference ruta = database.getReference("User/" + path + "/"); //Tabla
            ruta.child("Reportes/" + tinyDB.getInt("idReporte")).setValue(newReport); //Contenido

            limpiarPantalla();

        }
        else {
            Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
        }

    }

    public void limpiarPantalla(){
        idReporte++;
        tinyDB.putInt("idReporte", idReporte);

        etTitulo.setText("");
        etFecha.setText("");
        etHora.setText("");
        etDesc.setText("");

        Toast.makeText(getActivity(), "Reporte Enviado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
        //SharedPreferences Manager
        tinyDB = new TinyDB(mContext);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mContext = null;
    }

}