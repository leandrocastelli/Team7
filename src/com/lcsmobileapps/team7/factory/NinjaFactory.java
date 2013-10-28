package com.lcsmobileapps.team7.factory;

import com.lcsmobileapps.team7.pojo.Kakashi;
import com.lcsmobileapps.team7.pojo.Naruto;
import com.lcsmobileapps.team7.pojo.Ninja;
import com.lcsmobileapps.team7.pojo.Sakura;
import com.lcsmobileapps.team7.pojo.Sasuke;



public class NinjaFactory {

	public static Ninja  getNinja(int position) {
		Ninja ninja = null;
		switch(position) {
			case 0: {
				ninja = new Naruto();
				
			}
			break;
			case 1: {
				ninja = new Sasuke();
				
			}
			break;
			case 2: {
				ninja = new Sakura();
				
			}
			break;
			
			
			case 3: {
				ninja = new Kakashi();
			}
			break;
		}
		
		return ninja;
	}
}
