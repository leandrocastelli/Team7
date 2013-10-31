package com.lcsmobileapps.team7.pojo;

import java.util.Random;



public abstract class Ninja {
	protected String name;
	protected int imageID;
	protected int soundID;
	protected int color;
	protected int textColor;
	protected String keyword;
	protected int numSounds;
	protected int icon;
	protected int array;
	
	public String getName() { 
		return name;
	}
	public int getImageID(){
		return imageID;
	}
	public int getSound(){
		return soundID;
	}
	public int getRandomSound(){
		Random r = new Random();
		return r.nextInt(numSounds) + soundID;
	}
	public int getColor(){
		return color;
	}
	public String getKeyword() { 
		return keyword;
	}
	
	public int getNumSounds() {
		return numSounds;
	}
	
	public int getIcon () {
		return icon;
	}
	public int getArray () {
		return array;
	}
	public int getTextColor () {
		return textColor;
	}
	
	public static final String ARGS_POSITION = "position";
	
}
