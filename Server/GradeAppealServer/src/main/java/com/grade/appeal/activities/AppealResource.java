/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grade.appeal.activities;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author A
 */
@Path("/appeal")
public class AppealResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AppealResource
     */
    public AppealResource() {
    }

    /**
     * Retrieves representation of an instance of com.grade.appeal.activities.AppealResource
     * @return an instance of java.lang.String
     */
  
}
