/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver.representations;

import edu.asu.mscs.ashastry.appealserver.activities.InvalidOrderException;
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
            throw new InvalidOrderException(e);
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
            throw new InvalidOrderException(ex);
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

   
    
