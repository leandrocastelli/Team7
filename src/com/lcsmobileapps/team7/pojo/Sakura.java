package com.lcsmobileapps.team7.pojo;

import android.graphics.Color;

import com.lcsmobileapps.team7.R;



public class Sakura extends Ninja{
	public Sakura() {
		name = "Sakura";
		imageID = R.drawable.sakura;
		soundID = R.raw.sakura_punch;
		icon = R.drawable.ic_sakura_icon;
		color = Color.MAGENTA;
		keyword = "games";
		numSounds = 1;
		array = R.array.sakura_list;
	}
}
