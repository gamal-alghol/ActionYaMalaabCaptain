package com.malaab.ya.action.actionyamalaab;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.malaab.ya.action.actionyamalaab.di.component.ApplicationComponent;
import com.malaab.ya.action.actionyamalaab.di.component.DaggerApplicationComponent;
import com.malaab.ya.action.actionyamalaab.di.module.ApplicationModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import javax.inject.Inject;
public class AppController extends Application {
    private static AppController mInstance;
    private ApplicationComponent mApplicationComponent;
    @Inject
    CalligraphyConfig mCalligraphyConfig;
    @Override
    public void onCreate() {
        super.onCreate();
  // Fabric.with(this, new Crashlytics());
     //   Fabric.with(this, new Answers());
        // TODO: 31-Oct-17 remove this for release version
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
 //        }
   //        LeakCanary.install(this);
        //     mInstance = this;
       mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
//   mApplicationComponent.inject(this);
    //    TheActivityManager.getInstance().setLogEnabled(true);
   //     AppLogger.init();
//        Traceur.enableLogging();
    //    CalligraphyConfig.initDefault(mCalligraphyConfig);
    /*    EasyImage.configuration(this)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)    // because we are saving the compressed the image anyway
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false); // allows multiple picking in galleries that handle it. Also only for phones with API 18+
                                */                       // but it won't crash lower APIs. False by default
    }
/*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, Constants.LANGUAGE_ARABIC_CODE));
//        super.attachBaseContext(base);
        MultiDex.install(this);
    }
/*
    public static synchronized AppController getInstance() {
        return mInstance;
    }*/


    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
/*
    // Needed to replace the component with a layout_summary specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
*/
}