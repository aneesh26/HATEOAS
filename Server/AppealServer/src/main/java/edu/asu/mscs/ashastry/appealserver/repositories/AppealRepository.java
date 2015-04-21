/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.repositories;

import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.Identifier;
import edu.asu.mscs.ashastry.appealserver.model.Order;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author A
 */
public class AppealRepository {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderRepository.class);

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
        return getBackingStore().get(identifier.toString());
     }
    
    public Appeal take(Identifier identifier) {
        LOG.debug("Removing the Appeal object for identifier {}", identifier);
        Appeal appeal = getBackingStore().get(identifier.toString());
        remove(identifier);
        return appeal;
    }

    public Identifier store(Appeal appeal) {
        LOG.debug("Storing a new Appeal object");
                
        Identifier id = new Identifier();
        LOG.debug("New Appeal object id is {}", id);
                
        getBackingStore().put(id.toString(), appeal);
        return id;
    }
    
    public void store(Identifier identifier, Appeal appeal) {
        LOG.debug("Storing again the Appeal object with id", identifier);
        getBackingStore().put(identifier.toString(), appeal);
    }

    public boolean has(Identifier identifier) {
        LOG.debug("Checking to see if there is an Appeal object associated with the id {} in the Appeal store", identifier);
        
        boolean result =  getBackingStore().containsKey(identifier.toString());
        LOG.debug("The result of the search is {}", result);
        
        return result;
    }

    public void remove(Identifier identifier) {
        LOG.debug("Removing from storage the Appeal object with id", identifier);
        getBackingStore().remove(identifier.toString());
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
        for(Map.Entry<String, Appeal> entry : getBackingStore().entrySet()) {
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
        return getBackingStore().size();
    }

    /**
     * @return the backingStore
     */
    public HashMap<String, Appeal> getBackingStore() {
        return backingStore;
    }
    
    
}
