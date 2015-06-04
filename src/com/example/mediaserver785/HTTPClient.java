package com.example.mediaserver785;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class HTTPClient extends AsyncTask<String, String, String>{

	private String hostname;
	
	public HTTPClient(String hostname) {
		this.hostname = hostname;
		
		
	
	}
	
		
//	public String request(String path, String method, String data) {
//
//		BufferedReader bufferedReader = null;
//
//		try {
//			URL url = new URL(hostname + path);
//			Log.d("hostname", hostname);
//			Log.d("url", path);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod(method);
//			connection.setDoInput(true);
//			
//			if(method.equals("GET")) {
//				connection.setDoOutput(false);
//			} else {
//				connection.setDoOutput(true);
//				DataOutputStream dataOutputStream = new DataOutputStream (connection.getOutputStream());
//				dataOutputStream.writeBytes(data);
//				dataOutputStream.close();
//			}
//
//			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//			String line;
//			StringBuilder stringBuilder = new StringBuilder();
//
//			while((line = bufferedReader.readLine()) != null) {
//				stringBuilder.append(line);
//			}
//
//			return stringBuilder.toString();
//
//		} catch (Exception e) {
//			 e.printStackTrace();
//			 return "err";
//		} finally {
//			if(bufferedReader != null) {
//				try {
//					bufferedReader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	@Override
	protected String doInBackground(String[] params) {
		//Params
		// hostname is defined at in Constructor
		// 0 : path
		// 1 : method
		// 2 : data
		
		BufferedReader bufferedReader = null;

		this.hostname = params[0];
		
		
			
			//TODO HERE : PARSE params[0] for different paths as they return different things
			
			switch(params[0]){
				case "lel":
					break;
				case "lel2":
					break;
				default:

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

					return stringBuilder.toString();
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
		}
		return null;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		ProgressDialog  progressDialog = ProgressDialog.show(null, "Please wait...", "", true);

	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
	}
//	@Override
//	protected void onProgressUpdate() {
//		super.onProgressUpdate();
//	}
}
