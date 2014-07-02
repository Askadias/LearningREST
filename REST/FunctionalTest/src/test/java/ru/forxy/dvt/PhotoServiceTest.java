package ru.forxy.dvt;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.photo.IPhotoService;

public class PhotoServiceTest extends BaseSpringContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoServiceTest.class);

    @Autowired(required = false)
    private IPhotoService photoService;

    @Test
    public void testGetAllUsers() {
        /*final List<Photo> photos = photoService.getPhotos();
        Assert.assertTrue(CollectionUtils.isNotEmpty(photos));
        LOGGER.info("Photos successfully retrieved " + photos);*/
    }
}
