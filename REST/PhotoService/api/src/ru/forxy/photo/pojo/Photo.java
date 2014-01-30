package ru.forxy.photo.pojo;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import ru.forxy.common.pojo.SimpleJacksonDateSerializer;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Set;

@XmlRootElement(name = "photo")
public class Photo {

    private String url;

    private String name;

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    private Date createDate = new Date();

    private Set<String> likedBy;

    private Integer likes;

    public Photo() {
    }

    public Photo(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public Set<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(final Set<String> likedBy) {
        this.likedBy = likedBy;
    }

    @Override
    public String toString() {
        return String.format("{url=%s, name=%s, createDate=%s,}", url, name, createDate);
    }
}
