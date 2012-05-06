package org.mumumu.ti.android.speech;

import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

//
// This proxy can be created by calling Voicerecognition.createVoiceRecognition()
//
@Kroll.proxy(creatableInModule=VoicerecognitionModule.class)
public class VoiceRecognitionProxy extends KrollProxy
{
	
	private static final String TAG = "VoiceRecognitionProxy";
	// callback function for getting Voice Recognition result
	private KrollFunction callback = null;

	@Kroll.setProperty @Kroll.method
	public void setCallback(KrollFunction func) {
		this.callback = func;
	}
	
	@Kroll.method
	public void voiceRecognition() {	
		Log.d(TAG, "Voice Recognition entry point");
		Activity currentActivity = getActivity();
		Intent intent = new Intent(
				            currentActivity.getApplicationContext(),
				            VoiceRecognitionActivity.class
				        );
		intent.setAction(Intent.ACTION_VIEW);
		
		Handler handler = new Handler() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void handleMessage(Message msg) {
				Log.d(TAG, "Voice Recognition result handled.");
				if (callback != null) {
					Bundle bundle = msg.getData();
					ArrayList<String> matches = bundle.getStringArrayList("VOICE_RESULT");
		            for (int i = 0; i < matches.size(); i++) {
		            	Log.d(TAG, " Recognize result -> " + matches.get(i));
		            }
		            HashMap resultmap = new HashMap();
		            String[] result_string_array = matches.toArray(new String[0]);
		            resultmap.put("voice_results", result_string_array);
		            resultmap.put("voice_enabled", bundle.getBoolean("VOICE_RECOGNITION_ENABLED"));
				    resultmap.put("voice_canceled", bundle.getBoolean("VOICE_RECOGNITION_CANCELED"));
		            callback.callAsync(getKrollObject(), resultmap);	
				}
			}
		};
		Messenger msger = new Messenger(handler);
		intent.putExtra("VOICE_RESULT_MESSENGER", msger);
		currentActivity.startActivity(intent);
	}
}