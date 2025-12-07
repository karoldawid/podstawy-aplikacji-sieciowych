package sfs.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "users")
public class Admin extends User{

    public Admin()
    { super(); }
}
