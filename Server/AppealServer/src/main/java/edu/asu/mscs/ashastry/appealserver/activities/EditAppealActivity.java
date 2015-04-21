package edu.asu.mscs.ashastry.appealserver.activities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.model.Order;
import edu.asu.mscs.ashastry.appealserver.model.OrderStatus;
import edu.asu.mscs.ashastry.appealserver.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealserver.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import edu.asu.mscs.ashastry.appealserver.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.RestbucksUri;
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
            throw new NoSuchOrderException();
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
