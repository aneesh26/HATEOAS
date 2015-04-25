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


package edu.asu.mscs.ashastry.appealclient.model;

import edu.asu.mscs.ashastry.appealclient.representations.AppealServerUri;


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
