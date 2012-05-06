package org.mumumu.ti.android.speech;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import org.appcelerator.kroll.common.Log;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognizerIntent;

public class VoiceRecognitionActivity extends Activity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	private boolean isVoiceRecognitionEnabled = false;
	
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        isVoiceRecognitionEnabled = (activities.size() != 0);
        if (isVoiceRecognitionEnabled) {
        	startVoiceRecognitionActivity();
        } else {
        	sendResult(null, isVoiceRecognitionEnabled, false);
        }
    }

    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Handle the results from the recognition activity.
     */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
        	if (resultCode == RESULT_OK) {
	            // Fill the list view with the strings the recognizer thought it could have heard
	            ArrayList<String> matches = data.getStringArrayListExtra(
	                    RecognizerIntent.EXTRA_RESULTS);
	            Log.d("VoiceRecognitionActivity", "recognition result received");
	            for (int i = 0; i < matches.size(); i++) {
	            	Log.d("VoiceRecognitionActivity", matches.get(i));
	            }
	            sendResult(matches, true, false);
        	}
        	if (resultCode == RESULT_CANCELED) {
	            sendResult(null, true, true);        		
        	}
        }
    }
	
	/**
	 * send result to the source activity
	 */
	private void sendResult(ArrayList<String> matches, boolean enabled, boolean canceled) {
		if (matches == null) {
			matches = new ArrayList<String>();
		}
        Intent fromIntent = getIntent();
        Messenger msger = fromIntent.getParcelableExtra("VOICE_RESULT_MESSENGER");
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putBoolean("VOICE_RECOGNITION_CANCELED", canceled);
        bundle.putBoolean("VOICE_RECOGNITION_ENABLED", enabled);
        bundle.putStringArrayList("VOICE_RESULT", matches);
        msg.setData(bundle);
        try {
			msger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        finish();		
	}
}
