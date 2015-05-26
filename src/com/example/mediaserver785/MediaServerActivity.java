package com.example.mediaserver785;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MediaServerActivity extends Activity {

	//HTTPServerImpl server = new HTTPServerImpl("adresse", 8080);
	MediaPlayer mediaplayer;
	String currentFilePath, mediaLibraryPath;
	int currentFileID;
	String[] playlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_server);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_server, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void init(){
    	String rootPath = Environment.getExternalStorageDirectory().getPath();
        this.mediaLibraryPath = rootPath + "/Download/wutang_ff";
        
        Log.d("Library location", mediaLibraryPath);
        this.currentFileID = 0;
        
        File dir = new File(mediaLibraryPath);//all songs in playlist for now
        this.playlist = dir.list();
        Arrays.sort(this.playlist);
        
        //***temp***
        int i = 0;
        for (String path : this.playlist){
        	
        	Log.d("Playlist["+i+"]", path);
        	i++;
        }
        //***********
        
        updateCurrentFilePath();
		this.mediaplayer = new MediaPlayer();
    }
    
    public void updateCurrentFilePath() {
        this.currentFilePath = "file://" + this.mediaLibraryPath + "/" + this.playlist[this.currentFileID] ;
        
		
	}

    
    public void playPause(View v){
    	
    	try {
    		
    		if( this.mediaplayer.isPlaying()){
    	    	this.mediaplayer.pause();	
    	    	Log.d("MediaOP", "pause");
    	    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_play);
    		}
    		else{
    			playSong();
    		}
    		

        } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    }
    
    public void prev(View v){
    	Log.d("prevID", ""+this.currentFileID);
    	if(this.currentFileID > 0){ 	
    		setCurrentID(--this.currentFileID);
    		updateCurrentFilePath();
    		playSong();
    	}
    }
    public void next(View v){
    	Log.d("nextID", ""+this.currentFileID);
    	if(this.currentFileID < this.playlist.length - 1){
	    	setCurrentID(++this.currentFileID);
	    	updateCurrentFilePath();
	    	playSong();
    	}
    }
    
    public void stopSong(View v){
    	this.mediaplayer.stop();
    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_play);

    }
    
    public void setCurrentID(int newID){
    	this.currentFileID = newID;
    	}
    
    public void playSong(){
    	Log.d("MediaOP", "BEFOREPlaying : "+this.currentFilePath);
    	 ((TextView) findViewById(R.id.textView_songTitle)).setText("Now playing :  \n"+this.currentFilePath);
    	try {
    		this.mediaplayer.stop();
    		this.mediaplayer.release();
			this.mediaplayer = new MediaPlayer();
    		this.mediaplayer.setDataSource(this.currentFilePath);
			this.mediaplayer.prepare();		// Opération qui prend beaucoup de temps.
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.mediaplayer.start();
    	Log.d("MediaOP", "Playing : "+this.currentFilePath);
    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_pause);


    }
    
}
