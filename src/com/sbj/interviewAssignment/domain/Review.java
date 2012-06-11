package com.sbj.interviewAssignment.domain;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Domain object for storing Review data.
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Movie_Reviews
 *  
 * @author mhelfer
 *
 */
public class Review implements RTDomain{
	private final String TAG = Review.class.getSimpleName();
	
	private String critic;
	private String publication;
	private String freshness;
	private String quote;
	private String fullReviewUrl;
	
	//Constructor from a JSONObject.
	public Review(JSONObject review){
		try{
			critic = review.getString(CRITIC);
			publication = review.getString(PUBLICATION);
			freshness = review.getString(FRESHNESS);
			quote = review.getString(QUOTE);
			fullReviewUrl = review.getJSONObject(LINKS).getString(REVIEW);
			
		} catch(JSONException jse){
			Log.e(TAG, "there was a problem creating a Review from the response" + jse.getMessage());
		}
	}

	public String getCritic() {
		return critic;
	}

	public void setCritic(String critic) {
		this.critic = critic;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}
	
	public String getFreshness() {
		return freshness;
	}

	public void setFreshness(String freshness) {
		this.freshness = freshness;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getFullReviewUrl() {
		return fullReviewUrl;
	}

	public void setFullReviewUrl(String fullReviewUrl) {
		this.fullReviewUrl = fullReviewUrl;
	}
}
