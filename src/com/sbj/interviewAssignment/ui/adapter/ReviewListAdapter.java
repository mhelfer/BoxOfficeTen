package com.sbj.interviewAssignment.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbj.interviewAssignment.R;
import com.sbj.interviewAssignment.domain.Review;

/**
 * Custom list adapter for displaying Review data.
 * 
 * See /layout/review.xml for specific cell layout.
 * 
 * @author mhelfer
 *
 */
public class ReviewListAdapter extends BaseAdapter{
	
	private Activity context;
	private List<Review> data;

	public ReviewListAdapter(Activity context){
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return (data == null) ? 0 : data.size();
	}
	
	@Override
	public Object getItem(int position) {
		return (data == null) ? null : data.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<Review> getData(){
		return data;
	}
	
	public void setData(List<Review> data){
		this.data = data;
	}
	
	public void addData(List<Review> moreData){
		if(data == null){
			data = new ArrayList<Review>();
		}
		data.addAll(moreData);
	}
	
	/**
	 * Constructs a cell for use in the list.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	Review item = data.get(position);
    	
    	//Inflate an instance of the review layout.
		convertView = context.getLayoutInflater().inflate(R.layout.review, parent, false);
		
		ImageView freshness = (ImageView)convertView.findViewById(R.id.reviewFreshness);
		freshness.setImageResource(item.getFreshness().equals(Review.FRESH) ? R.drawable.fresh : R.drawable.rotten);
		
		TextView name = (TextView)convertView.findViewById(R.id.reviewName);
		name.setText(item.getCritic() + " - " + item.getPublication());
		
		TextView quote = (TextView)convertView.findViewById(R.id.reviewQuote);
		quote.setText(item.getQuote());
		
		
		TextView reviewUrl = (TextView)convertView.findViewById(R.id.reviewUrl);
		reviewUrl.setText(item.getFullReviewUrl());
		//Scan the text for URL's and make the clickable.
		Linkify.addLinks(reviewUrl, Linkify.WEB_URLS);
		
		convertView.setBackgroundColor((position % 2 == 0) ? Color.WHITE : Color.parseColor("#F2F2F2"));
			
        return convertView;
    }
}
