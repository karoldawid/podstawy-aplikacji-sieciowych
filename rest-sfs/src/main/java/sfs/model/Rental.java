package sfs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;


@MongoEntity(collection = "rentals")
public class Rental {
    @BsonId
    private ObjectId id;
    private String clientId;
    private String facilityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Rental(String clientId, String facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        this.clientId = clientId;
        this.facilityId = facilityId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Rental() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return this.startTime.isBefore(otherEnd) && otherStart.isBefore(this.endTime);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", facilityId=" + facilityId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
