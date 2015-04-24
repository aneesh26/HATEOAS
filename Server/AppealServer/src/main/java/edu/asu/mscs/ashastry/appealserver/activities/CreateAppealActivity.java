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


package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import edu.asu.mscs.ashastry.appealserver.representations.Link;
import edu.asu.mscs.ashastry.appealserver.representations.Representation;

/**
 *
 * @author A
 */
public class CreateAppealActivity {
    
    public AppealRepresentation create(Appeal appeal, AppealServerUri requestUri) {
        appeal.setStatus(AppealStatus.PENDING);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        AppealServerUri appealUri = new AppealServerUri(requestUri.getBaseUri() + "/appeals/" + identifier.toString());
        AppealServerUri withdrawUri = new AppealServerUri(requestUri.getBaseUri() + "/appeals/" + identifier.toString() + "/withdraw");
         AppealServerUri followUpUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/followup");
        AppealServerUri approveUri = new AppealServerUri(requestUri.getBaseUri() + "/appeals/" + identifier.toString() + "/approve");
        return new AppealRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "edit", appealUri), 
                new Link(Representation.RELATIONS_URI + "withdraw", withdrawUri), 
                new Link(Representation.RELATIONS_URI + "approve", approveUri),
                new Link(Representation.RELATIONS_URI + "followup", followUpUri),
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
    
}
