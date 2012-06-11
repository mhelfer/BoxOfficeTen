package com.sbj.interviewAssignment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(adapter); 
		getListView().setOnScrollListener(this);
		
		//Retrieve the detail id from the bundle;
	    Bundle extras = getIntent().getExtras(); 
	    movieId = extras.getString(BUNDLE_MOVIE_ID);
		
		//if there is no movie id, reload the box office list.
	    if(movieId == null || movieId.equals("")){
	    	Intent restart = new Intent(this,InterviewAssignmentActivity.class);
	    	startActivity(restart);
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
			if(loadMore && readyForNextPage) {
				
				//Stop trying to make requests if we've received all the reviews. 
				if(totalItemCount == 0 || totalItemCount < adapter.getTotalReviews()){
					readyForNextPage = false;
					RTReviewRequest request = new RTReviewRequest.Builder(RT_API_KEY, movieId )
													.pageLimit(String.valueOf(PAGE_SIZE))
													.page(String.valueOf(curPage++)).build();
					new ReviewRequest().execute(request);
				}
	        }
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//Not sure what to do here	
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
            Log.d(TAG,result.toString());
            
            List<Review> reviews = new ArrayList<Review>();
            int totalReviews = 0;
            try{
            	//Long-term this could be better served using a GSON.fromJSON call but without the schema's it was easier to manually
            	//parse the response into a smaller object containing only the members I needed.
            	totalReviews = result.getInt(Review.TOTAL);
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
            adapter.setTotalReviews(totalReviews);
            adapter.notifyDataSetChanged();
            readyForNextPage = true;
        } 
	}
	
}
