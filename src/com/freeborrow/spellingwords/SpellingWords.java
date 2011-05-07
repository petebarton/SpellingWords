package com.freeborrow.spellingwords;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
//Toast.makeText(SpellingWords.this, edittext.getText(), Toast.LENGTH_SHORT).show();

public class SpellingWords extends Activity implements OnInitListener {
	
	String txtList = "green happy family beautiful thought";
	String[] txtaList;
	int indexWeOn;
	ImageButton btnSayAgain;
	private TextToSpeech mTts;
	EditText edittext;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.i("i", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	btnSayAgain = (ImageButton) findViewById(R.id.btnSayAgain);
    	mTts = new TextToSpeech(this, this);
        edittext = (EditText) findViewById(R.id.editText1);
        
        txtaList = txtList.split(" ");
        
    	 btnSayAgain.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				sayCurrentWord();
			}
		});
    	
        edittext.setOnKeyListener(new OnKeyListener() {
        	public boolean onKey(View v, int keyCode, KeyEvent event){
        		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
        			isWordSpelledCorrectly(edittext.getText().toString());
        			return true;
        		}
        		return false;
        	}

        });
        
    }//onCreate
    
    
    private void incrementWord(){
    	Log.i("place", "start increment word");
    	if (txtaList.length == (indexWeOn + 1)) {
    		Log.i("place", "before call gameover()");
    		gameOver(); 
    		Log.i("place", "after call gameover()");
    	}else{
    		indexWeOn++;
    		sayCurrentWord();
    		edittext.setText("");
    	}
    	Log.i("place", "end increment word");
    }
    
    private void gameOver() {
    	Log.i("place", "start gameover");
		edittext.setEnabled(false);
		Log.i("place", "end gameover");
		}

	private void isWordSpelledCorrectly(String spelledWord) {
		Log.i("place", "start is word spelled correctly?");
    	if (spelledWord.toLowerCase().equals(txtaList[indexWeOn].toLowerCase())) {
    		incrementWord();
    	} else {
    		mTts.speak("That is not correct!", TextToSpeech.QUEUE_FLUSH, null);
    	}
    	Log.i("place", "end is word spelled correctly?");
    }
    
    public void sayCurrentWord(){
    	mTts.speak(txtaList[indexWeOn], TextToSpeech.QUEUE_FLUSH, null);
    }

    // Implements TextToSpeech.OnInitListener.
    @Override
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
                       int result = mTts.setLanguage(Locale.US);
           
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e("tts", "Language is not available.");
            } else {
                // Lets get this party rolling
            	sayCurrentWord();
            }
        } else {
            // Initialization failed.
            Log.e("tts", "Could not initialize TextToSpeech.");
        }
    }
    
    
    @Override
    public void onDestroy() {
    	// Don't forget to shutdown!
    	if (mTts != null) {
    		mTts.stop();
    		mTts.shutdown();
    	}
    	super.onDestroy();
    }
}