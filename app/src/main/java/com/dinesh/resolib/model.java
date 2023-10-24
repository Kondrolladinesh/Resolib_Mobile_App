package com.dinesh.resolib;

public class model
{
    public String filename, fileurl;
    int nol,nov;

    public model() {

    }

    public model(String filename, String fileurl){
        this.filename = filename;
        this.fileurl = fileurl;
    }

    public model(String filename, String fileurl, int nol, int nov) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.nol = nol;
        this.nov = nov;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public int getNol() {
        return nol;
    }

    public void setNol(int nol) {
        this.nol = nol;
    }

    public int getNov() {
        return nov;
    }

    public void setNov(int nov) {
        this.nov = nov;
    }
}