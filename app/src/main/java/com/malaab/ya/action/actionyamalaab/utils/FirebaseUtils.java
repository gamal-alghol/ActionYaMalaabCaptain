package com.malaab.ya.action.actionyamalaab.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.annotations.UserRole;
import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;


public class FirebaseUtils {

    public static DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_USERS_TABLE);
    }

    public static DatabaseReference getNotificationsRef() {
        return FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_DB_NOTIFICATIONS_TABLE);
    }


//    public static void sendBookingNotificationToAdmin(@NotificationType final String type, final String title, final String message,
//                                                      final String fromUserUid, final String fromUsername, final String fromUserProfileImage) {


    public static void sendNotificationToAdmin(@NotificationType final String type, final String title, final String message, final String fromUserUid, final String fromUsername, final String fromUserProfileImage) {

//        final String title;
//        final String message = String.format(activity.getString(R.string.notification_new_booking_message), booking.user.name, booking.playground.name, DateTimeUtils.getDatetime(booking.timeStart, DateTimeUtils.PATTERN_DATETIME_DEFAULT));
//
//        if (type.equals(NotificationType.BOOKING_FULL_NEW)) {
//            title = activity.getString(R.string.notification_new_booking_title);
//        } else {
//            title = activity.getString(R.string.notification_new_individual_title);
//        }

        getUsersRef()
                .orderByChild("role")
                .equalTo(UserRole.ROLE_ADMIN)
//                .limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                User admin = child.getValue(User.class);
                                if (admin != null) {
                                    if (admin.isActive) {
                                        String uid = getNotificationsRef().push().getKey();

                                        Notification notification = new Notification();
                                        notification.uid = uid;
                                        notification.type = type;
                                        notification.title = title;
                                        notification.message = message;
                                        notification.fromUserUid = StringUtils.isEmpty(fromUserUid) ? "" : fromUserUid;
                                        notification.fromUsername = StringUtils.isEmpty(fromUsername) ? "" : fromUsername;
                                        notification.fromUserProfileImage = StringUtils.isEmpty(fromUserProfileImage) ? "" : fromUserProfileImage;
                                        notification.toUserUid = admin.uId;
                                        notification.toUsername = admin.getUserFullName();
                                        notification.toUserProfileImage = StringUtils.isEmpty(admin.profileImageUrl) ? "" : admin.profileImageUrl;
                                        notification.toFCMToken = admin.fcmToken;

                                        sendNotification(notification);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        AppLogger.d("isUserExistInDB onCancelled = " + databaseError.getMessage());
                    }
                });
    }


    public static void sendNotification(final Notification notification) {
        getNotificationsRef()
                .child(notification.uid)
                .setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AppLogger.w("sendNotification to " + notification.toUsername + " -> onSuccess");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AppLogger.w("sendNotification to " + notification.toUsername + " -> onComplete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AppLogger.w("sendNotification to " + notification.toUsername + " -> Error " + e.getLocalizedMessage());
                    }
                });
    }

    //    public static DatabaseReference getUserRef(String email) {
//        return FirebaseDatabase.getInstance()
//                .getReference(Constants.USERS_KEYS)
//                .child(email);
//    }
//
//    public static DatabaseReference getPostRef() {
//        return FirebaseDatabase.getInstance()
//                .getReference(Constants.POSTS_KEY);
//    }
//
//    public static Query getPostQuery() {
//        return getPostRef().orderByChild(Constants.TIME_CREATED_KEY);
//    }
//
//    public static DatabaseReference getPostLikedRef() {
//        return FirebaseDatabase.getInstance()
//                .getReference(Constants.POSTS_LIKED_KEY);
//    }
//
//    public static DatabaseReference getPostLikedRef(String postId) {
//        return getPostLikedRef().child(getUserDetails().getEmail()
//                .replace(".", ","))
//                .child(postId);
//    }
//
//    public static FirebaseUser getUserDetails() {
//        return FirebaseAuth.getInstance().getUserDetails();
//    }
//
//    public static String getUid() {
//        String path = FirebaseDatabase.getInstance().getReference().push().toString();
//        return path.substring(path.lastIndexOf("/") + 1);
//    }
//
//    public static StorageReference getImagesSRef() {
//        return FirebaseStorage.getInstance().getReference(Constants.POST_IMAGES);
//    }
//
//    public static DatabaseReference getMyPostRef() {
//        return FirebaseDatabase.getInstance().getReference(Constants.MY_POSTS)
//                .child(getUserDetails().getEmail().replace(".", ","));
//    }
//
//    public static DatabaseReference getCommentRef(String postId) {
//        return FirebaseDatabase.getInstance().getReference(Constants.COMMENTS_KEY)
//                .child(postId);
//    }
//
//    public static DatabaseReference getMyRecordRef() {
//        return FirebaseDatabase.getInstance().getReference(Constants.USER_RECORD)
//                .child(getUserDetails().getEmail().replace(".", ","));
//    }
//
//    public static void addToMyRecord(String node, final String id) {
//        FirebaseUtils.getMyRecordRef().child(node).runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                ArrayList<String> myPostCollection;
//                if (mutableData.getValue() == null) {
//                    myPostCollection = new ArrayList<>(1);
//                    myPostCollection.add(id);
//                    mutableData.setValue(myPostCollection);
//                } else {
//                    myPostCollection = (ArrayList<String>) mutableData.getValue();
//                    myPostCollection.add(id);
//                    mutableData.setValue(myPostCollection);
//                }
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//            }
//        });
//    }
//
//    //I'll be creating the dynamic link now. Since all of our post has a unique Id in them we
//    // want that in our url. so let's start
//
//    public static String generateDeepLink(String uid) {
//        //We first need to get our firebase deep link prefix
//        //The https://firebasetutorial.com/<uid> is what will be recieved in our app when this
//        // linked is clicked. We need to add our package name so that android knows which app to
//        // open
//
//        return "https://t53y3.app.goo.gl/?link=https://firebasetutorial.com/" + uid +
//                "&apn=com.getmore.getmoreapp.firebasepostandchattutorial";
//    }
//
//    public static DatabaseReference getSharedRef(String postId) {
//        return getPostRef().child(postId).child(Constants.NUM_SHARES_KEY);
//    }
//
//    public static DatabaseReference getNotificationRef() {
//        return FirebaseDatabase.getInstance().getReference(Constants.NOTIFICATION_KEY).push();
//    }

}