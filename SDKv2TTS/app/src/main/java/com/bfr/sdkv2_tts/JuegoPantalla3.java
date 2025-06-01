package com.bfr.sdkv2_tts;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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


public class JuegoPantalla3 extends BuddyActivity {
    private final Handler handler = new Handler();
    private boolean juegoYaInicializado = false;

    //variables usadas en otros intent
    String nombre1, nombre2, nombre3, nombre4, nombre5;
    String respuesta1Ronda2, respuesta2Ronda2, respuesta3Ronda2, respuesta4Ronda2, respuesta5Ronda2;
    String respuesta1, respuesta2, respuesta3, respuesta4, respuesta5;
    private int puntuacion1, puntuacion2, puntuacion3, puntuacion4, puntuacion5;



    private int turnoActual = 0;       // 0..4
    private int preguntaActual = 0;

    private int jugadorAnterior;
    private String respuestaAnterior;
    private int preguntaAnterior;


    // ELEMENTOS DE LA INTERFAZ
    private LinearLayout layoutResultadoPrediccion;
    private TextView textEmoticonoResultado;
    private TextView textMensajeResultado;
    private TextView textPuntosSumados;
    private TextView textPuntuacionTotal;

    private Button botonDespedida;



    private Set<Integer> jugadoresDisponibles = new HashSet<>(Arrays.asList(0,1,2,3,4));  //para ir eliminando a los jugadores
    private String[][] preguntas = {
            {"¿qué prefieres?", "La montaña", "La playa"},
            {"¿qué te gusta más?", "El dulce", "El salado"},
            {"¿qué prefieres?", "Escuchar", "Hablar"},
            {"¿qué te gusta más?", "El frío", "El calor"},
            {"¿qué prefieres?", "La ciudad", "El campo"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_pantalla3);
        juegoYaInicializado = false;

        //UI
        layoutResultadoPrediccion = findViewById(R.id.layoutResultadoPrediccion);
        textEmoticonoResultado = findViewById(R.id.textEmoticonoResultado);
        textMensajeResultado = findViewById(R.id.textMensajeResultado);
        textPuntosSumados = findViewById(R.id.textPuntosSumados);
        textPuntuacionTotal = findViewById(R.id.textPuntuacionTotal);

        //recuperar nombres y puntuaciones
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
        respuesta1Ronda2=getIntent().getStringExtra("respuesta1");
        respuesta2Ronda2=getIntent().getStringExtra("respuesta2");
        respuesta3Ronda2=getIntent().getStringExtra("respuesta3");
        respuesta4Ronda2=getIntent().getStringExtra("respuesta4");
        respuesta5Ronda2=getIntent().getStringExtra("respuesta5");

        botonDespedida = findViewById(R.id.botonDespedida);
        // Configurar el OnClickListener para el nuevo botón
        botonDespedida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Buddy", "Botón Continuar a Ronda 4 presionado. Cambiando a la actividad juegopantalla4");
                Intent intent = new Intent(JuegoPantalla3.this, WelcomePage.class);

                intent.putExtra("nombre1", nombre1);
                intent.putExtra("nombre2", nombre2);
                intent.putExtra("nombre3", nombre3);
                intent.putExtra("nombre4", nombre4);
                intent.putExtra("nombre5", nombre5);
                intent.putExtra("respuesta1", respuesta1Ronda2);
                intent.putExtra("respuesta2", respuesta2Ronda2);
                intent.putExtra("respuesta3", respuesta3Ronda2);
                intent.putExtra("respuesta4", respuesta4Ronda2);
                intent.putExtra("respuesta5", respuesta5Ronda2);
                intent.putExtra("puntuacion1", puntuacion1);
                intent.putExtra("puntuacion2", puntuacion2);
                intent.putExtra("puntuacion3", puntuacion3);
                intent.putExtra("puntuacion4", puntuacion4);
                intent.putExtra("puntuacion5", puntuacion5);

                startActivity(intent);
                finish(); // Finalizar JuegoPantalla3 al pasar a la siguiente
            }
        });



    }

    @Override
    public void onSDKReady() {
        super.onSDKReady();

        if (!juegoYaInicializado) { // <--- COMPRUEBA LA BANDERA
            juegoYaInicializado = true; // <--- ESTABLECE LA BANDERA
            Log.i("BuddyFlow_DEBUG", "JuegoPantalla3: Inicializando juego POR PRIMERA VEZ.");

            BuddySDK.Speech.setSpeakerVoice("manuel");
            BuddySDK.Speech.setSpeakerSpeed(100);
            BuddySDK.Speech.setSpeakerVolume(220);

            handler.postDelayed(() -> {
                BuddySDK.Speech.startSpeaking("No os asustéis porque ya no podáis ver mi cara, ¡sigo aquí, chicos! . Vamos a empezar el 'Qué prefieres'");
            }, 2000);
            Log.i("Buddy","ya ha presentado el qué prefieres");

            handler.postDelayed(() -> moverAlPrimerJugador(), 12000);

        } else {
            Log.i("Buddy", "JuegoPantalla3.onSDKReady() llamado de nuevo, pero el juego ya está inicializado. Ignorando.");
        }
    }


    private void moverAlPrimerJugador() {
        Log.d("Buddy", "Moviendo a jugador 1)");
        BuddySDK.USB.moveBuddy(0.7F, 0.68F, new IUsbCommadRsp.Stub(){
            @Override public void onSuccess(String s) throws RemoteException {
                Log.i("Buddy","Llegó al primer jugador");
                handler.postDelayed(() -> mostrarPreguntaAlJugador(turnoActual), 4000);
            }
            @Override public void onFailed(String s) throws RemoteException {
                Log.e("Buddy","Error al mover a jugador1: "+s);
            }
        });
    }



    private void mostrarPreguntaAlJugador(int jugador) {
        Log.d("Buddy", "Mostrar preferencia a jugador " + jugador);
        runOnUiThread(() -> {
            // hacer aparecer la interfaz de la pregunta que prefieres (texto, nombres, botones)
            LinearLayout lp = findViewById(R.id.layoutPregunta);
            TextView tp = findViewById(R.id.textPregunta);  //textPregunta es el id en el xml
            TextView nombreJugador = findViewById(R.id.nombreJugador);
            Button b1 = findViewById(R.id.btnOpcion1);
            Button b2 = findViewById(R.id.btnOpcion2);

            // obtener el nombre del jugador para el TextView
            nombreJugador.setText(obtenerNombreJugador(jugador) + ",");

            String[] p = preguntas[preguntaActual];
            tp.setText(p[0]);
            b1.setText(p[1]); //el boton 1 coge el array preguntas en la posicion 1, recordar q este array tiene tres elementos: pregunta, opcion1 y opcion2
            b2.setText(p[2]);
            lp.setVisibility(View.VISIBLE);

            // habla para preguntarle
            handler.postDelayed(() -> BuddySDK.Speech.startSpeaking(obtenerNombreJugador(jugador) + ", ¿qué prefieres? " + p[1] + " o " + p[2]), 500);

            View.OnClickListener prefListener = v -> {
                String resp = ((Button)v).getText().toString();
                Log.i("Buddy", "Jugador " + jugador + " eligió: " + resp);
                guardarRespuestaJugador(jugador, resp);
                runOnUiThread(() -> lp.setVisibility(View.GONE));
                preguntarDestino(jugador);
            };
            b1.setOnClickListener(prefListener);
            b2.setOnClickListener(prefListener);
        });
    }





    private void preguntarDestino(int jugadorOrigen) {

        // primero verificar si queda alguien mas en el Set 'jugadoresDisponibles' aparte del jugador actual
        boolean alguienMasParaElegir = false;
        for (int jugadorIndex : jugadoresDisponibles) {
            if (jugadorIndex != jugadorOrigen) {
                alguienMasParaElegir = true;
                break; // si se encuentra al menos uno, no hace falta seguir buscando
            }
        }

        // Si no queda nadie más a quien elegir, fin del juego
        if (!alguienMasParaElegir) {
            Log.i("Buddy", "No quedan otros jugadores disponibles para seleccionar por Jugador. Finalizando juego");
            finalizarJuego(jugadorOrigen);
            return; // salir de preguntarDestino para no mostrar la pantalla vacia
        }

        handler.postDelayed(() -> BuddySDK.Speech.startSpeaking("¿Y a quién quieres que le pregunte sobre lo que has elegido?"), 500);

        // cargar la interfaz de eleccion de jugadores
        runOnUiThread(() -> {
            ocultarLayoutPregunta();
            ocultarLayoutResultado();
            LinearLayout ls = findViewById(R.id.layoutSeleccionJugador);
            ls.setVisibility(View.VISIBLE);
            Button[] botones = {
                    findViewById(R.id.btnJugador1),
                    findViewById(R.id.btnJugador2),
                    findViewById(R.id.btnJugador3),
                    findViewById(R.id.btnJugador4),
                    findViewById(R.id.btnJugador5)
            };

            // Solo habilita los botones de jugadores que esten en jugadoresDisponibles
            for (int i = 0; i < 5; i++) {
                Button btn = botones[i];
                final int jugadorDestinoIndex = i;

                // el jugador debe estar en el set Y no ser el jugador origen
                if (jugadoresDisponibles.contains(jugadorDestinoIndex) && jugadorDestinoIndex != jugadorOrigen) {
                    btn.setVisibility(View.VISIBLE);
                    btn.setText(obtenerNombreJugador(jugadorDestinoIndex));
                    btn.setOnClickListener(v -> {
                        Log.i("Buddy", "Jugador " + (jugadorOrigen + 1) + " eligió a Jugador " + (jugadorDestinoIndex + 1));
                        ls.setVisibility(View.GONE);
                        jugadoresDisponibles.remove(jugadorDestinoIndex); // eliminar al jugador elegido del set

                        // guardar info para la prediccion
                        jugadorAnterior = jugadorOrigen;
                        respuestaAnterior = obtenerRespuestaJugador(jugadorOrigen);
                        preguntaAnterior = preguntaActual;

                        moverAlCentroYRotar(jugadorOrigen, jugadorDestinoIndex);
                    });
                } else {
                    btn.setVisibility(View.GONE);
                }
            }
        });
    }







    private void moverAlCentroYRotar(int jugadorOrigenIndex, int jugadorDestinoIndex) {
        float velocidadMovimiento = 0.7F;
        float distanciaRetorno = calcularDistanciaRetorno(jugadorOrigenIndex);
        float velocidadRotacion = 130.0F;
        float anguloGiroInicial = 180.0F;
        ModuleUSB.BuddyAccelerations aceleracion = ModuleUSB.BuddyAccelerations.HIGH;

        Log.i("BuddyMove", "Girando 180 grados desde Jugador " + (jugadorOrigenIndex + 1));
        BuddySDK.USB.rotateBuddy(velocidadRotacion, anguloGiroInicial, aceleracion, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Moviendose hacia el centro");
                    BuddySDK.USB.moveBuddy(velocidadMovimiento, distanciaRetorno, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            if ("WHEEL_MOVE_FINISHED".equals(s)) {
                                Log.i("BuddyMove", "Ha llegado al centro.");
                                realizarRotacionCorreccion(jugadorOrigenIndex, jugadorDestinoIndex, velocidadRotacion, aceleracion);
                            }
                        }
                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse hacia el centro desde Jugador " + (jugadorOrigenIndex + 1) + ": " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "El giro inicial de 180 no termino correctamente. Respuesta: " + s);
                }
            }
            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar 180 grados inicialmente desde jugador " + (jugadorOrigenIndex + 1) + s);
            }
        });
    }


    private float calcularDistanciaRetorno(int jugadorOrigenIndex) {
        switch (jugadorOrigenIndex) {
            case 0: return 0.55F; // distancia desde Jugador 1 (indice 0) al centro
            case 1: return 0.55F;
            case 2: return 0.65F;
            case 3: return 0.73F;
            case 4: return 0.70F;
            default: return 0.65F;
        }
    }


    // rotacion para orientar a Buddy a 180 grados espalda a Jugador1
    private void realizarRotacionCorreccion(int jugadorOrigenIndex, int jugadorDestinoIndex, float velocidadRotacion, ModuleUSB.BuddyAccelerations aceleracion) {
        float anguloCorreccionFinal = 0.0F;
        switch (jugadorOrigenIndex) {
            case 0: anguloCorreccionFinal = 0.0F;    break;
            case 1: anguloCorreccionFinal = 77.0F;   break;
            case 2: anguloCorreccionFinal = -210.0F; break;
            case 3: anguloCorreccionFinal = -153.0F; break;
            case 4: anguloCorreccionFinal = -84.0F;  break;
            default: anguloCorreccionFinal = 0.0F; break; // No rotar si hay error
        }

        // normalizar angulo
        while (anguloCorreccionFinal <= -180) anguloCorreccionFinal += 360;
        while (anguloCorreccionFinal > 180) anguloCorreccionFinal -= 360;


        if (Math.abs(anguloCorreccionFinal) > 1.0) { // Umbral para evitar micro-rotaciones
            Log.i("Buddy", "Paso 3: Rotación final de corrección de " + anguloCorreccionFinal + " grados.");
            BuddySDK.USB.rotateBuddy(velocidadRotacion, anguloCorreccionFinal, aceleracion, new IUsbCommadRsp.Stub() {
                @Override
                public void onSuccess(String s) throws RemoteException {
                    if ("WHEEL_MOVE_FINISHED".equals(s)) {
                        Log.i("Buddy", "Rotacion final completada");
                        programarMovimientoHaciaJugador(jugadorDestinoIndex);
                    }
                }
                @Override
                public void onFailed(String s) throws RemoteException {
                    Log.e("Buddy", "Error en la rotación final de corrección: " + s);
                }
            });
        } else {
            Log.i("Buddy", "No se necesita rotacion final (origen era Jugador 1 o ángulo pequeño)");
            programarMovimientoHaciaJugador(jugadorDestinoIndex);
        }
    }


    private void programarMovimientoHaciaJugador(int jugadorDestinoIndex) {
        Log.i("Buddy", "Esperando 2 segundos antes de moverse a Jugador " + (jugadorDestinoIndex + 1));
        handler.postDelayed(() -> moverseAJugadorX(jugadorDestinoIndex), 1000);
    }


    private void moverseAJugadorX(int jugadorDestino) {
        final float velocidad = 0.7F;
        final float distancia;
        final float anguloRotacion;
        final float velocidadRotacion = 130.0F;
        final ModuleUSB.BuddyAccelerations aceleracion = ModuleUSB.BuddyAccelerations.HIGH;

        switch (jugadorDestino) {
            case 0:
                anguloRotacion = -180.0F;
                distancia = 0.55F;
                break;
            case 1:
                anguloRotacion = 103.0F;
                distancia = 0.55F;       // Distancia al J2
                break;
            case 2:
                anguloRotacion = 30.0F;
                distancia = 0.65F;
                break;
            case 3:
                anguloRotacion = -27.0F;
                distancia = 0.73F;
                break;
            case 4:
                anguloRotacion = -96.0F;
                distancia = 0.70F;
                break;
            default:
                return; // salir si el destino no es valido
        }

        Log.i("Buddy", "Girando " + anguloRotacion + " grados hacia Jugador " + (jugadorDestino+1));
        BuddySDK.USB.rotateBuddy(velocidadRotacion, anguloRotacion, aceleracion, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {
                if ("WHEEL_MOVE_FINISHED".equals(s)) {
                    Log.i("Buddy", "Rotacion hacia Jugador " + (jugadorDestino+ 1) + " completada. Moviendose...");
                    BuddySDK.USB.moveBuddy(velocidad, distancia, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String s) throws RemoteException {
                            if ("WHEEL_MOVE_FINISHED".equals(s)) {
                                Log.i("Buddy", "Llegó al Jugador " + (jugadorDestino + 1));
                                handler.postDelayed(() -> hacerPreguntaDePrediccion(jugadorDestino), 2000);
                            } else {
                                Log.w("Buddy", "Movimiento a Jugador " + (jugadorDestino+ 1) + " finalizado pero con mensaje inesperado: " + s);
                            }
                        }
                        @Override
                        public void onFailed(String s) throws RemoteException {
                            Log.e("Buddy", "Error al moverse al Jugador " + (jugadorDestino + 1) + ": " + s);
                        }
                    });
                } else {
                    Log.e("Buddy", "La rotacion hacia jugador " + (jugadorDestino + 1) + " no termino correctamente: " + s);
                }
            }
            @Override
            public void onFailed(String s) throws RemoteException {
                Log.e("Buddy", "Error al girar hacia el jugador " + (jugadorDestino+ 1) + ": " + s);
            }
        });
    }







    private void hacerPreguntaDePrediccion(int jugador) {
        Log.i("Buddy", "Preguntando a " + jugador + " qué cree que eligió " + jugadorAnterior);
        String[] p = preguntas[preguntaAnterior];
        String nombreOrigen = obtenerNombreJugador(jugadorAnterior);
        String textoPregunta = " , ¿Qué crees que respondió" +nombreOrigen +"?";

        handler.postDelayed(() -> {
            BuddySDK.Speech.startSpeaking(obtenerNombreJugador(jugador) + ", " + textoPregunta);
            mostrarOpcionesDePrediccion(jugador, p[1], p[2]);
        }, 1000);
    }



    private void mostrarOpcionesDePrediccion(int jugador, String opcion1, String opcion2) {
        runOnUiThread(() -> {
            LinearLayout layoutSeleccion = findViewById(R.id.layoutSeleccionJugador);
            if (layoutSeleccion != null) layoutSeleccion.setVisibility(View.GONE);
            if (layoutResultadoPrediccion != null) layoutResultadoPrediccion.setVisibility(View.GONE);

            LinearLayout layoutPregunta = findViewById(R.id.layoutPregunta);
            Button b1 = findViewById(R.id.btnOpcion1);
            Button b2 = findViewById(R.id.btnOpcion2);
            TextView pregunta = findViewById(R.id.textPregunta);
            TextView nombreJugador = findViewById(R.id.nombreJugador);
            nombreJugador.setText(obtenerNombreJugador(jugador) + ", adivina:");
            pregunta.setText("¿Qué crees que respondió " + obtenerNombreJugador(jugadorAnterior) + "?");
            b1.setText(opcion1);
            b2.setText(opcion2);
            layoutPregunta.setVisibility(View.VISIBLE); // Mostrar el layout de la pregunta

            View.OnClickListener listenerPrediccion = v -> {
                String eleccion = ((Button)v).getText().toString();
                layoutPregunta.setVisibility(View.GONE); // Ocultar la pregunta de predicción cuando se toca el boton
                boolean acierto = eleccion.equals(respuestaAnterior);
                Log.i("Buddy", "Jugador " + jugador + " predijo: " + eleccion + ". ¿Correcto? " + acierto);

                int puntosSumados = acierto ? 20 : 0;
                if (acierto) {
                    if (jugador == 0) puntuacion1 += puntosSumados;
                    else if (jugador == 1) puntuacion2 += puntosSumados;
                    else if (jugador == 2) puntuacion3 += puntosSumados;
                    else if (jugador == 3) puntuacion4 += puntosSumados;
                    else if (jugador == 4) puntuacion5 += puntosSumados;
                } // no es necesario el else aqui pq puntosSumados es 0 si falla

                mostrarResultadoVisual(jugador, acierto, puntosSumados);

                String mensajeBuddy;
                if (acierto) {
                    mensajeBuddy = "¡Correcto, has acertado! Sumas " + puntosSumados + " puntos.";
                } else {
                    mensajeBuddy = "¡Vaya, has fallado. No sumas puntos. ¡Pero no te preocupes que ya sumarás alguno!";
                }
                handler.postDelayed(() -> {
                    BuddySDK.Speech.startSpeaking(mensajeBuddy);
                }, 500);

                handler.postDelayed(() -> {
                    if (layoutResultadoPrediccion != null) {
                        layoutResultadoPrediccion.setVisibility(View.GONE);
                    }
                    hacerSegundaPreguntaAlJugador(jugador);
                }, 8000);
            };
            b1.setOnClickListener(listenerPrediccion);
            b2.setOnClickListener(listenerPrediccion);
        });
    }






    private void mostrarResultadoVisual(int jugador, boolean acierto, int puntosSumados) {
        runOnUiThread(() -> {
            if (textEmoticonoResultado == null || textMensajeResultado == null ||
                    textPuntosSumados == null || textPuntuacionTotal == null ||
                    layoutResultadoPrediccion == null) {
                Log.e("Buddy", "Error: Alguna vista del layout de resultado es null.");
                return;
            }

            if (acierto) {
                textEmoticonoResultado.setText(":)");
                textEmoticonoResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
                textMensajeResultado.setText("¡CORRECTO!");
                textPuntosSumados.setText("+" + puntosSumados + " Puntos");
            } else {
                textEmoticonoResultado.setText(":(");
                textEmoticonoResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
                textMensajeResultado.setText("¡FALLASTE!");
                textPuntosSumados.setText("+0 Puntos");
            }

            textPuntuacionTotal.setText("Puntuación Total: " + obtenerPuntuacionJugador(jugador));
            layoutResultadoPrediccion.setVisibility(View.VISIBLE); // hacer visible el layout de resultado
        });
    }




    private void hacerSegundaPreguntaAlJugador(int jugador) {

        preguntaActual++;

        // si ya no hay más preguntas en el array, se termina el juego
        if (preguntaActual >= preguntas.length) {
            finalizarJuego(jugador);
            return; // Salir del metodo
        }

        runOnUiThread(() -> {
            LinearLayout lp = findViewById(R.id.layoutPregunta);
            TextView tp = findViewById(R.id.textPregunta);
            TextView nombreJugador = findViewById(R.id.nombreJugador);
            Button b1 = findViewById(R.id.btnOpcion1);
            Button b2 = findViewById(R.id.btnOpcion2);
            nombreJugador.setText(obtenerNombreJugador(jugador) + ",");
            String[] p = preguntas[preguntaActual]; // Usa la preguntaActual incrementada
            tp.setText(p[0]);
            b1.setText(p[1]);
            b2.setText(p[2]);
            lp.setVisibility(View.VISIBLE);

            handler.postDelayed(() -> BuddySDK.Speech.startSpeaking("Te toca. ¿Qué prefieres? " + p[1] + " o " + p[2] + "?"), 500);

            View.OnClickListener prefListener = v -> {
                String resp = ((Button)v).getText().toString();
                Log.i("Buddy", "Jugador " + (jugador + 1) + " eligió: " + resp + " para pregunta " + preguntaActual);
                guardarRespuestaJugador(jugador, resp);
                runOnUiThread(() -> lp.setVisibility(View.GONE));
                Log.i("BuddyFlow", "Pregunta " + preguntaActual + " respondida por " + (jugador + 1) + ". Pasando a seleccionar siguiente jugador");
                preguntarDestino(jugador); // CONTINUAMOS CON EL CICLO DE SIEMPRE
            };
            b1.setOnClickListener(prefListener);
            b2.setOnClickListener(prefListener);
        });
    }





    private void finalizarJuego(int jugadorActualIndex) {
        Log.i("Buddy", "Finalizando juego.");
        ocultarLayoutPregunta();
        ocultarLayoutResultado();
        ocultarLayoutSeleccionJugador();
        // iniciar secuencia de retorno al centro y orientacion final
        retornarAlCentroFinal(jugadorActualIndex);
    }

    // METODOS PARA OCULTAR LOS LAYOUTS
    private void ocultarLayoutPregunta() {
        runOnUiThread(() -> {
            LinearLayout layout = findViewById(R.id.layoutPregunta);
            if (layout != null) layout.setVisibility(View.GONE);
        });
    }
    private void ocultarLayoutSeleccionJugador() {
        runOnUiThread(() -> {
            LinearLayout layout = findViewById(R.id.layoutSeleccionJugador);
            if (layout != null) layout.setVisibility(View.GONE);
        });
    }
    private void ocultarLayoutResultado() {
        runOnUiThread(() -> {
            if (layoutResultadoPrediccion != null) layoutResultadoPrediccion.setVisibility(View.GONE);
        });
    }






    private void retornarAlCentroFinal(int jugadorActualIndex) {
        Log.i("BuddyMove", "Iniciando retorno final al centro");
        final float velocidadMovimiento = 0.7F;
        final float distanciaRetorno = calcularDistanciaRetorno(jugadorActualIndex);
        final float velocidadRotacion = 130.0F;
        final float anguloGiroInicial = 180.0F;
        final ModuleUSB.BuddyAccelerations aceleracion = ModuleUSB.BuddyAccelerations.HIGH;

        // inicio del giro final de 180grados
        Log.i("BuddyCtrlFlow", "Iniciando giro de 180 grados.");
        BuddySDK.USB.rotateBuddy(velocidadRotacion, anguloGiroInicial, aceleracion, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String sRotateInitial) throws RemoteException {
                Log.i("Buddy", "retornarAlCentroFinal onSuccess: ");

                if ("WHEEL_MOVE_FINISHED".equals(sRotateInitial)) {

                    Log.i("Buddy", "retornarAlCentroFinal: Paso 2 - Iniciando movimiento al centro.");
                    BuddySDK.USB.moveBuddy(velocidadMovimiento, distanciaRetorno, new IUsbCommadRsp.Stub() {
                        @Override
                        public void onSuccess(String sMove) throws RemoteException {

                            if ("WHEEL_MOVE_FINISHED".equals(sMove)) {
                                Log.i("Buddy", "procediendo a rotacion final");
                                realizarRotacionFinalHaciaJugador1(jugadorActualIndex, velocidadRotacion, aceleracion);
                            } else if ("OK".equals(sMove)) {
                                Log.i("Buddy", "NO se procede aun");
                            } else {
                                Log.w("Buddy", " respuesta INESPERADA");

                            }
                        }

                        @Override
                        public void onFailed(String sMoveFail) throws RemoteException {
                            Log.e("Buddy", "error al moverse al centro");
                        }
                    });
                } else if ("OK".equals(sRotateInitial)) {
                    Log.i("Buddy", "giro incial OK");
                } else {
                    Log.w("Buddy", "respuesta INESPERADA: ");
                }
            }

            @Override
            public void onFailed(String sRotateInitialFail) throws RemoteException {
                Log.e("Buddy", "error en giro inicial");
            }
        });
    }



    private void realizarRotacionFinalHaciaJugador1(int jugadorOrigenIndex, float velocidadRotacion, ModuleUSB.BuddyAccelerations aceleracion) {
        float anguloRotacionFinal = 0.0F;

        switch (jugadorOrigenIndex) {
            case 0: anguloRotacionFinal = -0.0F - 180.0F;    break;
            case 1: anguloRotacionFinal = -(-77.0F) - 180.0F; break;
            case 2: anguloRotacionFinal = -(-150.0F) - 180.0F;break;
            case 3: anguloRotacionFinal = -(153.0F) - 180.0F; break;
            case 4: anguloRotacionFinal = -(84.0F) - 180.0F;  break;
            default:
                anguloRotacionFinal = -180.0F;
                break;
        }
        while (anguloRotacionFinal <= -180) anguloRotacionFinal += 360;
        while (anguloRotacionFinal > 180) anguloRotacionFinal -= 360;


        if (Math.abs(anguloRotacionFinal) > 1.0) {
            Log.i("Buddy", "intentando rotar " + anguloRotacionFinal + " grados.");
            BuddySDK.USB.rotateBuddy(velocidadRotacion, anguloRotacionFinal, aceleracion, new IUsbCommadRsp.Stub() {
                @Override
                public void onSuccess(String s) throws RemoteException {

                    if ("WHEEL_MOVE_FINISHED".equals(s)) {
                        Log.i("Buddy", "llamando a ejecutarAccionesFinales.");
                        ejecutarAccionesFinales(); // <--- SOLO SE LLAMA AQUÍ
                    } else if ("OK".equals(s)) {
                        Log.i("Buddy", "ES OK. NO se llama a ejecutarAccionesFinales todavía.");
                    } else {
                        Log.w("Buddy", "Respuesta INESPERADA: NO se llama a ejecutarAccionesFinales.");
                    }
                }

                @Override
                public void onFailed(String s) throws RemoteException {
                    Log.e("Buddy", "Retorno Final - Error en rotación final: " + s);
                }
            });
        } else {
            Log.i("Buddy", "angulo de rotacion final muy peq. Llamando a ejecutarAccionesFinales directamente.");
            ejecutarAccionesFinales();
        }
    }



    private void ejecutarAccionesFinales() {
        Log.i("BuddyFlow", "Ejecutando acciones finales del juego (preparando para botón).");
        String mensajeFinal = "¡Hemos terminado el que prefieres!";

        // anunciar fin de la ronda
        handler.postDelayed(() -> {
            BuddySDK.Speech.startSpeaking(mensajeFinal);
        }, 500);

        // Anunciar clasificacion
        handler.postDelayed(() -> {
            String[] nombres = {nombre1, nombre2, nombre3, nombre4, nombre5};
            int[] puntuaciones = {puntuacion1, puntuacion2, puntuacion3, puntuacion4, puntuacion5};

            StringBuilder mensajeClasificacion = new StringBuilder("Bueno, ha llegado el momento, voy a anunciar la clasificación final del juego. ");

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

            for (int i = 0; i < nombres.length; i++) {
                mensajeClasificacion.append("En el puesto ").append(i + 1).append(" está ").append(nombres[i]).append(" con ").append(puntuaciones[i]).append(" puntos. ");
            }

            BuddySDK.Speech.startSpeaking(mensajeClasificacion.toString());


            // tras anunciar la clasificación, mostrar el boton para continuar

            handler.postDelayed(() -> {
                runOnUiThread(() -> {
                    Log.i("BuddyFlow", "Mostrando botón para continuar a Ronda 4.");

                    layoutResultadoPrediccion.setVisibility(View.GONE);

                    ocultarLayoutPregunta();
                    ocultarLayoutSeleccionJugador();
                    ocultarLayoutResultado();

                    botonDespedida.setVisibility(View.VISIBLE);
                });
            }, 25000);




        }, 3000); // Delay para que Buddy empiece a anunciar la clasificación (despues del hemos terminado...)
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }














//METODOS AUXILIARES

    private int obtenerPuntuacionJugador(int jugador) {
        switch (jugador) {
            case 0: return puntuacion1;
            case 1: return puntuacion2;
            case 2: return puntuacion3;
            case 3: return puntuacion4;
            case 4: return puntuacion5;
        }
        return 0;
    }
    private String obtenerNombreJugador(int i) {
        switch(i){
            case 0: return nombre1;
            case 1: return nombre2;
            case 2: return nombre3;
            case 3: return nombre4;
            case 4: return nombre5;
        }
        return "Jugador";
    }
    private String obtenerRespuestaJugador(int jugador) {
        switch(jugador) {
            case 0: return respuesta1;
            case 1: return respuesta2;
            case 2: return respuesta3;
            case 3: return respuesta4;
            case 4: return respuesta5;
            default: return "";
        }
    }
    private void guardarRespuestaJugador(int jugador, String respuesta) {
        switch(jugador) {
            case 0: respuesta1 = respuesta; break;
            case 1: respuesta2 = respuesta; break;
            case 2: respuesta3 = respuesta; break;
            case 3: respuesta4 = respuesta; break;
            case 4: respuesta5 = respuesta; break;
        }
    }



}