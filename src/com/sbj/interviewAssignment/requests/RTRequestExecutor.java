package com.sbj.interviewAssignment.requests;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.sbj.interviewAssignment.dao.HttpGetDao;

/**
 * Android's thread policy does not allow network calls on the main thread.
 * This class will make the call to Rotten Tomatoes api on a separate thread.
 * 
 * Abstract so that the use of HTTPGetDao can be defaulted. 
 * Implementors can implement onPostExecute to build their views with the returned data.
 * 
 * @author mhelfer
 *
 */
public abstract class RTRequestExecutor extends AsyncTask<RTRequest, Void, JSONObject>{
	
	/**
	 * Creates an instance of HttpGetDao and executes the configured request.
	 */
	protected JSONObject doInBackground(RTRequest ... rtRequest) {
		
    	HttpGetDao dao = new HttpGetDao(rtRequest[0].getQueryURL());
    	//Send the request and return a JSON object containing the results.
    	return dao.get();
    }
	
	/**
	 * Implemented by the consumer to build views with the data returned.
	 */
	protected abstract void onPostExecute(JSONObject result);
}
