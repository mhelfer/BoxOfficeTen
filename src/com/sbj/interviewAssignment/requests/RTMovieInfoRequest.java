package com.sbj.interviewAssignment.requests;

/**
 * Used to construct a RottenTomatoes MovieInfo
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Movies_Search
 * 
 * @author mhelfer
 *
 */
public class RTMovieInfoRequest implements RTRequest {
	
	private static final String RT_BO_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies/%s.json";

	private final String apiKey;
	private final String movieId;
	
	/**
	 * The builder pattern is used to properly manage optional parameters.
	 * 
	 * @author mhelfer
	 *
	 */
	public static class Builder {
		private final String apiKey;
		private final String movieId;
		
		public Builder(String apiKey, String movieId) {
			this.apiKey = apiKey;
			this.movieId = movieId;
		}
		
		public RTMovieInfoRequest build() {
			return new RTMovieInfoRequest(this);
		}
	}
	
	/**
	 * Private constructor to prevent instantiation outside of the builder.
	 * 
	 * @param builder
	 */
	private RTMovieInfoRequest(Builder builder) {
		this.apiKey = builder.apiKey;
		this.movieId = builder.movieId;
	}
	
	/**
	 * Constructs the proper URL for making a MovieInfoRequest
	 */
	@Override
	public String getQueryURL() {
		StringBuilder query = new StringBuilder();
		
		query.append(String.format(RT_BO_URL, movieId));
		query.append("?apiKey=");
		query.append(apiKey);
		
		return query.toString();
	}
	
	
	
}
