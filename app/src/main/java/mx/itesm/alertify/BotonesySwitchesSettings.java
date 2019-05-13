package mx.itesm.alertify;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

class BotonesySwitchesSettings {

    BotonesySwitchesSettings(){
    }

    void isCheck(final Switch callContact, final Switch call911, final TinyDB ajustes, final FragmentActivity activity) {
        callContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (callContact.isChecked()) {
                    if(ajustes.getString("contactPrincipal").length()!=0 && ajustes.getString("numeroPrincipal").length()!=0) {
                        callContact.setChecked(true);
                        call911.setChecked(false);
                        ajustes.putBoolean("callContact", true);
                        ajustes.putBoolean("call911", false);
                    }

                    else{
                        callContact.setChecked(false);
                        call911.setChecked(false);
                        Toast.makeText(activity,"Agrega un contacto principal para esta función",Toast.LENGTH_LONG).show();
                    }
                }

                else{
                    ajustes.putBoolean("callContact",false);
                }
            }
        });

        call911.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (call911.isChecked()) {
                    call911.setChecked(true);
                    callContact.setChecked(false);
                    ajustes.putBoolean("callContact",false);
                    ajustes.putBoolean("call911",true);
                }

                else {
                    ajustes.putBoolean("call911", false);
                }
            }
        });
    }

    void isCheckShareOptions(final Switch useWhatsapp, final Switch useMessages, final EditText etMensaje, final TinyDB ajustes,final  EditText etMensajeCancel) {
        useWhatsapp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (useWhatsapp.isChecked()) {
                    useWhatsapp.setChecked(true);
                    ajustes.putBoolean("useWhatsapp",true);

                    checkMessage(ajustes,etMensaje,etMensajeCancel);
                }

                else{
                    ajustes.putBoolean("useWhatsapp",false);
                }
            }
        });

        useMessages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (useMessages.isChecked()) {
                    useMessages.setChecked(true);
                    ajustes.putBoolean("useMessages",true);

                    checkMessage(ajustes,etMensaje,etMensajeCancel);
                }

                else{
                    ajustes.putBoolean("useMessages",false);
                }
            }
        });
    }

    private void checkMessage(TinyDB ajustes, EditText etMensaje,EditText etMensajeCancel){
        if(ajustes.getString("mensaje").length()==0){
            ajustes.putString("mensaje","Ayuda, esto es una alerta");
            ajustes.putString("mensajeCancel","Ya me encuentro a salvo");
            etMensaje.setText(ajustes.getString("mensaje"));
            etMensajeCancel.setText(ajustes.getString("mensajeCancel"));
        }

        else
            etMensaje.setText(ajustes.getString("mensaje"));
            etMensajeCancel.setText(ajustes.getString("mensajeCancel"));
    }

    @SuppressLint("SetTextI18n")
    void checkCallPreferences(TinyDB ajustes, Switch call911, Switch callContact) {
        if(ajustes.getBoolean("callContact"))
            callContact.setChecked(true);

        else if(ajustes.getBoolean("call911"))
            call911.setChecked(true);

        if(ajustes.getString("contactPrincipal").length()!=0){
            callContact.setText("");
            callContact.setText("Llamar a: "+ajustes.getString("contactPrincipal")+" - "+ajustes.getString("numeroPrincipal"));
        }
    }

    void checkMessagePreferences(TinyDB ajustes, Switch useWhatsapp, Switch useMessages, EditText etMensaje, EditText etMensajeCancel) {
        if(ajustes.getString("mensaje").length()!=0) {
            etMensaje.setText(ajustes.getString("mensaje"));
            etMensajeCancel.setText(ajustes.getString("mensajeCancel"));
        }

        if(ajustes.getBoolean("useWhatsapp"))
            useWhatsapp.setChecked(true);

        if(ajustes.getBoolean("useMessages"))
            useMessages.setChecked(true);
    }

    void checkAccountPreferences(TextView tvCuenta, TinyDB tinyDB) {
        tvCuenta.setText(tinyDB.getString("path").substring(0,tinyDB.getString("path").indexOf("@")));
        tinyDB.putString("user",tinyDB.getString("path").substring(0,tinyDB.getString("path").indexOf("@")));
    }

    void missingPrincipalContact(Switch callContact, TinyDB ajustes) {
        if(callContact.isChecked() && ajustes.getString("contactPrincipal").length()==0 && ajustes.getString("numeroPrincipal").length()==0){
            callContact.setChecked(false);
        }
    }
}