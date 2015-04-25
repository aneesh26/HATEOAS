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


import java.net.URI;
import java.net.URISyntaxException;

import edu.asu.mscs.ashastry.appealclient.representations.Link;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.AppealStatus;
import edu.asu.mscs.ashastry.appealclient.representations.AppealListRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.AppealServerUri;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String APPEALS_MEDIA_TYPE = "application/vnd.cse564-appeals+xml";
    private static final String RESTBUCKS_MEDIA_TYPE = "application/vnd.restbucks+xml";
    
    private static final long ONE_MINUTE = 60000; 
    
    private static final String ENTRY_POINT_URI = "http://localhost:8080/AppealServer/webresources/appeals";
    private static final String BAD_ENTRY_POINT_URI = "http://localhost:8080/AppealServer/webresources/appeals/bad_link";

    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
        URI badServiceUri = new URI(BAD_ENTRY_POINT_URI);
        happyPathTest(serviceUri);
        System.out.println("Pausing the client, press a key to start Abandon Path");
        System.in.read();
        abandonPath(serviceUri);
        System.out.println("Pausing the client, press a key to start Forgotten Path");
        System.in.read();
        forgottenPath(serviceUri);
        System.out.println("Pausing the client, press a key to start Bad Start Path");
        System.in.read();
        badStart(badServiceUri);
        System.out.println("Pausing the client, press a key to start Bad ID Path");
        System.in.read();
        badID(serviceUri);
        
        
    }

    private static void hangAround(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void happyPathTest(URI serviceUri) throws Exception {
        System.out.println("\n////////////////////// START OF HAPPY PATH \\\\\\\\\\\\\\\\\\\\\\\n\n");
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("+++++++++++++++ Step 1. Create the Appeal +++++++++++++++");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(1,"Test Content  New Appeal - 1");
    //    LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
     //   LOG.debug("Created client {}", client);
       // AppealRepresentation 
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
       
       // LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());

        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        printAppeal(appealRepresentation.getAppeal());
         System.out.println("Links provided by the server:");
        appealRepresentation.getListOfLinks();
        
        //Edit the Appeal
        
        LOG.debug("\n\n+++++++++++++++ Step 2. Edit the Appeal +++++++++++++++");
        System.out.println(String.format("About to update appeal at [%s] via POST", appealRepresentation.getEditLink().getUri().toString()));
        
        Link getLink = appealRepresentation.getSelfLink();
        //LOG.debug("Created order Edit link {}", editLink);
        AppealRepresentation getRepresentation = client.resource(getLink.getUri()).get(AppealRepresentation.class);
        System.out.println("\nRetrieved Appeal Content from Server");
        printAppeal(getRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        getRepresentation.getListOfLinks();
         
        
        Appeal updatedAppeal = new Appeal(getRepresentation.getAppeal().getAppealID(),(getRepresentation.getAppeal().getAppealConent() + "\nEditing Content for the appeal"));
        
     //   appeal = new Appeal(1,"The new Contnt as a result of Edit appeal");
        System.out.println("Updated Content for the appeal at client side");
        printAppeal(updatedAppeal);
        
        //LOG.debug("Created base Appeal {}", appeal);
        Link editLink = getRepresentation.getEditLink();
        //LOG.debug("Created order Edit link {}", editLink);
        AppealRepresentation updatedRepresentation = client.resource(editLink.getUri()).accept(editLink.getMediaType()).type(editLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(updatedAppeal));
         
        //LOG.debug("Created updated Appeal representation link {}", updatedRepresentation);
        System.out.println("Updated Appeal Content from Server");
        printAppeal(updatedRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        updatedRepresentation.getListOfLinks();
        System.out.println(String.format("Appeal updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
        
        //Follow up the appeal
        /*LOG.debug("\n\nStep 3. Followup on the Appeal");
        System.out.println(String.format("About to follow up on an appeal at [%s] via GET", updatedRepresentation.getFollowUpLink().getUri().toString()));
        Link followUpLink = updatedRepresentation.getFollowUpLink();
         AppealRepresentation followUpRepresentation = client.resource(followUpLink.getUri()).get(AppealRepresentation.class);
        LOG.debug("Created follo up Appeal representation link {}", followUpRepresentation);
        System.out.println(String.format("Appeal followed up at [%s]", followUpRepresentation.getEditLink().getUri().toString()));
        printApppeal(followUpRepresentation);
        */
        
        // Approve the appeal
        LOG.debug("\n\n+++++++++++++++ Step 3. Approve the Appeal +++++++++++++++");
        System.out.println(String.format("About to approve appeal at [%s] via GET", updatedRepresentation.getApproveLink().getUri().toString()));
        
        Link approveLink = updatedRepresentation.getApproveLink();
        //LOG.debug("Created appeal Approve link {}", approveLink);
       
        AppealRepresentation approveRepresentation = client.resource(approveLink.getUri()).get(AppealRepresentation.class);
       
        System.out.println("Approved Appeal Content from Server");
        printAppeal(approveRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        approveRepresentation.getListOfLinks();
        //LOG.debug("Created approve Appeal representation link {}", approveRepresentation);
        System.out.println(String.format("Appeal approved at [%s]", approveRepresentation.getViewLink().getUri().toString()));
        
       
        LOG.info("\n\n*****************************************************************************\n\n");
    }
                
    private static void abandonPath(URI serviceUri) throws Exception{
        
    
    LOG.info("\n\n*****************************************************************************\n\n");
    System.out.println("\n////////////////////// START OF ABANDON PATH \\\\\\\\\\\\\\\\\\\\\\\n\n");
    LOG.info("Starting Abandon Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("+++++++++++++++ Step 1. Create the Appeal +++++++++++++++");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(5,"Abandon Path: Test Content");
        
       // LOG.debug("Created base appeal {}", appeal);
        
        Client client = Client.create();
       // LOG.debug("Created client {}", client);
       // AppealRepresentation 
      //  OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(GRADEAPPEALS_MEDIA_TYPE).type(GRADEAPPEALS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
      
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
       
       // LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
        System.out.println("Created Appeal Content from Server");
        printAppeal(appealRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        appealRepresentation.getListOfLinks();
        
         //withdraw the appeal
        LOG.debug("\n\n+++++++++++++++ Step 2. Withdrawing the Appeal +++++++++++++++");
        System.out.println(String.format("About to withdraw appeal at [%s] via DELETE", appealRepresentation.getWithdrawLink().getUri().toString()));
        
        Link withdrawLink = appealRepresentation.getWithdrawLink();
       // LOG.debug("Created appeal Approve link {}", withdrawLink);
        
        //actual delete takes place here
        AppealRepresentation withdrawRepresentation = client.resource(withdrawLink.getUri()).delete(AppealRepresentation.class);
        
       
       // LOG.debug("Created withdraw Appeal representation link {}", withdrawRepresentation);
        System.out.println(String.format("Appeal withdrawn at [%s]", withdrawRepresentation.getViewLink().getUri().toString()));
        
        System.out.println("Withdrawn Appeal Content from Server");
        printAppeal(withdrawRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        withdrawRepresentation.getListOfLinks();
        
        LOG.info("\n\n*****************************************************************************\n\n");
        
    }
    
    private static void forgottenPath(URI serviceUri) throws Exception{
       LOG.info("\n\n*****************************************************************************\n\n");
       System.out.println("\n////////////////////// START OF FORGOTTEN PATH \\\\\\\\\\\\\\\\\\\\\\\n\n");
    LOG.info("Starting Forgotten path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("+++++++++++++++ Step 1. Create the Appeal +++++++++++++++");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(6,"Forgotten Path: Test Content");
        //LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        //LOG.debug("Created client {}", client);
       // AppealRepresentation 
      //  OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(GRADEAPPEALS_MEDIA_TYPE).type(GRADEAPPEALS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
      
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
       
        //LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println("Withdrawn Appeal Content from Server");
        printAppeal(appealRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        appealRepresentation.getListOfLinks();
        
        //Follow up the appeal
        LOG.debug("\n\n+++++++++++++++ Step 2 Followup on the Appeal +++++++++++++++");
        System.out.println(String.format("About to follow up on an appeal at [%s] via GET", appealRepresentation.getFollowUpLink().getUri().toString()));
        Link followUpLink = appealRepresentation.getFollowUpLink();
         AppealRepresentation followUpRepresentation = client.resource(followUpLink.getUri()).get(AppealRepresentation.class);
        //LOG.debug("Created follow up Appeal representation link {}", followUpRepresentation);
        System.out.println(String.format("Appeal followed up at [%s]", followUpRepresentation.getEditLink().getUri().toString()));
        System.out.println("Followed up Appeal Content from Server");
        printAppeal(followUpRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        followUpRepresentation.getListOfLinks();
         // Approve the appeal
        LOG.debug("\n\n+++++++++++++++ Step 3. Approve the Appeal +++++++++++++++");
        System.out.println(String.format("About to approve appeal at [%s] via GET", followUpRepresentation.getApproveLink().getUri().toString()));
        
        Link approveLink = followUpRepresentation.getApproveLink();
        LOG.debug("Created appeal Approve link {}", approveLink);
       
        AppealRepresentation approveRepresentation = client.resource(approveLink.getUri()).get(AppealRepresentation.class);
       
       // LOG.debug("Created approve Appeal representation link {}", approveRepresentation);
        System.out.println(String.format("Appeal approved at [%s]", approveRepresentation.getViewLink().getUri().toString()));
        System.out.println("Approved Appeal Content from Server");
        printAppeal(approveRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        approveRepresentation.getListOfLinks();
       LOG.info("\n\n*****************************************************************************\n\n"); 
   
    }
    
    private static void badStart(URI serviceUri) throws Exception{
         LOG.info("\n\n*****************************************************************************\n\n");
         System.out.println("\n////////////////////// START OF BAD-URI START PATH \\\\\\\\\\\\\\\\\\\\\\\n\n");
    LOG.info("Starting Bad Start case path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("+++++++++++++++ Step 1. Create the Appeal +++++++++++++++");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(7,"Bad Start Path: Test Content");
        //LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        //LOG.debug("Created client {}", client);
       // AppealRepresentation 
        try{
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        }
        //bad link exception
        catch(UniformInterfaceException ble){
            if((ble.getMessage().toString().substring(ble.getMessage().toString().length()-13, ble.getMessage().toString().length())).equals("404 Not Found")){
                System.out.println(ble.getMessage().toString());
                System.out.println("\n\n404 Error, Incorrect entry point URI:" + serviceUri.toString());
            }
            
        }
         LOG.info("\n\n*****************************************************************************\n\n");
    }

    private static void badID(URI serviceUri) throws Exception {
          LOG.info("\n\n*****************************************************************************\n\n");
          System.out.println("\n////////////////////// START OF BAD-ID/FORGOT PATH \\\\\\\\\\\\\\\\\\\\\\\n\n");
        LOG.info("Starting Bad ID path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("+++++++++++++++ Step 1. Create the Appeal for Bad Start +++++++++++++++");
        System.out.println(String.format("Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(8,"Bad ID Path: Test Content 1");
        Appeal appeal2 = new Appeal(9,"Bad ID Path: Test Content 2");
        Appeal appeal3 = new Appeal(10,"Bad ID Path: Test Content 3");
     //   LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
       // LOG.debug("Created client {}", client);
       // AppealRepresentation 
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        AppealRepresentation appealRepresentation2 = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal2));
        AppealRepresentation appealRepresentation3 = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal3));
      
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
       
        //LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation2.getSelfLink().getUri().toString()));
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation3.getSelfLink().getUri().toString()));
         System.out.println("Created Appeal Content from Server");
        printAppeal(appealRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        appealRepresentation.getListOfLinks();
        printAppeal(appealRepresentation2.getAppeal());
        System.out.println("Links provided by the server:");
        appealRepresentation2.getListOfLinks();
        printAppeal(appealRepresentation3.getAppeal());
        System.out.println("Links provided by the server:");
        appealRepresentation3.getListOfLinks();
        
        System.out.println("Now the user sends out a bad appeal URI for a follow up.");
        
         //Follow up the appeal
        LOG.debug("\n\n+++++++++++++++ Step 2 Followup on the Appeal with bad URI +++++++++++++++");
      
        URI badServiceUri = new URI("http://localhost:8080/AppealServer/webresources/appeals/123456_Bad_URI_789/followup");
        try{
           AppealRepresentation followUpRepresentation = client.resource(badServiceUri).get(AppealRepresentation.class);
        }
        catch(UniformInterfaceException ble){
            if((ble.getMessage().toString().substring(ble.getMessage().toString().length()-13, ble.getMessage().toString().length())).equals("404 Not Found")){
                System.out.println("\n\n" + ble.getMessage().toString());
                System.out.println("\n\nIncorrect appeal URI:" + serviceUri.toString());
            }
            
        }
        
        System.out.println("\nNow, let us retrieve the list of all valid appeals from the server");
          System.out.println(String.format("About to retrieve list of appeals from server [%s] via GET", serviceUri.toString()));
        AppealListRepresentation appealListRepresentation = client.resource(serviceUri).get(AppealListRepresentation.class);
        
      //  LOG.debug("Created Get AppealList representation {}", appealListRepresentation);
        
        List<Link> returnedLinks = appealListRepresentation.getLinks();
        
        System.out.println("The following appeals were retrieved from the server:\n");
        HashMap<String,URI> list = new HashMap<>();
        
        for(Link l: returnedLinks){
           
            String id = (l.getRelValue()).substring((l.getRelValue()).lastIndexOf("/")+1,(l.getRelValue()).length());
            list.put(id,l.getUri());
           
        }
        
         Iterator it = list.entrySet().iterator();
         while (it.hasNext()) {
        HashMap.Entry pair = (HashMap.Entry)it.next();
          System.out.println("ID: " + pair.getKey() + ", URI: " + pair.getValue());
        }
        
       Set keyset=list.keySet(); 
       System.out.println("Enter the appeal ID to follow up " + keyset);
       
       
       // int input = Integer.parseInt(System.console().readLine());
       Scanner in = new Scanner(System.in);
       int input = in.nextInt();
        //int input = 8;
        URI uri;
        if(keyset.contains(String.valueOf(input))){
            uri = (URI)list.get(String.valueOf(input));
        }else{
            System.out.println("Incorrect Input, following up with the first ID from the list");
            Iterator iter = keyset.iterator();
            uri = (URI)list.get(iter.next());
        }
        
        AppealServerUri aURI = new AppealServerUri(uri);
        AppealServerUri appealUri = new AppealServerUri(aURI.toString() + "/followup");
        
        //Follow up the appeal
        LOG.debug("\n\n+++++++++++++++ Step 2 Followup on the Appeal +++++++++++++++");
        System.out.println(String.format("About to follow up on an appeal at [%s] via GET", appealUri.toString()));
       // Link followUpLink = appealRepresentation.getFollowUpLink();
         AppealRepresentation followUpRepresentation = client.resource(appealUri.getFullUri()).get(AppealRepresentation.class);
        //LOG.debug("Created follow up Appeal representation link {}", followUpRepresentation);
        System.out.println(String.format("Appeal followed up at [%s]", followUpRepresentation.getEditLink().getUri().toString()));
           System.out.println("Followed Up Appeal Content from Server");
        printAppeal(followUpRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        followUpRepresentation.getListOfLinks();
     
         // Approve the appeal
        LOG.debug("\n\n+++++++++++++++ Step 3. Approve the Appeal +++++++++++++++");
        System.out.println(String.format("About to approve appeal at [%s] via GET", followUpRepresentation.getApproveLink().getUri().toString()));
        
        Link approveLink = followUpRepresentation.getApproveLink();
        LOG.debug("Created appeal Approve link {}", approveLink);
       
        AppealRepresentation approveRepresentation = client.resource(approveLink.getUri()).get(AppealRepresentation.class);
       
        //LOG.debug("Created approve Appeal representation link {}", approveRepresentation);
        
        System.out.println(String.format("Appeal approved at [%s]", approveRepresentation.getViewLink().getUri().toString()));
          System.out.println("Approved Appeal Content from Server");
        printAppeal(approveRepresentation.getAppeal());
        System.out.println("Links provided by the server:");
        approveRepresentation.getListOfLinks();
     
        
         LOG.info("\n\n*****************************************************************************\n\n");
         
         System.out.println("\n////////////////////// END OF EXECUTION \\\\\\\\\\\\\\\\\\\\\\\n\n");
    }
    
    private static void printAppeal(Appeal appeal) {
       System.out.println("\n\n[START OF APPEAL CONTENT]");
        System.out.println("===================  (STATUS : " + appeal.getStatus().toString() + ")  ===================");
        System.out.println(appeal.getAppealConent());
        System.out.println("==============================================================");
        System.out.println("[END OF APPEAL CONTENT]");
        
        
    }

}
