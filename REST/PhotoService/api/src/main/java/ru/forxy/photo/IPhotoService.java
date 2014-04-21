package ru.forxy.photo;

import ru.forxy.photo.pojo.Photo;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/photos/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IPhotoService {

    @GET
    @Path("/{page}/")
    Response getPhotos(@PathParam("page") final Integer page, @Context final UriInfo uriInfo,
                      @Context final HttpHeaders headers);

    @GET
    @Path("/{page}/{size}/")
    Response getPhotos(@PathParam("page") final Integer page, @PathParam("size") final Integer size,
                      @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @GET
    Response getPhoto(@QueryParam("") final Photo requestedPhoto, @Context final UriInfo uriInfo,
                     @Context final HttpHeaders headers);

    @POST
    Response updatePhoto(final Photo Photo, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @PUT
    Response createPhoto(final Photo Photo, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @DELETE
    Response deletePhoto(@QueryParam("url") final String url, @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers);
}
