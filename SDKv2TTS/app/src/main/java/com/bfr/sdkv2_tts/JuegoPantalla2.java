package com.bfr.sdkv2_tts;

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
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.bfr.buddy.usb.shared.IUsbCommadRsp;
import com.bfr.buddysdk.services.ModuleUSB;
import com.bfr.buddysdk.services.speech.STTTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class JuegoPantalla2 extends BuddyActivity {

    private final Handler handler = new Handler();  // Handler para gestionar los retrasos
    String nombre1,nombre2,nombre3,nombre4,nombre5;


    private STTTask ssttTask2; // Tarea de reconocimiento de voz
    private boolean yapregunto=false;
    private boolean yapregunto2=false;
    private boolean yapregunto3=false;
    private boolean yapregunto4=false;
    private boolean yapregunto5=false;

    private boolean yacuestiono3=false;
    private boolean yacuestiono5=false;
    private boolean yacuestiono1=false;
    private boolean yacuestiono2=false;
    private boolean yacuestiono4=false;

    String respuesta1, respuesta2, respuesta3, respuesta4, respuesta5;
    String respuestaCuestion1,respuestaCuestion2, respuestaCuestion3, respuestaCuestion4, respuestaCuestion5;

    private int puntuacion1 = 0;
    private int puntuacion2 = 0;
    private int puntuacion3 = 0;
    private int puntuacion4 = 0;
    private int puntuacion5 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_pantalla2);
        nombre1 = getIntent().getStringExtra("nombreJugador1");
        nombre2 = getIntent().getStringExtra("nombreJugador2");
        nombre3 = getIntent().getStringExtra("nombreJugador3");
        nombre4 = getIntent().getStringExtra("nombreJugador4");
        nombre5 = getIntent().getStringExtra("nombreJugador5");

    }


    @Override
    public void onSDKReady() {
        super.onSDKReady();

        BuddySDK.UI.setMood(FacialExpression.THINKING);
        BuddySDK.UI.setViewAsFace(findViewById(R.id.view_face));
        BuddySDK.Speech.setSpeakerVoice("manuel");
        BuddySDK.Speech.setSpeakerSpeed(100);
        BuddySDK.Speech.setSpeakerVolume(220);


        //INFORMACION DE INICIO DE LA SEGUNDA RONDA
        handler.postDelayed(() -> {
            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
            BuddySDK.Speech.startSpeaking("Bueno, vamos a comenzar con la segunda ronda del juego.... Voy a ir haciendos preguntas uno por uno. Tenéis que estar atentos de lo que responden vuestros compañeros para poder ganar puntos. ¿Preparados?");
            Log.i("Buddy", "Buddy ya se ha dicho que es la 2da ronda");

            handler.postDelayed(this::moverseJugador1, 16000); // Esperar 16 segundos para mover a Buddy
        }, 3000); // Buddy cambia la cara y presenta la 2da ronda después de 3 segundos

    }










    private void moverseJugador1() {
        Log.i("Buddy", "moviendose hacia el jugador 1");
        BuddySDK.USB.moveBuddy(0.7F, 0.65F, new IUsbCommadRsp.Stub() {

            @Override
            public void onSuccess(String s) throws RemoteException {
                Log.i("Buddy", "llego al jugador 1");

                handler.postDelayed(() -> {
                    if(!yapregunto) {
                        yapregunto = true;
                        BuddySDK.Speech.startSpeaking(nombre1 +", Cuéntame, ¿si pudieras tener una mascota, preferirias un gato, un perro, o un conejo?");
                        handler.postDelayed(() -> escucharRespuesta("1"), 10000);// espera 10 segundos antes de que responda
                    }
                }, 4000); // espera 4 segundos después de llegar para preguntarle
            }
            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al moverse hacia el jugador 1: " + s);
            }
        });
    }









    private void escucharRespuesta(String numeroJugador) {
        Log.i("Buddy", "iniciando el reconocimiento de voz");

        ssttTask2 = BuddySDK.Speech.createGoogleSTTTask(new Locale("es", "ES"));
        Log.i("Buddy", "escuchando la respuesta del jugador " + numeroJugador);

        ssttTask2.start(false, new ISTTCallback.Stub() {

            @Override
            public void onSuccess(STTResultsData iResults) throws RemoteException {
                Log.i("Buddy", "Reconocimiento completado");

                if (iResults != null && !iResults.getResults().isEmpty()) {
                    STTResult resultado = iResults.getResults().get(0);
                    String respuesta = resultado.getUtterance(); // Nombre reconocido
                    Log.i("Buddy", "Respuesta: " + respuesta);


                    Log.i("Buddy", "Parando escucha antes de hablar");


                    ssttTask2.pause();



                    Log.i("Buddy", "Escucha detenida, ahora hablará");

                    if (numeroJugador.equals("1")) {
                        respuesta1 = respuesta;
                        handler.postDelayed(() -> {
                            BuddySDK.Speech.startSpeaking("Así que preferirías tener " + respuesta1 + ", tienes buen gusto");
                        }, 5000); // espera 5 segundos
                    }

                    else if(numeroJugador.equals("2")){
                        respuesta2=respuesta;
                        BuddySDK.Speech.startSpeaking("Con que te consideras más de " + respuesta2 + ". Eres de los míos entonces");

                    }
                    else if(numeroJugador.equals("3")){
                        respuesta3=respuesta;
                        BuddySDK.Speech.startSpeaking(respuesta3 + " ,que buen artista, está claro que hay cantantes que son atemporales");
                    }
                    else if(numeroJugador.equals("4")){
                        respuesta4=respuesta;
                        BuddySDK.Speech.startSpeaking("A mi también me parece que " + respuesta4 + ",es la mejor estación del año. Somos unos fenómenos.");
                    }

                    else if(numeroJugador.equals("5")){
                        respuesta5=respuesta;
                        BuddySDK.Speech.startSpeaking("Está claro que " + respuesta5 + " vale de mucho en esta vida. Te felicito entonces.");
                    }




                    if(numeroJugador.equals("1")){
                        handler.postDelayed(() -> volverAlCentro(), 10000); // para volver al centro, y luego iniciar el moverseJugador2
                    }
                    else if(numeroJugador.equals("2")){
                        handler.postDelayed(() -> volverAlCentroDesdeJugador(2), 8000);
                    } else if(numeroJugador.equals("3")){
                        handler.postDelayed(() -> volverAlCentroDesdeJugador(3), 8000);
                    } else if(numeroJugador.equals("4")){
                        handler.postDelayed(() -> volverAlCentroDesdeJugador(4), 8000);
                    }else if(numeroJugador.equals("5")){
                        handler.postDelayed(() -> volverAlCentroFinal(), 8000);
                    }



                } else {
                    Log.e("Buddy", "No se reconoció ninguna respuesta");
                    BuddySDK.Speech.startSpeaking("No te he podido entender, repitelo por favor");
                }
            }

            @Override
            public void onError(String error) throws RemoteException {
                Log.e("Buddy", "No se reconoció ninguna respuesta");
                BuddySDK.Speech.startSpeaking("No te he podido entender, repitelo por favor");
                if (ssttTask2 != null) {
                    ssttTask2.pause();
                }
            }
        });
    }







    private void volverAlCentro() {
        Log.i("Buddy", "Volviendo al centro...");

        float velocidad = 0.7F;
        float distancia = 0.70F;
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
                            handler.postDelayed(() -> moverseJugador2(), 2000); //espera 2 segs antes de volver al jugador 2
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











    private void moverseJugador2() {
        Log.i("Buddy", "Girando hacia el jugador 2...");

        float velocidadRotacion = 130.0F;
        float angulo = 108.0F; // Gira 72 grados a la derecha
        ModuleUSB.BuddyAccelerations aceleracion = ModuleUSB.BuddyAccelerations.HIGH;

        BuddySDK.USB.rotateBuddy(velocidadRotacion, angulo, aceleracion, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 2 completado, moviéndose...");

                    BuddySDK.USB.moveBuddy(0.7F, 0.60F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 2");
                            handler.postDelayed(() -> {
                                if(!yapregunto2){
                                    yapregunto2=true;
                                    BuddySDK.Speech.startSpeaking(nombre2 + ", Dime, ¿Te gustan más los libros, los deportes, o las películas?");
                                    handler.postDelayed(() -> escucharRespuesta("2"), 7000);
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




















    private void volverAlCentroDesdeJugador(int numJugador) {
        Log.i("Buddy", "Volviendo al centro desde el jugadorX...");

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
                                    case 2:
                                        moverseJugador3();
                                        break;
                                    case 3:
                                        moverseJugador4();
                                        break;
                                    case 4:
                                        moverseJugador5();
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












    private void moverseJugador3() {
        Log.i("Buddy", "Moviéndose hacia el jugador 3...");

        BuddySDK.USB.rotateBuddy(130.0F, 102.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 3 completado, moviéndose...");

                    BuddySDK.USB.moveBuddy(0.7F, 0.60F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 3");
                            handler.postDelayed(() -> {
                                if(!yapregunto3){
                                    yapregunto3=true;
                                    BuddySDK.Speech.startSpeaking(nombre3 + ",  Vamos a sincerarnos, ¿Quién crees que es tu cantante favorito? ");
                                    handler.postDelayed(() -> escucharRespuesta("3"), 7000);
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









    private void moverseJugador4() {
        Log.i("Buddy", "Moviéndose hacia el jugador 4...");

        BuddySDK.USB.rotateBuddy(130.0F, 100.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 4 completado, moviéndose...");

                    BuddySDK.USB.moveBuddy(0.7F, 0.60F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 4");
                            handler.postDelayed(() -> {
                                if (!yapregunto4) {
                                    yapregunto4 = true;
                                    BuddySDK.Speech.startSpeaking(nombre4 + ", dime, ¿Cuál crees que es la estación mas bonita del año? , ¿La primavera, el verano, el otoño, o el invierno?");
                                    handler.postDelayed(() -> escucharRespuesta("4"), 11000); // Escucha después de 11 segundos
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









    private void moverseJugador5() {
        Log.i("Buddy", "Moviéndose hacia el jugador 5");

        BuddySDK.USB.rotateBuddy(130.0F, 95.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 5 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.62F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 5");
                            handler.postDelayed(() -> {
                                if (!yapregunto5) {
                                    yapregunto5 = true;
                                    BuddySDK.Speech.startSpeaking(nombre5 + ", ¿Te creías que me había olvidado de tí? Dime, ¿Qué crees que te define mejor como persona? ¿La ambición, la generosidad, o la empatía?");
                                    handler.postDelayed(() -> escucharRespuesta("5"), 13000); // Escucha después de 13 segundos
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











    private void volverAlCentroFinal() {
        Log.i("Buddy", "Volviendo al centro por última vez");

        float velocidad = 0.7F;
        float distancia = 0.73F;
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

                                BuddySDK.USB.rotateBuddy(velocidadRotacion, 95.0F, aceleracion, new IUsbCommadRsp.Stub(){

                                    @Override
                                    public void onSuccess(String s) throws RemoteException {
                                        Log.i("Buddy", "Se ha vuelto a poner enfrente del jugador1");
                                        handler.postDelayed(() -> {

                                            BuddySDK.Speech.startSpeaking("Muy bien, ahora que ya habeis respondido todos, voy a poneros a prueba a ver si habeis estado atentos a lo que han respondido vuestros compañeros");

                                            // despues de que Buddy termine de hablar, pasa a EMPEZAR A CUESTIONAR
                                            handler.postDelayed(() -> {
                                                Log.i("Buddy", "ahora va a empezar a cuestionar");
                                                cuestionar3();
                                            }, 13000); // Espera hasta que termine el habla

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










    private void cuestionar3() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 3 sobre el jugador 1");

        BuddySDK.USB.rotateBuddy(130.0F, 218.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
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
                                    BuddySDK.Speech.startSpeaking(nombre3 + ",  ¿Qué dijo " + nombre1 + " que preferiría tener como mascota? ¿un conejo, un gato, o un perro?");
                                    handler.postDelayed(() -> escucharCuestion("3"), 9500); // Escucha despues de 9,5 segs
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





    private void escucharCuestion(String numeroJugador) {
        Log.i("Buddy", "iniciando el reconocimiento de voz");

        if (ssttTask2 != null) {
            ssttTask2.stop();
            ssttTask2 = null;
        }
        ssttTask2 = BuddySDK.Speech.createGoogleSTTTask(new Locale("es", "ES"));

        Log.i("Buddy", "escuchando la respuesta a la cuestion del jugador " + numeroJugador);

        ssttTask2.start(false, new ISTTCallback.Stub() {

            @Override
            public void onSuccess(STTResultsData iResults) throws RemoteException {
                Log.i("Buddy", "Reconocimiento completado");
                if (iResults != null && !iResults.getResults().isEmpty()) {
                    STTResult resultado = iResults.getResults().get(0);
                    String respuestaCuestion = resultado.getUtterance(); // Nombre reconocido
                    Log.i("Buddy", "Respuesta: " + respuestaCuestion);

                    if (ssttTask2 != null) {
                        ssttTask2.stop();
                        ssttTask2 = null;
                    }

                    if (numeroJugador.equals("1")) {
                        respuestaCuestion1 = respuestaCuestion;

                        // Normalizar ambas respuestas para evitar errores por mayusculas o espacios
                        String respuestaOriginalNormalizada = respuesta3.trim().toLowerCase();  //respuesta que dio el jugador 3 a su pregunta personal
                        String respuestaJugador1Normalizada = respuestaCuestion1.trim().toLowerCase();   // respuesta que da el jugador 1 sobre lo que respondio el jugador3
                        // .trim(): eliminar espacios en blanco al principio y al final de las respuestas
                        //.toLowerCase(): convertir todo el texto a minusculas

                        //aqui se separan las respuestas en palabras individuales y se crea un set para q no haya palabras repetidas
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador1Normalizada.split("\\s+")));

                        // retainAll(): mantiene en palabrasOriginal solo las palabras que tambien estan en palabrasJugador.
                        palabrasOriginal.retainAll(palabrasJugador);

                        int palabrasCoinciden = palabrasOriginal.size(); //si coinciden 2 palabras o mas, es correcto

                        if (palabrasCoinciden >= 2) {
                            puntuacion1 += 10;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            handler.postDelayed(() ->BuddySDK.Speech.startSpeaking("¡Correcto! Has acertado. Ganas 10 puntos. Tu puntuación actual es de " + puntuacion1 + " puntos."),1000);
                        } else {
                            BuddySDK.UI.setMood(FacialExpression.SAD);
                            handler.postDelayed(() -> BuddySDK.Speech.startSpeaking("¡Fallaste! Lo que dijo " + nombre3 + " fue " + respuesta3 + ". Lo siento pero no sumas puntos. Tu puntuación actual es de " + puntuacion1 + " puntos."),1000);
                        }

                    }

                    else if (numeroJugador.equals("2")) {
                        respuestaCuestion2 = respuestaCuestion;
                        String respuestaOriginalNormalizada = respuesta4.trim().toLowerCase();
                        String respuestaJugador2Normalizada = respuestaCuestion2.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador2Normalizada.split("\\s+")));
                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();

                        if (palabrasCoinciden >= 2) {
                            puntuacion2 += 10;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            handler.postDelayed(() ->BuddySDK.Speech.startSpeaking("¡Olé tú! Has acertado. Ganas 10 puntos. Tu puntuación actual es de " + puntuacion2 + " puntos.") ,1000);
                        } else {
                            BuddySDK.UI.setMood(FacialExpression.SAD);
                            handler.postDelayed(() -> BuddySDK.Speech.startSpeaking("¡Incorrecto! Lo que dijo " + nombre4 + " fue " + respuesta4 + ". ¡Suerte en la siguiente!. Tu puntuación actual es de " + puntuacion2 + " puntos."),1000);

                        }
                    }


                    else if (numeroJugador.equals("3")) {
                        respuestaCuestion3 = respuestaCuestion;
                        String respuestaOriginalNormalizada = respuesta1.trim().toLowerCase();
                        String respuestaJugador3Normalizada = respuestaCuestion3.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador3Normalizada.split("\\s+")));
                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();

                        if (palabrasCoinciden >= 2) {
                            puntuacion3 += 10;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Correcto! Has acertado. Ganas 10 puntos. Tu puntuación actual es de " + puntuacion3 + " puntos.");
                        } else {
                            BuddySDK.UI.setMood(FacialExpression.SAD);
                            BuddySDK.Speech.startSpeaking("¡Jo, fallate! Lo que dijo " + nombre1 + " fue " + respuesta1 + ". No te desanimes, ya ganarás más puntos. Tu puntuación actual es de " + puntuacion3 + " puntos.");
                        }
                    }

                    else if (numeroJugador.equals("4")) {
                        respuestaCuestion4 = respuestaCuestion;
                        String respuestaOriginalNormalizada = respuesta5.trim().toLowerCase();
                        String respuestaJugador4Normalizada = respuestaCuestion4.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador4Normalizada.split("\\s+")));
                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();

                        if (palabrasCoinciden >= 2) {
                            puntuacion4 += 10;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Bien! ¡Acertaste y ganas 10 puntos!. Tu puntuación actual es de " + puntuacion4 + " puntos.");
                        } else {
                            BuddySDK.UI.setMood(FacialExpression.SAD);
                            BuddySDK.Speech.startSpeaking("¡Vaya, fallaste! Lo que dijo " + nombre5 + " fue " + respuesta5 + ". Tu puntuación actual es de " + puntuacion4 + " puntos. Pero ya mejorarás más adelante");
                        }
                    }

                    else if (numeroJugador.equals("5")) {
                        respuestaCuestion5 = respuestaCuestion;
                        String respuestaOriginalNormalizada = respuesta2.trim().toLowerCase();
                        String respuestaJugador5Normalizada = respuestaCuestion5.trim().toLowerCase();
                        Set<String> palabrasOriginal = new HashSet<>(Arrays.asList(respuestaOriginalNormalizada.split("\\s+")));
                        Set<String> palabrasJugador = new HashSet<>(Arrays.asList(respuestaJugador5Normalizada.split("\\s+")));
                        palabrasOriginal.retainAll(palabrasJugador);
                        int palabrasCoinciden = palabrasOriginal.size();

                        if (palabrasCoinciden >= 2) {
                            puntuacion5 += 10;
                            BuddySDK.UI.setMood(FacialExpression.HAPPY);
                            BuddySDK.Speech.startSpeaking("¡Ole tú! Has acertado. Ganas 10 puntos. Tu puntuación actual es de " + puntuacion5 + " puntos.");
                        } else {
                            BuddySDK.UI.setMood(FacialExpression.SAD);
                            BuddySDK.Speech.startSpeaking("¡Lo siento, pero es incorrecto! Lo que dijo " + nombre2 + " fue " + respuesta2 + ". Tu puntuación actual es de " + puntuacion5 + " puntos.");
                        }
                    }





                    //PARA MOVERSE DE NUEVO AL CENTRO

                    if(numeroJugador.equals("1")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(1);
                        },10000);
                    }
                    else if(numeroJugador.equals("2")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(2);
                        },10000);
                    } else if(numeroJugador.equals("3")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(3);
                        },10000);
                    } else if(numeroJugador.equals("4")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentroFinal2();
                        },10000);
                    }else if(numeroJugador.equals("5")){
                        handler.postDelayed(() -> {
                            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                            volverAlCentro2DesdeJugador(5);
                        },10000);
                    }



                } else {
                    Log.e("Buddy", "No se reconoció ninguna respuesta");
                    BuddySDK.Speech.startSpeaking("No te he entendido, ¿me lo puedes repetir por favor?");
                }
            }

            @Override
            public void onError(String error) throws RemoteException {
                Log.e("Buddy", "No se reconoció ninguna respuesta");
                BuddySDK.Speech.startSpeaking("No te he entendido, ¿me lo puedes repetir por favor?");
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
                                        cuestionar2();
                                        break;
                                    case 2:
                                        cuestionar4();
                                        break;
                                    case 3:
                                        cuestionar5();
                                        break;
                                    case 4:
                                        moverseJugador5();
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








    private void cuestionar5() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 5 sobre el jugador 2");

        BuddySDK.USB.rotateBuddy(130.0F, 30.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 5 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.60F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 5 para cuestionarlo");
                            handler.postDelayed(() -> {
                                if (!yacuestiono5) {
                                    yacuestiono5 = true;
                                    BuddySDK.Speech.startSpeaking(nombre5 + " , ¿Dirías que " + nombre2 + " es una persona que le gustan más los libros, los deportes, o las películas?");
                                    handler.postDelayed(() -> escucharCuestion("5"), 8000); // Escucha después de 8 segs
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
        Log.i("Buddy", "Comienza a cuestionar al jugador 1 sobre el jugador 3");

        BuddySDK.USB.rotateBuddy(130.0F, 100.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
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
                                    BuddySDK.Speech.startSpeaking(nombre1 + " , Contesta, ¿Cuál es el cantante favorito de " + nombre3 + " ?");
                                    handler.postDelayed(() -> escucharCuestion("1"), 6000); // Escucha después de 6 segs
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







    private void cuestionar2() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 2 sobre el jugador 4");

        BuddySDK.USB.rotateBuddy(130.0F, 110.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
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
                                    BuddySDK.Speech.startSpeaking(nombre2 + " , ¿Cual es la estación del año favorita de " + nombre4 + " ?");
                                    handler.postDelayed(() -> escucharCuestion("2"), 5500); // Escucha después de 5,5 segs
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





    private void cuestionar4() {
        Log.i("Buddy", "Comienza a cuestionar al jugador 4 sobre el jugador 2");

        BuddySDK.USB.rotateBuddy(130.0F, 17.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 4 completado");

                    BuddySDK.USB.moveBuddy(0.7F, 0.60F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 4 para cuestionarlo");
                            handler.postDelayed(() -> {
                                if (!yacuestiono4) {
                                    yacuestiono4= true;
                                    BuddySDK.Speech.startSpeaking(nombre4 + " , ¿Qué define mejor a" + nombre5 + "? , ¿la empatía,la generosidad, o la ambición?");
                                    handler.postDelayed(() -> escucharCuestion("4"), 8500); // Escucha después de 8,5 segs
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



    private void volverAlCentroFinal2() {
        Log.i("Buddy", "Volviendo al centro por últimisima vez");

        float velocidad = 0.7F;
        float distancia = 0.72F;
        float velocidadRotacion = 130.0F;
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

                                BuddySDK.USB.rotateBuddy(velocidadRotacion, 33.0F, aceleracion, new IUsbCommadRsp.Stub(){

                                    @Override
                                    public void onSuccess(String s) throws RemoteException {
                                        Log.i("Buddy", "Se ha vuelto a poner enfrente del jugador1");
                                        handler.postDelayed(() -> {

                                            BuddySDK.Speech.startSpeaking("Sigamos con más preguntas");


                                            // ANUNCIAR LAS PUNTUACIONES
                                            handler.postDelayed(() -> {


                                                handler.postDelayed(() -> {
                                                    Log.i("Buddy", "Intentando cambiar a la tercera actividad");
                                                    Intent intent = new Intent(JuegoPantalla2.this, JuegoPantalla4.class);
                                                    intent.putExtra("nombre1", nombre1);
                                                    intent.putExtra("nombre2", nombre2);
                                                    intent.putExtra("nombre3", nombre3);
                                                    intent.putExtra("nombre4", nombre4);
                                                    intent.putExtra("nombre5", nombre5);

                                                    intent.putExtra("respuesta1", respuesta1);
                                                    intent.putExtra("respuesta2", respuesta2);
                                                    intent.putExtra("respuesta3", respuesta3);
                                                    intent.putExtra("respuesta4", respuesta4);
                                                    intent.putExtra("respuesta5", respuesta5);

                                                    intent.putExtra("puntuacion1", puntuacion1);
                                                    intent.putExtra("puntuacion2", puntuacion2);
                                                    intent.putExtra("puntuacion3", puntuacion3);
                                                    intent.putExtra("puntuacion4", puntuacion4);
                                                    intent.putExtra("puntuacion5", puntuacion5);

                                                    startActivity(intent);
                                                    finish();

                                                }, 7000); // Espera hasta que termine el habla


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

