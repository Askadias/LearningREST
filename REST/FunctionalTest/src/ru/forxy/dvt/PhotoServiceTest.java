package ru.forxy.dvt;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.forxy.photo.IPhotoService;
import ru.forxy.photo.pojo.Photo;

import java.util.List;

@ContextConfiguration(locations =
        {"classpath:/ru/forxy/spring-context.xml"})
public class PhotoServiceTest extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoServiceTest.class);

    @Autowired
    @Qualifier("photoServiceClient")
    private IPhotoService photoService;

    @Test
    public void testGetAllUsers() {
        List<Photo> photos = photoService.getPhotos();
        Assert.assertTrue(CollectionUtils.isNotEmpty(photos));
        LOGGER.info("Photos successfully retrieved " + photos);
    }
}
