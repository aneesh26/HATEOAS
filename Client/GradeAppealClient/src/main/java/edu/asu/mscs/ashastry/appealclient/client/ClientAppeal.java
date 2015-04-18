/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealclient.client;

import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.AppealStatus;
import edu.asu.mscs.ashastry.appealclient.model.Item;
import edu.asu.mscs.ashastry.appealclient.model.Location;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.Representation;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author A
 */

 @XmlRootElement(name = "appeal", namespace = Representation.APPEALS_NAMESPACE)

public class ClientAppeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientAppeal.class);
    
    @XmlElement(name = "appealID", namespace = Representation.APPEALS_NAMESPACE)
    private int ID;
    @XmlElement(name = "appealContent", namespace = Representation.APPEALS_NAMESPACE)
    private String content;
    @XmlElement(name = "status", namespace = Representation.APPEALS_NAMESPACE)
    private AppealStatus status;
    
    private ClientAppeal(){}
    
    public ClientAppeal(Appeal appeal) {
        LOG.debug("Executing ClientAppeal constructor");
        this.ID = appeal.getAppealID();
        this.content = appeal.getAppealConent();
    }
    
    public Appeal getAppeal() {
        LOG.debug("Executing ClientAppeal.getAppeal");
        return new Appeal(ID, content, status);
    }
    
    public int getID() {
        LOG.debug("Executing ClientAppeal.getID");
        return ID;
    }
    
     public String getContent() {
        LOG.debug("Executing ClientAppeal.getContent");
        return content;
    }
    
   
    @Override
    public String toString() {
        LOG.debug("Executing ClientAppeal.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientAppeal.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public AppealStatus getStatus() {
        LOG.debug("Executing ClientAppeal.getStatus");
        return status;
    }

    
    
}
