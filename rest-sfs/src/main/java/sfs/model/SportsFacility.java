package sfs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;


@MongoEntity(collection = "facilities")
@BsonDiscriminator
public abstract class SportsFacility {

    @BsonId
    private ObjectId id;
    private String name;
    private double pricePerHour;
    private int capacity;

    public SportsFacility() {}

    public SportsFacility(String name, double pricePerHour, int capacity) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    @JsonProperty("id")
    @BsonIgnore
    public String getHexId() {
        return id != null ? id.toHexString() : null;
    }

    @JsonProperty("id")
    @BsonIgnore
    public void setHexId(String id) {
        if (id != null && !id.isEmpty()) {
            this.id = new ObjectId(id);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerHour=" + pricePerHour + '\'' +
                ", capacity='" + capacity +
                '}';
    }
}
