package com.example.raj.deliveryyy.awb;

import java.io.Serializable;
import java.util.List;



public class AwbNo implements Serializable {


    private List<String> awbNoList;

    public List<String> getAwbNoList() {
        return awbNoList;
    }

    public void setAwbNoList(List<String> awbNoList) {
        this.awbNoList = awbNoList;
    }
}
