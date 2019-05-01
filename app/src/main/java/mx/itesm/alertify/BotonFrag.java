package mx.itesm.alertify;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BotonFrag extends Fragment{

    private TinyDB ajustes;
    private Context mContext;

    public BotonFrag() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_boton, container, false);
        Button btnAlert = v.findViewById(R.id.btnAlerta);

        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ajustes.getBoolean("callContact") &&
                        ajustes.getString("contactPrincipal")!=null &&
                        ajustes.getString("numeroPrincipal")!= null){
                    call();
                }

                else if(ajustes.getBoolean("call911")){
                    call911();
                }

                if(ajustes.getString("mensaje").length()!=0 && ajustes.getBoolean("useMessages")){
                    for(int i=0;i<ajustes.getListString("numeros").size();i++){
                        sendSMS(ajustes.getListString("numeros").get(i));
                        }
                }
            }
        });

        return v;
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

    //Metodo para hacer llada al usuario principal
    public void call() {
        String phoneNum = ajustes.getString("numeroPrincipal");

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

    //Metodo para enviar mensaje al contacto principal definido
    public void sendSMS(String phone){
        final String SMS = ajustes.getString("mensaje");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, SMS, null, null);
    }

}
