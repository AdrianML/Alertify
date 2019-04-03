package mx.itesm.alertify;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import mx.itesm.alertify.BotonesySwitchesSettings;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFrag extends Fragment {

    private BotonesySwitchesSettings buttonAndSwitchesManager;

    private View view;
    private Context context;

    private EditText etNumeroPrincipal;
    private EditText etNombrePrincipal;
    private EditText etNombre;
    private EditText etNumero;
    private EditText etMensaje;

    private Switch callContact;
    private Switch call911;

    private Button addPrincipalContact;
    private Button saveMessage;

    // SharedPreferences manager
    private TinyDB tinyDB;

    public SettingsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_settings,container,false);

        // Inicialización del manager de las preferencias
        tinyDB = new TinyDB(getContext());

        //Referencia a los campos de la pantalla
        buttonAndSwitchesManager = new BotonesySwitchesSettings();

        etNumeroPrincipal= view.findViewById(R.id.etNumeroPrincipal);
        etNombrePrincipal= view.findViewById(R.id.etNombrePrincipal);
        etNombre= view.findViewById(R.id.etNombre);
        etNumero= view.findViewById(R.id.etNumero);
        etMensaje=view.findViewById(R.id.etMensaje);

        callContact= view.findViewById(R.id.call_contact);
        call911=view.findViewById(R.id.call_911);

        addPrincipalContact=view.findViewById(R.id.addPrincipContactButton);
        saveMessage=view.findViewById(R.id.saveMessageButton);

        //Referencia y metodo onclick para el boton de Definir Contacto Principal y boton Añadir contacto
        addPrincipalContact=view.findViewById(R.id.addPrincipContactButton);

        addPrincipalContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNumeroPrincipal.getText().toString().length()==0 || etNombrePrincipal.getText().toString().length()==0) {
                    Log.i("Error:", "falta un dato para registrarse");
                }

                else{
                    callContact.setText("Llamar a "+etNombrePrincipal.getText().toString());
                    if(callContact.isChecked() && etNumeroPrincipal.getText().toString().length()!=0 && etNombrePrincipal.getText().toString().length()!=0) {
                        call();
                    }

                    Log.i("Numero: ", "" + etNumeroPrincipal.getText().toString() + "Nombre: " + etNombrePrincipal.getText().toString());
                }
            }
        });

        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMensaje.getText().toString().length()==0 && etNumeroPrincipal.getText().toString().length()!=0){
                    Log.i("Error:", "el mensaje esta vacio");
                }

                else{
                    Log.i("ENVIANDO:",""+etMensaje.getText().toString());
                    sendSMS();
                }
            }
        });

        return view;
    }

    public void sendSMS(){
        final String SMS = etMensaje.getText().toString();
        final String phoneNum = etNumeroPrincipal.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();

//Send the SMS//

        smsManager.sendTextMessage(phoneNum, null, SMS, null, null);
    }

    public void call()
    {
        final EditText phoneNumber = (EditText) view.findViewById(R.id.etNumeroPrincipal);
        String phoneNum = phoneNumber.getText().toString();
        if(!TextUtils.isEmpty(phoneNum)) {
            String dial = "tel:" + phoneNum;

//Make an Intent object of type intent.ACTION_CALL//

            startActivity(new Intent(Intent.ACTION_CALL,

//Extract the telephone number from the URI//

                    Uri.parse(dial)));
        }else {
            Toast.makeText(getActivity(), "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
        }

    }
}