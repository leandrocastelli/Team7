package com.lcsmobileapps.team7.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
		inflater.inflate(R.menu.ninja_menu, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemID = item.getItemId();
		switch (itemID) {
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
		break;
		case R.id.menu_wallpaper: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.title_wallpaper));
			builder.setMessage(R.string.msg_wallpaper);

			builder.setNegativeButton(getString(R.string.cancel), new AlertDialog.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});
			builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					ImageHelper.loadImageForWallpaper(ninja.getImageID(), getActivity());

				}
			});
			builder.show();
			
		}
		break;
		}
		return true;
	}
}
