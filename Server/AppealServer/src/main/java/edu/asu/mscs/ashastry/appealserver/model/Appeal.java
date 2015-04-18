/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.model;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author A
 */
public class Appeal {
    
    private int appealID;
    private String appealConent;
    @XmlTransient
    private AppealStatus status = AppealStatus.START;

    public Appeal() {
    }

    public Appeal(int appealID, String appealConent) {
       this(appealID, appealConent, AppealStatus.START);
    }

    
    
    public Appeal(int appealID, String appealConent, AppealStatus appealStatus) {
        this.appealID = appealID;
        this.appealConent = appealConent;
        this.status = appealStatus;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Appeal ID: " + appealID + "\n");
        sb.append("Appeal Status: " + status + "\n");
        sb.append("Appeal Content: " + appealConent + "\n");
        
        return sb.toString();
    }
    
    
    /**
     * @return the appealID
     */
    public int getAppealID() {
        return appealID;
    }

    /**
     * @param appealID the appealID to set
     */
    public void setAppealID(int appealID) {
        this.appealID = appealID;
    }

    /**
     * @return the appealConent
     */
    public String getAppealConent() {
        return appealConent;
    }

    /**
     * @param appealConent the appealConent to set
     */
    public void setAppealConent(String appealConent) {
        this.appealConent = appealConent;
    }

    /**
     * @return the status
     */
    public AppealStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(AppealStatus status) {
        this.status = status;
    }
    
}


