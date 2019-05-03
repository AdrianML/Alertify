package mx.itesm.alertify;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

class BotonesySwitchesSettings {

    BotonesySwitchesSettings(){
    }

    void isCheck(final Switch callContact, final Switch call911, final TinyDB ajustes) {
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

    void checkPreferences(TinyDB ajustes, Switch callContact, Switch call911, Switch useWhatsapp, Switch useMessages, Switch sendLocation, EditText etMensaje, TextView tvCuenta, TinyDB tinyDB) {
        if(ajustes.getBoolean("callContact"))
            callContact.setChecked(true);

        else if(ajustes.getBoolean("call911"))
            call911.setChecked(true);

        if(ajustes.getString("contactPrincipal").length()!=0){
            callContact.setText("");
            callContact.setText("Llamar a: "+ajustes.getString("contactPrincipal")+" - "+ajustes.getString("numeroPrincipal"));
        }

        if(ajustes.getString("mensaje").length()!=0)
            etMensaje.setText(ajustes.getString("mensaje"));

        if(ajustes.getBoolean("useWhatsapp"))
            useWhatsapp.setChecked(true);

        if(ajustes.getBoolean("useMessages"))
            useMessages.setChecked(true);

        if(ajustes.getBoolean("sendLocation"))
            sendLocation.setChecked(true);

        tvCuenta.setText(tinyDB.getString("path"));

    }


    void isCheckShareOptions(final Switch sendLocation, final Switch useWhatsapp, final Switch useMessages, final EditText etMensaje, final TinyDB ajustes) {
        sendLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sendLocation.isChecked()) {
                    sendLocation.setChecked(true);
                    ajustes.putBoolean("sendLocation",true);
                }

                else{
                    ajustes.putBoolean("sendLocation",false);
                }
            }
        });

        useWhatsapp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (useWhatsapp.isChecked()) {
                    useWhatsapp.setChecked(true);
                    ajustes.putBoolean("useWhatsapp",true);

                    checkMessage(ajustes,etMensaje);
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

                    checkMessage(ajustes,etMensaje);
                }

                else{
                    ajustes.putBoolean("useMessages",false);
                }
            }
        });
    }

    private void checkMessage(TinyDB ajustes, EditText etMensaje){
        if(ajustes.getString("mensaje").length()==0){
            ajustes.putString("mensaje","Ayuda, esto es una alerta");
            etMensaje.setText(ajustes.getString("mensaje"));
        }

        else
            etMensaje.setText(ajustes.getString("mensaje"));
    }
}