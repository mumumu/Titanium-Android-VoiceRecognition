# Titanium Module for Voice Recognition on Android.

## Description

Titanium Module for Voice Recognition on Android.

In nature, we can write Voice Recognition routine by the following blog entry, but
the code in this entry did not work in Android 2.x and Titanium 2.0.1GA environment.
This is because "Ti.Android.currentActivity.startActivityForResult" function does not
call callback function correctly.

http://www.matthuggins.com/articles/android-voice-recognition-in-appcelerator-titanium

Using this module, we can get Voice Recognition result correctly on Android 2.x, 3.x  
Note: I don't test this module on Android 4.x. If you can test it, please let me know the result.
      
## Requirement

Android min-sdk: Android 2.1 (API Level 7)  
Titanium 2.0.1GA

## Accessing the voicerecognition Module

To access this module from JavaScript, you would do the following:

    var voicerecognition = require("org.mumumu.ti.android.speech");

The voicerecognition variable is a reference to the Module object.	

## Usage

A) Download org.mumumu.ti.android.speech-android-0.2.zip and place it to your Titanium project root.

B) add the following setting to tiapp.xml between &lt;ti:app&gt; tag.

    <android xmlns:android="http://schemas.android.com/apk/res/android">
        <manifest>
            <application>
                <activity android:name="org.mumumu.ti.android.speech.VoiceRecognitionActivity" />
            </application>
        </manifest>
    </android>
    <modules>
        <module version="0.2">org.mumumu.ti.android.speech</module>
    </modules>

C) Invoke the module code.

    if (Ti.Platform.name == "android") {
        var speechModule = require('org.mumumu.ti.android.speech');
        var voiceRecognitionProxy = speechModule.createVoiceRecognition();
        var callback_func = function (e) {
            var voice_recognition_enabled = e.voice_enabled;
            var voice_results = e.voice_results;
            if (e.voice_canceled) {
                alert("voice recognition canceled");
            } else {
                if (!voice_recognition_enabled) {
                    alert("voice recognition seems to be disabled");
                } else {
                    alert(voice_results[0]); //  array.
                }
            }
        };
        voiceRecognitionProxy.voiceRecognition({
            "android.speech.extra.PROMPT": "please say something",
            "android.speech.extra.LANGUAGE_MODEL": "free_form",
            "callback": callback_func
        });
    }
    
## Author

Yoshinari Takaoka (reversethis -> gro tod umumum ta umumum)

## License

BSD License. See License.txt for details.
