package planet.info.skyline.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class LruBitmapCache extends LruCache< String, Bitmap> implements ImageCache {

	public static int getDefaultLruCacheSize(){
	final	int maxmemory=(int)(Runtime.getRuntime().maxMemory()/1024);
	final int cachesizw=maxmemory/8;
		return cachesizw;
		
	}
	public LruBitmapCache(){
		this(getDefaultLruCacheSize());
	}
	public LruBitmapCache(int maxSize) {
		super(maxSize);
	
	}

	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
	return	get(url);
	
	}

	@Override
	public void putBitmap(String url, Bitmap value) {
		// TODO Auto-generated method stub
		put(url, value);
	}
	@Override
	protected int sizeOf(String key, Bitmap value) {
		// TODO Auto-generated method stub
	return	value.getRowBytes()*value.getHeight()/1024;
		//return super.sizeOf(key, value);
	}

}
