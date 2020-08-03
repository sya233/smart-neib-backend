package com.upai.smartneib.update;

public class ApkResult {

    private String version;
    private String url;
    private String result;

    public ApkResult(String version, String url, String result) {
        this.version = version;
        this.url = url;
        this.result = result;
    }

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
