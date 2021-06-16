package com.malaab.ya.action.actionyamalaab.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.model.Location;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class CustomMapView extends FrameLayout {

    private CompositeDisposable mCompositeDisposable;
    private Subject<GoogleMap> mMapSubject;

    private View mCustomMarkerView;
    private CircleImageView mMarkerImageView;
//    private String ImageUrl = "http://remixwallpaper.com/wallpaper/Cristiano_Ronaldo_wallpaper_3.jpg";
//    private String ImageUrl = "http://remixwallpaper.com/wallpaper/Cristiano_ldo_wallpaper_3.jpg";

    private List<Marker> list = new ArrayList<>();

    private Location mLocation;
    private int zoom = 15;


    public CustomMapView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomMapView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        final SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        if (!isInEditMode()) {
            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(getId(), mapFragment);
            fragmentTransaction.commit();

            mCustomMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
            mMarkerImageView = mCustomMarkerView.findViewById(R.id.img_profile);

            mCompositeDisposable = new CompositeDisposable();
            mMapSubject = BehaviorSubject.create();

            Observable.create(new ObservableOnSubscribe<GoogleMap>() {
                @Override
                public void subscribe(final ObservableEmitter<GoogleMap> e) throws Exception {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
//                            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            e.onNext(googleMap);
                        }
                    });
                }
            }).subscribe(mMapSubject);
        }
    }

    public Observable<GoogleMap> getMapReadyObservable() {
        return mMapSubject;
    }


    public void enableOnMapListener(final User user) {
        mCompositeDisposable.add(
                mMapSubject.subscribe(new Consumer<GoogleMap>() {
                    @Override
                    public void accept(final GoogleMap googleMap) throws Exception {
                        if (googleMap == null) {
                            return;
                        }

                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng point) {
                                googleMap.clear();

                                mLocation = new Location(user.fName, "Your Location", point.latitude, point.longitude, user.profileImageUrl);
                                final LatLng position = new LatLng(point.latitude, point.longitude);

                                // adding a marker with image from URL using glide image loading library
                                Glide.with(getContext()).
                                        load(mLocation.profileImageUrl)
                                        .asBitmap()
                                        .override(200, 200)
                                        .fitCenter()
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                super.onLoadFailed(e, errorDrawable);

                                                MarkerOptions options = new MarkerOptions().position(position);

                                                if (!StringUtils.isEmpty(mLocation.title)) {
                                                    options.title(mLocation.title);
                                                }

                                                if (!StringUtils.isEmpty(mLocation.title)) {
                                                    options.snippet(mLocation.snippet);
                                                }

                                                Bitmap smallMarker = Bitmap.createScaledBitmap(
                                                        getMarkerBitmapFromView(mCustomMarkerView, R.drawable.img_profile_player_default),
                                                        100, 100, false
                                                );

                                                options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.img_profile_player_default)));

//                                        options.icon(BitmapDescriptorFactory.fromBitmap(createUserBitmap(null)));

                                                options.anchor(0.1f, 0.1f);

                                                Marker marker = googleMap.addMarker(options);
                                                list.add(marker);

                                                CameraPosition cameraPosition = CameraPosition.builder()
                                                        .target(position)
                                                        .zoom(zoom)
                                                        .build();

                                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                                            }

                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                MarkerOptions options = new MarkerOptions().position(position);

                                                if (!StringUtils.isEmpty(mLocation.title)) {
                                                    options.title(mLocation.title);
                                                }

                                                if (!StringUtils.isEmpty(mLocation.snippet)) {
                                                    options.snippet(mLocation.snippet);
                                                }

                                                options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap)));

//                                        options.icon(BitmapDescriptorFactory.fromBitmap(createUserBitmap(bitmap)));

                                                options.anchor(0.1f, 0.1f);

                                                Marker marker = googleMap.addMarker(options);
                                                list.add(marker);

                                                CameraPosition cameraPosition = CameraPosition.builder()
                                                        .target(position)
                                                        .zoom(zoom)
                                                        .build();

                                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                                            }
                                        });


//                                googleMap.addMarker(new MarkerOptions()
//                                        .position(point)
//                                        .anchor(0.1f, 0.1f)
//                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.img_profile_player_default))));
//
//                                CameraPosition cameraPosition = new CameraPosition.Builder()
//                                        .target(new LatLng(point.latitude, point.longitude)) // Center Set
//                                        .zoom(zoom)                // Zoom
////                                .bearing(90)                // Orientation of the camera to east
////                                .tilt(30)                   // Tilt of the camera to 30 degrees
//                                        .build();                   // Creates a CameraPosition from the builder
//                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }

    public Location getSelectedLocation() {
        return mLocation;
    }


    public void zoomMapInitial(final Playground nearestPlayground, final android.location.Location userCurrentLocation) {
        mCompositeDisposable.add(
                mMapSubject.subscribe(new Consumer<GoogleMap>() {
                    @Override
                    public void accept(final GoogleMap googleMap) throws Exception {
                        if (googleMap == null) {
                            return;
                        }

                        LatLng finalPlace = new LatLng(nearestPlayground.latitude, nearestPlayground.longitude);
                        LatLng currentLoc = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());

                        try {
                            int padding = 100; // Space (in px) between bounding box edges and view edges (applied to all four sides of the bounding box)
                            LatLngBounds.Builder bc = new LatLngBounds.Builder();

                            bc.include(finalPlace);
                            bc.include(currentLoc);

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), padding));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }


    public void focusOnMarker(final double latitude, final double longitude) {
        mCompositeDisposable.add(
                mMapSubject.subscribe(new Consumer<GoogleMap>() {
                    @Override
                    public void accept(final GoogleMap googleMap) throws Exception {
                        if (googleMap == null) {
                            return;
                        }

                        for (Marker marker : list) {
                            if (marker.getPosition().latitude == latitude && marker.getPosition().longitude == longitude) {
                                marker.showInfoWindow();

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(latitude, longitude)) // Center Set
                                        .zoom(zoom)                // Zoom
//                                .bearing(90)                // Orientation of the camera to east
//                                .tilt(30)                   // Tilt of the camera to 30 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                break;
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }


    public void addMarker(final Location location, final boolean isAnimateCamera) {
        mCompositeDisposable.add(
                mMapSubject.subscribe(new Consumer<GoogleMap>() {
                    @Override
                    public void accept(final GoogleMap googleMap) throws Exception {
                        if (googleMap == null) {
                            return;
                        }

                        mLocation = location;

                        final LatLng position = new LatLng(location.latitude, location.longitude);

//                        googleMap.setMyLocationEnabled(true);
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arabiaMall, 13));

//                        MarkerOptions options = new MarkerOptions()
//                                .position(position)
//                                .anchor(0.1f, 0.1f)
//                                .flat(true)
//                                .title(location.title)
//                                .snippet(location.snippet);

//                        MarkerOptions options = new MarkerOptions().position(position);

//                        if (createUserBitmap() != null) {
//                            options.title(location.title);
//                            options.snippet(location.snippet);
//                            options.icon(BitmapDescriptorFactory.fromBitmap(createUserBitmap()));
//                            options.anchor(0.5f, 0.907f);
//                        }


                        // adding a marker on map with image from  drawable
                        /* mGoogleMap.addMarker(new MarkerOptions()
                                .position(mDummyLatLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.avatar))));*/


                        // adding a marker with image from URL using glide image loading library
                        Glide.with(getContext()).
                                load(location.profileImageUrl)
                                .asBitmap()
                                .override(100, 100)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);

                                        MarkerOptions options = new MarkerOptions().position(position);

                                        if (!StringUtils.isEmpty(location.title)) {
                                            options.title(location.title);
                                        }

                                        if (!StringUtils.isEmpty(location.title)) {
                                            options.snippet(location.snippet);
                                        }

                                        Bitmap smallMarker = Bitmap.createScaledBitmap(
                                                getMarkerBitmapFromView(mCustomMarkerView, R.drawable.img_profile_player_default),
                                                100, 100, false
                                        );

                                        options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.img_profile_player_default)));

//                                        options.icon(BitmapDescriptorFactory.fromBitmap(createUserBitmap(null)));

                                        options.anchor(0.1f, 0.1f);

                                        Marker marker = googleMap.addMarker(options);
                                        list.add(marker);

                                        if (isAnimateCamera) {
                                            CameraPosition cameraPosition = CameraPosition.builder()
                                                    .target(position)
                                                    .zoom(zoom)
                                                    .build();

                                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                                        }
                                    }


                                    @Override
                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                        MarkerOptions options = new MarkerOptions().position(position);

                                        if (!StringUtils.isEmpty(location.title)) {
                                            options.title(location.title);
                                        }

                                        if (!StringUtils.isEmpty(location.snippet)) {
                                            options.snippet(location.snippet);
                                        }

                                        options.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap)));

//                                        options.icon(BitmapDescriptorFactory.fromBitmap(createUserBitmap(bitmap)));

                                        options.anchor(0.1f, 0.1f);

                                        Marker marker = googleMap.addMarker(options);
                                        list.add(marker);

                                        if (isAnimateCamera) {
                                            CameraPosition cameraPosition = CameraPosition.builder()
                                                    .target(position)
                                                    .zoom(zoom)
                                                    .build();

                                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                                        }
                                    }
                                });

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }


    public void clearMarkers() {
        mCompositeDisposable.add(
                mMapSubject.subscribe(new Consumer<GoogleMap>() {
                    @Override
                    public void accept(final GoogleMap googleMap) throws Exception {
                        if (googleMap == null) {
                            return;
                        }

                        googleMap.clear();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        AppLogger.e(throwable.getMessage());
                    }
                })
        );
    }


    private Bitmap getMarkerBitmapFromView(View view, @DrawableRes int resId) {
        mMarkerImageView.setImageResource(resId);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }


    //region SOLUTION #2
    private Bitmap createUserBitmap(Bitmap bitmap) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.drawable.img_pin);
            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_profile_player_default);
            }

            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*generate bitmap here if your image comes from any url*/
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

                Matrix matrix = new Matrix();
                float scale = dp(100) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
//                matrix.postTranslate(dp(10), dp(10));
                matrix.postScale(scale, scale);

                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();

            try {
                canvas.setBitmap(null);
            } catch (Exception e) {
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }
    //endregion


    public void onDestroy() {
        mCompositeDisposable.dispose();
    }
}
