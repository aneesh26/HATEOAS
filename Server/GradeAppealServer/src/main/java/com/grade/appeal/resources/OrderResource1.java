package com.grade.appeal.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.grade.appeal.activities.RemoveOrderActivity;
import com.grade.appeal.activities.CreateOrderActivity;
import com.grade.appeal.activities.CreateOrderActivity;
import com.grade.appeal.activities.InvalidOrderException;
import com.grade.appeal.activities.InvalidOrderException;
import com.grade.appeal.activities.NoSuchOrderException;
import com.grade.appeal.activities.NoSuchOrderException;
import com.grade.appeal.activities.OrderDeletionException;
import com.grade.appeal.activities.OrderDeletionException;
import com.grade.appeal.activities.ReadOrderActivity;
import com.grade.appeal.activities.ReadOrderActivity;
import com.grade.appeal.activities.RemoveOrderActivity;
import com.grade.appeal.activities.UpdateException;
import com.grade.appeal.activities.UpdateException;
import com.grade.appeal.activities.UpdateOrderActivity;
import com.grade.appeal.activities.UpdateOrderActivity;
import com.grade.appeal.representations.OrderRepresentation;
import com.grade.appeal.representations.RestbucksUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/orders")
public class OrderResource1 {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    private @Context UriInfo uriInfo;

    public OrderResource1() {
        LOG.info("OrderResource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public OrderResource1(UriInfo uriInfo) {
        LOG.info("OrderResource constructor with mock uriInfo {}", uriInfo);
        this.uriInfo = uriInfo;  
    }
    
    @GET
    @Path("/{orderId}")
    @Produces("application/vnd.restbucks+xml")
    public Response getOrder() {
        LOG.info("Retrieving an Order Resource");
        
        Response response;
        
        try {
            OrderRepresentation responseRepresentation = new ReadOrderActivity().retrieveByUri(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(responseRepresentation).build();
        } catch(NoSuchOrderException nsoe) {
            LOG.debug("No such order");
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong retriveing the Order");
            response = Response.serverError().build();
        }
        
        LOG.debug("Retrieved the order resource", response);
        
        return response;
    }
    
    
    @POST
    @Path("/{orderId}/test")
    @Consumes("application/vnd.restbucks+xml")
    public void testMethod(){
        LOG.info("Reached the inner location !!");
    System.out.println("Reached the inner location !!");
    }
    
    @POST
    
    @Consumes("application/vnd.restbucks+xml")
    @Produces("application/vnd.restbucks+xml")
    public Response createOrder(String orderRepresentation) {
        LOG.info("Creating an Order Resource");
        
        Response response;
        
        try {
            OrderRepresentation responseRepresentation = new CreateOrderActivity().create(OrderRepresentation.fromXmlString(orderRepresentation).getOrder(), new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getUpdateLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            LOG.debug("Invalid Order - Problem with the orderrepresentation {}", orderRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOG.debug("Someting went wrong creating the order resource");
            response = Response.serverError().build();
        }
        
        LOG.debug("Resulting response for creating the order resource is {}", response);
        
        return response;
    }

    @DELETE
    @Path("/{orderId}")
    @Produces("application/vnd.restbucks+xml")
    public Response removeOrder() {
        LOG.info("Removing an Order Reource");
        
        Response response;
        
        try {
            OrderRepresentation removedOrder = new RemoveOrderActivity().delete(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(removedOrder).build();
        } catch (NoSuchOrderException nsoe) {
            LOG.debug("No such order resource to delete");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(OrderDeletionException ode) {
            LOG.debug("Problem deleting order resource");
            response = Response.status(405).header("Allow", "GET").build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong deleting the order resource");
            response = Response.serverError().build();
        }
        
        LOG.debug("Resulting response for deleting the order resource is {}", response);
        
        return response;
    }

    @POST
    @Path("/{orderId}")
    @Consumes("application/vnd.restbucks+xml")
    @Produces("application/vnd.restbucks+xml")
    public Response updateOrder(String orderRepresentation) {
        LOG.info("Updating an Order Resource");
        
        Response response;
        
        try {
            OrderRepresentation responseRepresentation = new UpdateOrderActivity().update(OrderRepresentation.fromXmlString(orderRepresentation).getOrder(), new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            LOG.debug("Invalid order in the XML representation {}", orderRepresentation);
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (NoSuchOrderException nsoe) {
            LOG.debug("No such order resource to update");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Problem updating the order resource");
            response = Response.status(Status.CONFLICT).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong updating the order resource");
            response = Response.serverError().build();
        } 
        
        LOG.debug("Resulting response for updating the order resource is {}", response);
        
        return response;
     }
}
