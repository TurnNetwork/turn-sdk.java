package com.bubble.contracts.dpos.dto.resp;

import com.alibaba.fastjson.annotation.JSONField;

public class Description{

    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "Detail")
    private String detail;
    @JSONField(name = "ElectronURI")
    private String electronURI;
    @JSONField(name = "P2PURI")
    private String p2PUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getElectronURI() {
        return electronURI;
    }

    public void setElectronURI(String electronURI) {
        this.electronURI = electronURI;
    }

    public String getP2PUri() {
        return p2PUri;
    }

    public void setP2PUri(String p2PUri) {
        this.p2PUri = p2PUri;
    }

    public Description(String name, String detail, String electronURI, String p2PUri) {
        this.name = name;
        this.detail = detail;
        this.electronURI = electronURI;
        this.p2PUri = p2PUri;
    }
}
