package com.sbj.interviewAssignment.domain;

/**
 * This class exists because of the decision to parse my own objects rather than 
 * objectify the whole response using a library like GSON or JacksonJSON
 * 
 * @author mhelfer
 *
 */
public interface RTDomain {
	
	//Shared
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String MPAA_RATING = "mpaa_rating";
	public static final String POSTERS = "posters";
	public static final String RATINGS = "ratings";
	public static final String CRITICS_SCORE = "critics_score";
	
	//BoxOfficeMovie
	public static final String MOVIES = "movies";
	public static final String THUMBNAIL = "thumbnail";
	
	//Cast
	public static final String ABRIDGED_CAST = "abridged_cast";
	public static final String NAME = "name";
	public static final String CHARACTERS = "characters";
	
	//MovieInfo
	public static final String PROFILE = "detailed";
	public static final String SYNOPSIS = "synopsis";
	public static final String RUNTIME = "runtime";
	public static final String LINKS = "links";
	public static final String ALTERNATE = "alternate";
	
	//Review
	public static final String FRESHNESS = "freshness";
	public static final String CRITIC = "critic";
	public static final String QUOTE = "quote";
	public static final String REVIEWS = "reviews";
	public static final String TOTAL = "total";
	public static final String PUBLICATION = "publication";
	public static final String REVIEW = "review";
	public static final String FRESH = "fresh";

}
