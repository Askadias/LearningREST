package ru.forxy.photo.rest;

import ru.forxy.photo.IPhotoService;
import ru.forxy.photo.pojo.Photo;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoServiceImpl implements IPhotoService {

    private static Map<String, Photo> photos = new HashMap<String, Photo>(3);

    static {
        photos.put("alfred@gmail.com", new Photo("alfred@gmail.com"));
        photos.put("bob@gmail.com", new Photo("bob@gmail.com"));
        photos.put("cliff@gmail.com", new Photo("cliff@gmail.com"));
        photos.put("daniel@gmail.com", new Photo("daniel@gmail.com"));
        photos.put("eleanor@gmail.com", new Photo("eleanor@gmail.com"));
    }

    @Override
    public Response getPhotos(Integer page, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return null;
    }

    @Override
    public Response getPhotos(Integer page, Integer size, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return null;
    }

    @Override
    public Response getPhoto(Photo requestedPhoto, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return null;
    }

    @Override
    public Response updatePhoto(Photo Photo, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return null;
    }

    @Override
    public Response createPhoto(Photo Photo, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return null;
    }

    @Override
    public Response deletePhoto(String url, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return null;
    }
}
