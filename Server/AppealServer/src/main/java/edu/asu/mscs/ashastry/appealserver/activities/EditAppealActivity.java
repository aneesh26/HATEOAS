package edu.asu.mscs.ashastry.appealserver.activities;

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


import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import java.util.Date;



/**
 *
 * @author A
 */
public class EditAppealActivity {

    public EditAppealActivity() {
    }
    
    
    
    public AppealRepresentation edit(Appeal appeal, AppealServerUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotCreated(appealIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchAppealException();
        }

        if (!appealCanBeEdited(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        storedAppeal.setStatus(AppealStatus.PENDING);
        
        Date date = new Date();
        
        storedAppeal.setAppealContent(appeal.getAppealContent() + "\n\nAppeal updated on " + date.toString());
        
        //replacing the current appeal with the updated appeal object
        repository.store(appealIdentifier,storedAppeal);
       

        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeEdited(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.PENDING;
    }
    
    
}
