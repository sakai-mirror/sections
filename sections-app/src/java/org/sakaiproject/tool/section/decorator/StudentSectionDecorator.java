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
package org.sakaiproject.tool.section.decorator;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.section.api.coursemanagement.CourseSection;
import org.sakaiproject.tool.section.jsf.JsfUtil;

/**
 * Decorates a CourseSection for use in the students' UI.
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class StudentSectionDecorator extends SectionDecorator
	implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(StudentSectionDecorator.class);

	protected boolean full;
	protected boolean joinable;
	protected boolean switchable;
	protected boolean member;
	
	public StudentSectionDecorator(CourseSection courseSection, String categoryForDisplay,
			List instructorNames, int totalEnrollments, boolean member,
			boolean memberOtherSection) {
		super(courseSection, categoryForDisplay, instructorNames, totalEnrollments);
		this.member = member;
		if( ! this.member && this.spotsAvailable.equals("0")) {
			this.full = true;
		}
		if( ! this.member && ! this.full) {
			this.switchable = memberOtherSection;
			this.joinable = ! memberOtherSection;
		}
	}
	
	/**
	 * Overrides the behavior of the superclass.  Students should not see a
	 * negative number of available spots.
	 */
	protected void populateSpotsAvailable(CourseSection courseSection) {
		if(courseSection.getMaxEnrollments() == null) {
			spotsAvailable = JsfUtil.getLocalizedMessage("section_max_size_unlimited");
		} else {
			// Do not allow negative values to be displayed
			int spots = courseSection.getMaxEnrollments().intValue() - totalEnrollments;
			if(spots < 0) {
				spotsAvailable = "0";
			} else {
				spotsAvailable = Integer.toString(spots);
			}
		}
	}

	public StudentSectionDecorator() {
		// Needed for serialization
	}
	
	public List getInstructorNames() {
		return instructorNames;
	}
	
	public boolean isFull() {
		return full;
	}

	public boolean isJoinable() {
		return joinable;
	}

	public boolean isMember() {
		return member;
	}

	public boolean isSwitchable() {
		return switchable;
	}
}
