package mx.itesm.alertify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BotonFrag extends Fragment{

    private TinyDB ajustes;
    private Context mContext;
    private int clicked;
    private TextView timerTV;
    private int tiempo;
    private CountDownTimer timer2;

    CircularProgressButton sosButton;

    public BotonFrag() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_boton, container, false);

        clicked = 0;
        tiempo=15;
        timer2 = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(tiempo==10 || tiempo==7 || tiempo==3){
                    doActions(tiempo);
                }

                else {
                    timerTV.setText("0:" + checkDigit(tiempo));
                }

                tiempo--;
            }

            @Override
            public void onFinish() {
                timerTV.setText("");
                Toast.makeText(getActivity(), "Alerta envíada", Toast.LENGTH_LONG)
                        .show();
                tiempo=15;
                sosButton.revertAnimation();
                clicked=0;
                timer2.cancel();
            }
        };

        timerTV = v.findViewById(R.id.textView9);

        sosButton = v.findViewById(R.id.btnAlerta);

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked+=1;

                if(clicked==1) {
                    @SuppressLint("StaticFieldLeak")
                    AsyncTask<String, String, String> sosSend = new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                sosButton.setEnabled(false);
                                sosButton.setClickable(false);
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return "done";
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            if (s.equals("done")) {
                                sosButton.setText("Presiona para cancelar...");
                                sosButton.setEnabled(true);
                                sosButton.setClickable(true);
                                timer2.start();
                            }
                        }
                    };

                    sosButton.startAnimation();
                    sosButton.setClickable(true);
                    sosButton.setEnabled(true);
                    sosSend.execute();
                }

                else{
                    sosButton.revertAnimation();
                    clicked=0;
                    tiempo=15;
                    timerTV.setText("");
                    timer2.cancel();

                }

            }
        });

        getActivity().setTitle("Alertify");

        return v;
    }

    private void doActions(int tiempo) {
        if(tiempo==10 && ajustes.getString("mensaje").length()!=0 && ajustes.getBoolean("useMessages")){
            timerTV.setText("Envíando mensaje...");

            for(int i=0;i<ajustes.getListString("numeros").size();i++){
                sendSMS(ajustes.getListString("numeros").get(i));
           }
        }

        else if(tiempo==10 && (ajustes.getString("mensaje").length()==0 || !ajustes.getBoolean("useMessages"))){
            timerTV.setText("0:10");
            Toast.makeText(getActivity(), "No seleccionaste contactos", Toast.LENGTH_SHORT)
                    .show();
        }

        if(tiempo==7 && ajustes.getBoolean("useWhatsapp")){
            timerTV.setText("Abriendo Whatsapp...");
            onClickWhatsApp(getView());
        }

        else if(tiempo==7 && !ajustes.getBoolean("useWhatsapp"))
            timerTV.setText("0:07");

        if(tiempo==3 && ajustes.getBoolean("callContact") && ajustes.getString("contactPrincipal")!=null &&
                ajustes.getString("numeroPrincipal")!= null){
            timerTV.setText("Iniciando llamada...");
            call();
        }

        else if(tiempo==3 && ajustes.getBoolean("call911")){
            timerTV.setText("Llamando al 911...");
            call911();
        }

        else if(tiempo==3 && (!ajustes.getBoolean("call911") || !ajustes.getBoolean("callContact"))){
            timerTV.setText("0:03");
            Toast.makeText(getActivity(), "No tienes la opción de llamada seleccionada", Toast.LENGTH_SHORT)
                    .show();
        }
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

    //Metodo para llamar al 911
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

    //Metodo para enviar mensaje por whatsapp
    public void onClickWhatsApp(View view) {
        PackageManager pm=Objects.requireNonNull(getActivity()).getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = ajustes.getString("mensaje");

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivityForResult(Intent.createChooser(waIntent, "Share with"),1);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //Metodo para ir checando el timer
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
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
}
