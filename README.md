# Titanium Module for Voice Recognition on Android.

## Description

Titanium Module for Voice Recognition on Android.

In nature, we can write Voice Recognition routine by the following blog entry, but
the code in this entry did not work in Android 2.x and Titanium 2.0.1GA environment.
This is because "Ti.Android.currentActivity.startActivityForResult" function does not
call callback function correctly.

http://www.matthuggins.com/articles/android-voice-recognition-in-appcelerator-titanium

Using this module, we can get Voice Recognition result correctly on Android 2.x, 3.x, 4.0.3, 4.2  
      
## Requirement

Android min-sdk: Android 2.2 (API Level 8)  
Titanium 2.1.2GA

## Accessing the voicerecognition Module

To access this module from JavaScript, you would do the following:

    var voicerecognition = require("org.mumumu.ti.android.speech");

The voicerecognition variable is a reference to the Module object.	

## Usage

O) download repository archive from this site.

A) place dist/org.mumumu.ti.android.speech-android-0.3.zip to your Titanium project root.

B) add the following setting to tiapp.xml between &lt;ti:app&gt; tag.

    <modules>
        <module version="0.3">org.mumumu.ti.android.speech</module>
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