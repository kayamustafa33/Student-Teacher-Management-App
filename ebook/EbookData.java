package com.example.kayit.ebook;

public class EbookData {
    private String pdfTitle, pdfUrl;

    //argümansız constructor
    public EbookData() {
    }

    //argümana sahip constructor
    public EbookData(String name, String pdfUrl) {
        this.pdfTitle = name;
        this.pdfUrl = pdfUrl;
    }

    //getter - setter
    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String name) {
        this.pdfTitle = name;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
