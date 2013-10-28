package com.lcsmobileapps.team7.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcsmobileapps.team7.R;
import com.lcsmobileapps.team7.pojo.Ninja;
import com.lcsmobileapps.team7.util.ImageHelper;

public class DrawerAdapter extends BaseAdapter {

	private Context ctx;
	private Ninja ninja;
	private class ViewHolder {
		ImageView imageView;
		TextView txtName;
		
	}
	public DrawerAdapter (Context ctx, Ninja ninja) {
		super();
		this.ctx = ctx;
		this.ninja = ninja;
		
	}
	@Override
	public int getCount() {
		return ninja.getNumSounds();
	}

	@Override
	public Object getItem(int position) {
		return ninja.getSound()+position;
		
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.txtName = (TextView)convertView.findViewById(R.id.sound_name);
			
			holder.imageView = (ImageView) convertView.findViewById(R.id.ninja_icon);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ImageHelper.loadImage(holder.imageView, ninja.getIcon(), ctx);
		
		holder.txtName.setText(ctx.getResources().getStringArray(ninja.getArray())[position]);
		
		return convertView;
	}
	
	

}
