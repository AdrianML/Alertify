package mx.itesm.alertify;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mx.itesm.alertify.R;

public class InicioActiv extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private SharedPreferences prefs;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            switch (item.getItemId()) {
                //REEMPLAZAR CODIGO CON EL DEL FRAGMENTO PROPIO
                case R.id.navigation_inicio:
                    //if (fm.findFragmentByTag("boton") == null) {
                    fm.popBackStackImmediate(null, fm.POP_BACK_STACK_INCLUSIVE);
                    BotonFrag fragBoton = new BotonFrag();
                    transaction.replace(R.id.contFrag, fragBoton, "boton");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //}
                    return true;
                case R.id.navigation_mapa:
                    //if (fm.findFragmentByTag("mapa") == null) {
                    fm.popBackStackImmediate(null, fm.POP_BACK_STACK_INCLUSIVE);
                    MapaFrag fragMapa = new MapaFrag();
                    transaction.replace(R.id.contFrag, fragMapa, "mapa");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //}
                    return true;
                case R.id.navigation_reportes:
                    //if (fm.findFragmentByTag("reporte") == null){
                    fm.popBackStackImmediate(null, fm.POP_BACK_STACK_INCLUSIVE);
                    ReporteFrag fragReporte = new ReporteFrag();
                    transaction.replace(R.id.contFrag,fragReporte,"reporte");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //}
                    return true;
                case R.id.navigation_guia:
                    //if (fm.findFragmentByTag("guia") == null) {
                    fm.popBackStackImmediate(null, fm.POP_BACK_STACK_INCLUSIVE);
                    GuiaFrag fragGuia = new GuiaFrag();
                    transaction.replace(R.id.contFrag, fragGuia, "guia");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //}
                    return true;
                case R.id.navigation_settings:
                    //if (fm.findFragmentByTag("ajustes") == null) {
                    fm.popBackStackImmediate(null, fm.POP_BACK_STACK_INCLUSIVE);
                    SettingsFrag fragSettings = new SettingsFrag();
                    transaction.replace(R.id.contFrag, fragSettings, "ajustes");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //}
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
}
