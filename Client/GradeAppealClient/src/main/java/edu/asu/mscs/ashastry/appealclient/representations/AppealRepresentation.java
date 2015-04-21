/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealclient.representations;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.asu.mscs.ashastry.appealclient.activities.InvalidOrderException;
import edu.asu.mscs.ashastry.appealclient.activities.UriExchange;
import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.AppealStatus;
import edu.asu.mscs.ashastry.appealclient.model.Item;
import edu.asu.mscs.ashastry.appealclient.model.Location;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author A
 */

@XmlRootElement(name = "appeal", namespace = Representation.APPEALS_NAMESPACE)

public class AppealRepresentation extends Representation {
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepresentation.class);

    @XmlElement(name = "appealID", namespace = Representation.APPEALS_NAMESPACE)
    private int ID;
    @XmlElement(name = "appealContent", namespace = Representation.APPEALS_NAMESPACE)
    private String content;
    @XmlElement(name = "status", namespace = Representation.APPEALS_NAMESPACE)
    private AppealStatus status;

    public AppealRepresentation() {
         LOG.debug("In AppealRepresentation Constructor");
    }

    public AppealRepresentation(int ID, String content, AppealStatus status) {
        this.ID = ID;
        this.content = content;
        this.status = status;
    }
    
     public static AppealRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);
                
        AppealRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidOrderException(e);
        }
        
        LOG.debug("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }
    
    public static AppealRepresentation createResponseOrderRepresentation(Appeal appeal, AppealServerUri appealUri) {
        LOG.info("Creating a Response Order for order = {} and order URI", appeal.toString(), appealUri.toString());
        
        AppealRepresentation appealRepresentation;     
        
        AppealServerUri withdrawUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/withdraw");
        AppealServerUri approveUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/approve");
        AppealServerUri followUpUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/followup");
        AppealServerUri selfUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString());
        
        LOG.debug("Withdraw URI = {}", withdrawUri);
        
        if(appeal.getStatus() == AppealStatus.PENDING) {
            LOG.debug("The order status is {}", AppealStatus.PENDING);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "edit", appealUri), 
                    new Link(RELATIONS_URI + "withdraw", withdrawUri), 
                    new Link(RELATIONS_URI + "approve", approveUri), 
                    new Link(RELATIONS_URI + "followUp", followUpUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.COMPLETED) {
            LOG.debug("The appeal status is {}", AppealStatus.COMPLETED);
            appealRepresentation = new AppealRepresentation(appeal, new Link(RELATIONS_URI + "view",selfUri));
        } else if(appeal.getStatus() == AppealStatus.WITHDRAWN) {
            LOG.debug("The appeal status is {}", AppealStatus.WITHDRAWN);
            appealRepresentation = new AppealRepresentation(appeal, new Link(RELATIONS_URI + "view",selfUri));
        } else if(appeal.getStatus() == AppealStatus.START) {
            LOG.debug("The appeal status is {}", AppealStatus.START);
            appealRepresentation = new AppealRepresentation(appeal);            
        } else {
            LOG.debug("The appeal status is in an unknown status");
            throw new RuntimeException("Unknown appeal Status");
        }
        
        LOG.debug("The appeal representation created for the Create Response Appeal Representation is {}", appealRepresentation);
        
        return appealRepresentation;
    }
    
    public AppealRepresentation(Appeal appeal, Link... links) {
        LOG.info("Creating an Appeal Representation for order = {} and links = {}", appeal.toString(), links.toString());
        
        try {
            
            this.ID = appeal.getAppealID();
            this.content = appeal.getAppealConent();
            this.status = appeal.getStatus();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidOrderException(ex);
        }
        
        LOG.debug("Created the AppealRepresentation {}", this);
    }
    
    @Override
    public String toString() {
        LOG.info("Converting Appeal Representation object to string");
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appeal getAppeal() {
        LOG.info("Retrieving the Appeal Representation");
        LOG.debug("ID = {}", ID);
        LOG.debug("Content = {}", content);
        if (ID < 1 || content == null) {
            throw new InvalidOrderException();
        }
        
        
        Appeal appeal = new Appeal(ID, content, status);
        
        LOG.debug("Retrieving the Appeal Representation {}", appeal);

        return appeal;
    }

    public Link getEditLink() {
        LOG.info("Retrieving the Edit link ");
        return getLinkByName(RELATIONS_URI + "edit");
    }
    
    public Link getApproveLink() {
        LOG.info("Retrieving the Approve link ");
        return getLinkByName(RELATIONS_URI + "approve");
    }

    public Link getWithdrawLink() {
        LOG.info("Retrieving the Withdraw link ");
        return getLinkByName(RELATIONS_URI + "withdraw");
    }

    public Link getSelfLink() {
        LOG.info("Retrieving the Self link ");
        return getLinkByName("self");
    }
    
      public Link getViewLink() {
        LOG.info("Retrieving the Self link ");
        return getLinkByName(RELATIONS_URI + "view");
    }
      
       public Link getFollowUpLink() {
        LOG.info("Retrieving the FollowUp link ");
        return getLinkByName(RELATIONS_URI + "followUp");
    }
    
    public AppealStatus getStatus() {
        LOG.info("Retrieving the appeal status {}", status);
        return status;
    }

   
    
}
