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
import edu.asu.mscs.ashastry.appealserver.model.AppealLink;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealserver.representations.AppealListRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import edu.asu.mscs.ashastry.appealserver.representations.Link;
import edu.asu.mscs.ashastry.appealserver.representations.Representation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author A
 */
public class CreateAppealListActivity {
    
     public AppealListRepresentation createList(AppealServerUri requestUri) {
         
         ArrayList<AppealLink> appealList = new ArrayList<AppealLink>();
         
         
         AppealRepository appealsRepository = AppealRepository.current();
         HashMap<String, Appeal> appealsMap= appealsRepository.getBackingStore();
         
         Iterator it = appealsMap.entrySet().iterator();
        
         while(it.hasNext()) {
             HashMap.Entry pair = (HashMap.Entry)it.next();
             AppealServerUri appealsUri = new AppealServerUri(requestUri.getBaseUri() + "/appeals/" + pair.getKey());
             AppealLink appealLink = new AppealLink(((Appeal)pair.getValue()).getAppealID(),appealsUri);
             if(((Appeal)pair.getValue()).getStatus() == AppealStatus.PENDING){
                 appealList.add(appealLink);
             }
             
        }
        
        return AppealListRepresentation.createResponseAppealListRepresentation(appealList); 
    }
    
}
