package com.codershubham.quickcarhire.ClassFile;

public class Booking {
    int id;
    private String registrationNo;
    private String name;
    private String image;
    private String startDate;
    private String endDate;
    private String address;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getSelected_Kms() {
        return Selected_Kms;
    }

    public void setSelected_Kms(String selected_Kms) {
        Selected_Kms = selected_Kms;
    }

    public String getSecurity_Deposit() {
        return Security_Deposit;
    }

    public void setSecurity_Deposit(String security_Deposit) {
        Security_Deposit = security_Deposit;
    }


    private String City;
    private String Selected_Kms;
    private String Security_Deposit;

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private double amount;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
