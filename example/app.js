// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});
var label = Ti.UI.createLabel();
win.add(label);
win.open();

// voice recognition proxy module test
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