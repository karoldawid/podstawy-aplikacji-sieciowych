package sfs.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "facilities")
public class TennisCourt extends SportsFacility{

    private SurfaceType surfaceType;
    private boolean isIndoor;
    public TennisCourt(String name, double pricePerHour, int capacity, SurfaceType surfaceType, boolean isIndoor) {
        super(name, pricePerHour, capacity);
        this.surfaceType = surfaceType;
        this.isIndoor = isIndoor;
    }

    public TennisCourt() {}

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }

    public boolean isIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }

    @Override
    public String toString() {
        return "TennisCourt{" +
                super.toString() +
                ", surfaceType=" + surfaceType +
                ", isIndoor=" + isIndoor +
                '}';
    }
}
