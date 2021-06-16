package com.malaab.ya.action.actionyamalaab.events;

public class OnEventShowPlayground {

    private String playgroundId;
    private boolean isIndividual;
    private String bookingId;


    public OnEventShowPlayground(String playgroundId, boolean isIndividual, String bookingId) {
        this.playgroundId = playgroundId;
        this.isIndividual = isIndividual;
        this.bookingId = bookingId;
    }


    public String getPlaygroundId() {
        return playgroundId;
    }

    public boolean isIndividual() {
        return isIndividual;
    }

    public String getBookingId() {
        return bookingId;
    }
}
