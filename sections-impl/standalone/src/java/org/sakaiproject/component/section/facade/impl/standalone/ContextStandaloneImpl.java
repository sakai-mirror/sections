/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright 2005, 2006 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.component.section.facade.impl.standalone;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.sakaiproject.section.api.facade.manager.Context;

/**
 * Standalone implementation of Context, using the HttpSession to store current
 * context.
 * 
 * @author <a href="jholtzman@berkeley.edu">Josh Holtzman</a>
 */
public class ContextStandaloneImpl implements Context {

	public static final String CONTEXT = "context";

	public String getContext(Object request) {
    	HttpSession session = null;
    	if(request == null) {
            session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    	} else {
    		session = ((HttpServletRequest)request).getSession();
    	}
        return (String)session.getAttribute(CONTEXT);
	}

	public String getContextTitle(Object request) {
		return getContext(request);
	}	
	
}
