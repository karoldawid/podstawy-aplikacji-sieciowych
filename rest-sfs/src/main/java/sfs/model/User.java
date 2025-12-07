package sfs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;


@MongoEntity(collection = "users")
@BsonDiscriminator
public abstract class User {
    @BsonId
    private ObjectId id;
    private String firstName;
    private String lastName;
    boolean isActive;
    String login;

    public User(String login, String firstName, String lastName) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = false;
    }

    public User() {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "login= " + login +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }

}
