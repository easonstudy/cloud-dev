package com.cjcx.wechat.open.object;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ScanCodeInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScanCodeInfo {

    @XmlElement(name="ScanType")
    private String scanType;            // 扫描类型，一般是qrcode

    @XmlElement(name="ScanResult")
    private String scanResult;          //扫描结果，即二维码对应的字符串信息

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    @Override
    public String toString() {
        return "ScanCodeInfo{" +
                "scanType='" + scanType + '\'' +
                ", scanResult='" + scanResult + '\'' +
                '}';
    }
}
