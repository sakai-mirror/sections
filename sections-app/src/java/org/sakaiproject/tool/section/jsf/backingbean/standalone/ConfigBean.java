/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.tool.section.jsf.backingbean.standalone;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.sakaiproject.api.section.SectionManager;
import org.sakaiproject.tool.section.jsf.backingbean.CourseDependentBean;

public class ConfigBean extends CourseDependentBean {
	private static final long serialVersionUID = 1L;

	public String updateConfigAllManual() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.ALWAYS_MANUAL);
		return "overview";
	}
	
	public String updateConfigAllAutomatic() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.ALWAYS_AUTOMATIC);
		return "overview";
	}

	public String updateConfigOptionalAll() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.OPTIONAL_ALL);
		return "overview";
	}

	public String updateConfigOptionalMultiple() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.OPTIONAL_MULTIPLE);
		return "overview";
	}

	private ServletContext getServletContext() {
		return (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
}
