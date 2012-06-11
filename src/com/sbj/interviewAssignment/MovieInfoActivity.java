package com.sbj.interviewAssignment;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbj.interviewAssignment.domain.Cast;
import com.sbj.interviewAssignment.domain.MovieInfo;
import com.sbj.interviewAssignment.requests.RTMovieInfoRequest;
import com.sbj.interviewAssignment.requests.RTMoviePosterRequest;
import com.sbj.interviewAssignment.requests.RTRequestExecutor;

/**
 * Activity to display the details of a specific movie. 
 * 
 * @author mhelfer
 *
 */
public class MovieInfoActivity extends Activity implements RTActivity{
	
	private static final String TAG = MovieInfoActivity.class.getSimpleName();
	
	//Instance member so it can be accessed by the AsyncTask
	private MovieInfo movieInfo = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.movieinfo);
	    
	    //Retrieve the detail id from the bundle;
	    Bundle extras = getIntent().getExtras(); 
	    final String movieId = extras.getString(BUNDLE_MOVIE_ID);
	    
	    //if there is no movie id, reload the box office list.
	    if(movieId == null || movieId.equals("")){
	    	Intent restart = new Intent(this,InterviewAssignmentActivity.class);
	    	startActivity(restart);
	    }
	    else {
	    	//Retrieve the movieInfo details in a different thread.
	    	RTMovieInfoRequest request = new RTMovieInfoRequest.Builder(RT_API_KEY, movieId ).build();
	    	new MovieInfoRequest().execute(request);
	    }
	    
	    //Create onClick behavior for the reviews button.
	    Button button = (Button)findViewById(R.id.infoReviewButton);
	    button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intents can be used to navigate between activities. This version will direct
				//the user to the Review Page.
				Intent intent = new Intent(MovieInfoActivity.this, ReviewsActivity.class);
				
				//Add the selected movie to the intent so it can be read by the detail page.
				intent.putExtra(BUNDLE_MOVIE_ID, movieId);
				startActivity(intent);
			}
		});
	}
	
	/**
     * Implementation of RTRequestExecutor to retrieve the movieInfo data and construct views with the returned information.
     * 
     * @author mhelfer
     *
     */
	private class MovieInfoRequest extends RTRequestExecutor {
		
		/**
    	 * Populates views with the movie info data.	
    	 * 
    	 * @param JSONObject passed after doInBackground method completes.
    	 */
        protected void onPostExecute(JSONObject result) {
            Log.d(TAG,result.toString());
            
            try{
            	//Construct a movie info object from the result object.
            	movieInfo = new MovieInfo(result);
            } catch(Exception e){
            	Log.e(TAG,"there was a problem parsing the response" + e.getMessage());
            }
            
            //populate the UI elements with the data returned.
            if(movieInfo != null) {
            	
            	TextView title = (TextView)findViewById(R.id.infoTitle);
                title.setText(movieInfo.getTitle());
                
                ImageView poster = (ImageView)findViewById(R.id.infoPoster);
                
                try { 
                	//Attempt to get the movie poster.
                	Bitmap posterImage = new RTMoviePosterRequest().execute(movieInfo.getPoster()).get();
                	poster.setImageBitmap(posterImage);
                
                	//if an exception occurs replace the desired image with a local not found image to preserve the UI.
                } catch(ExecutionException ee){
                	poster.setImageResource(R.drawable.notavailable_detail);
    				Log.e(TAG, "There was a problem retrieving the movie poster " + ee.getMessage());
    			} catch(InterruptedException ie){
    				poster.setImageResource(R.drawable.notavailable_detail);
    				//Propagate the interruption
    				Log.e(TAG, "The movie poster thread was interupted " + ie.getMessage());
    				Thread.currentThread().interrupt();
    			}
                
                TextView synopsis = (TextView)findViewById(R.id.infoSynopsis);
                synopsis.setText(movieInfo.getSynopsis());
                
                TextView rated = (TextView)findViewById(R.id.infoRated);
                rated.setText("Rated " + movieInfo.getRating());
                
                TextView freshness = (TextView)findViewById(R.id.infoFreshness);
                freshness.setText("Freshness " + movieInfo.getFreshness() + "%");
                
                TextView runtime = (TextView)findViewById(R.id.infoRuntime);
                int time = Integer.parseInt(movieInfo.getRuntime());
                runtime.setText(time/60 + "hrs " + time%60 + "mins");
                
                //Linear layout was used inside of a relative layout, since it is easier to add a list of items dynamically
                LinearLayout layout = (LinearLayout)findViewById(R.id.infoCastLayout);
                
                //Dynamically create views' for the cast members and add it to the layout.
                for(Cast character : movieInfo.getCast()){
                	final TextView textView = new TextView(MovieInfoActivity.this);
                	textView.setText(character.getName() + " as " + character.getCharacter());
                	textView.setTextColor(Color.BLACK);
                	layout.addView(textView);
                }
                
            }
        }
    }
	
	/**
	 * Method that will create the share button.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//Create a menu element and assign the send intent to it. 
		//This will allow the user to share the movie URL via FB,Twitter, email, etc.
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, movieInfo.getRtLink());	
		intent.setType("text/plain");
		
	    menu.add("Share").setIntent(intent);	    
	    
	    return super.onCreateOptionsMenu(menu);
	}

}
