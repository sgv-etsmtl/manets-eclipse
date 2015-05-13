package com.example.manets;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void playTest(View v){
    	Log.d("Tag123", "msg123");
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        Log.d("rootPath", rootPath);
        String filePath = rootPath+"/Download/test.mp3";
    	try {
    		
    		
			//AssetFileDescriptor afd = getAssets().openFd(path+"/Download/test.mp3");
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();		// Opération qui prend beaucoup de temps.
			mediaPlayer.start();
        } catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void prev(View v)
    {
    	
    }
    public void next(View v){
    	
    }
    
}
