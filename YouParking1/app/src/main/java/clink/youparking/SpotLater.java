package clink.youparking;

public class SpotLater extends Spot{

    private int spotId;
    private int currentBid;
    private String buyer;

    public SpotLater(double slat, double slong, int point, int hc, String email, String comment, int percent, int spots,
                int time, int spotId, int currentBid, String buyer) {

        this.latitude = slat;
        this.longitude = slong;
        this.points = point - 1;
        this.holder_car = hc;
        this.holder_email = email;
        this.comments = comment;
        this.holder_percentage = percent;
        this.holder_spots_held = spots;
        this.time = time;
        this.spotId = spotId;
        this.currentBid = currentBid;
        this.buyer = buyer;
    }

    public int getSpotId() {
        return spotId;
    }

    public int getCurrentBid() {
        return currentBid;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getNormalTime(){
        long now = System.currentTimeMillis() / 1000;
        long diff = time - now;
        long hours = diff / 3600;
        long minutes = (diff - (hours*3600)) / 60;

        return ("Departs in " + hours + " Hour and " + minutes + " Minutes");
    }
}
