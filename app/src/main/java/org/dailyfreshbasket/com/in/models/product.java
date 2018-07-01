package org.dailyfreshbasket.com.in.models;

import java.util.ArrayList;

/**
 * Created by shubham on 4/22/2018.
 */

public class product{
    private	  String pid;
    private	  String pname;
    private	  int pmrp;
    private	  int pactualprice;
    private	  int pquantity;
    private	  String pfeature;
    private	  String pspecs;
    private	  String ptags;
    private	  float prating;
    private	  String poffer;
    private	  int pdiscount;
    private   int psold;
    private ArrayList<String> url;
    private String createDate;

public product(){

}
    public product(String pid, String pname, int pmrp, int pactualprice, int pquantity, String pfeature, String pspecs, String ptags, int prating, String poffer, int pdiscount, int psold, ArrayList<String> url, String createDate) {
        this.pid = pid;
        this.pname = pname;
        this.pmrp = pmrp;
        this.pactualprice = pactualprice;
        this.pquantity = pquantity;
        this.pfeature = pfeature;
        this.pspecs = pspecs;
        this.ptags = ptags;
        this.prating = prating;
        this.poffer = poffer;
        this.pdiscount = pdiscount;
        this.psold = psold;
        this.url = url;
        this.createDate = createDate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPmrp() {
        return pmrp;
    }

    public void setPmrp(int pmrp) {
        this.pmrp = pmrp;
    }

    public int getPactualprice() {
        return pactualprice;
    }

    public void setPactualprice(int pactualprice) {
        this.pactualprice = pactualprice;
    }

    public int getPquantity() {
        return pquantity;
    }

    public void setPquantity(int pquantity) {
        this.pquantity = pquantity;
    }

    public String getPfeature() {
        return pfeature;
    }

    public void setPfeature(String pfeature) {
        this.pfeature = pfeature;
    }

    public String getPspecs() {
        return pspecs;
    }

    public void setPspecs(String pspecs) {
        this.pspecs = pspecs;
    }

    public String getPtags() {
        return ptags;
    }

    public void setPtags(String ptags) {
        this.ptags = ptags;
    }

    public float getPrating() {
        return prating;
    }

    public void setPrating(float prating) {
        this.prating = prating;
    }

    public String getPoffer() {
        return poffer;
    }

    public void setPoffer(String poffer) {
        this.poffer = poffer;
    }

    public int getPdiscount() {
        return pdiscount;
    }

    public void setPdiscount(int pdiscount) {
        this.pdiscount = pdiscount;
    }

    public int getPsold() {
        return psold;
    }

    public void setPsold(int psold) {
        this.psold = psold;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
