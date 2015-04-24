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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public abstract class Representation {
    public static final String RELATIONS_URI = "http://relations.appeals.com/";
    public static final String APPEALS_NAMESPACE = "http://schemas.appeals.com";
    public static final String DAP_NAMESPACE = APPEALS_NAMESPACE + "/dap";
    public static final String APPEALS_MEDIA_TYPE = "application/vnd.cse564-appeals+xml";
    public static final String SELF_REL_VALUE = "self";

    @XmlElement(name = "link", namespace = DAP_NAMESPACE)
    protected List<Link> links;

    protected Link getLinkByName(String uriName) {
        if (links == null) {
            return null;
        }

        for (Link l : links) {
            if (l.getRelValue().toLowerCase().equals(uriName.toLowerCase())) {
                return l;
            }
        }
        return null;
    }
}
