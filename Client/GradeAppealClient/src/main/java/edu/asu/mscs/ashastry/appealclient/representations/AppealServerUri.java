/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.mscs.ashastry.appealclient.representations;

import edu.asu.mscs.ashastry.appealclient.model.Identifier;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author A
 */
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
