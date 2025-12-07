package sfs;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class RestSfsApplication {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}