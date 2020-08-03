package com.upai.smartneib.update;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Apk {
    @Id
    private String version;
    private String url;
    private String des;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
