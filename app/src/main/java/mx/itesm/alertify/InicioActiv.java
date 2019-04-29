package mx.itesm.alertify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class InicioActiv extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private TinyDB ajustes;
    ArrayList<String> contactos;
    ArrayList<String> numeros;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_inicio:
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BotonFrag fragBoton = new BotonFrag();
                    transaction.replace(R.id.contFrag, fragBoton, "boton");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_mapa:
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    MapaFrag fragMapa = new MapaFrag();
                    transaction.replace(R.id.contFrag, fragMapa, "mapa");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_reportes:
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ReporteFrag fragReporte = new ReporteFrag();
                    transaction.replace(R.id.contFrag,fragReporte,"reporte");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_guia:
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    GuiaFrag fragGuia = new GuiaFrag();
                    transaction.replace(R.id.contFrag, fragGuia, "guia");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_settings:
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    SettingsFrag fragSettings = new SettingsFrag();
                    transaction.replace(R.id.contFrag, fragSettings, "ajustes");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ajustes = new TinyDB(this);
        contactos = new ArrayList<>();
        numeros= new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Log.e("permission", "Permission already granted.");
                editor.putInt("permiso",1);
                editor.apply();

            } else {

//If the app doesn’t have the CALL_PHONE permission, request it//

                requestPermission();
            }
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        BotonFrag fragBoton = new BotonFrag();

        transaction.replace(R.id.contFrag,fragBoton);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount() > 1){
            fm.popBackStack();
        }
        /*else{
            super.onBackPressed();
        }*/

    }

    public boolean checkPermission() {

        int CallPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        int ContactPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        int SMSPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);

        return CallPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ContactPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SMSPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(InicioActiv.this, new String[]
                {
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS
                }, PERMISSION_REQUEST_CODE);
    }

    protected void onActivityResult(int RequestCode,int ResultCode,Intent data) {
        super.onActivityResult(RequestCode, ResultCode, data);

        if (data != null) {
            Uri result = data.getData();

            assert result != null;
            @SuppressLint("Recycle") Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone._ID + "=?",
                    new String[]{result.getLastPathSegment()}, null);

            assert c != null;
            if (c.getCount() >= 1 && c.moveToFirst()) {
                final String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                final String nombre = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                if(!ajustes.getListString("contactos").contains(nombre)){
                    contactos.add(nombre);
                    numeros.add(number);
                }

                else if(ajustes.getListString("contactos").contains(nombre)){
                    contactos.remove(nombre);
                    numeros.remove(contactos.indexOf(nombre));
                    contactos.add(nombre);
                    numeros.add(number);
                }

                ajustes.putListString("contactos",contactos);
                ajustes.putListString("numeros",numeros);

                Log.i("IMPRIMIENDO"," "+ajustes.getListString("contactos")+" "+ajustes.getListString("numeros"));

                //AÑADIR INFORMACION A PREFERENCIAS CON EL NOMBRE DE ABAJO PARA QUE SIGA FUNCIONANDO EN SETTINGSFRAGS

                //AÑADIR nombre a arreglo "contactos"
                //AÑADIR number a arreglo "numeros"

            }
        }
    }
}
