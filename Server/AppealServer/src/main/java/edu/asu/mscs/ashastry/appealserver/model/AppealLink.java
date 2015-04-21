/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.model;

import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;

/**
 *
 * @author A
 */
public class AppealLink {

    private int appealiD;
    private AppealServerUri appealUri;
    
    public AppealLink() {
    }
    
    public AppealLink(int appealID,AppealServerUri appealURI ){
        this.appealiD = appealID;
        this.appealUri = appealURI;
    }

    /**
     * @return the appealiD
     */
    public int getAppealiD() {
        return appealiD;
    }

    /**
     * @param appealiD the appealiD to set
     */
    public void setAppealiD(int appealiD) {
        this.appealiD = appealiD;
    }

    /**
     * @return the appealUri
     */
    public AppealServerUri getAppealUri() {
        return appealUri;
    }

    /**
     * @param appealUri the appealUri to set
     */
    public void setAppealUri(AppealServerUri appealUri) {
        this.appealUri = appealUri;
    }
    
     
    
}
