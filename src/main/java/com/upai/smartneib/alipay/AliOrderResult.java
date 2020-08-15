package com.upai.smartneib.alipay;

import java.util.List;

public class AliOrderResult {

    private List<Aliorder> aliorderList;
    private String result;

    public AliOrderResult(List<Aliorder> aliorderList, String result) {
        this.aliorderList = aliorderList;
        this.result = result;
    }

    public List<Aliorder> getAliorderList() {
        return aliorderList;
    }

    public void setAliorderList(List<Aliorder> aliorderList) {
        this.aliorderList = aliorderList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
