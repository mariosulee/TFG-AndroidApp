package com.bfr.sdkv2_tts;

import android.os.Bundle;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.bfr.buddy.speech.shared.ITTSCallback;

public class WelcomePage extends BuddyActivity {

    Button botonLeerInstrucciones, botonStop;
    final String TAG="SDKv2TTS";
    private TextView instrucciones;
    private SeekBar controlVolumen;
    private float volumenInicial=0.5f;  //equivale al 50% en seekbar

    // Usamos los mismos nombres que JuegoPantalla3 envía y JuegoPantalla4 espera
    private String nombre1, nombre2, nombre3, nombre4, nombre5;
    private String respuesta1, respuesta2, respuesta3, respuesta4, respuesta5;
    private int puntuacion1, puntuacion2, puntuacion3, puntuacion4, puntuacion5;
    private boolean datosParaRonda4Disponibles = false; // Flag interno para saber si tenemos datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. DISEÑO DE LA PÁGINA
        // para ocultar la barra de estado y navegación
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        // aplicar el diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //recibir todo, para cuando venga de la ronda del que prefieres
        nombre1 = getIntent().getStringExtra("nombre1");
        nombre2 = getIntent().getStringExtra("nombre2");
        nombre3 = getIntent().getStringExtra("nombre3");
        nombre4 = getIntent().getStringExtra("nombre4");
        nombre5 = getIntent().getStringExtra("nombre5");

        puntuacion1 = getIntent().getIntExtra("puntuacion1", 0);
        puntuacion2 = getIntent().getIntExtra("puntuacion2", 0);
        puntuacion3 = getIntent().getIntExtra("puntuacion3", 0);
        puntuacion4 = getIntent().getIntExtra("puntuacion4", 0);
        puntuacion5 = getIntent().getIntExtra("puntuacion5", 0);

        respuesta1 = getIntent().getStringExtra("respuesta1");
        respuesta2 = getIntent().getStringExtra("respuesta2");
        respuesta3 = getIntent().getStringExtra("respuesta3");
        respuesta4 = getIntent().getStringExtra("respuesta4");
        respuesta5 = getIntent().getStringExtra("respuesta5");



        // 2. BOTONES Y FUNCIONALIDADES

        // 2.1 Instrucciones
        instrucciones = findViewById(R.id.instruccionesTexto); // Se inicializa el TextView

        String instruccionesTexto = "1. Debemos estar en silencio para que el juego se ejecute correctamente.\n" +
                "2. Para contestar a Buddy, debemos hablarle SOLO cuando esté escuchando. \n"+
                "3. Hablaremos solo en nuestro turno. \n"+
                "4. Si se necesita ayuda se debe consultar al supervisor del juego.\n"+
                "5. Lo más importante es pasarlo bien.";


        instrucciones.setText(instruccionesTexto); // para poner las instrucciones en el TextView

        botonLeerInstrucciones = findViewById(R.id.botonLeer); // se inicializa el boton
        // 2.1.1 Boton Leer en voz alta para leer en voz alta instrucciones
        botonLeerInstrucciones.setOnClickListener(v -> {
            if (BuddySDK.Speech.isReadyToSpeak()) {
                int volumenConvertido=(int)(volumenInicial*400);
                BuddySDK.Speech.setSpeakerVolume(volumenConvertido);
                Log.i(TAG, "Volumen ajustado antes de hablar: "+volumenConvertido);
                BuddySDK.Speech.startSpeaking(
                        instruccionesTexto,
                        new ITTSCallback.Stub() {
                            @Override
                            public void onSuccess(String s) {
                                Log.i(TAG, "Buddy ha leído las instrucciones correctamente: " + s);
                            }
                            @Override
                            public void onPause() throws RemoteException {
                            }
                            @Override
                            public void onResume() throws RemoteException {
                            }
                            @Override
                            public void onError(String s) {
                                Log.e(TAG, "Error en la lectura de instrucciones: " + s);
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "TTS de Buddy no está listo", Toast.LENGTH_SHORT).show();
            }
        });

        // 2.2 control de volumen
        controlVolumen=findViewById(R.id.controlVolumen);
        controlVolumen.setProgress(50);

        controlVolumen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumenInicial=progress/100.0f; //esto es para convertir el progreso 0-100 a un valor entre 0 y 1
                int volumenConvertido=(int)(volumenInicial*400); //para convertirlo a 0-100
                BuddySDK.Speech.setSpeakerVolume(volumenConvertido);
                Log.i(TAG, "Volumen ajustado a " + volumenConvertido);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 2.3 para el boton stop
        Button botonStop=findViewById(R.id.botonStop);
        botonStop.setOnClickListener(v-> {
            BuddySDK.Speech.stopSpeaking();
            Log.i(TAG, "Buddy ha detenido la lectura");
        });

        // 2.4 boton para volver al inicio
        findViewById(R.id.botonVolverAtras).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                startActivity(intent);
                finish();  // cerrar la actividad actual para que no se quede en el stack
            }
        });


        // 2.5 boton para comenzar el juego
        Button botonComenzar=findViewById(R.id.botonComenzar);
        botonComenzar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(WelcomePage.this, JuegoPantalla.class); //crear intent para iniciar la actividad JuegoPantalla
                startActivity(intent); // inicio la actividad
                finish();
            }
        });


        // NUEVO, BOTON PARA IR A LA DESPEDIDA
        Button botonIrDespedida=findViewById(R.id.botonIrDespedida);
        botonIrDespedida.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(WelcomePage.this, Despedida.class); //crear intent para iniciar la actividad JuegoPantalla
                startActivity(intent); // inicio la actividad
                finish();
            }
        });
    }
}