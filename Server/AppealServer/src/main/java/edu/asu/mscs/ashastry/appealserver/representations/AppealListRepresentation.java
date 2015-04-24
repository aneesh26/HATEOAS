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
import edu.asu.mscs.ashastry.appealserver.model.AppealLink;
import edu.asu.mscs.ashastry.appealserver.model.AppealStatus;
import static edu.asu.mscs.ashastry.appealserver.representations.Representation.RELATIONS_URI;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeal", namespace = Representation.APPEALS_NAMESPACE)

public class AppealListRepresentation extends Representation {
    private static final Logger LOG = LoggerFactory.getLogger(AppealListRepresentation.class);

    @XmlElement(name = "appealList", namespace = Representation.APPEALS_NAMESPACE)
    private ArrayList<AppealLink> appealList;
    
    public AppealListRepresentation() {
         LOG.debug("In AppealListRepresentation Constructor");
    }

    public AppealListRepresentation(int ID, ArrayList<AppealLink> appealList, AppealStatus status) {
        this.appealList = appealList;
     }
    
     public static AppealListRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);
                
        AppealListRepresentation appealListRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealListRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealListRepresentation = (AppealListRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }
        
        LOG.debug("Generated the object {}", appealListRepresentation);
        return appealListRepresentation;
    }
    
    public static AppealListRepresentation createResponseAppealListRepresentation(ArrayList<AppealLink> appealList) {
        //LOG.info("Creating a Appleal List = {} and  URI", appealList.toString(), appealUri.toString());
        
        List<Link> listOfLinks = new ArrayList<Link>();
        AppealListRepresentation appealListRepresentation;     
        
       // AppealServerUri appealsUri = new AppealServerUri(appealUri.getBaseUri() + "/appeals");
        for(AppealLink a : appealList){
            
            listOfLinks.add(new Link(Representation.RELATIONS_URI + a.getAppealiD(), a.getAppealUri()));
        }
        
        
        appealListRepresentation = new AppealListRepresentation(appealList, listOfLinks);
        
        LOG.debug("The appeal representation created for the Create Response Appeal Representation is {}", appealListRepresentation);
        
        return appealListRepresentation;
    }
    
    public AppealListRepresentation(ArrayList<AppealLink> appealList, List<Link> links) {
        LOG.info("Creating an Appeal Representation for order = {} and links = {}", appealList.toString(), links.toString());
        
        try {
            
            this.appealList = appealList;
            this.links = links;
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }
        
        LOG.debug("Created the AppealListRepresentation {}", this);
    }
    
    @Override
    public String toString() {
        LOG.info("Converting AppealListRepresentation object to string");
        try {
            JAXBContext context = JAXBContext.newInstance(AppealListRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<AppealLink> getAppealList() {
        LOG.info("Retrieving the AppealList Representation");
              
        LOG.debug("Retrieving the AppealList ", this.appealList);

        return this.appealList;
    }

    public Link getAppealsLink() {
        LOG.info("Retrieving the Appeals link ");
        return getLinkByName(RELATIONS_URI + "appeals");
    }
    
    public List<Link> getLinks(){
        return this.links;
    }
    
}

   
    
