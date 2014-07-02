package ru.forxy.photo.rest;

import org.junit.Assert;
import org.junit.Test;
import ru.forxy.photo.IPhotoService;

public class PhotoServiceImplTest {

    private final IPhotoService photoService = new PhotoServiceImpl();

    @Test
    public void testAddDeletePhoto() {
        Assert.assertNotNull(photoService);
    }

    @Test
    public void testGetAllPhotos() {
        /*final List<Photo> photos = photoService.getPhotos();
        Assert.assertNotNull(photos);
        Assert.assertTrue(CollectionUtils.isNotEmpty(photos));*/
    }
}
