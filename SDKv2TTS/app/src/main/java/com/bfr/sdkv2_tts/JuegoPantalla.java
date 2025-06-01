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

import java.util.Locale;

public class JuegoPantalla extends BuddyActivity {

    private final Handler handler = new Handler();  // Handler para gestionar los retrasos
    private STTTask ssttTask; // Tarea de reconocimiento de voz
    private boolean yapregunto=false;
    private boolean yapregunto2=false;
    private boolean yapregunto3=false;
    private boolean yapregunto4=false;
    private boolean yapregunto5=false;


    private String nombreJugador1;
    private String nombreJugador2;
    private String nombreJugador3;
    private String nombreJugador4;
    private String nombreJugador5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_pantalla);
    }

    @Override
    public void onSDKReady() {
        super.onSDKReady();
        BuddySDK.UI.setMood(FacialExpression.HAPPY);
        BuddySDK.UI.setViewAsFace(findViewById(R.id.view_face3));
        BuddySDK.Speech.setSpeakerVoice("manuel");
        BuddySDK.Speech.setSpeakerSpeed(110);
        BuddySDK.Speech.setSpeakerVolume(220);

        handler.postDelayed(() -> {
            BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
            BuddySDK.Speech.startSpeaking("¡Hola chicos!, ¿Cómo estáis? Me llamo Buddy y soy un robot social que dirigirá el juego que vamos a hacer. ¿Estáis listos?");
            Log.i("Buddy", "Buddy ya se ha presentado");

            handler.postDelayed(this::moverseJugador1, 12000); // Esperar 12 segundos y moverse hacia el jugador 1
        }, 3000); // Buddy empieza a presentarse despues de 3 segundos
    }










    private void moverseJugador1() {
        Log.i("Buddy", "moviendose hacia el jugador 1");
        BuddySDK.USB.moveBuddy(0.7F, 0.50F, new IUsbCommadRsp.Stub() {

            @Override
            public void onSuccess(String s) throws RemoteException {
                Log.i("Buddy", "llego al jugador 1");

                handler.postDelayed(() -> {
                    if(!yapregunto) {
                        yapregunto = true;
                        BuddySDK.Speech.startSpeaking("¡Hola! ¿Cómo te llamas?");
                        handler.postDelayed(() -> escucharNombre("1"), 3000);// espera 3 segundos antes de escuchar el nombre
                    }
                }, 3000); // espera 3 segundos después de llegar para preguntarle como se llama
            }
            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al moverse hacia el jugador 1: " + s);
            }
        });
    }









    private void escucharNombre(String numeroJugador) {
        Log.i("Buddy", "iniciando el reconocimiento de voz");

        ssttTask = BuddySDK.Speech.createGoogleSTTTask(new Locale("es", "ES"));
        Log.i("Buddy", "escuchando el nombre del jugador " + numeroJugador);

        ssttTask.start(false, new ISTTCallback.Stub() {

            @Override
            public void onSuccess(STTResultsData iResults) throws RemoteException {
                Log.i("Buddy", "Reconocimiento completado");
                if (iResults != null && !iResults.getResults().isEmpty()) {
                    STTResult resultado = iResults.getResults().get(0);
                    String nombreJugador = resultado.getUtterance(); // Nombre reconocido
                    Log.i("Buddy", "Nombre reconocido: " + nombreJugador);

                    ssttTask.pause();

                    if (numeroJugador.equals("1")) {
                        nombreJugador1=nombreJugador;
                        BuddySDK.UI.setMood(FacialExpression.SURPRISED);
                        handler.postDelayed(() -> BuddySDK.Speech.startSpeaking(nombreJugador1 + ", ¡qué nombre tan bonito! Estoy encantado de conocerte. Eres el jugador número " + numeroJugador), 1000);

                    } else if (numeroJugador.equals("2")) {
                        nombreJugador2=nombreJugador;
                        BuddySDK.UI.setMood(FacialExpression.LOVE);
                        handler.postDelayed(() -> BuddySDK.Speech.startSpeaking(nombreJugador2 + ", ¡me encanta! Es un placer conocerte. Tu vas a ser el jugador número " + numeroJugador),1000);

                    } else if (numeroJugador.equals("3")) {
                        nombreJugador3=nombreJugador;
                        BuddySDK.UI.setMood(FacialExpression.HAPPY);
                        handler.postDelayed(() -> BuddySDK.Speech.startSpeaking(nombreJugador3 + ", ¡vaya un nombre chulo! Pues tu número de jugador es el " + numeroJugador),1000);

                    }
                    else if (numeroJugador.equals("4")) {
                        nombreJugador4=nombreJugador;
                        BuddySDK.UI.setMood(FacialExpression.THINKING);
                        handler.postDelayed(() -> BuddySDK.Speech.startSpeaking(nombreJugador4 + ", ¡genial! Es un placer. Tu vas a ser el jugador" + numeroJugador),1000);

                    }else if (numeroJugador.equals("5")) {
                        nombreJugador5=nombreJugador;
                        BuddySDK.UI.setMood(FacialExpression.LOVE);
                        handler.postDelayed(() -> BuddySDK.Speech.startSpeaking(nombreJugador5 + ", ¡vale! Me gusta mucho tu nombre también. Tu número de jugador es el" + numeroJugador),1000);
                    }


                    handler.postDelayed(() -> {
                        if(numeroJugador.equals("1")){
                            handler.postDelayed(() -> {
                                BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                                volverAlCentro();
                            }, 9000); // para volver al centro, y luego iniciar el moverseJugador2
                        } else if(numeroJugador.equals("2")){
                            handler.postDelayed(() -> {
                                BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                                volverAlCentroDesdeJugador(2);
                            }, 9000);
                        } else if(numeroJugador.equals("3")){
                            handler.postDelayed(() -> {
                                BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                                volverAlCentroDesdeJugador(3);
                            }, 9000);
                        } else if(numeroJugador.equals("4")){
                            handler.postDelayed(() -> {
                                BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                                volverAlCentroDesdeJugador(4);
                            }, 9000);
                        } else if(numeroJugador.equals("5")){
                            handler.postDelayed(() -> {{
                                BuddySDK.UI.setMood(FacialExpression.NEUTRAL);
                                volverAlCentroFinal();
                            }}, 9000);
                        }
                    }, 1000);

                } else {
                    Log.e("Buddy", "No se reconoció ningún nombre");
                    BuddySDK.Speech.startSpeaking("No he podido entender tu nombre, ¿me lo puedes repetir por favor?");
                }
            }

            @Override
            public void onError(String error) throws RemoteException {
                Log.e("Buddy", "Error al reconocer el nombre: " + error);
                BuddySDK.Speech.startSpeaking("No he podido entender tu nombre, ¿me lo puedes repetir por favor?");
            }
        });
    }



    //primera vez que vuelve al centro
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
                                    BuddySDK.Speech.startSpeaking("Y tú, ¿Cómo te llamas?");
                                    handler.postDelayed(() -> escucharNombre("2"), 3000);
                                }

                            }, 2000);
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











    // para volver al centro despues del jugador 2, metodo usado para el resto de jugadores
    private void volverAlCentroDesdeJugador(int numJugador) {
        Log.i("Buddy", "Volviendo al centro desde el jugadorX...");

        float velocidad = 0.7F;
        float distancia = 0.70F;
        float velocidadRotacion = 130.0F;
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

        BuddySDK.USB.rotateBuddy(130.0F, 100.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
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
                                    BuddySDK.Speech.startSpeaking("Y tú, ¿quién eres? ");
                                    handler.postDelayed(() -> escucharNombre("3"), 3000);
                                }
                            }, 2000);
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

        BuddySDK.USB.rotateBuddy(130.0F, 105.0F, ModuleUSB.BuddyAccelerations.HIGH, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Giro hacia el jugador 4 completado, moviéndose...");

                    BuddySDK.USB.moveBuddy(0.7F, 0.62F, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            Log.i("Buddy", "Llegó al jugador 4");
                            handler.postDelayed(() -> {
                                if (!yapregunto4) {
                                    yapregunto4 = true;
                                    BuddySDK.Speech.startSpeaking("¡Hola! ¿Cómo te llamas tu?");
                                    handler.postDelayed(() -> escucharNombre("4"), 3000); // Escucha el nombre después de 3 segundos
                                }
                            }, 2000);
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
                                    BuddySDK.Speech.startSpeaking("Los últimos serán los primeros. ¿Cuál es tu nombre?");
                                    handler.postDelayed(() -> escucharNombre("5"), 4500); // Escucha el nombre después de 4,5 segundos
                                }
                            }, 2000); //espera 2 segs antes de preguntar su nombre
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
                Log.e("Buddy", "Error al girar hacia el jugador 51: " + s);
            }
        });


    }




    private void volverAlCentroFinal() {
        Log.i("Buddy", "Volviendo al centro por última vez");

        float velocidad = 0.7F;
        float distancia = 0.78F;
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

                                BuddySDK.USB.rotateBuddy(velocidadRotacion, 100.0F, aceleracion, new IUsbCommadRsp.Stub(){

                                    @Override
                                    public void onSuccess(String s) throws RemoteException {
                                        Log.i("Buddy", "Se ha vuelto a poner enfrente del jugador1");
                                        handler.postDelayed(() -> {
                                            String fraseRecordatorio = String.format(
                                                    "Vale chicos, enseguida empezaremos con las preguntas. Os recuerdo que el jugador 1 es %s ,   el jugador 2 es %s ,   el jugador 3 es %s ,  el jugador 4 es %s ,  y el jugador 5 es %s",
                                                    nombreJugador1, nombreJugador2, nombreJugador3, nombreJugador4, nombreJugador5
                                            );

                                            BuddySDK.Speech.startSpeaking(fraseRecordatorio);

                                            // despues de q Buddy termine de hablar, pasa a la segunda actividad
                                            handler.postDelayed(() -> {
                                                Log.i("Buddy", "Intentando cambiar a la segunda actividad");
                                                Intent intent = new Intent(JuegoPantalla.this, JuegoPantalla2.class);
                                                intent.putExtra("nombreJugador1", nombreJugador1);
                                                intent.putExtra("nombreJugador2", nombreJugador2);
                                                intent.putExtra("nombreJugador3", nombreJugador3);
                                                intent.putExtra("nombreJugador4", nombreJugador4);
                                                intent.putExtra("nombreJugador5", nombreJugador5);


                                                startActivity(intent);

                                                finish();

                                            }, 18000); // Espera hasta que termine el habla

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