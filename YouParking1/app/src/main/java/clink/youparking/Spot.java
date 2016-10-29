package clink.youparking;

public class Spot {

    protected double latitude;
    protected double longitude;

    protected int points;
    protected int holder_car;
    protected int holder_percentage;
    protected int holder_spots_held;
    protected int time;

    protected String holder_email;
    protected String comments;

    public Spot() {

    }

    public Spot(double slat, double slong, int point, int hc, String email, String comment, int percent, int spots,
                int time) {

        this.latitude = slat;
        this.longitude = slong;
        this.points = point;
        this.holder_car = hc;
        this.holder_email = email;
        this.comments = comment;
        this.holder_percentage = percent;
        this.holder_spots_held = spots;
        this.time = time;

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getHolder_car() {
        return holder_car;
    }

    public int getPoints() {
        return points;
    }

    public String getComments() {
        return comments;
    }

    public String getHolder_email() {
        return holder_email;
    }

    public int getHolder_percentage() {
        return holder_percentage;
    }

    public int getHolder_spots_held() {
        return holder_spots_held;
    }

    public int getTime() {
        return time;
    }
}
