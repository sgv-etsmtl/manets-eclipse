package com.example.manetsclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;



import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ManetsClientActivity extends Activity {

	
	private MediaPlayer mediaplayer;
	private String currentFilePath, mediaLibraryPath;
	private int currentFileID;
	private long songCurrentTime = 0;
	private String[] playlist;
	private boolean isPlaying = false, isPaused = false;
	
	private HTTPClient httpClient, songTitleHTTPClient, songArtistHTTPClient, songAlbumHTTPClient, songDurationHTTPClient;
	private static String DEFAULT_HOSTNAME = "http://10.192.180.231:8080";
	
//	Handler timerHandler = new Handler();
//    Runnable timerRunnable = new Runnable() {
//
//        @Override
//        public void run() {
//            long millis = System.currentTimeMillis() - songCurrentTime;
//            int seconds = (int) (millis / 1000);
//            int minutes = seconds / 60;
//            seconds = seconds % 60;
//
//			String secondsStr, minStr = minutes + ":";
//				
//			if(seconds < 10)
//				{secondsStr = "0" + seconds;}
//			else
//				{secondsStr = "" + seconds;}
//			 
//			
//			((TextView) findViewById(R.id.textView_songDuration)).setText(minStr+secondsStr);
//			((SeekBar) findViewById(R.id.seekBar_duration)).setProgress((int) millis);
//            timerHandler.postDelayed(this, 500);
//        }
//    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manets_client_activity_layout);
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
    	 
        this.currentFileID = 0;
        this.httpClient = new HTTPClient(DEFAULT_HOSTNAME);
        this.httpClient.execute("/getavailablesongs/", "GET", "");
    	((SeekBar) findViewById(R.id.seekBar_duration)).setOnSeekBarChangeListener(new SongSeekBarListener() );
        
    }
    
    public void updateCurrentFilePath() {
        //local version
    	//this.currentFilePath = "file://" + this.mediaLibraryPath + "/" + this.playlist[this.currentFileID] ;
        
        //network version
         this.currentFilePath = this.playlist[this.currentFileID];
		
	}

    
    public void playPause(View v){
    	
    	
    	if(isPlaying || isPaused){
        	this.httpClient = new HTTPClient(DEFAULT_HOSTNAME);//temp

        	this.httpClient.execute("/togglepause/", "GET", "");
        	
    	}
    	else{
    		playSong();
    	}
    	
    	/*LOCAL VERSION
    	 * try {
    		
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
    	*/
    
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
    	//this.mediaplayer.stop();
    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_play);
    	this.httpClient = new HTTPClient(DEFAULT_HOSTNAME);//temp
    	this.httpClient.execute("/stop/", "GET", "");
    	


    }
    
    public void setCurrentID(int newID){
    	this.currentFileID = newID;
    	}
    
    public void playSong(){

    	//NETWORK CODE
    	this.httpClient = new HTTPClient(DEFAULT_HOSTNAME);
    	
    	this.httpClient.execute("/play?song=" + this.currentFilePath, "GET", "");
    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_pause);
    	this.isPlaying = true;
    	this.isPaused = false;
    	
    	this.songTitleHTTPClient = new HTTPClient(DEFAULT_HOSTNAME);
    	this.songTitleHTTPClient.execute("/getsongtitle?song=" + this.currentFilePath, "GET", "");
    	
    	this.songArtistHTTPClient = new HTTPClient(DEFAULT_HOSTNAME);
    	this.songArtistHTTPClient.execute("/getsongartist?song=" + this.currentFilePath, "GET", "");
    	
    	this.songAlbumHTTPClient = new HTTPClient(DEFAULT_HOSTNAME);
    	this.songAlbumHTTPClient.execute("/getsongalbum?song=" + this.currentFilePath, "GET", "");
    	
    	this.songDurationHTTPClient = new HTTPClient(DEFAULT_HOSTNAME);
    	this.songDurationHTTPClient.execute("/getsongtotalduration?song=" + this.currentFilePath, "GET", "");
    	
    	
    	
    	
    	//LOCAL PLAYBACK CODE
//    	Log.d("MediaOP", "BEFOREPlaying : "+this.currentFilePath);
//    	 ((TextView) findViewById(R.id.textView_songTitle)).setText("Now playing :  \n"+this.currentFilePath);
//    	try {
//    		this.mediaplayer.stop();
//    		this.mediaplayer.release();
//			this.mediaplayer = new MediaPlayer();
//    		this.mediaplayer.setDataSource(this.currentFilePath);
//			this.mediaplayer.prepare();		// Opération qui prend beaucoup de temps.
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	this.mediaplayer.start();
//    	Log.d("MediaOP", "Playing : "+this.currentFilePath);
//    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_pause);


    }
    private class HTTPClient extends AsyncTask<String, String, String>{
    	//Flags for the state 
    	private final String GET_AVAILABLE_SONGS_RETURNED = "GetAvailableSongs";
    	private final String GET_SONG_ALBUM_RETURNED = "GetSongAlbum";
    	private final String GET_SONG_ARTIST_RETURNED = "GetSongArtist";
    	private final String GET_SONG_TITLE_RETURNED = "GetSongTitle";
    	private final String GET_SONG_TOTAL_DURATION_RETURNED = "GetSongDuration";
    	private final String GET_PLAY_RETURNED = "Play";
    	private final String GET_STOP_RETURNED = "Stop";
    	private final String GET_TOGGLE_PAUSE_RETURNED = "TogglePause";
    	private final String GET_VOLUME_RETURNED = "Volume";
    	private final String GET_SCROLL_RETURNED = "Seek";
    	private final String GET_UNKNOWN_RETURNED = "Unknown";
    	
    	private String hostname;
    	private String state = ""; 
    	
    	public HTTPClient(String hostname) {
    		this.hostname = hostname;
    		
    		
    	
    	}
    	
    		
    	@Override
    	protected String  doInBackground(String... params) {
			String outputLine  = ""; //will be returned

    		//Params
    		// hostname is defined at in Constructor
    		// 0 : path
    		// 1 : method
    		// 2 : data
    		Log.d("httpClient", "param : " + params[0]);
    		Log.d("httpClient", "param : " + params[1]);
    		Log.d("httpClient", "param : " + params[2]);
    		;
    		
    		
    		BufferedReader bufferedReader = null;
			
					try {
						URL url = new URL(this.hostname + params[0]);
						Log.d("hostname", this.hostname);
						Log.d("url", params[0]);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod(params[1]);
						connection.setDoInput(true);
						
						if(params[1].equals("GET")) {
							connection.setDoOutput(false);
						} else {
							connection.setDoOutput(true);
							DataOutputStream dataOutputStream = new DataOutputStream (connection.getOutputStream());
							dataOutputStream.writeBytes(params[2]);
							dataOutputStream.close();
						}
						
						bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						String line;
						StringBuilder stringBuilder = new StringBuilder();
		
						while((line = bufferedReader.readLine()) != null) {
							stringBuilder.append(line);
						}
						outputLine = stringBuilder.toString();
					
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					if(params[0] == "/getavailablesongs/" ){
						this.state = GET_AVAILABLE_SONGS_RETURNED;
						outputLine = outputLine.substring(1, outputLine.length()-1);//removing [ and ]

					}
					else if ( params[0].contains("/getsongtitle") ){
						this.state = GET_SONG_TITLE_RETURNED;
					}
					else if ( params[0].contains("/getsongartist") ){
						this.state = GET_SONG_ARTIST_RETURNED;
					}
					else if ( params[0].contains("/getsongalbum") ){
						this.state = GET_SONG_ALBUM_RETURNED;
					}
					else if ( params[0].contains("/getsongtotalduration") ){
						this.state = GET_SONG_TOTAL_DURATION_RETURNED;
					}
					else if ( params[0].contains("/play") ){
						this.state = GET_PLAY_RETURNED;
					}
					else if ( params[0].contains("/stop") ){
						this.state = GET_STOP_RETURNED;
					}
					else if ( params[0].contains("/togglepause") ){
						this.state = GET_TOGGLE_PAUSE_RETURNED;
					}
					else if ( params[0].contains("/volume") ){
						this.state = GET_VOLUME_RETURNED;
					}
					else if ( params[0].contains("/scroll") ){
						this.state = GET_SCROLL_RETURNED;
					}
					else{	
						this.state = GET_UNKNOWN_RETURNED;	
					}
					
					return outputLine;
						
					
					
			
		}
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();

    	}
    	@Override
    	protected void onPostExecute(String result) {
    		if(result != null){
    			Log.d("JSONResult", result);
    			
    			//I apologize to anyone who must read this aberration.
    			if(this.state == GET_AVAILABLE_SONGS_RETURNED){
    				Log.d("JSONResult_GETAVAILABLESONGS",result);
	    			if(result.contains(", ")){
	    				ManetsClientActivity.this.playlist = result.split(", ");
	    				setCurrentID(0);
	    				updateCurrentFilePath();
	    				((TextView) findViewById(R.id.textView_message)).setText("Playlist has Loaded");
	    			}
	    			else if(result.contains(",")){
	    				ManetsClientActivity.this.playlist = result.split(",");
	    				setCurrentID(0);
	    				updateCurrentFilePath();
	    				((TextView) findViewById(R.id.textView_message)).setText("Playlist has Loaded");
	    			}
	    			
	    			//dirtyHack++;
	    			for(int i = 0; i < ManetsClientActivity.this.playlist.length; i++){
	    				ManetsClientActivity.this.playlist[i] = 
	    						ManetsClientActivity.this.playlist[i].replace(" ", "%20");//TEMP, should be encoded by server
	    				ManetsClientActivity.this.playlist[i] = 
	    						ManetsClientActivity.this.playlist[i].replace("\"", "");//TEMP, should be encoded by server
						Log.d("CALVAIRE", ManetsClientActivity.this.playlist[i] );
	    			}
	    			ManetsClientActivity.this.updateCurrentFilePath();
	    			//dirtyHack--;
	    			
    			}
    			else if (this.state == GET_SONG_TITLE_RETURNED){
    				((TextView) findViewById(R.id.textView_songTitle)).setText(result);
    			}
    			else if (this.state == GET_SONG_ALBUM_RETURNED){
    				((TextView) findViewById(R.id.textView_songAlbum)).setText(result);
    			}
    			else if (this.state == GET_SONG_ARTIST_RETURNED){
    				((TextView) findViewById(R.id.textView_songArtist)).setText(result);
    			}
    			else if (this.state == GET_PLAY_RETURNED){
    				((TextView) findViewById(R.id.textView_message)).setText("Server is Playing");
    				((TextView) findViewById(R.id.textView_songCurrentTime)).setText(R.string.textView_tt_defaultSongCurrentTime);
    				((SeekBar) findViewById(R.id.seekBar_duration)).setProgress(0);
    				
    				
    			}
    			else if (this.state == GET_STOP_RETURNED){
    				((TextView) findViewById(R.id.textView_message)).setText("Server has stopped playback");
        	    	((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_play);
    				ManetsClientActivity.this.isPlaying = false;
					ManetsClientActivity.this.isPaused = false;
					//StopTimer -> put at 0
    			}
    			else if (this.state == GET_TOGGLE_PAUSE_RETURNED){
    				if(ManetsClientActivity.this.isPaused){
    					((TextView) findViewById(R.id.textView_message)).setText("Server is Playing");
    					((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_pause);
    					ManetsClientActivity.this.isPlaying = true;
    					ManetsClientActivity.this.isPaused = false;
    					//resume timer 
    				}
    				else{
    					((TextView) findViewById(R.id.textView_message)).setText("Server is Paused");
    					((Button) findViewById(R.id.buttonPlayPause)).setText(R.string.button_tt_play);
    					ManetsClientActivity.this.isPlaying = false;
    					ManetsClientActivity.this.isPaused = true;
    					//pause timer
    				}
    			}
    			else if (this.state == GET_VOLUME_RETURNED){
    				
    			}
    			else if (this.state == GET_SCROLL_RETURNED){
    				

    			}
 				else if (this.state == GET_SONG_TOTAL_DURATION_RETURNED){
    				Integer resultDuration = Integer.parseInt(result);
    				
 	   				long minutes =  (resultDuration/1000)/60;
 	   				long seconds = (resultDuration/1000)%60;
 	   				String secondsStr, minStr = minutes + ":";
 	   				
 	   				if(seconds < 10)
 	   					{secondsStr = "0" + seconds;}
 	   				else
 	   					{secondsStr = "" + seconds;}
    				result =    minStr+secondsStr;
    				
    				Log.d("JSONResult_songTotalDuration", result);
    				((TextView) findViewById(R.id.textView_songDuration)).setText(result);
    				((SeekBar) findViewById(R.id.seekBar_duration)).setMax(resultDuration);
    				
    			}
    			else if(this.state == ""){
    				Log.d("JSONResult_empty", result);
    				Log.d("State", this.state);
    			}
    			else{
    				Log.d("JSONResult_UNKNOWN", result);
    				Log.d("State", this.state);
    			}
    		}
    		else{
    			Log.d("JSONResult_NULL", "NULL");
    			((TextView) findViewById(R.id.textView_message)).setText("Error Loading Playlist");

    		}	
    	}
    }
    private class SongSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
                            // Log the progress
            Log.d("DEBUG", "Progress is: "+progress);
                            //set textView's text
            
            	long minutes =  (progress/1000)/60;
				long seconds = (progress/1000)%60;
				String secondsStr, minStr = minutes + ":";
				
				if(seconds < 10)
					{secondsStr = "0" + seconds;}
				else
					{secondsStr = "" + seconds;}
            
            ((TextView) findViewById(R.id.textView_songCurrentTime)).setText(""+minStr+secondsStr);
			
            if(fromUser){
            	ManetsClientActivity.this.songDurationHTTPClient = new HTTPClient(DEFAULT_HOSTNAME);
            	ManetsClientActivity.this.songDurationHTTPClient.execute("/scroll?payload=" + (progress), "GET", "");
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }
}
