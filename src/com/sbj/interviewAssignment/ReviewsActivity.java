package com.sbj.interviewAssignment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.sbj.interviewAssignment.domain.Review;
import com.sbj.interviewAssignment.requests.RTRequestExecutor;
import com.sbj.interviewAssignment.requests.RTReviewRequest;
import com.sbj.interviewAssignment.ui.adapter.ReviewListAdapter;

/**
 * Activity to display the reviews for a movie
 * 
 * @author mhelfer
 *
 */
public class ReviewsActivity extends ListActivity implements OnScrollListener, RTActivity{
	
	private static final String TAG = ReviewsActivity.class.getSimpleName();
	
	//Default page-size for this view.
	private static final int PAGE_SIZE = 10;

	ReviewListAdapter adapter = new ReviewListAdapter(this);
	
	private String movieId;
	private int curPage = 1;
	private boolean readyForNextPage = true;
	private boolean hasMore = true;
	
	@Override
	@SuppressWarnings("unchecked") //cast from getLastNonConfigurationInstance
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(adapter); 
		getListView().setOnScrollListener(this);
		
		//Retrieve the detail id from the bundle;
	    Bundle extras = getIntent().getExtras(); 
	    movieId = extras.getString(BUNDLE_MOVIE_ID);
	    
	    final List<Review> data = (List<Review>) getLastNonConfigurationInstance();
		if(data == null){
			//if there is no movie id, reload the box office list.
		    if(movieId == null || movieId.equals("")){
		    	Intent restart = new Intent(this,InterviewAssignmentActivity.class);
		    	startActivity(restart);
		    }
		}
		else{
			adapter.setData(data);
			adapter.notifyDataSetChanged();
		}  
	}
	
	/**
	 * This method will execute when the users scrolls the list view.
	 * When the user gets near the bottom it will attempt to load more results.
	 * 
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		//onScroll runs before onCreate so the movieId won't have been retrieved from the intent bundle yet.
		if(StringUtils.isNotBlank(movieId)){
			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
			
			//ready for next page is a very simplistic synchronization to ensure we don't retrieve the list more than once.
			if(loadMore && readyForNextPage && hasMore) {
				readyForNextPage = false;
				RTReviewRequest request = new RTReviewRequest.Builder(RT_API_KEY, movieId )
												.pageLimit(String.valueOf(PAGE_SIZE))
												.page(String.valueOf(curPage++)).build();
				new ReviewRequest().execute(request);
	        }
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//Not sure what to do here	
	}
	
	/**
     * Optimization to avoid retrieving BoxOffice data again on screen rotation.
     * This will store the page data, so a reorientation will not require retrieving the data again.
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
        final List<Review> data = adapter.getData();
        return data;
    }
	
	/**
     * Implementation of RTRequestExecutor to retrieve the review data and construct views with the returned information.
     * 
     * @author mhelfer
     *
     */
	private class ReviewRequest extends RTRequestExecutor {
		
		/**
    	 * Populates views with the movie info data.	
    	 * 
    	 * @param JSONObject passed after doInBackground method completes.
    	 */
        protected void onPostExecute(JSONObject result) {
            if(result != null) {
	        	Log.d(TAG,result.toString());
	            
	            List<Review> reviews = new ArrayList<Review>();
	            try{
	            	//Attempt to retrieve the next link if it fails set the more indicator to false;
	            	try{
	            		result.getJSONObject(Review.LINKS).get("next");
	            	}
	            	catch(JSONException e){
	            		hasMore = false;
	            	}
	            	
	            	//Long-term this could be better served using a GSON.fromJSON call but without the schema's it was easier to manually
	            	//parse the response into a smaller object containing only the members I needed.
	            	JSONArray jsonReviews = (JSONArray)result.get(Review.REVIEWS);
	            	JSONObject jsonReview = null;
	            	
	            	for(int i = 0; i<jsonReviews.length(); i++){
	            		jsonReview = jsonReviews.getJSONObject(i);
	            		reviews.add(new Review(jsonReview));
	            	}
	            	
	            } catch(Exception e){
	            	Log.e(TAG,"there was a problem parsing the response" + e.getMessage());
	            }
	             
	            adapter.addData(reviews);
	            adapter.notifyDataSetChanged();
	            readyForNextPage = true;
            }
            else {
            	Log.d(TAG,"JSON object was null");
            }
        } 
	}
}
