/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006 The Regents of the University of California and The Regents of the University of Michigan
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
package org.sakaiproject.component.section.facade.impl.sakai21;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.section.coursemanagement.User;
import org.sakaiproject.component.section.sakai21.UserImpl;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;

public class SakaiUtil {
	private static final Log log = LogFactory.getLog(SakaiUtil.class);

	/**
	 * Gets a User from Sakai's UserDirectory (legacy) service.
	 * 
	 * @param userUid The user uuid
	 * @return
	 */
	public static final User getUserFromSakai(String userUid) {
		final org.sakaiproject.user.api.User sakaiUser;
		try {
			sakaiUser = UserDirectoryService.getUser(userUid);
		} catch (UserNotDefinedException e) {
			log.error("User not found: " + userUid);
			e.printStackTrace();
			return null;
		}
		return convertUser(sakaiUser);
	}

	/**
	 * Converts a sakai user object into a user object suitable for use in the section
	 * manager tool and in section awareness.
	 * 
	 * @param sakaiUser The sakai user, as returned by Sakai's legacy SecurityService.
	 * 
	 * @return
	 */
	public static final User convertUser(final org.sakaiproject.user.api.User sakaiUser) {
		UserImpl user = new UserImpl(sakaiUser.getDisplayId(), sakaiUser.getDisplayName(),
				sakaiUser.getSortName(), sakaiUser.getId());
		return user;
	}
	
    /**
     * @return The current sakai authz reference
     */
    public static final String getSiteReference() {
        Placement placement = ToolManager.getCurrentPlacement();
        String context = placement.getContext();
        return SiteService.siteReference(context);
    }


}
