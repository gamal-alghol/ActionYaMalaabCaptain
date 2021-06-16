package com.malaab.ya.action.actionyamalaab.utils;

import android.location.Location;

import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ListUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }


    public static List<Playground> sortPlaygroundsByNearest(ArrayList<Playground> list, final Location userCurrentLocation) {
        List<Playground> playgrounds = new ArrayList<>(list);

        Collections.sort(playgrounds, new Comparator<Playground>() {
            @Override
            public int compare(Playground a, Playground b) {
                Location locationA = new Location("point A");
                locationA.setLatitude(a.latitude);
                locationA.setLongitude(a.longitude);

                Location locationB = new Location("point B");
                locationB.setLatitude(b.latitude);
                locationB.setLongitude(b.longitude);

                float distanceOne = userCurrentLocation.distanceTo(locationA);
                float distanceTwo = userCurrentLocation.distanceTo(locationB);

                int result = Float.compare(distanceOne, distanceTwo);
                if (result == 0) {
                    if (a.booking == null || b.booking == null) {
                        return 0;
                    }
                    result = Float.compare(a.booking.timeStart, b.booking.timeStart);
                }

                return result;
            }
        });

        return playgrounds;
    }

    public static List<Playground> sortPlaygroundsByNearestAndDatetime(ArrayList<Playground> list, final Location userCurrentLocation) {
        List<Playground> playgrounds = new ArrayList<>(list);

        Collections.sort(playgrounds, new Comparator<Playground>() {
            @Override
            public int compare(Playground a, Playground b) {
                int result = -1;
                if (a.booking != null && b.booking != null) {
                    result = Float.compare(a.booking.timeStart, b.booking.timeStart);
                }

                if (result == 0) {
                    Location locationA = new Location("point A");
                    locationA.setLatitude(a.latitude);
                    locationA.setLongitude(a.longitude);

                    Location locationB = new Location("point B");
                    locationB.setLatitude(b.latitude);
                    locationB.setLongitude(b.longitude);

                    float distanceOne = userCurrentLocation.distanceTo(locationA);
                    float distanceTwo = userCurrentLocation.distanceTo(locationB);

                    result = Float.compare(distanceOne, distanceTwo);
                }

                return result;
            }
        });

        return playgrounds;
    }

    public static List<Booking> sortBookingsList(List<Booking> bookings) {
        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking a, Booking b) {
                Date dateStart = new Date();
                dateStart.setTime(a.timeStart);

                Date dateEnd = new Date();
                dateEnd.setTime(b.timeStart);

                return dateStart.compareTo(dateEnd);
            }
        });

        return bookings;
    }

    public static List<Fine> sortFinesList(List<Fine> fines) {
        Collections.sort(fines, new Comparator<Fine>() {
            @Override
            public int compare(Fine a, Fine b) {
                Date dateStart = new Date();
                dateStart.setTime(a.datetimeCreated);

                Date dateEnd = new Date();
                dateEnd.setTime(b.datetimeCreated);

                return dateStart.compareTo(dateEnd);
            }
        });

        return fines;
    }


//    public static List<NewArrivalsProducts.Item> cloneNewArrivalsProductsList(List<NewArrivalsProducts.Item> items) {
//        List<NewArrivalsProducts.Item> clonedList = new ArrayList<>(items.size());
//        for (NewArrivalsProducts.Item item : items) {
//            clonedList.add(new NewArrivalsProducts.Item(item));
//        }
//        return clonedList;
//    }
}
