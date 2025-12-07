package sfs.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "users")
public class FacilityManager extends User{

    public FacilityManager()
    { super(); }

}
