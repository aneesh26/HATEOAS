/**
 * Copyright 2015 Aneesh Shastry,
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: Grade Appeal HATEOAS application using RESTful web services. Using 
 *          this function, a client can  Create, Update, FollowUp, Approve,
 *          Withdraw an appeal. Also, the client can get a list of pending 
 *          appeals for followup
 *
 * @author Aneesh Shastry ashastry@asu.edu
 *         MS Computer Science, CIDSE, IAFSE, Arizona State University
 * @version April 24, 2015
 */


package edu.asu.mscs.ashastry.appealserver.model;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author A
 */
public class Appeal {
    
    private int appealID;
    private String appealContent;
    @XmlTransient
    private AppealStatus status = AppealStatus.START;

    public Appeal() {
    }

    public Appeal(int appealID, String appealConent) {
       this(appealID, appealConent, AppealStatus.START);
    }

    
    
    public Appeal(int appealID, String appealConent, AppealStatus appealStatus) {
        this.appealID = appealID;
        this.appealContent = appealConent;
        this.status = appealStatus;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Appeal ID: " + appealID + "\n");
        sb.append("Appeal Status: " + status + "\n");
        sb.append("Appeal Content: " + appealContent + "\n");
        
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
     * @return the appealContent
     */
    public String getAppealContent() {
        return appealContent;
    }

    /**
     * @param appealContent the appealConent to set
     */
    public void setAppealContent(String appealContent) {
        this.appealContent = appealContent;
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


