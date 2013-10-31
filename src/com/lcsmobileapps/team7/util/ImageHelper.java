package com.lcsmobileapps.team7.util;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageHelper extends AsyncTask<Integer,Void,Bitmap>{
	private final WeakReference<ImageView> imageViewReference;
	private int data = 0;
	private Context ctx;
	public static LruCache<String, Bitmap> mMemoryCache;
	public ImageHelper(ImageView imageView, Context ctx) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.ctx = ctx;
	}

	// Decode image in background.
	@Override
	protected Bitmap doInBackground(Integer... params) {
		data = params[0];
		

		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width;
		int heigth;
		if (android.os.Build.VERSION.SDK_INT >=13) {
			Point size =new Point();
			display.getSize(size);
			width = size.x;
			heigth = size.y;
		}
		else{
			width = display.getWidth();
			heigth = display.getHeight();
		}
		if (params.length > 1)
			width = width <<1;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(ctx.getResources(), data, options);

		options.inSampleSize = calculateInSampleSize(options, width, heigth);

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), data,options);
		if(params.length ==1)
			addBitmapToMemoryCache(String.valueOf(data), bitmap);
		return bitmap;
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
			else {
				//Wallpaper
				WallpaperManager wm = WallpaperManager.getInstance(ctx);
				try {
					wm.setBitmap(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	    	
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	public static void loadImage(ImageView imgView, int id, Context ctx) {
		final String imageKey = String.valueOf(id);
		
		
		ImageHelper imageHelper = new ImageHelper(imgView, ctx);
		final Bitmap bitmap = imageHelper.getBitmapFromMemCache(imageKey);
		
		if (bitmap != null) {
			imgView.setImageBitmap(bitmap);
	    }
		else {
			imageHelper.execute(id);
		}
		
	}
	public static void loadImageForWallpaper(int id, Context ctx) {
		final String imageKey = String.valueOf(id);
		
		
		ImageHelper imageHelper = new ImageHelper(null, ctx);
		
		imageHelper.execute(id,1);
		
		
	}
}

