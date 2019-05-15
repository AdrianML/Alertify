package mx.itesm.alertify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BotonFrag extends Fragment implements LocationListener {

    private TinyDB ajustes;
    private Context mContext;
    private int clicked;
    private TextView timerTV;
    private int tiempo;
    private CountDownTimer timer;
    private Location posicion;
    private LocationManager gps;
    private TinyDB tinyDB;
    private static final int PERMISO_GPS = 200;
    private int idReporte;
    private double latitud;
    private double longitud;
    LocationManager locationManager;

    CircularProgressButton sosButton;

    public BotonFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_boton, container, false);

        idReporte = tinyDB.getInt("idReporte");

        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        configurarGPS();

        if (!gps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), "Favor de prender el GPS.", Toast.LENGTH_LONG).show();
        }

        timerTV = v.findViewById(R.id.timer);
        sosButton = v.findViewById(R.id.btnAlerta);

        clicked = 0;
        tiempo=15;
        timer = new CountDownTimer(15000,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                if(tiempo==10 || tiempo==7 || tiempo==3){
                    doActions(tiempo);
                }

                else {
                    timerTV.setText("0:" + checkDigit(tiempo));
                }

                tiempo--;
            }

            @Override
            public void onFinish() {
                timerTV.setText("");
                Toast.makeText(getActivity(), "Alerta enviada", Toast.LENGTH_LONG)
                        .show();
                tiempo=15;
                sosButton.revertAnimation();
                clicked=0;
                sosButton.setTextSize(60);
                timer.cancel();
                subirReporte();
            }
        };

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked+=1;

                if(clicked==1) {
                    getLocation();

                    @SuppressLint("StaticFieldLeak")
                    AsyncTask<String, String, String> sosSend = new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                sosButton.setEnabled(false);
                                sosButton.setClickable(false);
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return "done";
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (s.equals("done"))
                                enableCancelation(sosButton,timer);
                        }
                    };
                    startAlert(sosButton,sosSend);

                }

                else {
                    if(ajustes.getBoolean("useMessages") && ajustes.getListString("contactos").size()!=0 && tiempo<10) {
                        sendCancelSMS();
                        Toast.makeText(getActivity(), "Mensaje de cancelación enviado, alerta cancelada", Toast.LENGTH_LONG)
                                .show();
                    }
                    else
                        Toast.makeText(getActivity(), "Alerta cancelada", Toast.LENGTH_LONG)
                                .show();
                    clicked=0;
                    tiempo=15;
                    alertCanceled(sosButton, timerTV, ajustes);
                }
            }
        });

        Objects.requireNonNull(getActivity()).setTitle("Alertify");

        return v;
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //Metodo para cencelar una alerta
    private void alertCanceled(CircularProgressButton sosButton, TextView timerTV, TinyDB ajustes) {
        sosButton.revertAnimation();
        timerTV.setText("");
        sosButton.setTextSize(60);
        timer.cancel();
    }

    //Inicio de la alerta
    private void startAlert(CircularProgressButton sosButton, AsyncTask<String,String,String> sosSend) {
        sosButton.startAnimation();
        sosButton.setClickable(true);
        sosButton.setEnabled(true);
        sosSend.execute();
    }

    //Cuando se da click a la alarte se puede cancelar
    @SuppressLint("SetTextI18n")
    private void enableCancelation(CircularProgressButton sosButton, CountDownTimer timer) {
        sosButton.setText("Presiona para cancelar...");
        sosButton.setEnabled(true);
        sosButton.setClickable(true);
        sosButton.setTextSize(30);
        timer.start();
    }

    //Metodo para enviar el mensaje de alerta
    public void sendCancelSMS() {
        for(int i=0;i<ajustes.getListString("numeros").size();i++){
            sendCancel(ajustes.getListString("numeros").get(i));
        }
    }

    //Si se cancela la alarta, se envia el mensaje de cancelacion
    private void sendCancel(String numeros) {
        final String SMS = ajustes.getString("mensajeCancel");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(numeros, null, SMS, null, null);
    }

    //Lanzar mensajes correspondientes a las acciones de alerta
    @SuppressLint("SetTextI18n")
    private void doActions(int tiempo) {
        if(tiempo==10 && ajustes.getString("mensaje").length()!=0 && ajustes.getBoolean("useMessages") && ajustes.getListString("contactos").size()!=0){
            timerTV.setText("Enviando mensaje...");

            for(int i=0;i<ajustes.getListString("numeros").size();i++){
                sendSMS(ajustes.getListString("numeros").get(i));
           }
        }

        else if(tiempo==10 && (ajustes.getString("mensaje").length()==0 || !ajustes.getBoolean("useMessages") || ajustes.getListString("contacts").size()==0)){
            timerTV.setText("0:10");
            Toast.makeText(getActivity(), "No activaste la opción de mensajes o no añadiste contactos", Toast.LENGTH_SHORT)
                    .show();
        }

        if(tiempo==7 && ajustes.getBoolean("useWhatsapp")){
            timerTV.setText("Abriendo Whatsapp...");
            onClickWhatsApp(getView());
        }

        else if(tiempo==7 && !ajustes.getBoolean("useWhatsapp"))
            timerTV.setText("0:07");

        if(tiempo==3 && ajustes.getBoolean("callContact") && ajustes.getString("contactPrincipal")!=null &&
                ajustes.getString("numeroPrincipal")!= null){
            timerTV.setText("Iniciando llamada...");
            call();
        }

        else if(tiempo==3 && ajustes.getBoolean("call911")){
            timerTV.setText("Llamando al 911...");
            call911();
        }

        else if(tiempo==3 && (!ajustes.getBoolean("call911") || !ajustes.getBoolean("callContact"))){
            timerTV.setText("0:03");
            Toast.makeText(getActivity(), "No tienes la opción de llamada seleccionada", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //Metodo para hacer llada al usuario principal
    public void call() {
        String phoneNum = ajustes.getString("numeroPrincipal");

        if(!TextUtils.isEmpty(phoneNum)) {
            String dial = "tel:" + phoneNum;

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

        else{
            Toast.makeText(getActivity(), "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para llamar al 911
    public void call911(){
        String dial = "tel://911";

        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }

    //Metodo para enviar mensaje al contacto principal definido
    public void sendSMS(String phone){
       // final String SMS = ajustes.getString("mensaje")+"/ LATITUD: "+String.valueOf(latitud)+" ,LONGITUD: "+String.valueOf(longitud);
        final String SMS = ajustes.getString("mensaje")+" http://maps.google.com/?q="+String.valueOf(latitud)+","+String.valueOf(longitud);


        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, SMS, null, null);
    }

    //Metodo para enviar mensaje por whatsapp
    public void onClickWhatsApp(View view) {
        PackageManager pm=Objects.requireNonNull(getActivity()).getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            final String text = ajustes.getString("mensaje")+" http://maps.google.com/?q="+String.valueOf(latitud)+","+String.valueOf(longitud);

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivityForResult(Intent.createChooser(waIntent, "Share with"),1);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //Metodo para ir checando el timer
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void subirReporte() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        String[] fechaaux = formattedDate.split("-");

        double lat = latitud;//posicion.getLatitude();
        double lng = longitud;//posicion.getLongitude();
        String titulo = ajustes.getString("user");
        String dd = fechaaux[0];
        String mm = fechaaux[1];
        String aaaa = fechaaux[2];
        String fecha = dd + "/" + mm + "/" + aaaa;
        String hora = "10";
        String min = "00";
        String horaMin = hora+":"+min;
        String desc = "Alerta";

        if(!titulo.isEmpty() && !dd.isEmpty() && !mm.isEmpty() && !aaaa.isEmpty() && !hora.isEmpty() && !min.isEmpty() && !desc.isEmpty()){
            Report newReport = new Report(idReporte,titulo, fecha, horaMin, desc, lat, lng);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            String email = tinyDB.getString("path");
            String path = "";

            for(int i = 0; i < email.length(); i++){
                if(email.charAt(i) != '.'){
                    path += email.charAt(i);
                }
            }
            tinyDB.putInt("idReporte", idReporte);
            DatabaseReference ruta = database.getReference("User/" + path + "/"); //Tabla
            ruta.child("Reportes/" + tinyDB.getInt("idReporte")).setValue(newReport); //Contenido

            idReporte++;
            tinyDB.putInt("idReporte", idReporte);
        }
        else {
            Toast.makeText(getActivity(), "Favor de llenar todos los campos.", Toast.LENGTH_LONG).show();
        }

    }

    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
        //SharedPreferences Manager
        ajustes = new TinyDB(mContext);
        tinyDB = new TinyDB(mContext);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mContext = null;

        if(tiempo!=15)
        timer.cancel();
    }

    private void configurarGPS() {
        // Crea el administrador del gps
        gps = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        // Pregunta si está prendido el GPS en el sistema
        if (!gps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Abrir Settings para prender el GPS, no se puede hacer con código
            prenderGPS();
        }
    }

    private void prenderGPS() {
        // El usuario lo debe encender, no se puede con programación
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
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

   @Override
    public void onResume() {
        super.onResume();
        // Prueba si tiene permiso para acceder al gps
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // No lo tiene, solicitar el permiso
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISO_GPS);
            // Contestará con onRequestPermissionsResult...
        } else {
            // Ya tiene permiso, iniciar actualizaciones
            gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISO_GPS && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Contestó que SI, otorga el permiso. Iniciar actualizaciones.
                gps.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
            } else {
                Log.i("onRequestPerm...", "Contestó NO, indicarle cómo dar permiso...");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        posicion = location;
        latitud=posicion.getLatitude();
        longitud=posicion.getLongitude();
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
}
