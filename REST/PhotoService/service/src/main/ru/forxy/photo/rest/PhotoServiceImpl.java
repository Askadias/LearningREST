package ru.forxy.photo.rest;

import ru.forxy.photo.IPhotoService;
import ru.forxy.photo.pojo.Photo;

import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoServiceImpl implements IPhotoService {

    private static Map<String, Photo> users = new HashMap<String, Photo>(3);

    static {
        users.put("alfred@gmail.com", new Photo("alfred@gmail.com"));
        users.put("bob@gmail.com", new Photo("bob@gmail.com"));
        users.put("cliff@gmail.com", new Photo("cliff@gmail.com"));
        users.put("daniel@gmail.com", new Photo("daniel@gmail.com"));
        users.put("eleanor@gmail.com", new Photo("eleanor@gmail.com"));
    }

    @Override
    public List<Photo> getPhotos() {
        return new ArrayList<Photo>(users.values());
    }

    @Override
    public Photo addPhoto(String url) {
        Photo photo = users.get(url);
        if (photo != null) {
            return photo;
        } else {
            return null;
        }
    }

    @Override
    public void addPhoto(Photo photo) {
        if (photo.getUrl() != null) {
            if (!users.containsKey(photo.getUrl())) {
                users.put(photo.getUrl(), photo);
            } else {
                throw new WebServiceException("Photo with url " + photo.getUrl() + " already exist");
            }
        } else {
            throw new WebServiceException("Photo's url is null");
        }
    }

    @Override
    public void deletePhoto(String url) {
        users.remove(url);
    }
}
