/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.repositories.OrderRepository;
import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.model.Order;
import edu.asu.mscs.ashastry.appealserver.model.OrderStatus;
import edu.asu.mscs.ashastry.appealserver.repositories.AppealRepository;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import edu.asu.mscs.ashastry.appealserver.representations.Link;
import edu.asu.mscs.ashastry.appealserver.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.Representation;
import edu.asu.mscs.ashastry.appealserver.representations.RestbucksUri;

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
