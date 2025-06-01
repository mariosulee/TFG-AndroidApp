package com.bfr.sdkv2_tts;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.os.RemoteException;
import android.widget.TextView;

import com.bfr.buddy.speech.shared.ISTTCallback;
import com.bfr.buddy.speech.shared.STTResult;
import com.bfr.buddy.speech.shared.STTResultsData;
import com.bfr.buddy.ui.shared.FacialExpression;
import com.bfr.buddy.vision.shared.enums.TrackingMode;
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.bfr.buddy.usb.shared.IUsbCommadRsp;
import com.bfr.buddysdk.services.ModuleUSB;
import com.bfr.buddysdk.services.speech.STTTask;




import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class JuegoPantalla4 extends BuddyActivity {
    private final Handler handler = new Handler();  // Handler para gestionar los retrasos
    String nombre1, nombre2, nombre3, nombre4, nombre5;

    private STTTask ssttTask4; // Tarea de reconocimiento de voz

    private boolean yacuestiono3 = false;
    private boolean yacuestiono5 = false;
    private boolean yacuestiono1 = false;
    private boolean yacuestiono2 = false;
    private boolean yacuestiono4 = false;

    String respuesta1, respuesta2, respuesta3, respuesta4, respuesta5;
    String respuestaCuestion1, respuestaCuestion2, respuestaCuestion3, respuestaCuestion4, respuestaCuestion5;
    private int puntuacion1, puntuacion2, puntuacion3, puntuacion4, puntuacion5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_pantalla4);
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


    }



    @Override
    public void onSDKReady() {
        super.onSDKReady();

        BuddySDK.UI.setViewAsFace(findViewById(R.id.view_face4));
        BuddySDK.Speech.setSpeakerVoice("manuel");
        BuddySDK.Speech.setSpeakerSpeed(100);
        BuddySDK.Speech.setSpeakerVolume(220);
        BuddySDK.UI.setMood(FacialExpression.NEUTRAL);

        BuddySDK.Speech.startSpeaking("veamos de qué respuestas anteriores os acordáis");
        Log.i("Buddy", "Presentación de la ronda 4");

        handler.postDelayed(this::cuestionar4, 4000);

    }


    private void cuestionar4() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 4 sobre el jugador 1");

        BuddySDK.USB.rotateBuddy(130.0F, 150.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 4 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.70F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 4");
                            handler.postDelayed(() -> {
                                if (!yacuestiono4) {
                                    yacuestiono4 = true;
                                    BuddySDK.Speech.startSpeaking(nombre4 + ",  ¿Te acuerdas de lo que más le gustaba como mascota a " + nombre1 + " ¿un gato, un conejo, o un perro?");
                                    handler.postDelayed(() -> escucharCuestion("4"), 10000); // Escucha después de 10 segs
                                }
                            }, 4000);
                        }

                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el jugador 4: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro hacia el jugador 4 no se completó correctamente. Respuesta: " + s);
                }
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar hacia el jugador 4: " + s);
            }
        });
    }




    private void escucharCuestion(String numeroJugador) {
        Log.i("Buddy", "iniciando el reconocimiento de voz");

        if (ssttTask4 != null) {
            ssttTask4.stop();
            ssttTask4 = null;
        }
        ssttTask4= BuddySDK.Speech.createGoogleSTTTask(new Locale("es", "ES"));

        Log.i("Buddy", "escuchando la respuesta a la cuestion del jugador " + numeroJugador);

        ssttTask4.start(false, new ISTTCallback.Stub() {

            @Override
            public void onSuccess(STTResultsData iResults) throws RemoteException {
                Log.i("Buddy", "Reconocimiento completado");
                if (iResults != null && !iResults.getResults().isEmpty()) {
                    STTResult resultado = iResults.getResults().get(0);
                    String respuestaCuestion = resultado.getUtterance(); // Nombre reconocido
                    Log.i("Buddy", "Respuesta: " + respuestaCuestion);

                    if (ssttTask4 != null) {
                        ssttTask4.stop();
                        ssttTask4 = null;
                    }

                    if (numeroJugador.equals("1")) {
                        respuestaCuestion1 = respuestaCuestion;

                        String respuestaOriginalNormalizada = respuesta4.trim().toLowerCase();
                        String respuestaJugador1Normalizada = respuestaCuestion1.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador1Normalizada.split("\\s+")));

                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();
                        //si coinciden 2 palabras o mas, es correcto
                        if (palabrasCoinciden >= 2) {
                            puntuacion1 += 15;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Olé tú! Has acertado. Ganas 15 puntos. Ahora tienes" + puntuacion1 +"puntos");
                        } else {
                            BuddySDK.UI.setMood(FacialExpression.ANGRY);
                            puntuacion1 += 0;
                            BuddySDK.Speech.startSpeaking("¡Vaya, fallaste! Su estación favorita era " + respuesta4 + ". Lo siento, no sumas puntos. Ahora tienes" + puntuacion1 +"puntos");
                        }

                    }

                    else if (numeroJugador.equals("2")) {
                        respuestaCuestion2 = respuestaCuestion;
                        String respuestaOriginalNormalizada = respuesta5.trim().toLowerCase();
                        String respuestaJugador2Normalizada = respuestaCuestion2.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador2Normalizada.split("\\s+")));
                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();

                        if (palabrasCoinciden >= 2) {
                            puntuacion2 += 15;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Olé tú! Has acertado. Ganas 15 puntos. Ahora tienes" + puntuacion2 +"puntos");
                        } else {
                            puntuacion2 += 0;
                            BuddySDK.UI.setMood(FacialExpression.ANGRY);
                            BuddySDK.Speech.startSpeaking("¡Vaya, fallaste! Lo que dijo " + nombre5 + " fue " + respuesta5 + ". Lo siento, no sumas puntos. Tienes" +puntuacion2+ "puntos." );
                        }
                    }


                    else if (numeroJugador.equals("3")) {
                        respuestaCuestion3 = respuestaCuestion;
                        String respuestaJugador3Normalizada = respuestaCuestion3.trim().toLowerCase();

                        // Respuestas validas aceptadas
                        Set<String> respuestasValidas = new HashSet<>(Arrays.asList("Buddy", "buddy", "boodie", "budy", "budi", "budy", "Budi", "kodi", "koni", "puri", "vale", "woody", "Woody"));

                        boolean acierto = false;
                        for (String valida : respuestasValidas) {
                            if (respuestaJugador3Normalizada.contains(valida)) {
                                acierto = true;
                                break;
                            }
                        }
                        if (acierto) {
                            puntuacion3 += 15;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Bien! Has acertado. Ganas 15 puntos. Tus puntos ahora son" +puntuacion3);
                        } else {
                            puntuacion3 += 0;
                            BuddySDK.UI.setMood(FacialExpression.ANGRY);
                            BuddySDK.Speech.startSpeaking("¡Noo, fallaste! ¡Mi nombre es Buddy! Siento decirte que no sumas puntos. Tu puntuacion es" +puntuacion3 +"puntos");
                        }
                    }

                    else if (numeroJugador.equals("4")) {
                        respuestaCuestion4 = respuestaCuestion;
                        String respuestaOriginalNormalizada = respuesta1.trim().toLowerCase();
                        String respuestaJugador4Normalizada = respuestaCuestion4.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador4Normalizada.split("\\s+")));
                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();

                        if (palabrasCoinciden >= 2) {
                            puntuacion4 += 15;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Olé tú! Has acertado. Ganas 15 puntos. Tienes "+ puntuacion4 +"puntos");
                        } else {
                            puntuacion4 += 0;
                            BuddySDK.UI.setMood(FacialExpression.ANGRY);
                            BuddySDK.Speech.startSpeaking("¡Vaya, fallaste! Lo que dijo " + nombre1 + " fue " + respuesta1 + ". Jolin, no sumas puntos. Tienes" +puntuacion4 +" puntos.");
                        }
                    }

                    else if (numeroJugador.equals("5")) {
                        respuestaCuestion5 = respuestaCuestion;
                        String respuestaJugador5Normalizada = respuestaCuestion5.trim().toLowerCase();

                        // Respuestas validas aceptadas
                        Set<String> respuestasValidas = new HashSet<>(Arrays.asList("tres", "el tres", "3", "el 3", "friends"));

                        boolean acierto = false;
                        for (String valida : respuestasValidas) {
                            if (respuestaJugador5Normalizada.contains(valida)) {
                                acierto = true;
                                break;
                            }
                        }

                        if (acierto) {
                            puntuacion5 += 15;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Genial! Has acertado. Ganas 15 puntos. Tienes "+puntuacion5+" puntos");
                        } else {
                            puntuacion5 += 0;
                            BuddySDK.UI.setMood(FacialExpression.ANGRY);
                            BuddySDK.Speech.startSpeaking("¡Oh, has fallado! El número de jugador de " + nombre3 + " es el tres. Lo siento, sumas cero puntos. Tu puntuación es" +puntuacion5 +" puntos.");
                        }
                    }






                    //PARA MOVERSE DE NUEVO AL CENTRO

                    if(numeroJugador.equals("1")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(1);
                        }, 11000);
                    }
                    else if(numeroJugador.equals("2")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(2);
                        }, 11000);
                    } else if(numeroJugador.equals("3")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentroFinal2();
                        }, 11000);
                    } else if(numeroJugador.equals("4")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(4);
                        }, 11000);
                    }else if(numeroJugador.equals("5")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(5);
                        }, 11000);
                    }

                } else {
                    Log.e("Buddy", "No se reconoció ninguna respuesta");
                    BuddySDK.Speech.startSpeaking("No te he entendido, ¿me lo puedes repetir por favor?");
                }
            }
            @Override
            public void onError(String error) throws RemoteException {
                Log.e("Buddy", "Error al reconocer el nombre: " + error);
                BuddySDK.Speech.startSpeaking("No he podido entenderlo, ¿me lo puedes repetir por favor?");
            }
        });
    }





    private void volverAlCentro2DesdeJugador(int numJugador) {
        Log.i("Buddy", "Volviendo al centro desde el jugadorX para cuestionar...");

        float velocidad = 0.7F;
        float distancia = 0.70F;
        float velocidadRotacion = 130.0F; //
        float angulo = 180.0F; // Girar 180 grados
        ModuleUSB.BuddyAccelerations aceleracion = ModuleUSB.BuddyAccelerations.HIGH;

        Log.i("Buddy", "Iniciando giro de 180 grados");

        // Primero gira 180 grados
        BuddySDK.USB.rotateBuddy(velocidadRotacion, angulo, aceleracion, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {

                if ("WHEEL_MOVE_FINISHED".equals(s)) {

                    Log.i("Buddy", "Iniciando movimiento hacia el centro");

                    // Ahora se mueve al centro
                    BuddySDK.USB.moveBuddy(velocidad, distancia, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Ha vuelto al centro");
                            handler.postDelayed(() -> {

                                switch (numJugador) {
                                    case 1:
                                        cuestionar3();
                                        break;
                                    case 2:
                                        cuestionar5();
                                        break;

                                    case 4:
                                        cuestionar2();
                                        break;
                                    case 5:
                                        cuestionar1();
                                        break;

                                    default:
                                        Log.e("Buddy", "Jugador no válido: " + numJugador);
                                        break;
                                }
                            }, 2000); // Espera 2 segs antes de moverse
                        }

                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el centro: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro no terminó correctamente. Respuesta: " + s);
                }
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar: " + s);
            }
        });
    }


    private void cuestionar2() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 2 sobre el jugador 5");

        BuddySDK.USB.rotateBuddy(130.0F, -45.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 2 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.60F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 2 para cuestionarlo");
                            handler.postDelayed(() -> {
                                if (!yacuestiono2) {
                                    yacuestiono2= true;
                                    BuddySDK.Speech.startSpeaking(nombre2 + " , ¿Que definía mejor a " + nombre5 + ", la ambición, la empatía o la generosidad?");
                                    handler.postDelayed(() -> escucharCuestion("2"), 9000);
                                }
                            }, 4000);
                        }

                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el jugador 2: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro hacia el jugador 2 no se completó correctamente. Respuesta: " + s);
                }
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar hacia el jugador 2: " + s);
            }
        });
    }


    private void cuestionar5() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 5 sobre el jugador 3");

        BuddySDK.USB.rotateBuddy(130.0F, -30.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 5 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.65F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 5 para cuestionarlo");
                            handler.postDelayed(() -> {
                                if (!yacuestiono5) {
                                    yacuestiono5 = true;
                                    BuddySDK.Speech.startSpeaking(nombre5 + " ,Venga, una fácil, ¿Qué número de jugador tenía " + nombre3 + "?" + "¿el 1, el 2, el 3, el 4 o el 5?");
                                    handler.postDelayed(() -> escucharCuestion("5"), 12000); // Escucha después de 12 segs
                                }
                            }, 4000);
                        }

                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el jugador 5: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro hacia el jugador 5 no se completó correctamente. Respuesta: " + s);
                }
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar hacia el jugador 5: " + s);
            }
        });
    }

    private void cuestionar1() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 1 sobre el jugador 4");

        BuddySDK.USB.rotateBuddy(130.0F, 98.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 1 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.62F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 1 para cuestionarlo");
                            handler.postDelayed(() -> {
                                if (!yacuestiono1) {
                                    yacuestiono1 = true;
                                    BuddySDK.Speech.startSpeaking(nombre1 + " , ¿Cual es la mejor estación del año según " + nombre4 + " ? , ¿el otoño, la primavera, el verano, o el invierno?");
                                    handler.postDelayed(() -> escucharCuestion("1"), 10000); // Escucha después de 10 segs
                                }
                            }, 4000);
                        }

                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el jugador 1: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro hacia el jugador 1 no se completó correctamente. Respuesta: " + s);
                }
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar hacia el jugador 1: " + s);
            }
        });
    }



    private void cuestionar3() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 3 sobre buddy");

        BuddySDK.USB.rotateBuddy(130.0F, 37.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 3 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.65F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 3");
                            handler.postDelayed(() -> {
                                if (!yacuestiono3) {
                                    yacuestiono3 = true;
                                    BuddySDK.Speech.startSpeaking(nombre3 + ",  ¿Te acuerdas de mi nombre? ¿soy el robot Budi?, ¿Coni?, ¿o Brody?");
                                    handler.postDelayed(() -> escucharCuestion("3"), 8000); // Escucha después de 8 segs
                                }
                            }, 4000);
                        }
                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el jugador 3: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro hacia el jugador 3 no se completó correctamente. Respuesta: " + s);
                }
            }
            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar hacia el jugador 3: " + s);
            }
        });
    }










    private void volverAlCentroFinal2() {
        Log.i("Buddy", "Volviendo al centro por últimisima vez");

        float velocidad = 0.7F;
        float distancia = 0.72F;
        float velocidadRotacion = 130.0F; //
        float angulo = 180.0F; // Girar 180 grados
        ModuleUSB.BuddyAccelerations aceleracion = ModuleUSB.BuddyAccelerations.HIGH;

        Log.i("Buddy", "Iniciando giro de 180 grados...");

        // primero gira 180 grados
        BuddySDK.USB.rotateBuddy(velocidadRotacion, angulo, aceleracion, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {

                if ("WHEEL_MOVE_FINISHED".equals(s)) {

                    Log.i("Buddy", "Iniciando movimiento hacia el centro");

                    // ahora se mueve al centro
                    BuddySDK.USB.moveBuddy(velocidad, distancia, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "ha vuelto al centro");
                            handler.postDelayed(() ->{

                                BuddySDK.USB.rotateBuddy(velocidadRotacion, -43.0F, aceleracion, new IUsbCommadRsp.Stub(){

                                    @Override
                                    public void onSuccess(String s) throws RemoteException {
                                        Log.i("Buddy", "Se ha vuelto a poner enfrente del jugador1");
                                        handler.postDelayed(() -> {

                                            BuddySDK.Speech.startSpeaking("Bueno chicos, esta ronda ha terminado");


                                            // ANUNCIAR LAS PUNTUACIONES
                                            handler.postDelayed(() -> {
                                                // Crear array de nombres y puntuaciones
                                                String[] nombres = {nombre1, nombre2, nombre3, nombre4, nombre5};
                                                int[] puntuaciones = {puntuacion1, puntuacion2, puntuacion3, puntuacion4, puntuacion5};

                                                // Ordenar por puntuación de mayor a menor
                                                for (int i = 0; i < puntuaciones.length - 1; i++) {
                                                    for (int j = i + 1; j < puntuaciones.length; j++) {
                                                        if (puntuaciones[j] > puntuaciones[i]) {
                                                            int tempPuntos = puntuaciones[i];
                                                            puntuaciones[i] = puntuaciones[j];
                                                            puntuaciones[j] = tempPuntos;
                                                            String tempNombre = nombres[i];
                                                            nombres[i] = nombres[j];
                                                            nombres[j] = tempNombre;
                                                        }
                                                    }
                                                }
                                                StringBuilder mensaje = new StringBuilder("Voy a anunciar la clasificación habiendo hecho esta segunda ronda: ");
                                                for (int i = 0; i < nombres.length; i++) {
                                                    mensaje.append("En el puesto número ").append(i + 1).append(" está ").append(nombres[i]).append(" con ").append(puntuaciones[i]).append(" puntos. ");
                                                }

                                                BuddySDK.Speech.startSpeaking(mensaje.toString());

                                                handler.postDelayed(() -> {
                                                    BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                                                    BuddySDK.Speech.startSpeaking( "Dicho eso, comencemos ahora la tercera y última ronda del juego. Para esta ronda jugaremos al que prefieres, en el que os daré a escoger una preferencia entre dos opciones y tendréis que elegir usando mi pantalla táctil. Otro jugador tendrá que adivinar lo que habeis pulsado, así pondré a prueba lo que os conoceis. ¿Os parece?\"");

                                                }, 37000);

                                                handler.postDelayed(() -> {
                                                    Log.i("Buddy", "Intentando cambiar a la ultimisima actividad");
                                                    Intent intent = new Intent(JuegoPantalla4.this, JuegoPantalla3.class);
                                                    intent.putExtra("nombre1", nombre1);
                                                    intent.putExtra("nombre2", nombre2);
                                                    intent.putExtra("nombre3", nombre3);
                                                    intent.putExtra("nombre4", nombre4);
                                                    intent.putExtra("nombre5", nombre5);


                                                    intent.putExtra("puntuacion1", puntuacion1);
                                                    intent.putExtra("puntuacion2", puntuacion2);
                                                    intent.putExtra("puntuacion3", puntuacion3);
                                                    intent.putExtra("puntuacion4", puntuacion4);
                                                    intent.putExtra("puntuacion5", puntuacion5);

                                                    startActivity(intent);

                                                    finish();

                                                }, 48000); // Espera hasta que termine el habla

                                            }, 4000); // Espera 4 segundos tras el primer mensaje

                                        }, 4000); // Espera 4 segundos antes de hablar
                                    }

                                    @Override
                                    public void onFailed(String s) throws RemoteException {
                                        Log.e("Buddy", "No se ha vuelto a poner enfrente del jugador1");
                                    }
                                });


                            }, 2000);
                        }

                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "error al moverse hacia el centro: " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", " el giro no terminó correctamente. Respuesta: " + s);
                }
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "error al girar: " + s);
            }

        });
    }













}