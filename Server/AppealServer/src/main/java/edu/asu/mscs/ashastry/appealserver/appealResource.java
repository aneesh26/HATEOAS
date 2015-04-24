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

package edu.asu.mscs.ashastry.appealserver;


import edu.asu.mscs.ashastry.appealserver.activities.ApproveAppealActivity;
import edu.asu.mscs.ashastry.appealserver.activities.CreateAppealActivity;
import edu.asu.mscs.ashastry.appealserver.activities.CreateAppealListActivity;
import edu.asu.mscs.ashastry.appealserver.activities.EditAppealActivity;
import edu.asu.mscs.ashastry.appealserver.activities.FollowUpActivity;
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




import edu.asu.mscs.ashastry.appealserver.activities.InvalidAppealException;
import edu.asu.mscs.ashastry.appealserver.activities.NoSuchAppealException;
import edu.asu.mscs.ashastry.appealserver.activities.UpdateException;
import edu.asu.mscs.ashastry.appealserver.activities.WithdrawAppealActivity;
import edu.asu.mscs.ashastry.appealserver.representations.AppealListRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealRepresentation;
import edu.asu.mscs.ashastry.appealserver.representations.AppealServerUri;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * REST Web Service
 *
 * @author A
 */
@Path("/appeals")
public class appealResource {
    
 private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(appealResource.class);

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
        } catch (InvalidAppealException ioe) {
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
        } catch (InvalidAppealException ioe) {
            LOG.debug("Invalid order in the XML representation {}", appealRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NoSuchAppealException nsoe) {
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
    
    
    @GET
    @Path("/{appealId}/approve/")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response approveAppeal(String appealRepresentation) {
        LOG.info("Approving an Appeal Resource");
        
        Response response;
        
        try {
             AppealRepresentation responseRepresentation = new ApproveAppealActivity().approve(new AppealServerUri(uriInfo.getRequestUri()));
             response = Response.created(responseRepresentation.getViewLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.debug("Invalid order in the XML representation {}", appealRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such order resource to update");
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Problem updating the order resource");
            response = Response.status(Response.Status.CONFLICT).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.debug("Something went wrong updating the order resource");
            response = Response.serverError().build();
        } 
        
        LOG.debug("Resulting response for updating the order resource is {}", response);
        
        return response;
     }
    
    
    @DELETE
    @Path("/{appealId}/withdraw/")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response withdrawAppeal(String appealRepresentation) {
        LOG.info("Withdrawing an Appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new WithdrawAppealActivity().withdraw(new AppealServerUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getViewLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.debug("Invalid order in the XML representation {}", appealRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NoSuchAppealException nsoe) {
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
    
    @GET
    @Path("/{appealId}/followup/")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response followUpAppeal(String appealRepresentation) {
        LOG.info("Following up an Appeal");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation =  new FollowUpActivity().followUp(new AppealServerUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getEditLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            LOG.debug("Invalid order in the XML representation {}", appealRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such order resource to update");
           // LOG.debug("Sending the Client the list of Valid appeals for a folloup");
           // this.createAppealList(appealRepresentation);
            
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Problem updating the order resource");
            response = Response.status(Response.Status.CONFLICT).build();
        } catch (Exception ex) {
            LOG.debug("Something went wrong updating the order resource");
            response = Response.serverError().build();
        } 
        
        LOG.debug("Resulting response for following up the appeal resource is {}", response);
        
        return response;
     }
    
    
    
    @GET
    @Produces("application/vnd.cse564-appeals+xml")
    public Response createAppealList(String appealListRepresentation) {
        LOG.info("Creating an AppealList Resource");
        Response response;
        System.out.println("Reached get !!");
       
        try {
            //AppealListRepresentation responseRepresentation = new CreateAppealListActivity().createList(AppealListRepresentation.fromXmlString(appealRepresentation).getAppeal(), new AppealServerUri(uriInfo.getRequestUri()));
            AppealListRepresentation responseRepresentation = new CreateAppealListActivity().createList(new AppealServerUri(uriInfo.getRequestUri()));
           // response = Response.created(responseRepresentation.getAppealsLink().getUri()).entity(responseRepresentation).build();
            response = Response.ok().entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            ioe.printStackTrace();
            
            LOG.debug("Invalid Order - Problem with the orderrepresentation {}", appealListRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOG.debug("Someting went wrong creating the order resource");
            response = Response.serverError().build();
        }
        
        LOG.debug("Resulting response for creating the order resource is {}", response);
        
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
