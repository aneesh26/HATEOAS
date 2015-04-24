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


package edu.asu.mscs.ashastry.appealclient.representations;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import java.net.URI;
import java.net.URISyntaxException;


public class AppealServerUri {
    private URI uri;
    
    public AppealServerUri(){
        
    }
    
    public AppealServerUri(String uri) {
        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    public AppealServerUri(URI uri) {
        this(uri.toString());
    }

    public AppealServerUri(URI uri, Identifier identifier) {
        this(uri.toString() + "/" + identifier.toString());
    }

    public Identifier getId() {
        String path = uri.getPath();
        return new Identifier(path.substring(path.lastIndexOf("/") + 1, path.length()));
    }

    public URI getFullUri() {
        return uri;
    }
    
    @Override
    public String toString() {
        return uri.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AppealServerUri) {
            return ((AppealServerUri)obj).uri.equals(uri);
        }
        return false;
    }

    public String getBaseUri() {
        
        
        /* // Old implementation
        String port = "";
        if(uri.getPort() != 80 && uri.getPort() != -1) {
            port = ":" + String.valueOf(uri.getPort());
        }
        
        return uri.getScheme() + "://" + uri.getHost() + port;
        * */
        
       // String uriString = uri.toString();
       // String baseURI   = uriString.substring(0, uriString.lastIndexOf("webresources/")+"webresources".length());
        
       // return baseURI;
        
        String port = "";
        if(uri.getPort() != 80 && uri.getPort() != -1) {
            port = ":" + String.valueOf(uri.getPort());
        }
        
        return uri.getScheme() + "://" + uri.getHost() + port;
        
        
    }
    
    

    
}
