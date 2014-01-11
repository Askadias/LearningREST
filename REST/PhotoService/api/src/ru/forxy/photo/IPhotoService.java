package ru.forxy.photo;

import ru.forxy.photo.pojo.Photo;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/")
public interface IPhotoService {

    @GET
    @Path("/photos")
    @Produces("application/json")
    List<Photo> getPhotos();

    @GET
    @Path("/photos/{url}")
    @Consumes("application/json")
    @Produces("application/json")
    Photo addPhoto(@PathParam("url") final String url);

    @POST
    @Path("/photos")
    @Consumes("application/json")
    void addPhoto(final Photo photo);

    @DELETE
    @Path("/photos/{url}")
    void deletePhoto(@PathParam("url") final String url);
}
