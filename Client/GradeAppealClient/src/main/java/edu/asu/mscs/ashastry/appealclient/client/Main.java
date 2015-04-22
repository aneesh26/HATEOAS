package edu.asu.mscs.ashastry.appealclient.client;

import static edu.asu.mscs.ashastry.appealclient.model.OrderBuilder.order;

import java.net.URI;
import java.net.URISyntaxException;

import edu.asu.mscs.ashastry.appealclient.client.activities.Actions;
import edu.asu.mscs.ashastry.appealclient.client.activities.GetReceiptActivity;
import edu.asu.mscs.ashastry.appealclient.client.activities.PaymentActivity;
import edu.asu.mscs.ashastry.appealclient.client.activities.PlaceOrderActivity;
import edu.asu.mscs.ashastry.appealclient.client.activities.ReadOrderActivity;
import edu.asu.mscs.ashastry.appealclient.client.activities.UpdateOrderActivity;
import edu.asu.mscs.ashastry.appealclient.model.Order;
import edu.asu.mscs.ashastry.appealclient.model.OrderStatus;
import edu.asu.mscs.ashastry.appealclient.model.Payment;
import edu.asu.mscs.ashastry.appealclient.representations.Link;
import edu.asu.mscs.ashastry.appealclient.representations.OrderRepresentation;
//import edu.asu.mscs.ashastry.appealclient.representations.PaymentRepresentation;
//import edu.asu.mscs.ashastry.appealclient.representations.ReceiptRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.RestbucksUri;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import edu.asu.mscs.ashastry.appealclient.model.Appeal;
import edu.asu.mscs.ashastry.appealclient.model.AppealStatus;
import edu.asu.mscs.ashastry.appealclient.representations.AppealListRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealclient.representations.AppealServerUri;
import java.util.List;

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
        forgottenPath(serviceUri);
        badStart(badServiceUri);
        
    }

    private static void hangAround(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void happyPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("Step 1. Create the Appeal");
        System.out.println(String.format("About to start happy path test. Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(1,"Test Content  New Appeal - 1");
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
       // AppealRepresentation 
      //  OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(GRADEAPPEALS_MEDIA_TYPE).type(GRADEAPPEALS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
      
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
       
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
        
        //Edit the Appeal
        
        LOG.debug("\n\nStep 2. Edit the Appeal");
        System.out.println(String.format("About to update appeal at [%s] via POST", appealRepresentation.getEditLink().getUri().toString()));
        
        appeal = new Appeal(1,"The new Contnt as a result of Edit appeal");
        LOG.debug("Created base Appeal {}", appeal);
        Link editLink = appealRepresentation.getEditLink();
        LOG.debug("Created order Edit link {}", editLink);
        AppealRepresentation updatedRepresentation = client.resource(editLink.getUri()).accept(editLink.getMediaType()).type(editLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.debug("Created updated Appeal representation link {}", updatedRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
        
        //Follow up the appeal
        LOG.debug("\n\nStep 2.5 Followup on the Appeal");
        System.out.println(String.format("About to follow up on an appeal at [%s] via GET", updatedRepresentation.getFollowUpLink().getUri().toString()));
        Link followUpLink = updatedRepresentation.getFollowUpLink();
         AppealRepresentation followUpRepresentation = client.resource(followUpLink.getUri()).get(AppealRepresentation.class);
        LOG.debug("Created follo up Appeal representation link {}", followUpRepresentation);
        System.out.println(String.format("Appeal followed up at [%s]", followUpRepresentation.getEditLink().getUri().toString()));
        
        
        
        // Approve the appeal
        LOG.debug("\n\nStep 3. Approve the Appeal");
        System.out.println(String.format("About to approve appeal at [%s] via GET", updatedRepresentation.getApproveLink().getUri().toString()));
        
        Link approveLink = updatedRepresentation.getApproveLink();
        LOG.debug("Created appeal Approve link {}", approveLink);
       
        AppealRepresentation approveRepresentation = client.resource(approveLink.getUri()).get(AppealRepresentation.class);
       
        LOG.debug("Created approve Appeal representation link {}", approveRepresentation);
        System.out.println(String.format("Appeal approved at [%s]", approveRepresentation.getViewLink().getUri().toString()));
        
        
        
        
        //withdraw the appeal
        LOG.debug("\n\nStep 4. Withdrawing the Appeal");
        System.out.println(String.format("About to withdraw appeal at [%s] via GET", updatedRepresentation.getWithdrawLink().getUri().toString()));
         appeal = new Appeal(2,"Creating a new Appeal - New Appeal - 2");
         appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
      System.out.println(String.format("About to withdraw appeal at [%s] via POST", appealRepresentation.getWithdrawLink().getUri().toString()));
        
         
        Link withdrawLink = appealRepresentation.getWithdrawLink();
        LOG.debug("Created appeal Approve link {}", withdrawLink);
        
        //actual delete takes place here
        AppealRepresentation withdrawRepresentation = client.resource(withdrawLink.getUri()).delete(AppealRepresentation.class);
        
       
        LOG.debug("Created withdraw Appeal representation link {}", withdrawRepresentation);
        System.out.println(String.format("Appeal withdrawn at [%s]", withdrawRepresentation.getViewLink().getUri().toString()));
        
        
       
        
        
        LOG.debug("\n\nStep 5. Get an appeal List");
       
         appeal = new Appeal(8,"New Appeal - 8");
         appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
         
          appeal = new Appeal(9,"New Appeal - 9");
         appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
         
         
        
        System.out.println(String.format("About to view an appeal [%s] via GET", serviceUri.toString()));
        
        //Link Link = serviceUri;
        //LOG.debug("Retrieved the receipt link {} for payment represntation {}", receiptLink, paymentRepresentation);
        AppealListRepresentation appealListRepresentation = client.resource(serviceUri).get(AppealListRepresentation.class);
        
        LOG.debug("Created Get Appeal representation link {}", appealListRepresentation);
        
        List<Link> returnedLinks = appealListRepresentation.getLinks();
        
        for(Link l: returnedLinks){
            
            String id = (l.getRelValue()).substring((l.getRelValue()).lastIndexOf("/")+1,(l.getRelValue()).length());
            LOG.debug("Order No.: " + id + ", URI: "+ l.getUri().toString());  
        }
       System.out.println("Pausing the client, press a key to continue");
        System.in.read();
       // LOG.debug("\n Received Appeals : {}",appealListRepresentation.toStringAppealList() );
           
        // Try to update a different order
      
        /*
        LOG.info("\n\nStep 2. Try to update a different order");
        System.out.println(String.format("About to update an order with bad URI [%s] via POST", orderRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        order = order().withRandomItems().build();
        LOG.debug("Created base order {}", order);
        Link badLink = new Link("bad", new AppealServerUri(orderRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), RESTBUCKS_MEDIA_TYPE);
        LOG.debug("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new OrderRepresentation(order));
        LOG.debug("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update order with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));
        
        // Change the order
        LOG.debug("\n\nStep 3. Change the order");
        System.out.println(String.format("About to update order at [%s] via POST", orderRepresentation.getUpdateLink().getUri().toString()));
        order = order().withRandomItems().build();
        LOG.debug("Created base order {}", order);
        Link updateLink = orderRepresentation.getUpdateLink();
        LOG.debug("Created order update link {}", updateLink);
        OrderRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(OrderRepresentation.class, new OrderRepresentation(order));
        LOG.debug("Created updated order representation link {}", updatedRepresentation);
        System.out.println(String.format("Order updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
        
        // Pay for the order 
        LOG.debug("\n\nStep 4. Pay for the order");
        System.out.println(String.format("About to create a payment resource at [%s] via PUT", updatedRepresentation.getPaymentLink().getUri().toString()));
        Link paymentLink = updatedRepresentation.getPaymentLink();
        LOG.debug("Created payment link {} for updated order representation {}", paymentLink, updatedRepresentation);
        LOG.debug("paymentLink.getRelValue() = {}", paymentLink.getRelValue());
        LOG.debug("paymentLink.getUri() = {}", paymentLink.getUri());
        LOG.debug("paymentLink.getMediaType() = {}", paymentLink.getMediaType());
        Payment payment = new Payment(updatedRepresentation.getCost(), "A.N. Other", "12345677878", 12, 2999);
        LOG.debug("Created new payment object {}", payment);
      //  PaymentRepresentation  paymentRepresentation = client.resource(paymentLink.getUri()).accept(paymentLink.getMediaType()).type(paymentLink.getMediaType()).put(PaymentRepresentation.class, new PaymentRepresentation(payment));        
      //  LOG.debug("Created new payment representation {}", paymentRepresentation);
      //  System.out.println(String.format("Payment made, receipt at [%s]", paymentRepresentation.getReceiptLink().getUri().toString()));
        
        // Get a receipt
        LOG.debug("\n\nStep 5. Get a receipt");
     //   System.out.println(String.format("About to request a receipt from [%s] via GET", paymentRepresentation.getReceiptLink().getUri().toString()));
      //  Link receiptLink = paymentRepresentation.getReceiptLink();
      //  LOG.debug("Retrieved the receipt link {} for payment represntation {}", receiptLink, paymentRepresentation);
       // ReceiptRepresentation receiptRepresentation = client.resource(receiptLink.getUri()).get(ReceiptRepresentation.class);
       // System.out.println(String.format("Payment made, amount in receipt [%f]", receiptRepresentation.getAmountPaid()));
        
        // Check on the order status
        LOG.debug("\n\nStep 6. Check on the order status");
       // System.out.println(String.format("About to check order status at [%s] via GET", receiptRepresentation.getOrderLink().getUri().toString()));
       // Link orderLink = receiptRepresentation.getOrderLink();
   //     OrderRepresentation finalOrderRepresentation = client.resource(orderLink.getUri()).accept(RESTBUCKS_MEDIA_TYPE).get(OrderRepresentation.class);
      //  System.out.println(String.format("Final order placed, current status [%s]", finalOrderRepresentation.getStatus()));
        
        // Allow the barista some time to make the order
        LOG.debug("\n\nStep 7. Allow the barista some time to make the order");
        System.out.println("Pausing the client, press a key to continue");
        System.in.read();
        
        // Take the order if possible
        LOG.debug("\n\nStep 8. Take the order if possible");
     //   System.out.println(String.format("Trying to take the ready order from [%s] via DELETE. Note: the internal state machine must progress the order to ready before this should work, otherwise expect a 405 response.", receiptRepresentation.getOrderLink().getUri().toString()));
      //  ClientResponse finalResponse = client.resource(orderLink.getUri()).delete(ClientResponse.class);
  //      System.out.println(String.format("Tried to take final order, HTTP status [%d]", finalResponse.getStatus()));
   //     if(finalResponse.getStatus() == 200) {
   //         System.out.println(String.format("Order status [%s], enjoy your drink", finalResponse.getEntity(OrderRepresentation.class).getStatus()));
    //    }
                */
    }
                
    private static void abandonPath(URI serviceUri) throws Exception{
        
    
    LOG.info("************************************************************");
    LOG.info("Starting Abandon Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("Step 1. Create the Appeal");
        System.out.println(String.format("About to start happy path test. Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(5,"Abandon Path: Test Content");
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
       // AppealRepresentation 
      //  OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(GRADEAPPEALS_MEDIA_TYPE).type(GRADEAPPEALS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
      
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
       
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
         //withdraw the appeal
        LOG.debug("\n\nStep 4. Withdrawing the Appeal");
        System.out.println(String.format("About to withdraw appeal at [%s] via GET", appealRepresentation.getWithdrawLink().getUri().toString()));
       
        System.out.println(String.format("About to withdraw appeal at [%s] via POST", appealRepresentation.getWithdrawLink().getUri().toString()));
        
         
        Link withdrawLink = appealRepresentation.getWithdrawLink();
        LOG.debug("Created appeal Approve link {}", withdrawLink);
        
        //actual delete takes place here
        AppealRepresentation withdrawRepresentation = client.resource(withdrawLink.getUri()).delete(AppealRepresentation.class);
        
       
        LOG.debug("Created withdraw Appeal representation link {}", withdrawRepresentation);
        System.out.println(String.format("Appeal withdrawn at [%s]", withdrawRepresentation.getViewLink().getUri().toString()));
        
        
        
    }
    
    private static void forgottenPath(URI serviceUri) throws Exception{
         LOG.info("************************************************************");
    LOG.info("Starting Forgotten path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("Step 1. Create the Appeal");
        System.out.println(String.format("About to start forgotten path test. Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(6,"Forgotten Path: Test Content");
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
       // AppealRepresentation 
      //  OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(GRADEAPPEALS_MEDIA_TYPE).type(GRADEAPPEALS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
      
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
       
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
        
        //Follow up the appeal
        LOG.debug("\n\nStep 2 Followup on the Appeal");
        System.out.println(String.format("About to follow up on an appeal at [%s] via GET", appealRepresentation.getFollowUpLink().getUri().toString()));
        Link followUpLink = appealRepresentation.getFollowUpLink();
         AppealRepresentation followUpRepresentation = client.resource(followUpLink.getUri()).get(AppealRepresentation.class);
        LOG.debug("Created follow up Appeal representation link {}", followUpRepresentation);
        System.out.println(String.format("Appeal followed up at [%s]", followUpRepresentation.getEditLink().getUri().toString()));
        
         // Approve the appeal
        LOG.debug("\n\nStep 3. Approve the Appeal");
        System.out.println(String.format("About to approve appeal at [%s] via GET", followUpRepresentation.getApproveLink().getUri().toString()));
        
        Link approveLink = followUpRepresentation.getApproveLink();
        LOG.debug("Created appeal Approve link {}", approveLink);
       
        AppealRepresentation approveRepresentation = client.resource(approveLink.getUri()).get(AppealRepresentation.class);
       
        LOG.debug("Created approve Appeal representation link {}", approveRepresentation);
        System.out.println(String.format("Appeal approved at [%s]", approveRepresentation.getViewLink().getUri().toString()));
       
   
    }
    
    private static void badStart(URI serviceUri) throws Exception{
         LOG.info("************************************************************");
    LOG.info("Starting Bad Start case path Test with Service URI {}", serviceUri);
        // Place the order
        LOG.info("Step 1. Create the Appeal");
        System.out.println(String.format("About to start bad start path test. Creating appeal at [%s] via POST", serviceUri.toString()));
        //Order order = order().withRandomItems().build();
        Appeal appeal = new Appeal(7,"Bad Start Path: Test Content");
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
       // AppealRepresentation 
      //  OrderRepresentation orderRepresentation = client.resource(serviceUri).accept(GRADEAPPEALS_MEDIA_TYPE).type(GRADEAPPEALS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        try{
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        }
        //bad link exception
        catch(UniformInterfaceException ble){
            if((ble.getMessage().toString().substring(ble.getMessage().toString().length()-13, ble.getMessage().toString().length())).equals("404 Not Found")){
                LOG.debug(ble.getMessage().toString());
            }
            
        }
        //AppealStatus status = appealRepresentation.getStatus();
        
        
        // OrderRepresentation orderRepresentation1 = client.resource(serviceUri).accept(RESTBUCKS_MEDIA_TYPE).type(RESTBUCKS_MEDIA_TYPE).post(OrderRepresentation.class, new ClientOrder(order));
        
    }
}
