package mx.itesm.alertify;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class BotonesySwitchesSettings {

    private Button addPrincipalContact;
    private Button addContact;
    private Switch sCallContact;
    private Switch sCall911;
    private EditText etNumberPrincipal;
    private EditText etNombrePrincipal;
    private EditText etNombre;
    private EditText etNumber;

    public BotonesySwitchesSettings(){
        /*settingsPrefs=PreferenceManager.getDefaultSharedPreferences(context);
        settingsEditor=settingsPrefs.edit();

        if(settingsPrefs.getBoolean("callDefault",false)) {
            settingsEditor.putBoolean("callDefault", true);
        }*/
    }

    public void isCheck(final Switch callContact, final Switch call911, final EditText etNombrePrincipal, final SharedPreferences.Editor prefs) {
        callContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (callContact.isChecked()) {
                        Log.i("SE LLAMARA A", " " + etNombrePrincipal.getText().toString().length());
                        callContact.setChecked(true);
                        call911.setChecked(false);
                        prefs.putBoolean("callOption",false);
                        prefs.apply();
                    }
                }
            });

            call911.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (call911.isChecked()) {
                        Log.i("SE LLAMARA A", "911");
                        call911.setChecked(true);
                        callContact.setChecked(false);
                        prefs.putBoolean("callOption",true);
                        prefs.apply();
                    }
                }
            });
    }
}