package com.lcsmobileapps.team7.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lcsmobileapps.team7.NinjaActivity;
import com.lcsmobileapps.team7.R;
import com.lcsmobileapps.team7.factory.NinjaFactory;
import com.lcsmobileapps.team7.pojo.Ninja;
import com.lcsmobileapps.team7.util.ImageHelper;

public class GenericFragment extends Fragment{

	Ninja ninja;
	public Ninja getNinja() {
		return ninja;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.fragment_layout, container,false);
		setHasOptionsMenu(true);
		ninja = NinjaFactory.getNinja(getArguments().getInt(Ninja.ARGS_POSITION));
		
		
		ImageView imgView = (ImageView)view.findViewById(R.id.ninjaImage);
		ImageHelper.loadImage(imgView, ninja.getImageID(),getActivity());
		
		imgView.setScaleType(ScaleType.FIT_XY);
		imgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NinjaActivity mainActivity = (NinjaActivity)getActivity();
				mainActivity.getPlayer().startPlaying(ninja.getRandomSound());
			}
		});

		return view;
	};
	
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
//		inflater.inflate(R.menu.activity_pokemon, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int x = item.getItemId();
		switch (x) {
		case android.R.id.home: {
			NinjaActivity mainActivity = (NinjaActivity)getActivity();
			DrawerLayout drawerLayout = (DrawerLayout)mainActivity.findViewById(R.id.drawer_layout);
			if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
				drawerLayout.closeDrawers();
			}
			else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		}
		}
		return true;
	}
}
