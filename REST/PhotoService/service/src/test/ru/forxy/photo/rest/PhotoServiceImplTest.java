package ru.forxy.photo.rest;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import ru.forxy.photo.IPhotoService;
import ru.forxy.photo.pojo.Photo;

import java.util.List;

public class PhotoServiceImplTest {

    private final IPhotoService photoService = new PhotoServiceImpl();

    @Test
    public void testAddDeletePhoto() {
        Assert.assertNotNull(photoService);
    }

    @Test
    public void testGetAllPhotos() {
        final List<Photo> photos = photoService.getPhotos();
        Assert.assertNotNull(photos);
        Assert.assertTrue(CollectionUtils.isNotEmpty(photos));
    }
}
