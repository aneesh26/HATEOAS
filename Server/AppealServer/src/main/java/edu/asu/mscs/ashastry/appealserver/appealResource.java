/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealserver;


import edu.asu.mscs.ashastry.appealserver.activities.CreateAppealActivity;
import edu.asu.mscs.ashastry.appealserver.activities.EditAppealActivity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;



import edu.asu.mscs.ashastry.appealserver.activities.RemoveOrderActivity;

import edu.asu.mscs.ashastry.appealserver.activities.InvalidOrderException;
import edu.asu.mscs.ashastry.appealserver.activities.NoSuchOrderException;
import edu.asu.mscs.ashastry.appealserver.activities.OrderDeletionException;
import edu.asu.mscs.ashastry.appealserver.activities.ReadOrderActivity;
import edu.asu.mscs.ashastry.appealserver.activities.UpdateException;
import edu.asu.mscs.ashastry.appealserver.activities.UpdateOrderActivity;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import edu.asu.mscs.ashastry.appealserver.representations.OrderRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.RestbucksUri;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * REST Web Service
 *
 * @author A
 */
@Path("/appeal")
public class appealResource {
    
 private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderResource.class);

    private @Context UriInfo uriInfo;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of appealResource
     */
    public appealResource() {
    }

    
    @POST
    @Consumes("application/vnd.cse564-appeals+xml")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response createAppeal(String appealRepresentation) {
        LOG.info("Creating an Appeal Resource");
        
        Response response;
        System.out.println("Reached !!");
       
        try {
            AppealRepresentation responseRepresentation = new CreateAppealActivity().create(AppealRepresentation.fromXmlString(appealRepresentation).getAppeal(), new AppealServerUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getEditLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            ioe.printStackTrace();
            
            LOG.debug("Invalid Order - Problem with the orderrepresentation {}", appealRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOG.debug("Someting went wrong creating the order resource");
            response = Response.serverError().build();
        }
        
        LOG.debug("Resulting response for creating the order resource is {}", response);
        
        return response;
    }
    
    @POST
    @Path("/{appealId}")
    @Consumes("application/vnd.cse564-appeals+xml")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response updateAppeal(String appealRepresentation) {
        LOG.info("Updating an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new EditAppealActivity().edit(AppealRepresentation.fromXmlString(appealRepresentation).getAppeal(), new AppealServerUri(uriInfo.getRequestUri()));
            //response = Response.ok().entity(responseRepresentation).build();
             response = Response.created(responseRepresentation.getEditLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidOrderException ioe) {
            LOG.debug("Invalid order in the XML representation {}", appealRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NoSuchOrderException nsoe) {
            LOG.debug("No such order resource to update");
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Problem updating the order resource");
            response = Response.status(Response.Status.CONFLICT).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong updating the order resource");
            response = Response.serverError().build();
        } 
        
        LOG.debug("Resulting response for updating the order resource is {}", response);
        
        return response;
     }
    
    /**
     * Retrieves representation of an instance of edu.asu.mscs.ashastry.appealserver.appealResource
     * @return an instance of java.lang.String
     
    @GET
    @Path("/{orderId}")
    @Produces(MediaType.TEXT_XML)
    public String getMessage(){
        
        return new AppealRepository()
    }
*/
    /**
     * PUT method for updating or creating an instance of appealResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
