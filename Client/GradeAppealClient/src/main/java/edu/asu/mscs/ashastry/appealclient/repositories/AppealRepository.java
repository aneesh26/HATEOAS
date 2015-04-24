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


package edu.asu.mscs.ashastry.appealclient.repositories;

import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppealRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepository.class);

    private static final AppealRepository theRepository = new AppealRepository();
    private HashMap<String, Appeal> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static AppealRepository current() {
        return theRepository;
    }
    
    private AppealRepository(){
        LOG.debug("AppealRepository Constructor");
    }
    
    public Appeal get(Identifier identifier) {
        LOG.debug("Retrieving Appeal object for identifier {}", identifier);
        return backingStore.get(identifier.toString());
     }
    
    public Appeal take(Identifier identifier) {
        LOG.debug("Removing the Appeal object for identifier {}", identifier);
        Appeal appeal = backingStore.get(identifier.toString());
        remove(identifier);
        return appeal;
    }

    public Identifier store(Appeal appeal) {
        LOG.debug("Storing a new Appeal object");
                
        Identifier id = new Identifier();
        LOG.debug("New Appeal object id is {}", id);
                
        backingStore.put(id.toString(), appeal);
        return id;
    }
    
    public void store(Identifier identifier, Appeal appeal) {
        LOG.debug("Storing again the Appeal object with id", identifier);
        backingStore.put(identifier.toString(), appeal);
    }

    public boolean has(Identifier identifier) {
        LOG.debug("Checking to see if there is an Appeal object associated with the id {} in the Appeal store", identifier);
        
        boolean result =  backingStore.containsKey(identifier.toString());
        LOG.debug("The result of the search is {}", result);
        
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.debug("Removing from storage the Appeal object with id", identifier);
        backingStore.remove(identifier.toString());
    }
    
    public boolean appealCreated(Identifier identifier) {
        LOG.debug("Checking to see if the appeal with id = {} has been created", identifier);
        return AppealRepository.current().has(identifier);
    }
    
    public boolean appealNotCreated(Identifier identifier) {
        LOG.debug("Checking to see if the appeal with id = {} has not been created", identifier);
        return !appealCreated(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Appeal> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<>();
    }

    public int size() {
        return backingStore.size();
    }
}
