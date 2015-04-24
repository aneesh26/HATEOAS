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


package edu.asu.mscs.ashastry.appealserver.representations;

import edu.asu.mscs.ashastry.appealserver.activities.InvalidAppealException;
import edu.asu.mscs.ashastry.appealserver.model.Appeal;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import static edu.asu.mscs.ashastry.appealserver.representations.Representation.RELATIONS_URI;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
            throw new InvalidAppealException(e);
        }
        
        LOG.debug("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }
    
    public static AppealRepresentation createResponseAppealRepresentation(Appeal appeal, AppealServerUri appealUri) {
        LOG.info("Creating a Response Appeal for appeal = {} and order URI", appeal.toString(), appealUri.toString());
        
        AppealRepresentation appealRepresentation;     
        
        AppealServerUri withdrawUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/withdraw");
        AppealServerUri approveUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/approve");
        AppealServerUri followUpUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString() + "/followup");
        AppealServerUri selfUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals/" + appealUri.getId().toString());   
     
        LOG.debug("Withdraw URI = {}", withdrawUri);
         LOG.debug("Approve URI = {}", approveUri);
       
        
        if(appeal.getStatus() == AppealStatus.PENDING) {
            LOG.debug("The appeal status is {}", AppealStatus.PENDING);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "edit", appealUri), 
                    new Link(RELATIONS_URI + "withdraw", withdrawUri),
                    new Link(RELATIONS_URI + "approve", approveUri), 
                    new Link(RELATIONS_URI + "followup", followUpUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.COMPLETED) {
            LOG.debug("The appeal status is {}", AppealStatus.COMPLETED);
            appealRepresentation = new AppealRepresentation(appeal, new Link(RELATIONS_URI + "view", selfUri));
        } else if(appeal.getStatus() == AppealStatus.WITHDRAWN) {
            LOG.debug("The appeal status is {}", AppealStatus.WITHDRAWN);
            appealRepresentation = new AppealRepresentation(appeal, new Link(RELATIONS_URI + "view" ,selfUri));
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
            this.content = appeal.getAppealContent();
            this.status = appeal.getStatus();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
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
            throw new InvalidAppealException();
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
        LOG.info("Retrieving the View link ");
        return getLinkByName(RELATIONS_URI + "view");
    }
      
      public Link getFollowUpLink() {
        LOG.info("Retrieving the FollowUp link ");
        return getLinkByName(RELATIONS_URI + "followup");
    }
    
    public AppealStatus getStatus() {
        LOG.info("Retrieving the appeal status {}", status);
        return status;
    }

   
    
}
