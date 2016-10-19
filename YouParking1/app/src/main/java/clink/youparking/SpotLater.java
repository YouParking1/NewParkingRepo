package clink.youparking;

/**
 * Created by Aaron on 9/26/2016.
 */
public class SpotLater extends Spot{

    private int spotId;
    private int currentBid;

    public SpotLater(double slat, double slong, int point, int hc, String email, String comment, int percent, int spots,
                int time, int spotId, int currentBid) {

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
    }

    public int getSpotId() {
        return spotId;
    }

    public int getCurrentBid() {
        return currentBid;
    }
}
