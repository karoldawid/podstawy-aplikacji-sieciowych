package sfs.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "users")
public class Client extends User{

    public Client(){
        super();
    }
}
