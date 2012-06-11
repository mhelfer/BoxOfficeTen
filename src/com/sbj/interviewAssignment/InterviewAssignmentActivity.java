package com.sbj.interviewAssignment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sbj.interviewAssignment.domain.BoxOfficeMovie;
import com.sbj.interviewAssignment.requests.RTBoxOfficeRequest;
import com.sbj.interviewAssignment.requests.RTRequestExecutor;
import com.sbj.interviewAssignment.ui.adapter.BoxOfficeListAdapter;

/**
 * Main Activity, which will display the current top 10 movies at the box office.
 * 
 * @author mhelfer
 *
 */
public class InterviewAssignmentActivity extends ListActivity implements RTActivity{
    private static final String TAG = InterviewAssignmentActivity.class.getSimpleName();
	
    //Instance member so it is available from the AsyncTask
    private BoxOfficeListAdapter adapter;	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Create the adapter for the box office screen
        adapter = new BoxOfficeListAdapter(this);
        setListAdapter(adapter);
        
        //Add an on-click listener to navigate to the detail page.
        ListView movieList = getListView();
        movieList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Intents can be used to navigate between activities. 
				//This version will send the user to the MovieInfo Page.
				Intent intent = new Intent(InterviewAssignmentActivity.this, MovieInfoActivity.class);
				
				//Add the selected movie to the intent so it can be read by the detail page.
				intent.putExtra(BUNDLE_MOVIE_ID, ((BoxOfficeMovie)adapter.getItem(position)).getId());
				startActivity(intent);
			}
        	
        });
        
        //Build an instance of the RTBoxOfficeRequest
        RTBoxOfficeRequest request = new RTBoxOfficeRequest.Builder(RT_API_KEY).country("us").limit("10").build();
        //Android's thread policies disallow network calls on the main thread. So we use an executor to make networking calls
        new BoxOfficeRequest().execute(request);
        
    }
    
    /**
     * Implementation of RTRequestExecutor to construct the views based on the box office data.
     * 
     * @author mhelfer
     *
     */
    private class BoxOfficeRequest extends RTRequestExecutor {
    	
    	/**
    	 * Populates views with the box office data.	
    	 * 
    	 * @param JSONObject passed after doInBackground method completes.
    	 */
        protected void onPostExecute(JSONObject result) {
            Log.d(TAG,result.toString());
            List<BoxOfficeMovie> movies = new ArrayList<BoxOfficeMovie>();

            try{
            	//Long-term this could be better served using a GSON.fromJSON call but without the schema's it was easier to manually
            	//parse the response into a smaller object containing only the members I needed.
            	JSONArray jsonMovies = (JSONArray)result.get(BoxOfficeMovie.MOVIES);
            	JSONObject jsonMovie = null;
            	
            	//Build the List of box office movies
            	for(int i = 0; i<jsonMovies.length(); i++){
            		jsonMovie = jsonMovies.getJSONObject(i);
            		movies.add(new BoxOfficeMovie(jsonMovie));
            	}
            } catch(Exception e){
            	Log.e(TAG,"there was a problem parsing the response" + e.getMessage());
            }
            
            //Populate the returned movies into the listAdapter.
            adapter.setData(movies);
            
            //Notify the UI that the data-set has been updated so it can repopulate the list.
            adapter.notifyDataSetChanged();
        }
    }
}