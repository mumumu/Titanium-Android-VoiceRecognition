package org.mumumu.ti.android.speech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiActivityResultHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;

//
// This proxy can be created by calling Voicerecognition.createVoiceRecognition()
//
@Kroll.proxy(creatableInModule=VoicerecognitionModule.class)
public class VoiceRecognitionProxy extends KrollProxy implements TiActivityResultHandler

{
    
    private final String TAG = "VoiceRecognitionProxy";
    // callback function for getting Voice Recognition result
    private KrollFunction callback = null;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
 
    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity(KrollDict options) {
        //
        //  You can override callback functions.
        //
        if (callback == null && options != null
         && options.containsKey("callback")
         && (options.get("callback") instanceof KrollFunction)) {
        	Log.d(TAG, "overriding callback value");
        	callback = (KrollFunction)options.get("callback");
        }
    	
    	//
    	//  overriding option value
    	//
       	Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if (options != null) {
        	if (options.containsKey(RecognizerIntent.EXTRA_LANGUAGE_MODEL)) {
        		String extraVal = (String)options.get(RecognizerIntent.EXTRA_LANGUAGE_MODEL);
        		Log.d(TAG, "overriding RecognizerIntent.EXTRA_LANGUAGE_MODEL value -> " + extraVal);
            	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, extraVal);
            }
            if (options.containsKey(RecognizerIntent.EXTRA_PROMPT)) {
            	String extraVal = (String)options.get(RecognizerIntent.EXTRA_PROMPT);
            	Log.d(TAG, "overriding RecognizerIntent.EXTRA_PROMPT value");
               	intent.putExtra(RecognizerIntent.EXTRA_PROMPT, extraVal);
            }
        }
        TiApplication.getInstance().getRootActivity().launchActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE, this);
    }
    
    private void sendResult(ArrayList<String> matches, boolean enabled, boolean canceled) {
    	if (callback == null) {
    		return;
    	}
        if (matches == null) {
            matches = new ArrayList<String>();
        }
        HashMap<String,Object> resultmap = new HashMap<String,Object>();
        String[] result_string_array = matches.toArray(new String[0]);
        resultmap.put(TiC.EVENT_PROPERTY_SOURCE, VoiceRecognitionProxy.this);
        resultmap.put("voice_results", result_string_array);
        resultmap.put("voice_enabled", enabled);
        resultmap.put("voice_canceled", canceled);
        callback.callAsync(getKrollObject(), resultmap);    
    }
 
    @Kroll.setProperty @Kroll.method
    public void setCallback(KrollFunction func) {
    	this.callback = func;
    }
    
    @Kroll.method
    public void voiceRecognition(KrollDict options) {      
    	Log.d(TAG, "Voice Recognition entry point");
        // Check to see if a recognition activity is present
        PackageManager pm = TiApplication.getInstance().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        boolean isVoiceRecognitionEnabled = (activities.size() != 0);
        if (isVoiceRecognitionEnabled) {
            startVoiceRecognitionActivity(options);
        } else { 
            sendResult(null, isVoiceRecognitionEnabled, false);
        }
    }

    @Override
	public void onError(Activity activity, int requestCode, Exception e) {
		if (VOICE_RECOGNITION_REQUEST_CODE == requestCode) {
	    	if (callback == null) {
	    		return;
	    	}
	        HashMap<String,Object> resultmap = new HashMap<String,Object>();
	        resultmap.put("voice_error_message", e.getLocalizedMessage());
	        resultmap.put(TiC.EVENT_PROPERTY_SOURCE, VoiceRecognitionProxy.this);
	        callback.callAsync(getKrollObject(), resultmap);    
		}
	}

 	@Override
	public void onResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Fill the list view with the strings the recognizer thought it could have heard
                ArrayList<String> matches = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                Log.d("VoiceRecognitionActivity", "recognition result received");
                for (int i = 0; i < matches.size(); i++) {
                    Log.d("VoiceRecognitionActivity", matches.get(i));
                }
                sendResult(matches, true, false);
            }else if (resultCode == Activity.RESULT_CANCELED) {
                sendResult(null, true, true);                
            }
        }
	}
}