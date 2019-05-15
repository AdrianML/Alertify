package mx.itesm.alertify;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
public class ReporteFrag extends Fragment implements  LocationListener{
    private EditText etTitulo;
    private EditText etFecha;
    private EditText etFecha2;
    private EditText etFecha3;
    private EditText etHora;
    private EditText etHora2;
    private EditText etDesc;
    private Button btnEnviar;
    private TinyDB tinyDB;
    private int idReporte;
    private Location posicion;
    private LocationManager gps;

    private Context mContext;
    private static final int PERMISO_GPS = 200;

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
        etFecha2 = v.findViewById(R.id.etFecha2);
        etFecha3 = v.findViewById(R.id.etFecha3);
        etHora = v.findViewById(R.id.etHora);
        etHora2 = v.findViewById(R.id.etHora2);
        etDesc = v.findViewById(R.id.etDesc);
        btnEnviar = v.findViewById(R.id.btnEnviar);

        idReporte = tinyDB.getInt("idReporte");

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        configurarGPS();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!gps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getActivity(), "Favor de prender el GPS.", Toast.LENGTH_LONG).show();
                }
                else {
                    subirReporte();
                    }
            }
        });

        getActivity().setTitle("Reportes");

        return v;
    }
    //double lat, double lng
    @SuppressLint("MissingPermission")
    public void subirReporte() {
        if(posicion == null){
            gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            Toast.makeText(getActivity(), "Error de conexión, intente más tarde.", Toast.LENGTH_LONG).show();


        }else {
            double lat = posicion.getLatitude();
            double lng = posicion.getLongitude();
            String titulo = etTitulo.getText().toString();
            String dd = etFecha.getText().toString();
            String mm = etFecha2.getText().toString();
            String aaaa = etFecha3.getText().toString();
            String fecha = dd + "/" + mm + "/" + aaaa;
            String hora = etHora.getText().toString();
            String min = etHora2.getText().toString();
            String horaMin = etHora.getText().toString()+":"+etHora2.getText().toString();
            String desc = etDesc.getText().toString();

            if(titulo.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            if(dd.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            if(mm.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            if(aaaa.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            if(hora.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            if(min.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            if(desc.isEmpty()){
                Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            int day = Integer.parseInt(etFecha.getText().toString());
            int month = Integer.parseInt(etFecha2.getText().toString());
            int year = Integer.parseInt(etFecha3.getText().toString());
            int hour = Integer.parseInt(etHora.getText().toString());
            int minutes = Integer.parseInt(etHora2.getText().toString());
            if(day < 0 || day > 31){
                Toast.makeText(getActivity(), "El día ingresado no es válido.", Toast.LENGTH_LONG).show();
                return;
            }
            else if(month < 0 || month > 12){
                Toast.makeText(getActivity(), "El mes ingresado es válido.", Toast.LENGTH_LONG).show();
                return;
            }
            else if(year < 2018 || year > 2030){
                Toast.makeText(getActivity(), "El año ingresado no es válido.", Toast.LENGTH_LONG).show();
                return;
            }

            else if (hour < 0 || hour > 24){
                Toast.makeText(getActivity(), "La hora ingresada no es válida.", Toast.LENGTH_LONG).show();
                return;
            }
            else if (minutes < 0 || minutes > 59){
                Toast.makeText(getActivity(), "Los minutos ingresados no son válidos.", Toast.LENGTH_LONG).show();
                return;
            }
            Report newReport = new Report(idReporte,titulo, fecha, horaMin, desc, lat, lng);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            String email = tinyDB.getString("path");
            String path = "";
            for(int c = 0; c < email.length(); c++){
                if(email.charAt(c) != '.'){
                    path += email.charAt(c);
                }
            }
            tinyDB.putInt("idReporte", idReporte);
            DatabaseReference ruta = database.getReference("User/" + path + "/"); //Tabla
            ruta.child("Reportes/" + tinyDB.getInt("idReporte")).setValue(newReport); //Contenido
            limpiarPantalla();
        }
    }



    public void limpiarPantalla(){
        idReporte++;
        tinyDB.putInt("idReporte", idReporte);

        etTitulo.setText("");
        etFecha.setText("");
        etFecha2.setText("");
        etFecha3.setText("");
        etHora.setText("");
        etHora2.setText("");
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

    @Override
    public void onResume() {
        super.onResume();
        // Prueba si tiene permiso para acceder al gps
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // No lo tiene, solicitar el permiso
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISO_GPS);
            // Contestará con onRequestPermissionsResult...
        } else {
            // Ya tiene permiso, iniciar actualizaciones
            gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
    }
    private void configurarGPS() {
        // Crea el administrador del gps
        gps = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Pregunta si está prendido el GPS en el sistema
        if (!gps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Abrir Settings para prender el GPS, no se puede hacer con código
            prenderGPS();
        }
    }

    private void prenderGPS() {
        // El usuario lo debe encender, no se puede con programación
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("El GPS está apagado, ¿Quieres prenderlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new
                                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); // Abre settings
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISO_GPS && grantResults.length>0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Contestó que SI, otorga el permiso. Iniciar actualizaciones.
                gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            } else {
                Log.i("onRequestPerm...","Contestó NO, indicarle cómo dar permiso...");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        posicion = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //configurarGPS();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //prenderGPS();
    }
}