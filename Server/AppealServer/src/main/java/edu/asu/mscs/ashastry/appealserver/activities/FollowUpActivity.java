/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author A
 */
public class FollowUpActivity {

    public FollowUpActivity() {
    }
    
    public AppealRepresentation followUp(AppealServerUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotCreated(appealIdentifier)) { // Defensive check to see if we have the appeal
            throw new NoSuchOrderException();
        }

        if (!appealCanBeFollowedUp(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        storedAppeal.setStatus(storedAppeal.getStatus());
        Date date = new Date();
        storedAppeal.setAppealContent(storedAppeal.getAppealContent() + "\n\nAppeal follow up on: " + date.toString());
        
        //replacing the current appeal with the updated appeal object
        repository.store(appealIdentifier,storedAppeal);
       

        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeFollowedUp(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.PENDING;
    }
    
    
}
