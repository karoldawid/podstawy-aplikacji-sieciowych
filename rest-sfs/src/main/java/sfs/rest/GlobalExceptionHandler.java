package sfs.rest;


import com.mongodb.MongoWriteException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import sfs.exception.ResourceConflictException;
import sfs.exception.ResourceNotFoundException;
import sfs.service.RentalException;
import java.util.Map;

public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Response handleNotFound(ResourceNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleConflict(ResourceConflictException ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleRentalException(RentalException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleMongoWriteException(MongoWriteException ex) {
        if (ex.getError().getCode() == 11000) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Duplikat wartości: " + ex.getMessage()))
                    .build();
        }
        return handleGeneralException(ex);
    }

    @ServerExceptionMapper
    public Response handleGeneralException(Exception ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}