package com.sbj.interviewAssignment.requests;

import org.apache.commons.lang3.StringUtils;

/**
 * Used to construct a RottenTomatoes boxOfficeRequset. 
 * 
 * See more at: http://developer.rottentomatoes.com/docs/read/json/v10/Box_Office_Movies 
 * 
 * @author mhelfer
 *
 */
public class RTBoxOfficeRequest implements RTRequest{
	
	private static final String RT_BO_URL = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
	
	private final String country;
	private final String limit;
	private final String apiKey;
	
	
	/**
	 * The builder pattern is used to properly manage optional parameters.
	 * 
	 * @author mhelfer
	 *
	 */
	public static class Builder {
		private final String apiKey;
		
		private String country = "us";
		private String limit = "10";
		
		public Builder(String apiKey){
			this.apiKey = apiKey;
		}
		
		public Builder country(String val){
			country = val;
			return this;
		}
		
		public Builder limit(String val){
			limit = val;
			return this;
		}
		
		public RTBoxOfficeRequest build() {
			return new RTBoxOfficeRequest(this);
		}
	}
	
	/**
	 * Private constructor to prevent instantiation outside of the builder.
	 * 
	 * @param builder
	 */
	private RTBoxOfficeRequest(Builder builder){
		country = builder.country;
		limit = builder.limit;
		apiKey = builder.apiKey;
	}
	
	/**
	 * Constructs the proper URL for making a BoxOfficeRequest
	 */
	@Override
	public String getQueryURL(){
		StringBuilder query = new StringBuilder();
		
		query.append(RT_BO_URL);
		query.append("?");
		
		if(StringUtils.isNotBlank(country)){
			query.append("country=");	
			query.append(country);
		}
		
		if(StringUtils.isNotBlank(limit)){
			query.append("&limit=");
			query.append(limit);
		}
		
		if(StringUtils.isNotBlank(apiKey)){
			query.append("&apiKey=");
			query.append(apiKey);
		}
		
		return query.toString();
	}
	
	
}
