package com.example.sih2020.model;

import java.util.List;

public class ServiceMan {

    String userName, Email;
    int load = 0;

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    List<String> pendingRequest, completedRequest, pendingComplaint, completedComplaint;

    public ServiceMan(){}

    public ServiceMan(String userName, String email, List<String> pendingRequest, List<String> completedRequest, List<String> pendingComplaint, List<String> completedComplaint) {
        this.userName = userName;
        Email = email;
        this.pendingRequest = pendingRequest;
        this.completedRequest = completedRequest;
        this.pendingComplaint = pendingComplaint;
        this.completedComplaint = completedComplaint;
    }

    public List<String> getPendingRequest() {
        return pendingRequest;
    }

    public List<String> getCompletedRequest() {
        return completedRequest;
    }

    public List<String> getPendingComplaint() {
        return pendingComplaint;
    }

    public List<String> getCompletedComplaint() {
        return completedComplaint;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setPendingRequest(List<String> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public void setCompletedRequest(List<String> completedRequest) {
        this.completedRequest = completedRequest;
    }

    public void setPendingComplaint(List<String> pendingComplaint) {
        this.pendingComplaint = pendingComplaint;
    }

    public void setCompletedComplaint(List<String> completedComplaint) {
        this.completedComplaint = completedComplaint;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageURL() {return  "https://firebasestorage.googleapis.com/v0/b/sih2020-eeaf0.appspot.com/o/93.jpg?alt=media&token=f5cb3bfd-d361-4509-8b0b-55e7285239b1";}
}
