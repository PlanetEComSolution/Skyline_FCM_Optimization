package planet.info.skyline.controller;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.fabric.sdk.android.Fabric;
import planet.info.skyline.BuildConfig;
/*import planet.info.skyline.httpimage.FileSystemPersistence;
import planet.info.skyline.httpimage.HttpImageManager;
import planet.info.skyline.util.LruBitmapCache;*/

//import planet.info.skyline.util.LruBitmapCache;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    public static final String BASEDIR = "/sdcard/httpimage";
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static AppController mInstance;
    private static Context mcontext;
    private static boolean activityVisible;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
//    private HttpImageManager mHttpImageManager;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mcontext;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        //ImageLoader.getInstance().init(config);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mInstance = this;
        mcontext = getApplicationContext();
        initImageLoader(getApplicationContext());
        //registerForNetworkChangeEvents(this);

        /*********this will report both debug and release issues*/

//	final Fabric fabric = new Fabric.Builder(this)
//			.kits(new Crashlytics())
//			.debuggable(true)           // Enables Crashlytics debugger
//			.build();
//	Fabric.with(fabric);

        /****************this will report only release issues************/

     /*   Fabric.with(this, new Crashlytics.Builder().
                core(new CrashlyticsCore.Builder().
                        disabled(BuildConfig.DEBUG).
                        build()).build());*/

        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());
        // init HttpImageManager manager.
     /*   mHttpImageManager = new HttpImageManager(HttpImageManager.createDefaultMemoryCache(),
                new FileSystemPersistence(BASEDIR));*/

    }

    public RequestQueue getRequestQue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;

    }

 /*   public HttpImageManager getHttpImageManager() {
        return mHttpImageManager;
    }

    public ImageLoader getImageloader() {
        getRequestQue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }*/

    public <T> void addToRequestQueue(com.android.volley.Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQue().add(req);

    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> req) {
        req.setTag(TAG);
        getRequestQue().add(req);

    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
	/*public static void registerForNetworkChangeEvents(final Context context) {
		NetworkStateChangeReceiver networkStateChangeReceiver = new NetworkStateChangeReceiver();
		context.registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
		context.registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
	}*/
}
