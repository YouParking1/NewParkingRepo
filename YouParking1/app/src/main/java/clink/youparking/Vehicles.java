package clink.youparking;

public class Vehicles {

    private int id;
    private String make;
    private String model;
    private int year;
    private String color;

    public Vehicles(int vId, String vMake, String vModel, int vYear, String vColor) {

        this.id = vId;
        this.make = vMake;
        this.model = vModel;
        this.year = vYear;
        this.color = vColor;
    }

    public int getId() { return id; }

    public String getMake() {
        return make;
    }

    public String getModel() { return model; }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }
}
