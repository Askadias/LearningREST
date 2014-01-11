package ru.forxy.photo.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "photo")
public class Photo {

    private String url;

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

    @Override
    public String toString() {
        return String.format("{url=%s}", url);
    }
}
