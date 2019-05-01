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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFrag extends Fragment {

    private EditText etNombrePrincipal;
    private EditText etMensaje;
    private TextView lstContactos;

    private Switch callContact;
    private Switch useMessages;

    private TinyDB ajustes;
    private Context mContext;

    public SettingsFrag() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Referencia a la actividad contenedora del fragmento
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Referencia a los campos de la pantalla y al manager de Botones y Switches
        BotonesySwitchesSettings buttonAndSwitchesManager = new BotonesySwitchesSettings();

        lstContactos= view.findViewById(R.id.lstContactos);

        etNombrePrincipal= view.findViewById(R.id.etNombrePrincipal);
        etMensaje= view.findViewById(R.id.etMensaje);

        callContact= view.findViewById(R.id.call_contact);
        Switch call911 = view.findViewById(R.id.call_911);
        useMessages=view.findViewById(R.id.useMensajes);
        Switch useWhatsapp = view.findViewById(R.id.useWhatsapp);
        Switch sendLocation = view.findViewById(R.id.sendLocation);


        Button addPrincipalContact = view.findViewById(R.id.addPrincipContactButton);
        Button saveMessage = view.findViewById(R.id.saveMessageButton);
        Button addContact = view.findViewById(R.id.addNewContactButton);

        //Definir changeListener para switches, si uno esta activado, el otro debe desactivars
        buttonAndSwitchesManager.isCheck(callContact, call911,ajustes);
        buttonAndSwitchesManager.isCheckShareOptions(sendLocation, useWhatsapp,useMessages,etMensaje,ajustes);

        //Referencia y metodo onclick para el boton de Definir Contacto Principal, Añadir contacto y Guardar mensaje
        addPrincipalContact.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                ajustes.putBoolean("addPrincipal",true);

              Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
              startActivityForResult(intent, 1);
       }
        });

        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMensaje.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(),"Error el mensaje esta vacio",Toast.LENGTH_LONG).show();
                }

                else {
                    ajustes.putString("mensaje",etMensaje.getText().toString());
                    Toast.makeText(getActivity(),"Mensaje: "+ ajustes.getString("mensaje"),Toast.LENGTH_LONG).show();

                    useMessages.setChecked(true);

                    //onClickWhatsApp(view);
                }
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        buttonAndSwitchesManager.checkPreferences(ajustes,callContact, call911,etNombrePrincipal,etMensaje,useWhatsapp,useMessages,sendLocation);

        return view;
    }

    private void refreshContactos() {
        lstContactos.setText("");

        for(int i = 0; i< ajustes.getListString("contactos").size(); i++){
            lstContactos.append(ajustes.getListString("contactos").get(i)+" , "+ ajustes.getListString("numeros").get(i)+"\n");
        }
 }

    private void showPrincipalContact() {
        etNombrePrincipal.setText(ajustes.getString("contactPrincipal"));
        callContact.setText("");
        callContact.setText("Llamar a: "+ajustes.getString("contactPrincipal"));
    }

    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = activity;
        //SharedPreferences Manager
        ajustes = new TinyDB(mContext);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mContext = null;
    }

    public void onClickWhatsApp(View view) {

        PackageManager pm=Objects.requireNonNull(getActivity()).getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void onResume() {
        super.onResume();

        refreshContactos();
        showPrincipalContact();
        principalAdd();
    }

    private void principalAdd() {
        if(ajustes.getBoolean("callContact"))
            callContact.setChecked(true);
    }
}