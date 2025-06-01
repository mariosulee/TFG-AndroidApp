package com.bfr.sdkv2_tts;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bfr.buddy.speech.shared.ITTSCallback;
import com.bfr.buddy.ui.shared.FacialExpression;
import com.bfr.buddy.utils.events.EventItem;
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;

//declarar la clase mainActivity, que hereda de BuddyActivity para integrar el SDK de Buddy
public class MainActivity extends BuddyActivity {

    final String  TAG = "SDKv2TTS"; //registra mensajes en los logs para depurar
    TextView mPositivityValue;   //inicializa la variable textView


    @Override    //onCreate ES EL METODO QUE SE EJECUTA CUANDO SE CREA LA ACTIVIDAD
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //esto carga el diseño XML asociado


        //CONFIGURACION DEL BOTON DE INFORMACION
        Button btnInformacion=findViewById(R.id.button);
        TextView textView=findViewById(R.id.textView);

        btnInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //QUE OCURRE CUANDO PULSO EL BOTON

                if(textView.getVisibility() == View.VISIBLE){  //SI EL TEXTO YA ESTA VISIBLE, LO QUITAMOS
                    textView.setVisibility(GONE);
                }
                else{
                    textView.setText("Hola, mi nombre es Mario Sulé, soy estudiante de último año de Ingeniería Informática" +
                            " en la Universidad de Granada y este es mi Proyecto de Fin de Grado: el desarrollo de una " +
                            "aplicación para hacer un juego social con el fin de incrementar las interacciones sociales " +
                            "de los adultos mayores, usando el robot social Buddy. ETSIIT, UGR (2025).");
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });


        //codigo para el boton inicio
        Button inicioButton = findViewById(R.id.inicioButton);
        inicioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //metodo para cuando se clicka ese boton
                // Crea un intent para abrir WelcomePage
                Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                startActivity(intent);
                finish();
            }
        });

    }



    @Override
    //se llama a esta funcion cuando el SDK de Buddy este listo para ser usado
    public void onSDKReady() {
        BuddySDK.UI.setViewAsFace(findViewById(R.id.view_face));  //para mandar los touch information al background
        BuddySDK.UI.setFacialExpression(FacialExpression.NEUTRAL);

    }

    // Catches SPEAKING event.
    // Writes what has been spoken.
    @Override
    public void onEvent(EventItem iEvent) {

    }

}


