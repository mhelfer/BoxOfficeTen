package com.sbj.interviewAssignment.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.sbj.interviewAssignment.R;
import com.sbj.interviewAssignment.requests.RTMoviePosterRequest;


/**
 * Domain object for storing box office movie data. 
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Box_Office_Movies
 * 
 * @author mhelfer
 *
 */
public class BoxOfficeMovie implements RTDomain{
	
	private final String TAG = BoxOfficeMovie.class.getSimpleName();
	
	//Map for deciding which drawable image to show.
	public static Map<String,Integer> ratingImageMap;
	static {
		ratingImageMap = new HashMap<String,Integer>();
		ratingImageMap.put("G",R.drawable.g);
		ratingImageMap.put("PG",R.drawable.pg);
		ratingImageMap.put("PG-13",R.drawable.pg_13);
		ratingImageMap.put("R",R.drawable.r);
		ratingImageMap.put("NC-17",R.drawable.nc_17);
	}
	
	private String id;
	private String title;
	private String rating;
	private String poster;
	private int freshness;
	private Bitmap bitmap;
	
	//Constructor from a JSONObject.
	public BoxOfficeMovie(JSONObject movie){
		try{
			this.id = movie.get(ID).toString();
			this.title = movie.get(TITLE).toString();
			this.rating = movie.get(MPAA_RATING).toString();
			this.poster = movie.getJSONObject(POSTERS).get(THUMBNAIL).toString();
			this.freshness = Integer.parseInt(movie.getJSONObject(RATINGS).get(CRITICS_SCORE).toString());
			
			try{
				//Retrieve the bitmap image from the URL.
				Bitmap posterImage = new RTMoviePosterRequest().execute(this.getPoster()).get();
				this.bitmap = posterImage;
			} catch(ExecutionException ee){
				Log.e(TAG, "There was a problem retrieving the movie poster " + ee.getMessage());
			} catch(InterruptedException ie){
				Log.e(TAG, "There was a problem retrieving the movie poster " + ie.getMessage());
				//propigate the interuption
				Thread.currentThread().interrupt();
			}
			
			
		} catch(JSONException jse){
			Log.e(TAG, "there was a problem creating a BoxOfficeMovie from the response" + jse.getMessage());
		}
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getRating() {
		return rating;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public int getFreshness() {
		return freshness;
	}
	
	public void setFreshness(int freshness) {
		this.freshness = freshness;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
