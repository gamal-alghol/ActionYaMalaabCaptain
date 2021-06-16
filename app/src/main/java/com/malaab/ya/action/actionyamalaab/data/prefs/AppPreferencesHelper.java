package com.malaab.ya.action.actionyamalaab.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.malaab.ya.action.actionyamalaab.data.model.AppSettings;
import com.malaab.ya.action.actionyamalaab.data.model.Counter;
import com.malaab.ya.action.actionyamalaab.data.model.FirebaseSettings;
import com.malaab.ya.action.actionyamalaab.data.model.UserFavouritePlaygrounds;
import com.malaab.ya.action.actionyamalaab.data.model.Notifications;
import com.malaab.ya.action.actionyamalaab.data.model.NotificationsSettings;
import com.malaab.ya.action.actionyamalaab.data.model.UsersFavouritePlaygrounds;
import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.ApplicationContext;
import com.malaab.ya.action.actionyamalaab.di.PreferenceInfo;
import com.malaab.ya.action.actionyamalaab.utils.JsonConverter;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String DEFAULT_VALUE = "";
    private static final String KEY_APP_SETTINGS = "AppSettings";
    private static final String KEY_FIREBASE_SETTINGS = "FirebaseSettings";
    private static final String KEY_PUSH_COUNTERS = "Counters";
    private static final String KEY_NOTIFICATIONS_SETTINGS = "NotificationsSettings";
    public static final String PUSH_NOTIFICATION = "NotificationContent";
    private static final String KEY_USER = "User";
    public static final String KEY_FAVOURITE_PLAYGROUND = "UserFavouritePlaygrounds";

    private final SharedPreferences mPrefs;


    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    //region APP_SETTINGS
    @Override
    public void setAppSettings(AppSettings appSettings) {
        String json = JsonConverter.objectToJson(appSettings);
        if (json != null) {
            mPrefs.edit().putString(KEY_APP_SETTINGS, json).apply();
        }
    }

    @Override
    public AppSettings getAppSettings() {
        String json = mPrefs.getString(KEY_APP_SETTINGS, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(AppSettings.class, json);
    }
    //endregion


    //region FIREBASE_SETTINGS
    @Override
    public void setFirebaseSettings(FirebaseSettings firebaseSettings) {
        String json = JsonConverter.objectToJson(firebaseSettings);
        if (json != null) {
            mPrefs.edit().putString(KEY_FIREBASE_SETTINGS, json).apply();
        }
    }

    @Override
    public FirebaseSettings getFirebaseSettings() {
        String json = mPrefs.getString(KEY_FIREBASE_SETTINGS, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(FirebaseSettings.class, json);
    }
    //endregion


    //region COUNTERS
    @Override
    public void setCounters(Counter counters) {
        String json = JsonConverter.objectToJson(counters);
        if (json != null) {
            mPrefs.edit().putString(KEY_PUSH_COUNTERS, json).apply();
        }
    }

    @Override
    public void resetNotificationsCounters() {
        Counter counter = getCounters();
        if (counter == null) {
            counter = new Counter();
        }

        counter.notificationsCounter = 0;

        setCounters(counter);
    }

    @Override
    public void resetMessagesCounters() {
        Counter counter = getCounters();
        if (counter == null) {
            counter = new Counter();
        }

        counter.messagesCounter = 0;

        setCounters(counter);
    }

    @Override
    public Counter getCounters() {
        String json = mPrefs.getString(KEY_PUSH_COUNTERS, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(Counter.class, json);
    }
    //endregion


    //region NOTIFICATIONS_SETTINGS
    @Override
    public void setNotificationsSettings(String firebaseToken, boolean isRegistered) {
        NotificationsSettings notificationsSettings = new NotificationsSettings(firebaseToken, isRegistered);

        String json = JsonConverter.objectToJson(notificationsSettings);
        if (json != null) {
            mPrefs.edit().putString(KEY_NOTIFICATIONS_SETTINGS, json).apply();
        }
    }

    @Override
    public void updateNotificationsSettings(String firebaseToken, boolean isRegistered) {
        NotificationsSettings notificationsSettings = getNotificationsSettings();

        if (notificationsSettings != null) {
            if (StringUtils.isEmpty(firebaseToken)) {
                setNotificationsSettings(notificationsSettings.firebaseToken, isRegistered);
                return;
            }
        }

        setNotificationsSettings(firebaseToken, isRegistered);
    }

    @Override
    public NotificationsSettings getNotificationsSettings() {
        String json = mPrefs.getString(KEY_NOTIFICATIONS_SETTINGS, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(NotificationsSettings.class, json);
    }
    //endregion


    //region NOTIFICATIONS
    @Override
    public void addNotificationContent(Notification notification) {
        Notifications notifications = getNotifications();
        if (notifications == null) {
            notifications = new Notifications();
            notifications.mNotifications = new ArrayList<>();
        }

        if (!notifications.mNotifications.contains(notification)) {
            notifications.mNotifications.add(notification);
        }

        String json = JsonConverter.objectToJson(notifications);
        if (json != null) {
            mPrefs.edit().putString(PUSH_NOTIFICATION, json).apply();
        }
    }

    @Override
    public void removeNotificationContent(Notification notification) {
        Notifications notifications = getNotifications();
        if (notifications != null && notifications.mNotifications != null) {
            if (notifications.mNotifications.contains(notification)) {
                notifications.mNotifications.remove(notification);

                String json = JsonConverter.objectToJson(notifications);
                if (json != null) {
                    mPrefs.edit().putString(PUSH_NOTIFICATION, json).apply();
                }
            }
        }
    }

    @Override
    public Notifications getNotifications() {
        String json = mPrefs.getString(PUSH_NOTIFICATION, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(Notifications.class, json);
    }
    //endregion


    //region CURRENT_USER
    @Override
    public void setCurrentUser(User user) {
        String json = JsonConverter.objectToJson(user);
        if (json != null) {
            mPrefs.edit().putString(KEY_USER, json).apply();
        }
    }

    @Override
    public User getCurrentUser() {
        String json = mPrefs.getString(KEY_USER, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(User.class, json);
    }

    @Override
    public boolean isPlaygroundInFavouriteList(String userUid, Playground playground) {
        UserFavouritePlaygrounds user = getUserFavouritePlaygrounds(userUid);

        if (user != null && !ListUtils.isEmpty(user.playgrounds)) {
            return user.playgrounds.contains(playground);
        }

        return false;
    }
    //endregion


    //region FAVOURITE_PLAYGROUND
    @Override
    public void addPlaygroundToFavouriteList(String userUid, Playground playground) {
        UsersFavouritePlaygrounds users = getUsersFavouritePlaygrounds();
        if (users == null) {
            users = new UsersFavouritePlaygrounds();
            users.userFavouritePlaygrounds = new ArrayList<>();
        }

        UserFavouritePlaygrounds user = getUserFavouritePlaygrounds(userUid);
        if (user == null) {
            user = new UserFavouritePlaygrounds();
            user.userUid = userUid;
            user.playgrounds = new ArrayList<>();
        }

        if (!user.playgrounds.contains(playground)) {
            user.playgrounds.add(playground);
        }

        if (users.userFavouritePlaygrounds.contains(user)) {
            users.userFavouritePlaygrounds.remove(user);
        }

        users.userFavouritePlaygrounds.add(user);

        String json = JsonConverter.objectToJson(users);
        if (json != null) {
            mPrefs.edit().putString(KEY_FAVOURITE_PLAYGROUND, json).apply();
        }
    }

    @Override
    public void removePlaygroundTFromFavouriteList(String userUid, Playground playground) {
        UsersFavouritePlaygrounds users = getUsersFavouritePlaygrounds();
        if (users != null) {
            UserFavouritePlaygrounds user = getUserFavouritePlaygrounds(userUid);
            if (user != null && !ListUtils.isEmpty(user.playgrounds)) {
                if (user.playgrounds.contains(playground)) {
                    user.playgrounds.remove(playground);
                }

                if (users.userFavouritePlaygrounds.contains(user)) {
                    users.userFavouritePlaygrounds.remove(user);
                }

                users.userFavouritePlaygrounds.add(user);

                String json = JsonConverter.objectToJson(users);
                if (json != null) {
                    mPrefs.edit().putString(KEY_FAVOURITE_PLAYGROUND, json).apply();
                }
            }
        }
    }

    @Override
    public UserFavouritePlaygrounds getUserFavouritePlaygrounds(String userUid) {

        if (getUsersFavouritePlaygrounds() != null && !ListUtils.isEmpty(getUsersFavouritePlaygrounds().userFavouritePlaygrounds)) {

            for (UserFavouritePlaygrounds user : getUsersFavouritePlaygrounds().userFavouritePlaygrounds) {
                if (user.userUid.equals(userUid)) {
                    return user;
                }
            }
        }

        return null;
    }

    @Override
    public UsersFavouritePlaygrounds getUsersFavouritePlaygrounds() {
        String json = mPrefs.getString(KEY_FAVOURITE_PLAYGROUND, DEFAULT_VALUE);
        return JsonConverter.jsonToObject(UsersFavouritePlaygrounds.class, json);
    }
    //endregion

}
