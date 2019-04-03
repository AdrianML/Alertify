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
import android.widget.TextView;

import java.util.ArrayList;

public class BotonesySwitchesSettings {

    private ArrayList<String> contactsNameArray ;
    private ArrayList<String> contactsNumbersArray;

    public BotonesySwitchesSettings(){
        contactsNameArray=new ArrayList<>();
        contactsNumbersArray=new ArrayList<>();
    }

    public ArrayList<String> contactsNameArray(){
        return contactsNameArray;
    }

    public ArrayList<String> contactsNumbersArray(){
        return contactsNumbersArray;
    }

    public void isCheck(final Switch callContact, final Switch call911, final EditText etNombrePrincipal) {
        callContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (callContact.isChecked()) {
                    Log.i("SE LLAMARA A", " " + etNombrePrincipal.getText().toString().length());
                    callContact.setChecked(true);
                    call911.setChecked(false);
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
                }
            }
        });
    }

    public int userExists(EditText etNombrePrincipal, ArrayList<String> nombresContactosAnadidos, ArrayList<String> numerosContactosAnadidos, ArrayList<String> contactsApp) {
        for(int i=0;i<contactsNameArray.size();i++){
            if(etNombrePrincipal.getText().toString().equals(contactsNameArray.get(i))) {
                Log.i("USUARIO EXISTE",""+i);
                return i; }
        }
        return -1;
    }

    public int isNotAdded(EditText etNombrePrincipal, ArrayList<String> numerosContactosAnadidos, ArrayList<String> nombresContactosAnadidos) {
        String temp;

        for(int i = 0; i< numerosContactosAnadidos.size(); i++){
            temp= nombresContactosAnadidos.get(i);

            if(temp.equals(etNombrePrincipal.getText().toString())) { return i; }
        }
        return -1;
    }
}