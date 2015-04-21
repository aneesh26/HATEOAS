/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.activities;

import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealLink;
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
             
             appealList.add(appealLink);
        }
        
        
     /*   return new AppealListRepresentation(appealList, 
                new Link(Representation.RELATIONS_URI + "appeals", appealsUri)
                );
       */ 
        return AppealListRepresentation.createResponseAppealListRepresentation(appealList); 
    }
    
}
