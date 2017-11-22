package app.com.addshopcar;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @author ddy
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(getApplicationContext());
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }
}
