package com.sbj.interviewAssignment.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Request class to retrieve the moviePoster for the specified movie. 
 * Extends AsyncTask so that it can execute in a separate thread and not block the UI with networking code.
 * 
 * @author mhelfer
 *
 */
public class RTMoviePosterRequest extends AsyncTask<String, Void, Bitmap>{
	private static final String TAG = RTMoviePosterRequest.class.getSimpleName();
	
	/**
	 * Connects to the specified URL and creates a bitmap from the returned content.
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = null;
		
		try {
			bitmap = BitmapFactory.decodeStream((InputStream)new URL(params[0]).getContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		Log.d(TAG,"Successfully retrieved movie poster bitmap");
	}

	
	
}
