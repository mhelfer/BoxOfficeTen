package com.sbj.interviewAssignment.ui.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sbj.interviewAssignment.R;
import com.sbj.interviewAssignment.domain.BoxOfficeMovie;
import com.sbj.interviewAssignment.requests.RTMoviePosterRequest;

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
	
	public void setData(List<BoxOfficeMovie> data){
		this.data = data;
	}
	
	/**
	 * Constructs a cell for use in the list.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Cache the view for reuse.
		ViewHolder holder;
		BoxOfficeMovie item = data.get(position);
		
		//if the view hasn't already been populated.
		if(convertView == null){
			
			//Inflate an instance of the boxOffice layout.
			convertView = context.getLayoutInflater().inflate(R.layout.boxoffice, parent, false);
			
			holder = new ViewHolder();
			holder.poster = (ImageView)convertView.findViewById(R.id.boxOfficePoster);
			
			try{
				//Retrieve the bitmap image from the URL.
				Bitmap posterImage = new RTMoviePosterRequest().execute(item.getPoster()).get();
				holder.poster.setImageBitmap(posterImage);
			} catch(ExecutionException ee){
				Log.e(TAG, "There was a problem retrieving the movie poster " + ee.getMessage());
				holder.poster.setImageResource(R.drawable.notavailable_thumb);
			} catch(InterruptedException ie){
				holder.poster.setImageResource(R.drawable.notavailable_thumb);
				Log.e(TAG, "There was a problem retrieving the movie poster " + ie.getMessage());
				//propigate the interuption
				Thread.currentThread().interrupt();
			}
			
			holder.rating = (ImageView)convertView.findViewById(R.id.boxOfficeRating);
			holder.rating.setImageResource(BoxOfficeMovie.ratingImageMap.get(item.getRating()));
			
			holder.title = (TextView) convertView.findViewById(R.id.boxOfficeName);
			holder.title.setText(item.getTitle());
			
			holder.freshness = (ProgressBar) convertView.findViewById(R.id.boxOfficeFreshness);
			holder.freshness.setProgress(item.getFreshness());
			
			convertView.setBackgroundColor((position % 2 == 0) ? Color.WHITE : Color.parseColor("#F2F2F2"));
			
			convertView.setTag(holder);
			
		}
		//Use existing view.
		else{
			holder = (ViewHolder)convertView.getTag();
		}	
		
		return convertView;	
	}
	
	protected static class ViewHolder {
		protected ImageView poster;
		protected ImageView rating;
		protected TextView title;
		protected ProgressBar freshness;
	}

}
