package com.sbj.interviewAssignment.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


/**
 * This class will wrap a HTTPUrlConnection object to retrieve a JSON object from a configurable endpoint.
 * 
 * @author mhelfer
 *
 */
public class HttpGetDao {
	
	private static final String TAG = HttpGetDao.class.getSimpleName();
	
	private String targetUrl;
	
	/**
	 * Constructor that takes in the targetURL.
	 * If there is a problem with the call a null object will be returned.
	 * @param targetUrl
	 */
	public HttpGetDao(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
	/** 
	 * Method to get the JSON object.
	 * @return
	 */
	public JSONObject get() {
		
		JSONObject json = null;
		HttpURLConnection urlConn = null;
		
		try{
			
			//Construct an HttpUrlConnection object with the targetURL.
			URL url = new URL(targetUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			
			//Log the url before opening the connection.
			Log.d(TAG,"Request: " + targetUrl);
			urlConn.connect();
			
			int httpStatus = urlConn.getResponseCode();
			
			//Ensure that we only take action on successful return codes.
			switch(httpStatus) {
				case 200:
				case 201:
					
					//Retrieve the data from the inputStream and create a JSONObject from it.
					BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					
					String line;
		            while ((line = br.readLine()) != null) {
		                sb.append(line);
		            }
		            if(sb != null) {
		            	json = new JSONObject(sb.toString());
		            }
		            break;
		        default:
		        	Log.e(TAG,"Request was not successful httpStatus:"+httpStatus);
			}
            
		} catch(MalformedURLException mue) {
			//JSONObject returned is null
			Log.e(TAG, "Invalid URL:" + targetUrl + " " + mue.getMessage());
		} catch(IOException ioe) {
			//JSONObject returned is null
			Log.e(TAG, "There was a problem connecting to that endpoint " + ioe.getMessage());
		} catch(JSONException je){
			//JSONObject returned is null
			Log.e(TAG, "There was a problem parsing the JSON " + je.getMessage());
		}
		finally {
			if(urlConn != null) {
				//release the connection
				urlConn.disconnect();
			}
		}
		
		return json;
	}
}
