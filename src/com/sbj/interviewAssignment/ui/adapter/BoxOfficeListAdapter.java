package com.sbj.interviewAssignment.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sbj.interviewAssignment.R;
import com.sbj.interviewAssignment.domain.BoxOfficeMovie;

/**
 * Custom list adapter for displaying box office movie data.
 * 
 * See /layout/boxoffice.xml for specific cell layout.
 * 
 * @author mhelfer
 *
 */
public class BoxOfficeListAdapter extends BaseAdapter{
	private static final String TAG = BoxOfficeListAdapter.class.getSimpleName();
	
	private Activity context;
	private List<BoxOfficeMovie> data;
	
	//Constructor takes in an Activity so we can attach views to it.
	public BoxOfficeListAdapter(Activity context){
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
	
	public List<BoxOfficeMovie> getData(){
		return data;
	}
	
	public void setData(List<BoxOfficeMovie> data){
		this.data = data;
	}
	
	/**
	 * Constructs a cell for use in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Cache the view for reuse.
		
		BoxOfficeMovie item = data.get(position);
			
		//Inflate an instance of the boxOffice layout.
		convertView = context.getLayoutInflater().inflate(R.layout.boxoffice, parent, false);
		
		ImageView poster = (ImageView)convertView.findViewById(R.id.boxOfficePoster);
		
		if(item.getBitmap() == null){
			poster.setImageResource(R.drawable.notavailable_thumb);
		}
		else {
			poster.setImageBitmap(item.getBitmap());
			poster.setContentDescription(item.getTitle() + " " + R.string.boxOfficePoster);
		}
		
		ImageView rating = (ImageView)convertView.findViewById(R.id.boxOfficeRating);
		rating.setImageResource(BoxOfficeMovie.ratingImageMap.get(item.getRating()));
		rating.setContentDescription(item.getRating());
		
		TextView title = (TextView) convertView.findViewById(R.id.boxOfficeName);
		title.setText(item.getTitle());
		
		ProgressBar freshness = (ProgressBar) convertView.findViewById(R.id.boxOfficeFreshness);
		freshness.setProgress(item.getFreshness());
		
		convertView.setBackgroundColor((position % 2 == 0) ? Color.WHITE : Color.parseColor("#F2F2F2"));
		
		return convertView;	
	}

}
