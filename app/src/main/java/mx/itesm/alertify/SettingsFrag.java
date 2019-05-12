package mx.itesm.alertify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class SettingsFrag extends Fragment {

    private EditText etMensaje;
    private EditText etMensajeCancel;
    private TextView lstContactos;
    private  TextView tvCuenta;
    private Switch callContact;
    private Switch useMessages;
    private TinyDB ajustes;
    private TinyDB tinyDB;
    private Context mContext;

    private BotonesySwitchesSettings buttonAndSwitchesManager;
    private Switch useWhatsapp;
    private Switch call911;

    public SettingsFrag() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Referencia a la actividad contenedora del fragmento
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Referencia a los campos de la pantalla y al manager de Botones y Switches
        buttonAndSwitchesManager = new BotonesySwitchesSettings();

        lstContactos= view.findViewById(R.id.lstContactos);
        etMensaje= view.findViewById(R.id.etMensaje);
        etMensajeCancel=view.findViewById(R.id.etMensajeCancel);
        tvCuenta = view.findViewById(R.id.tvCuenta);

        callContact= view.findViewById(R.id.call_contact);
        call911 = view.findViewById(R.id.call_911);
        useMessages=view.findViewById(R.id.useMensajes);
        useWhatsapp = view.findViewById(R.id.useWhatsapp);

        Button addPrincipalContact = view.findViewById(R.id.addPrincipContactButton);
        Button saveMessage = view.findViewById(R.id.saveMessageButton);
        Button addContact = view.findViewById(R.id.addNewContactButton);
        Button deleteContact = view.findViewById(R.id.deleteContactButton);
        Button logout = view.findViewById(R.id.logOutButton);

        //Definir changeListener para switches, si uno esta activado, el otro debe desactivars
        buttonAndSwitchesManager.isCheck(callContact, call911,ajustes,getActivity());
        buttonAndSwitchesManager.isCheckShareOptions(useWhatsapp,useMessages,etMensaje,ajustes,etMensajeCancel);

        //Referencia y metodo onclick para el boton de Definir Contacto Principal, Añadir contacto y Guardar mensaje
        addPrincipalContact.setOnClickListener(new View.OnClickListener() {@SuppressLint("SetTextI18n") @Override
            public void onClick(View v) {
                ajustes.putBoolean("addPrincipal",true);
            Log.i("MENSAJE",ajustes.getString("contactPrincipal"));
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                ajustes.putBoolean("addContact",true);
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        deleteContact.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                ajustes.putBoolean("deleteContact",true);
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        saveMessage.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                if (etMensaje.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(),"Error el mensaje esta vacio",Toast.LENGTH_LONG).show();
                }
                else {
                    ajustes.putString("mensaje",etMensaje.getText().toString());
                    ajustes.putString("mensajeCancel",etMensajeCancel.getText().toString());
                    Toast.makeText(getActivity(),"Mensaje: "+ ajustes.getString("mensaje"),Toast.LENGTH_LONG).show();

                    useMessages.setChecked(true);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
            ajustes.clear();
            tinyDB.clear();
            Intent intInicio = new Intent(getActivity(),LoginActiv.class);
            startActivity(intInicio);
            }
        });

        getActivity().setTitle("Ajustes");
        return view;
    }

    private void setPreferences(){
        buttonAndSwitchesManager.checkPreferences(ajustes,callContact,call911,useWhatsapp,useMessages,etMensaje,tvCuenta,tinyDB,etMensajeCancel);

        if(callContact.isChecked() && ajustes.getString("contactPrincipal").length()==0 && ajustes.getString("numeroPrincipal").length()==0){
            callContact.setChecked(false);

        }
    }

    private void refreshContactos() {
        lstContactos.setText("");

        for(int i = 0; i< ajustes.getListString("contactos").size(); i++){
            lstContactos.append(ajustes.getListString("contactos").get(i)+" , "+ ajustes.getListString("numeros").get(i)+"\n");
        }
 }

    private void showPrincipalContact() {
        callContact.setText("");
        callContact.setText("Llamar a: "+ajustes.getString("contactPrincipal")+" - "+ajustes.getString("numeroPrincipal"));
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
    }

    @SuppressLint("SetTextI18n")
    public void onResume() {
        super.onResume();

        setPreferences();
        refreshContactos();
        showPrincipalContact();
        principalAdd();
    }

    private void principalAdd() {
        if(ajustes.getBoolean("callContact"))
            callContact.setChecked(true);
    }
}