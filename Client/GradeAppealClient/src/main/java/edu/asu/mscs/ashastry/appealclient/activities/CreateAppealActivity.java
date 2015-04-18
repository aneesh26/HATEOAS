/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealclient.activities;

import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.AppealStatus;
import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealclient.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealclient.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.AppealServerUri;
import edu.asu.mscs.ashastry.appealclient.representations.Link;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.Representation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;
/**
 *
 * @author A
 */
public class CreateAppealActivity {
    
    public AppealRepresentation create(Appeal appeal, AppealServerUri requestUri) {
        appeal.setStatus(AppealStatus.PENDING);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        AppealServerUri appealUri = new AppealServerUri(requestUri.getBaseUri() + "/appeal/" + identifier.toString());
        AppealServerUri withdrawUri = new AppealServerUri(requestUri.getBaseUri() + "/" + identifier.toString() + "/withdraw");
        return new AppealRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "edit", appealUri), 
                new Link(Representation.RELATIONS_URI + "withdraw", withdrawUri), 
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
    
}
