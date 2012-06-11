package com.sbj.interviewAssignment.requests;

import org.apache.commons.lang3.StringUtils;

/**
 * Used to construct a RottenTomatoes Review Request. 
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Movie_Reviews
 * 
 * @author mhelfer
 *
 */
public class RTReviewRequest implements RTRequest {
	
	private static final String RT_REVIEW_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies/%s/reviews.json";
	
	private final String apiKey;
	private final String movieId;
	private final String reviewType;
	private final String pageLimit;
	private final String page;
	private final String country;
	
	/**
	 * The builder pattern is used to properly manage optional parameters.
	 * 
	 * @author mhelfer
	 *
	 */
	public static class Builder {
		private final String apiKey;
		private final String movieId;
		
		private String reviewType = null;
		private String pageLimit = null;
		private String page = null;
		private String country = null;
		
		public Builder(String apiKey, String movieId) {
			this.apiKey = apiKey;
			this.movieId = movieId;
		}
		
		public Builder reviewType(String val) {
			reviewType = val;
			return this;
		}
		
		public Builder pageLimit(String val) {
			pageLimit = val;
			return this;
		}
		
		public Builder page(String val) {
			page = val;
			return this;
		}
		
		public Builder country(String val) {
			country = val;
			return this;
		}
		
		public RTReviewRequest build() {
			return new RTReviewRequest(this);
		}
	}
	
	/**
	 * Private constructor to prevent instantiation outside of the builder.
	 * 
	 * @param builder
	 */
	private RTReviewRequest(Builder builder) {
		this.apiKey = builder.apiKey;
		this.movieId = builder.movieId;
		this.reviewType = builder.reviewType;
		this.pageLimit = builder.pageLimit;
		this.page = builder.page;
		this.country = builder.country;
	}
	
	
	/**
	 * Constructs the proper URL for making a ReviewRequest
	 */
	@Override
	public String getQueryURL() {
		StringBuilder query = new StringBuilder();
		
		//There is probably a more generic way to do this with reflection.
		query.append(String.format(RT_REVIEW_URL, movieId));
		query.append("?");
		query.append("apiKey=");
		query.append(apiKey);
		
		if(StringUtils.isNotBlank(reviewType)) {
			query.append("&review_type=");
			query.append(reviewType);
		}
		
		if(StringUtils.isNotBlank(pageLimit)) {
			query.append("&page_limit=");
			query.append(pageLimit);
		}
		
		if(StringUtils.isNotBlank(page)) {
			query.append("&page=");
			query.append(page);
		}
		
		if(StringUtils.isNotBlank(country)) {
			query.append("&country=");
			query.append(country);
		}
		
		return query.toString();
	}

}
