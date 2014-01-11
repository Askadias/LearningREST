package ru.forxy.photo.rest;

import ru.forxy.photo.IPhotoService;
import ru.forxy.photo.pojo.Photo;

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
    public List<Photo> getPhotos() {
        return new ArrayList<Photo>(photos.values());
    }

    @Override
    public Photo addPhoto(final String url) {
        Photo photo = photos.get(url);
        if (photo != null) {
            return photo;
        } else {
            return null;
        }
    }

    @Override
    public void addPhoto(final Photo photo) {
        if (photo.getUrl() != null) {
            if (!photos.containsKey(photo.getUrl())) {
                photos.put(photo.getUrl(), photo);
            } else {
                throw new WebServiceException("Photo with url " + photo.getUrl() + " already exist");
            }
        } else {
            throw new WebServiceException("Photo's url is null");
        }
    }

    @Override
    public void deletePhoto(final String url) {
        photos.remove(url);
    }
}
