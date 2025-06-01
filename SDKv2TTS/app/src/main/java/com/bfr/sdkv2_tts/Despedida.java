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
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.bfr.buddy.usb.shared.IUsbCommadRsp;
import com.bfr.buddysdk.services.ModuleUSB;
import com.bfr.buddysdk.services.speech.STTTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Despedida extends BuddyActivity {

    private STTTask ssttTask5; // Tarea de reconocimiento de voz
    private final Handler handler = new Handler();  // Handler para gestionar los retrasos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despedida);

    }

    @Override
    public void onSDKReady() {
        super.onSDKReady();
        BuddySDK.UI.setMood(FacialExpression.SAD);
        BuddySDK.UI.setViewAsFace(findViewById(R.id.view_face));
        BuddySDK.Speech.setSpeakerVoice("manuel");
        BuddySDK.Speech.setSpeakerSpeed(100);
        BuddySDK.Speech.setSpeakerVolume(220);

        //DESPEDIDA
        handler.postDelayed(() -> {
            BuddySDK.Speech.startSpeaking("Chicos estoy triste porque esto se ha acabado.");
            Log.i("Buddy", "Buddy ya se ha despedido");

            handler.postDelayed(() -> {
                BuddySDK.UI.setMood(FacialExpression.HAPPY);
                BuddySDK.Speech.startSpeaking("¡Pero lo he pasado muy bien con vosotros! ¡Muchas gracias por jugar conmigo. Ha sido un gran placer, y os deseo lo mejor!");
            }, 5000); // espera 5 segundos tras el mensaje triste

        }, 3000); // Buddy cambia la cara y presenta la 2da ronda despues de 3 segundos

    }
}