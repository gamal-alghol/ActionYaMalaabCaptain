package com.malaab.ya.action.actionyamalaab.ui.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.PermissionsCodes;
import com.malaab.ya.action.actionyamalaab.custom.BSheetBook;
import com.malaab.ya.action.actionyamalaab.custom.CustomMapView;
import com.malaab.ya.action.actionyamalaab.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.model.Location;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.PermissionsUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.providers.AndroidGeocodingProvider;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import io.nlopez.smartlocation.utils.LoggerFactory;
import io.nlopez.smartlocation.utils.ServiceConnectionListener;

import static io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS;


public class MapActivity extends BaseActivity implements MapMvpView, OnBottomSheetListener, OnLocationUpdatedListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.mCustomMapView)
    public CustomMapView mCustomMapView;

    @BindView(R.id.btn_continue)
    public Button btn_continue;
    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    MapMvpPresenter<MapMvpView> mPresenter;

    private LinearLayoutManager layoutManager;
    private PlaygroundsAdapter adapter;

    private User mUser;
    private Playground mPlayground;
    private double lat, lng;

    private boolean isShowPlaygroundLocation;
    private boolean isToSelectUserLocation;

    private LocationGooglePlayServicesProvider provider;
    private static final LocationAccuracy ACCURACY_HIGH = LocationAccuracy.HIGH;    //HIGH
    private static final long INTERVAL_HIGH = 1 * 1000;                               //1s
    private static final float DISTANCE_HIGH = 1f;                                  //1m
    private String imgUrl;
    private BSheetBook mBSheetBook;
    android.location.Location userLocation;
    private SmartLocation smartLocation;

//    protected GoogleApiClient mGoogleApiClient;
//    protected LocationRequest locationRequest;
//    private int REQUEST_CHECK_SETTINGS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.INTENT_KEY_PLAYGROUND)) {
                mPlayground = getIntent().getParcelableExtra(Constants.INTENT_KEY_PLAYGROUND);
                isShowPlaygroundLocation = true;
            }

            if (getIntent().hasExtra(Constants.INTENT_KEY_IS_FIRST_TIME)) {
                isToSelectUserLocation = getIntent().getBooleanExtra(Constants.INTENT_KEY_IS_FIRST_TIME, false);
                pBar_loading.setVisibility(View.VISIBLE);
            }
        }

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);
        provider.setLocationSettingsAlwaysShow(true);
        provider.setServiceListener(new ServiceConnectionListener() {
            @Override
            public void onConnected() {
                AppLogger.w("onConnected");
            }

            @Override
            public void onConnectionSuspended() {
                AppLogger.w("onConnectionSuspended");
            }

            @Override
            public void onConnectionFailed() {
                AppLogger.w("onConnectionFailed");
            }
        });
//        provider.init(this, LoggerFactory.buildLogger(true));

        LocationParams locationParams = new LocationParams.Builder()
                .setAccuracy(ACCURACY_HIGH)
                .setInterval(INTERVAL_HIGH)
                .setDistance(DISTANCE_HIGH)
                .build();

        smartLocation = new SmartLocation.Builder(this).logging(true).build();
        smartLocation.location(provider)
                .config(locationParams)
//                    .config(LocationParams.NAVIGATION)
//                .continuous()
                .oneFix()
                .start(this);

        mPresenter.getCurrentUserLocal();
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */

        if (isShowPlaygroundLocation) {
            header_txt_title.setText(mPlayground.name);
        } else if (isToSelectUserLocation) {
            header_txt_title.setText(R.string.title_change_location);
        }

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(layoutManager);

        mBSheetBook = new BSheetBook();
        mBSheetBook.attachAndInit(this);
        mBSheetBook.setOnBottomSheetListener(this);

//        mCustomMapView.addMarker(new Location("ttt", "asdasd", 3.046003, 101.706252));
//        mCustomMapView.addMarker(new Location("vvv", "vsvv", 3.043486, 101.704765));

//        mCompositeDisposable.add(
//                mCustomMapView.getMapReadyObservable()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<GoogleMap>() {
//                            @Override
//                            public void accept(GoogleMap googleMap) throws Exception {
////                                CircleOptions circleOptions = new CircleOptions()
////                                        .center(new LatLng(3.046003, 101.706252))
////                                        .radius(100000)
////                                        .clickable(true);
////                                googleMap.addCircle(circleOptions);
//
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//
//                            }
//                        })
//        );
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_continue)
    public void goContinue() {
        if (mCustomMapView != null && mCustomMapView.getSelectedLocation() == null) {
//            onError(R.string.error_user_owner);
            mPresenter.updateCurrentUserLocation("", "", 0, 0);

        } else {
//            android.location.Location userLocation = new android.location.Location("User Current location");
//            userLocation.setLatitude(lat);
//            userLocation.setLongitude(lng);
//
//            SmartLocation.with(this).geocoding()
//                    .reverse(userLocation, new OnReverseGeocodingListener() {
//                        @Override
//                        public void onAddressResolved(android.location.Location location, List<Address> list) {
//                            String city = list.get(0).getLocality();
//                        }
//                    });

            if (mCustomMapView != null) {
//                final android.location.Location userLocation = new android.location.Location("User Current location");
                final android.location.Location userLocation = new android.location.Location("User Current location");
                userLocation.setLatitude(mCustomMapView.getSelectedLocation().latitude);
                userLocation.setLongitude(mCustomMapView.getSelectedLocation().longitude);

//                userLocation.setLatitude(21.505239);
//                userLocation.setLongitude(39.215379);

                final Location selectedLocation = mCustomMapView.getSelectedLocation();

                Locale locale = new Locale("ar");
                final AndroidGeocodingProvider provider = new AndroidGeocodingProvider(locale);
                smartLocation
                        .geocoding(provider)
                        .reverse(userLocation, new OnReverseGeocodingListener() {
                            @Override
                            public void onAddressResolved(android.location.Location location, List<Address> list) {
                                if (ListUtils.isEmpty(list)) {
                                    return;
                                }

                                String city = list.get(0).getLocality();
                                String region = list.get(0).getAdminArea();

                                mPresenter.updateCurrentUserLocation(city, region, selectedLocation.latitude, selectedLocation.longitude);
//                                mPresenter.updateCurrentUserLocation(city, region, userLocation.getLatitude(), userLocation.getLongitude());
                            }
                        });
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AppLogger.w( "requestCode= " + requestCode + " | resultCode= " + resultCode + " | data= " + data.getData());

        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
Log.d("ttt","RESULT_OK");
            } else {
                Log.d("ttt","RESULT_nO");

                pBar_loading.setVisibility(View.GONE);
                btn_continue.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), getString(R.string.msg_gps_error), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onLocationUpdated(android.location.Location location) {
       Log.d("ttt","onLocationUpdated");

        pBar_loading.setVisibility(View.GONE);

        if (mCustomMapView != null) {
            mCustomMapView.clearMarkers();
        }

        if (location == null) {
            if (mUser.latitude != 0 && mUser.longitude != 0) {
                if (mCustomMapView != null) {
                    mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", mUser.latitude, mUser.longitude, mUser.profileImageUrl), mPlayground == null);
                }
            } else {
                showMessage("Could not get your location!");
            }
            return;
        }

        AppLogger.w("onLocationUpdated location= " + location.getLatitude() + " | " + location.getLongitude());
        lat = location.getLatitude();
        lng = location.getLongitude();

        if (lat != 0 && lng != 0) {
            if (mCustomMapView != null) {
                mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", lat, lng, mUser.profileImageUrl), mPlayground == null);
            }
        } else {
            if (mUser.latitude != 0 && mUser.longitude != 0) {
                if (mCustomMapView != null) {
                    mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", mUser.latitude, mUser.longitude, mUser.profileImageUrl), mPlayground == null);
                }
            }
        }

        if (isToSelectUserLocation) {
            btn_continue.setVisibility(View.VISIBLE);

            if (mCustomMapView != null) {
                mCustomMapView.enableOnMapListener(mUser);
            }

        } else {
            if (isShowPlaygroundLocation) {
                 imgUrl = "";
                if (!ListUtils.isEmpty(mPlayground.images)) {

                    imgUrl = mPlayground.images.get(0);
                    Log.d("ttt",imgUrl);
                }else{
                    imgUrl = "";

                }

                if (mCustomMapView != null) {

                    mCustomMapView.addMarker(
                            new Location(mPlayground.name, mPlayground.address_region + "-" + mPlayground.address_city + "-" + mPlayground.address_direction,
                                    mPlayground.latitude, mPlayground.longitude, imgUrl)
                            ,
                            true);

                }
            } else {
                android.location.Location userLocation = new android.location.Location("User Current location");
                userLocation.setLatitude(lat);
                userLocation.setLongitude(lng);
                if(getIntent().hasExtra("activity")){
if(getIntent().getIntExtra("activity",0)==0){
    mPresenter.getPlaygrounds(userLocation);
    header_txt_title.setText(R.string.full);

}else if(getIntent().getIntExtra("activity",1)==1){
    mPresenter.getExercises(userLocation);
    header_txt_title.setText(R.string.individual);

}
                }

            }
        }
    }


    @PermissionYes(PermissionsCodes.LOCATION)
    private void gotGPSPermission(@NonNull List<String> grantedPermissions) {
        pBar_loading.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);

//        mGoogleApiClient.connect();

//        if (!smartLocation.location().state().locationServicesEnabled()) {
//            mGoogleApiClient.connect();
//
////            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
////            builder.setMessage(getString(R.string.msg_gps_enable))
////                    .setCancelable(false)
////                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                        public void onClick(final DialogInterface dialog, final int id) {
////                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
////                        }
////                    })
////                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
////                        public void onClick(final DialogInterface dialog, final int id) {
////
////                            pBar_loading.setVisibility(View.GONE);
////                            btn_continue.setVisibility(View.VISIBLE);
////
////                            if (mUser.latitude != 0 && mUser.longitude != 0) {
////                                if (mCustomMapView != null) {
////                                    mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", mUser.latitude, mUser.longitude, mUser.profileImageUrl), mPlayground == null);
////                                }
////                            }
////
////                            dialog.cancel();
////                        }
////                    });
////
////            AlertDialog alert = builder.create();
////            alert.show();
////
//            return;
//        }

//                .oneFix()
//                .start(new OnLocationUpdatedListener() {
//                    @Override
//                    public void onLocationUpdated(android.location.Location location) {
//                        if (mCustomMapView != null) {
//                            mCustomMapView.clearMarkers();
//                        }
//
//                        if (location == null) {
//                            if (mUser.latitude != 0 && mUser.longitude != 0) {
//                                if (mCustomMapView != null) {
//                                    mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", mUser.latitude, mUser.longitude, mUser.profileImageUrl), mPlayground == null);
//                                }
//                            } else {
//                                showMessage("Could not get your location!");
//                            }
//                            return;
//                        }
//
//                        lat = location.getLatitude();
//                        lng = location.getLongitude();
//
//                        if (lat != 0 && lng != 0) {
//                            if (mCustomMapView != null) {
//                                mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", lat, lng, mUser.profileImageUrl), mPlayground == null);
//                            }
//                        } else {
//                            if (mUser.latitude != 0 && mUser.longitude != 0) {
//                                if (mCustomMapView != null) {
//                                    mCustomMapView.addMarker(new Location(mUser.fName, "Your Location", mUser.latitude, mUser.longitude, mUser.profileImageUrl), mPlayground == null);
//                                }
//                            }
//                        }
//
//                        if (!isToSelectUserLocation) {
//                            if (isOnePlayground) {
//
//                                String imgUrl = "";
//                                if (!ListUtils.isEmpty(mPlayground.images)) {
//                                    imgUrl = mPlayground.images.get(0);
//                                }
//
//                                if (mCustomMapView != null) {
//                                    mCustomMapView.addMarker(new Location(mPlayground.name, mPlayground.address_region + "-" + mPlayground.address_city + "-" + mPlayground.address_direction,
//                                                    mPlayground.latitude, mPlayground.longitude, imgUrl)
//                                            , true);
//                                }
//                            } else {
//                                android.location.Location userLocation = new android.location.Location("User Current location");
//                                userLocation.setLatitude(lat);
//                                userLocation.setLongitude(lng);
//
//                                mPresenter.getPlaygrounds(userLocation);
//                            }
//                        }
//
//                        pBar_loading.setVisibility(View.GONE);
//                        btn_continue.setVisibility(View.VISIBLE);
//                    }
//                });
    }

    @PermissionNo(PermissionsCodes.LOCATION)
    private void noGPSPermission(@NonNull List<String> deniedPermissions) {
        Log.d("ttt","noGPSPermission");
        pBar_loading.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);

        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            PermissionsUtils.showAlwaysDeniedCustomDialog(this);
        }
    }


    @Override
    public void onGetCurrentUser(User user) {
        if (user != null) {

            mUser = user;
//            lat = mUser.latitude;
//            lng = mUser.loggedInMode;
            AppLogger.w("onGetCurrentUser");
           // PermissionsUtils.requestPermission(this, PermissionsCodes.LOCATION, Permission.LOCATION);
Log.d("ttt","PermissionsUtils");
        }
/*
    if (!isToSelectUserLocation) {
        if (isOnePlayground) {
            String imgUrl = "";
            if (!ListUtils.isEmpty(mPlayground.images)) {
                  imgUrl = mPlayground.images.get(0);
              }

                mCustomMapView.addMarker(new Location(mPlayground.name, mPlayground.address_region + "-" + mPlayground.address_city + "-" + mPlayground.address_direction,
                            mPlayground.latitude, mPlayground.longitude, imgUrl)
                        , true);
            } else {
             userLocation = new android.location.Location("User Current location");
        }
               userLocation.setLatitude(lat);
                userLocation.setLongitude(lng);

               mPresenter.getPlaygrounds(userLocation);
           }*/
   }



    @Override
    public void onUpdateCurrentUserLocation() {
        EventBus.getDefault().post(new OnEventRefresh());
        ActivityUtils.goTo(MapActivity.this, HomeActivity.class, true);
    }


    @Override
    public void onGetPlayground(List<Playground> playgrounds) {
        isShowPlaygroundLocation = false;

         imgUrl = "";

        for (Playground playground : playgrounds) {

            if (!ListUtils.isEmpty(playground.images)) {
                imgUrl = playground.images.get(0);
            }else{
                 imgUrl = "";

            }

            if (mCustomMapView != null) {
                mCustomMapView.addMarker(new Location(playground.name, playground.address_region + "-" + playground.address_city + "-" + playground.address_direction,
                        playground.latitude, playground.longitude, imgUrl), false);
            }
        }

        adapter = new PlaygroundsAdapter(playgrounds);
        adapter.registerListener(new PlaygroundsAdapter.OnPlaygroundClicked() {
            @Override
            public void setOnPlaygroundClicked(Playground playground, int position) {
                rv_items.smoothScrollToPosition(position);
                EventBus.getDefault().post(new OnEventItemClicked<>(playground, ItemAction.DETAILS, position));
            }
        });

        rv_items.setAdapter(adapter);
        rv_items.setVisibility(View.VISIBLE);

        SnapHelper snapHelper = new LinearSnapHelper();
        rv_items.setOnFlingListener(null);              /* remove this will cause an error 'An instance of OnFlingListener already set' */
        snapHelper.attachToRecyclerView(rv_items);

        rv_items.setOnFlingListener(snapHelper);

        rv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        AppLogger.d("onScrollStateChanged -> The RecyclerView is not scrolling");

                        if (layoutManager.findLastCompletelyVisibleItemPosition() >= 0) {
                            mPlayground = adapter.getItem(layoutManager.findLastCompletelyVisibleItemPosition());
                            if (mPlayground != null) {
                                if (mCustomMapView != null) {
                                    mCustomMapView.focusOnMarker(mPlayground.latitude, mPlayground.longitude);
                                }
                            }
                        }
                        break;

                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        AppLogger.d("onScrollStateChanged -> Scrolling now");
                        break;

                    case RecyclerView.SCROLL_STATE_SETTLING:
                        AppLogger.d("onScrollStateChanged -> Scrolling Settling");
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        android.location.Location location = new android.location.Location("User Current location");
        location.setLatitude(lat);
        location.setLongitude(lng);

        mPresenter.getNearestPlayground(playgrounds, location);
    }

    @Override
    public void onGetNearestPlayground(Playground playground, android.location.Location userCurrentLocation) {
        AppLogger.d("Nearest Playground = " + playground.name);
        if (mCustomMapView != null) {
            mCustomMapView.zoomMapInitial(playground, userCurrentLocation);
//        mCustomMapView.focusOnMarker(playground.latitude, playground.longitude);
        }
    }


    @Override
    public void onPlaygroundInFavouriteList(boolean isFavourite) {
        mBSheetBook.setFavourite(isFavourite);
    }

    @Override
    public void onAddPlaygroundToFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(true);
    }

    @Override
    public void onRemovePlaygroundFromFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(false);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    public void onSlide(float slideOffset) {

    }

    @Override
    public void onBottomSheetExpanded() {

    }

    @Override
    public void onBottomSheetCollapsed(String... args) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof MapActivity) {

            Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof Playground) {
                if (action == ItemAction.DETAILS) {
                    mBSheetBook.show((Playground) item, mUser);
                    mPresenter.isPlaygroundInFavouriteList((Playground) item);

                    //                mBSheetFragmentBook.setPlayground((Playground) item);
//                mBSheetFragmentBook.show(getSupportFragmentManager(), "BSheetFragmentShare");

                } else if (action == ItemAction.LOCATION) {
                    mBSheetBook.onBackPressed();

                } else if (action == ItemAction.FAVOURITE_ADD) {
                    mPresenter.addPlaygroundToFavouriteList((Playground) item);

                } else if (action == ItemAction.FAVOURITE_REMOVE) {
                    mPresenter.removePlaygroundFromFavouriteList((Playground) item);

                }

            } else if (item instanceof CalendarDay && action == ItemAction.PICK) {
                mBSheetBook.setDaySelected(((CalendarDay) item), event.getPosition());

            } else if (item instanceof CalendarTime && action == ItemAction.PICK) {
                mBSheetBook.setTimeSelected((CalendarTime) event.getItem(), event.getPosition());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onBackPressed() {
        if (mBSheetBook.onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        smartLocation.location().stop();

        if (mCustomMapView != null) {
            mCustomMapView.onDestroy();
        }
        mBSheetBook.onDetach();
        mPresenter.onDetach();

        rv_items.setAdapter(null);

        super.onDestroy();
    }

}
