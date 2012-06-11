package com.sbj.interviewAssignment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Log;

import com.sbj.interviewAssignment.requests.RTMoviePosterRequest;

/**
 * Domain object for storing movieInfo data
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Movies_Search
 * 
 * @author mhelfer
 *
 */
public class MovieInfo implements RTDomain {
	
	private final String TAG = MovieInfo.class.getSimpleName();
	
	private String id;
	private String title;
	private String poster;
	private String synopsis;
	private String rating;
	private String freshness;
	private String runtime;
	private String rtLink;
	private Bitmap bitmap;
	
	private List<Cast> cast;
	
	//Constructor from a JSONObject.
	public MovieInfo(JSONObject movieInfo){
		try{
			this.id = movieInfo.getString(ID);
			this.title = movieInfo.getString(TITLE);
			this.poster = movieInfo.getJSONObject(POSTERS).getString(PROFILE);
			
			try { 
	        	//Attempt to get the movie poster.
	        	Bitmap posterImage = new RTMoviePosterRequest().execute(this.poster).get();
	        	bitmap = posterImage;
	        	
	        	//if an exception occurs replace the desired image with a local not found image to preserve the UI.
	        } catch(ExecutionException ee){
				Log.e(TAG, "There was a problem retrieving the movie poster " + ee.getMessage());
			} catch(InterruptedException ie){
				//Propagate the interruption
				Log.e(TAG, "The movie poster thread was interupted " + ie.getMessage());
				Thread.currentThread().interrupt();
			}
			
			this.synopsis = movieInfo.getString(SYNOPSIS);
			this.rating = movieInfo.getString(MPAA_RATING);
			this.freshness = movieInfo.getJSONObject(RATINGS).getString(CRITICS_SCORE);
			this.runtime = movieInfo.getString(RUNTIME);
			this.cast = new ArrayList<Cast>();
			this.rtLink = movieInfo.getJSONObject(LINKS).getString(ALTERNATE);
			
			JSONArray abridgedCast = movieInfo.getJSONArray(ABRIDGED_CAST);
			JSONObject currentMember = null;
			Cast member = null;
			for(int i=0;i<abridgedCast.length();i++){
				currentMember = (JSONObject)abridgedCast.get(i);
				member = new Cast();
				member.setName(currentMember.getString(NAME));
				member.setCharacter(currentMember.getJSONArray(CHARACTERS).getString(0));
				member.setId(currentMember.getString(ID));
				this.cast.add(member);
			}	
		} catch(JSONException jse){
			Log.e(TAG, "there was a problem creating a BoxOfficeMovie from the response" + jse.getMessage());
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) { 
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public String getSynopsis() {
		return synopsis;
	}
	
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	public String getRating() {
		return rating;
	}
	
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getFreshness() {
		return freshness;
	}
	
	public void setFreshness(String freshness) {
		this.freshness = freshness;
	}
	
	public String getRuntime() {
		return runtime;
	}
	
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public List<Cast> getCast() {
		return cast;
	}

	public void setCast(List<Cast> cast) {
		this.cast = cast;
	}

	public String getRtLink() {
		return rtLink;
	}
	
	public void setRtLink(String rtLink) {
		this.rtLink = rtLink;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
