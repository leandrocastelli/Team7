package com.lcsmobileapps.team7;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.lcsmobileapps.team7.adapter.DrawerAdapter;
import com.lcsmobileapps.team7.factory.NinjaFactory;
import com.lcsmobileapps.team7.filemanager.FileManager;
import com.lcsmobileapps.team7.filemanager.FileManager.Props;
import com.lcsmobileapps.team7.fragment.GenericFragment;
import com.lcsmobileapps.team7.pojo.Naruto;
import com.lcsmobileapps.team7.pojo.Ninja;
import com.lcsmobileapps.team7.service.MediaService;
import com.lcsmobileapps.team7.service.MediaService.LocalBinder;
import com.lcsmobileapps.team7.service.SoundPlayer;
import com.lcsmobileapps.team7.util.AppRater;
import com.lcsmobileapps.team7.util.ImageHelper;

public class NinjaActivity extends ActionBarActivity implements ServiceConnection {


	
	SectionsPagerAdapter mSectionsPagerAdapter;

	
	ViewPager mViewPager;
	
	static Context ctx;
	private SoundPlayer soundPlayer;
	private ActionBarDrawerToggle mDrawerToggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		bindService(new Intent(this,MediaService.class), this, Context.BIND_AUTO_CREATE);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
		
		int cacheSize;
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			cacheSize = maxMemory / 8;
		}
		else {
			cacheSize = maxMemory / 4;
		}
		
	
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		ImageHelper.mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
	
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				PagerTitleStrip pageTitle = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
				
				NinjaActivity ninjaActivity = (NinjaActivity)ctx;
				ListView listView = (ListView)ninjaActivity.findViewById(R.id.left_drawer);
				Ninja currentNinja = NinjaFactory.getNinja(currentItem);
				listView.setAdapter(new DrawerAdapter(ninjaActivity,currentNinja));
				pageTitle.setBackgroundColor(currentNinja.getColor());
				pageTitle.setTextColor(currentNinja.getTextColor());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		ListView listView = (ListView)findViewById(R.id.left_drawer);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Integer soundID = (Integer)parent.getItemAtPosition(position);
				getPlayer().startPlaying(soundID);
				
			}
			
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int currentItem = mViewPager.getCurrentItem();
			
				Ninja currentNinja = NinjaFactory.getNinja(currentItem);
				createDialog(currentNinja,position).show();
				return false;
			}
			
		});
		
		
		AdRequest adRequest = new AdRequest();
	//	adRequest.addTestDevice("5A873CD5069A96C1FCBBEB66EB7CBC5A");
//		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		AdView adView = (AdView)findViewById(R.id.ad);
		
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
		adRequest.setLocation(lastKnownLocation);
		adView.loadAd(adRequest);
		
	//	invalidateOptionsMenu();
		
		AppRater.app_launched(this);
		FileManager.map.put(0,Props.RINGTONE);
		FileManager.map.put(1,Props.NOTIFICATION);
		FileManager.map.put(2,Props.ALARM);
		FileManager.map.put(3,Props.SEND);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}
	@Override
	public void onStop() {
		super.onStop();
		boolean isBound = bindService(new Intent(this,MediaService.class), this, Context.BIND_AUTO_CREATE);
		if(isBound)
			unbindService(this);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		bindService(new Intent(this,MediaService.class), this, Context.BIND_AUTO_CREATE);
		
	}
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			Naruto naruto = new Naruto();
			NinjaActivity ninjaActivity = (NinjaActivity)ctx;
			ListView listView = (ListView)ninjaActivity.findViewById(R.id.left_drawer);
			
			listView.setAdapter(new DrawerAdapter(ninjaActivity,naruto));
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
//			if (list.isEmpty() || position >= list.size()) {
				
				fragment = new GenericFragment();
				Bundle args = new Bundle();
				args.putInt(Ninja.ARGS_POSITION, position);
				fragment.setArguments(args);
				
//			}
//			else {
//				fragment = list.get(position);
//			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			String[] titles = getResources().getStringArray(R.array.title_list);
			return titles[position];
			
		}
	}
	public void onServiceConnected(ComponentName arg0, IBinder service) {
		LocalBinder localBinder = (LocalBinder) service;
		soundPlayer = localBinder.getSoundPlayer();

	}
	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		soundPlayer = null;

	}
	public SoundPlayer getPlayer() {
		return soundPlayer;
	}
	
	public AlertDialog.Builder createDialog(final Ninja ninja,final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_set_title));

		builder.setNegativeButton(getString(R.string.cancel), new AlertDialog.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});

		List<String> listString = new ArrayList<String>(3);
		listString.add(getString(R.string.ringtone));
		listString.add(getString(R.string.notification));
		listString.add(getString(R.string.alarm));
		listString.add(getString(R.string.send));
		
		
		
		
		final ArrayAdapter<String>  adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, android.R.id.text1, listString);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				
					String path;
					boolean result = false;
					InputStream in = getResources().openRawResource(ninja.getSound()+position);
					Props selection = FileManager.map.get(arg1);
					path = FileManager.getInstance().copyFile(adapter.getContext(), selection, in,ninja,position);
					if((path.length()>0)) { //API Lvl 8 doesnt have isEmpty
						if (selection.equals(Props.SEND)) {
							Intent share = new Intent(Intent.ACTION_SEND);
							share.setType("audio/*");
							share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + path));
							startActivity(share);
						}
						result = FileManager.getInstance().setAs(path,selection, adapter.getContext());
					}
					
					if(result) {
						Toast.makeText(adapter.getContext(), adapter.getItem(arg1)+getString(R.string.set_success),Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(adapter.getContext(), adapter.getItem(arg1)+getString(R.string.set_fail),Toast.LENGTH_SHORT).show();
					}
				
				arg0.dismiss();

			}
		});
		return builder;
	}


}
