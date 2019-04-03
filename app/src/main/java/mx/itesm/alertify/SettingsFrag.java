package mx.itesm.alertify;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFrag extends Fragment {

    private BotonesySwitchesSettings buttonAndSwitchesManager;

    private EditText etNombrePrincipal;
    private EditText etNombre;
    private EditText etMensaje;
    private TextView lstContactos;

    private Switch callContact;
    private Switch call911;
    private Switch useMessages;

    private ArrayList<String> numerosContactosAnadidos;
    private ArrayList<String> nombresContactosAnadidos;
    private ArrayList<String> contactsApp;
    private String contactNumber ;

    private String nombreContactoPrincipal;
    private String numeroContactoPrincipal;
    private String mensaje;

    public SettingsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Referencia a la actividad contenedora del fragmento
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Referencia a los campos de la pantalla y al manager de Botones y Switches
        buttonAndSwitchesManager = new BotonesySwitchesSettings();

        lstContactos= view.findViewById(R.id.lstContactos);
        numerosContactosAnadidos =new ArrayList<>();
        nombresContactosAnadidos =new ArrayList<>();
        contactsApp =new ArrayList<>();

        etNombrePrincipal= view.findViewById(R.id.etNombrePrincipal);
        etNombre= view.findViewById(R.id.etNombre);
        etMensaje= view.findViewById(R.id.etMensaje);

        callContact= view.findViewById(R.id.call_contact);
        call911= view.findViewById(R.id.call_911);
        useMessages=view.findViewById(R.id.useMensajes);

        Button addPrincipalContact = view.findViewById(R.id.addPrincipContactButton);
        Button saveMessage = view.findViewById(R.id.saveMessageButton);
        Button addContact = view.findViewById(R.id.addNewContactButton);
        Button testCallSMS = view.findViewById(R.id.testCallSMS);

        // Inicialización del manager de las preferencias
        // SharedPreferences manager
        TinyDB tinyDB = new TinyDB(getContext());

        //Arrays para guardar datos de los contactos guardados en la aplicacion
       // contactsNameArray = new ArrayList<>();
        //contactsNumbersArray= new ArrayList<>();
        AddContactstoArray(buttonAndSwitchesManager.contactsNameArray(),buttonAndSwitchesManager.contactsNumbersArray());

        //Definir changeListener para switches, si uno esta activado, el otro debe desactivarse
        buttonAndSwitchesManager.isCheck(callContact,call911,etNombrePrincipal);

        //Referencia y metodo onclick para el boton de Definir Contacto Principal, Añadir contacto y Guardar mensaje
        addPrincipalContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombrePrincipal.getText().toString().length()==0) {
                    Toast.makeText(getActivity(),"Error: falta un dato para registrarse",Toast.LENGTH_SHORT).show();
                }

                else{
                    int userExist,isNotAdded;

                    userExist=buttonAndSwitchesManager.userExists(etNombrePrincipal,nombresContactosAnadidos,numerosContactosAnadidos,contactsApp);
                    isNotAdded=buttonAndSwitchesManager.isNotAdded(etNombrePrincipal,numerosContactosAnadidos,nombresContactosAnadidos);

                    if(userExist!=-1 && isNotAdded==-1) {
                        nombresContactosAnadidos.add(etNombrePrincipal.getText().toString());
                        numerosContactosAnadidos.add(buttonAndSwitchesManager.contactsNumbersArray().get(userExist));
                        contactsApp.add(etNombrePrincipal.getText().toString() + " , " +
                                buttonAndSwitchesManager.contactsNumbersArray().get(userExist) + "\n");

                        numeroContactoPrincipal= buttonAndSwitchesManager.contactsNumbersArray().get(userExist);
                        nombreContactoPrincipal=etNombrePrincipal.getText().toString();

                        //Log.i("TAMAÑO"," "+ numerosContactosAnadidos.size()+" "+ nombresContactosAnadidos.size());

                        refreshContactos();

                        callContact.setChecked(true);
                    }

                    else if(userExist!=-1 && isNotAdded!=-1) {
                        numeroContactoPrincipal=buttonAndSwitchesManager.contactsNumbersArray().get(userExist);
                        nombreContactoPrincipal=etNombrePrincipal.getText().toString();
                    }

                    callContact.setText("Llamar a " + nombreContactoPrincipal);
                }
            }
        });

        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMensaje.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(),"Error el mensaje esta vacio",Toast.LENGTH_SHORT).show();
                }

                else {
                    mensaje=etMensaje.getText().toString();
                    Toast.makeText(getActivity(),"MENSAJE: "+ mensaje,Toast.LENGTH_SHORT).show();

                    useMessages.setChecked(true);
                }
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().length()!=0){
                    int userExist,isNotAdded;

                    userExist=buttonAndSwitchesManager.userExists(etNombre,nombresContactosAnadidos,numerosContactosAnadidos,contactsApp);
                    isNotAdded=buttonAndSwitchesManager.isNotAdded(etNombre,numerosContactosAnadidos,nombresContactosAnadidos);

                    //Log.i("VALORES",""+etNombre.getText().toString()+" "+userExist+" "+isNotAdded);

                    if(userExist!=-1 && isNotAdded==-1) {
                        nombresContactosAnadidos.add(etNombre.getText().toString());
                        numerosContactosAnadidos.add(buttonAndSwitchesManager.contactsNumbersArray().get(userExist));
                        contactsApp.add(etNombre.getText().toString() + " , " +
                                buttonAndSwitchesManager.contactsNumbersArray().get(userExist) + "\n");
                        refreshContactos();
                    }

                    //Log.i("TAMAÑO"," "+ numerosContactosAnadidos.size()+" "+ nombresContactosAnadidos.size());
                }

                else{
                    Toast.makeText(getActivity(),"ERROR, ESCRIBE ALGO",Toast.LENGTH_SHORT).show();
                }
            }
        });


        testCallSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callContact.isChecked() && nombreContactoPrincipal!=null && numeroContactoPrincipal!= null){
                    call();
                }

                else if(call911.isChecked()){
                    call911();
                }

                if(etMensaje.getText().toString().length()!=0 && useMessages.isChecked()){
                    for(int i=0;i<numerosContactosAnadidos.size();i++){
                        sendSMS(numerosContactosAnadidos.get(i));
                    }
                }

            }
        });

        return view;
    }

    private void refreshContactos() {
        lstContactos.setText("");

        for(int i = 0; i< numerosContactosAnadidos.size(); i++){
            lstContactos.append(nombresContactosAnadidos.get(i)+" , "+ numerosContactosAnadidos.get(i)+"\n");
        }
    }


    //Metodo para enviar mensaje al contacto principal definido
    public void sendSMS(String phone){
        final String SMS = etMensaje.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, SMS, null, null);
    }

    //Metodo para hacer llada al usuario principal
    public void call() {
        String phoneNum = numeroContactoPrincipal;

        if(!TextUtils.isEmpty(phoneNum)) {
            String dial = "tel:" + phoneNum;

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

        else{
            Toast.makeText(getActivity(), "Please enter a valid telephone number", Toast.LENGTH_SHORT).show();
        }
    }

    public void call911(){
        String dial = "tel://911";

        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }

    //Metodo para añadir los contactos al array
    public void AddContactstoArray(ArrayList<String> strings, ArrayList<String> stringArrayList){

//Query the phone number table using the URI stored in CONTENT_URI//

        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        assert cursor != null;
        while (cursor.moveToNext()) {

//Get the display name for each contact//

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

//Get the phone number for each contact//

            contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

//Add each display name and phone number to the Array//
            buttonAndSwitchesManager.contactsNameArray().add(name);
            buttonAndSwitchesManager.contactsNumbersArray().add(contactNumber);
        }

        cursor.close();

    }
}