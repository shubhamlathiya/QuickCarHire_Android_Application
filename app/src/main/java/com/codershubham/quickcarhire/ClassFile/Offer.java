package com.codershubham.quickcarhire.ClassFile;

public class Offer {
    private String imageUrl;
    private String name;
    private String code;
    private String percentage;
    private String startDate;
    private String endDate;

    public Offer(String imageUrl, String name, String code, String percentage, String startDate, String endDate) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.code = code;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
