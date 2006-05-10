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
package org.sakaiproject.component.section.sakai21;

import java.io.Serializable;

import org.sakaiproject.api.section.coursemanagement.LearningContext;
import org.sakaiproject.api.section.coursemanagement.User;
import org.sakaiproject.api.section.facade.Role;

/**
 * A detachable TeachingAssistantRecord for persistent storage.
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class TeachingAssistantRecordImpl extends ParticipationRecordImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * No-arg constructor needed for hibernate
	 */
	public TeachingAssistantRecordImpl() {		
	}
	
	public TeachingAssistantRecordImpl(LearningContext learningContext, User user) {
		this.learningContext = learningContext;
		this.user = user;
		this.userUid = user.getUserUid();
	}

	public Role getRole() {
		return Role.TA;
	}

}
