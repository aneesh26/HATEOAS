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


package edu.asu.mscs.ashastry.appealclient.client;

import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.AppealStatus;
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
