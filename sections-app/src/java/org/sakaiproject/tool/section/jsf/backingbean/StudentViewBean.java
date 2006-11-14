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
package org.sakaiproject.tool.section.jsf.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.section.api.coursemanagement.CourseSection;
import org.sakaiproject.section.api.coursemanagement.EnrollmentRecord;
import org.sakaiproject.section.api.coursemanagement.ParticipationRecord;
import org.sakaiproject.section.api.exception.RoleConfigurationException;
import org.sakaiproject.tool.section.decorator.InstructorSectionDecorator;
import org.sakaiproject.tool.section.decorator.StudentSectionDecorator;
import org.sakaiproject.tool.section.jsf.JsfUtil;

/**
 * Controls the student view page.
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class StudentViewBean extends CourseDependentBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(StudentViewBean.class);
	
	private String sortColumn;
	private boolean sortAscending;
	private String sectionFilter;
	private boolean externallyManaged;
	private boolean joinAllowed;
	private boolean switchAllowed;
	
	private List sections;
	private String rowClasses;
	private String instructions;
	
	public StudentViewBean() {
		sortColumn = "meetingTimes";
		sortAscending = true;
	}
	
	public void init() {
		// Determine whether this course is externally managed
		externallyManaged = getSectionManager().isExternallyManaged(getCourse().getUuid());
		
		// Determine whether the sections are joinable and/or switchable
		joinAllowed = getSectionManager().isSelfRegistrationAllowed(getSiteContext());
		switchAllowed = getSectionManager().isSelfSwitchingAllowed(getSiteContext());

		// Keep track of whether there are joinable Sections
		boolean joinableSectionsExist = false;

		// Keep track of whether there are switchable Sections
		boolean switchableSectionsExist = false;
		
		// Get all sections in the site
		List sectionSet = getAllSiteSections();
		sections = new ArrayList();
		
		// Get the section enrollments for this student
		Set enrolledSections = getMyEnrolledSections();

		for(Iterator sectionIter = sectionSet.iterator(); sectionIter.hasNext();) {
			CourseSection section = (CourseSection)sectionIter.next();
			String catName = getCategoryName(section.getCategory());
			
			// Generate the string showing the TAs
			List tas = getSectionManager().getSectionTeachingAssistants(section.getUuid());
			List taNames = new ArrayList();
			for(Iterator taIter = tas.iterator(); taIter.hasNext();) {
				ParticipationRecord ta = (ParticipationRecord)taIter.next();
				taNames.add(ta.getUser().getSortName());
			}

			Collections.sort(taNames);

			int totalEnrollments = getSectionManager().getTotalEnrollments(section.getUuid());
			boolean member = isEnrolledInSection(enrolledSections, section);
			boolean memberOtherSection = isEnrolledInOtherSection(enrolledSections, section);
			
			StudentSectionDecorator decoratedSection = new StudentSectionDecorator(
					section, catName, taNames, totalEnrollments, member, memberOtherSection);

			// Ignore non-member sections if we're filtering for my sections
			if("MY".equals(sectionFilter) && ! decoratedSection.isMember()) {
				continue;
			}
			
			sections.add(decoratedSection);
			
			if(decoratedSection.isJoinable()) {
				joinableSectionsExist = true;
			} else if (decoratedSection.isSwitchable()) {
				switchableSectionsExist = true;
			}
		}
		Collections.sort(sections, getComparator());
		buildRowClasses();

		instructions = generateInstructions(joinableSectionsExist, switchableSectionsExist);
	}

	private String generateInstructions(boolean joinableSectionsExist, boolean switchableSectionsExist) {

		// No instructions if the site is externally managed
		if(externallyManaged) {
			return null;
		} else

		// No instructions if not allowed to join or switch
		if(!joinAllowed && !switchAllowed) {
			return null;
		} else

		// No instructions if there are no joinable or switchable sections
		if(!joinableSectionsExist && !switchableSectionsExist) {
			return null;
		} else

		// Joining is possible, but switching is not
		if(joinAllowed && joinableSectionsExist && !(switchAllowed && switchableSectionsExist)) {
			return JsfUtil.getLocalizedMessage("student_view_instructions_join");
		} else
		
		// Switching is possible, but joining is not
		if(switchAllowed && switchableSectionsExist && !(joinAllowed && joinableSectionsExist)) {
			return JsfUtil.getLocalizedMessage("student_view_instructions_switch");
		} else

		// Joining and switching are both possible
		if(switchAllowed && switchableSectionsExist && joinAllowed && joinableSectionsExist) {
			return JsfUtil.getLocalizedMessage("student_view_instructions_join_or_switch");
		} else {
			// Something is wrong with this picture
			log.error("A serious mistake occurred in the section info student view!");
			return null;
		}
	}
	
	private void buildRowClasses() {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for(Iterator iter = sections.iterator(); iter.hasNext();) {
			StudentSectionDecorator decoratedSection = (StudentSectionDecorator)iter.next();
			if(iter.hasNext()) {
				StudentSectionDecorator nextSection = (StudentSectionDecorator)sections.get(++index);
				if(nextSection.getCategory().equals(decoratedSection.getCategory())) {
					if(decoratedSection.isMember()) {
						sb.append("member");
					} else {
						sb.append("nonmember");
					}
				} else {
					if(decoratedSection.isMember()) {
						sb.append("memberPadRow");
					} else {
						sb.append("nonmemberPadRow");
					}
				}
				sb.append(",");
			} else {
				if(decoratedSection.isMember()) {
					sb.append("member");
				} else {
					sb.append("nonmember");
				}
			}
		}
		rowClasses = sb.toString();
	}

	private boolean isEnrolledInSection(Set enrolledSections, CourseSection section) {
		for(Iterator iter = enrolledSections.iterator(); iter.hasNext();) {
			EnrollmentRecord enr = (EnrollmentRecord)iter.next();
			if(enr.getLearningContext().equals(section)) {
				return true;
			}
		}
		return false;
	}

	private boolean isEnrolledInOtherSection(Set enrolledSections, CourseSection section) {
		String category = section.getCategory();
		for(Iterator iter = enrolledSections.iterator(); iter.hasNext();) {
			EnrollmentRecord enr = (EnrollmentRecord)iter.next();
			if(((CourseSection)enr.getLearningContext()).getCategory().equals(category)) {
				return true;
			}
		}
		return false;
	}

	private Comparator getComparator() {
		if(sortColumn.equals("instructor")) {
			return InstructorSectionDecorator.getManagersComparator(sortAscending); 
		} else if(sortColumn.equals("category")) {
			// These are already sorted by category, so just sort by title
			return InstructorSectionDecorator.getFieldComparator("title", sortAscending);
		} else if(sortColumn.equals("available")) {
			return InstructorSectionDecorator.getEnrollmentsComparator(sortAscending, true); 
		} else {
			return InstructorSectionDecorator.getFieldComparator(sortColumn, sortAscending); 
		}
	}

	public void processJoinSection(ActionEvent event) {
		String sectionUuid = (String)FacesContext.getCurrentInstance().getExternalContext()
		.getRequestParameterMap().get("sectionUuid");
		//is this section still joinable?
		CourseSection section = getSectionManager().getSection(sectionUuid);
		
		// The section might have been deleted
		if(section == null) {
			// There's nothing we can do in the UI, really.
			return;
		}

		//check that there are still places available
		int maxEnrollments = Integer.MAX_VALUE;
		if(section.getMaxEnrollments() != null) {
			maxEnrollments = section.getMaxEnrollments().intValue();
		}
		int numEnrolled = getSectionManager().getTotalEnrollments(section.getUuid());
		if (numEnrolled >= maxEnrollments) {
			if(log.isDebugEnabled()) log.debug("Attempted to join a section with no spaces");
			JsfUtil.addErrorMessage(JsfUtil.getLocalizedMessage("student_view_membership_full", new String[] {section.getTitle()}));
			return;
		}
		try {
			getSectionManager().joinSection(sectionUuid);
		} catch (RoleConfigurationException rce) {
			JsfUtil.addErrorMessage(JsfUtil.getLocalizedMessage("role_config_error"));
		}
	}
	
	public void processSwitchSection(ActionEvent event) {
		String sectionUuid = (String)FacesContext.getCurrentInstance().getExternalContext()
		.getRequestParameterMap().get("sectionUuid");
		
		// Does the section still exist, and is it still joinable?
		CourseSection section = getSectionManager().getSection(sectionUuid);
		
		if(section == null) {
			// There's nothing we can do in the UI, really.
			return;
		}

		//check that there are still places available
		int maxEnrollments = Integer.MAX_VALUE;
		if(section.getMaxEnrollments() != null) {
			maxEnrollments = section.getMaxEnrollments().intValue();
		}
		int numEnrolled = getSectionManager().getTotalEnrollments(section.getUuid());
		if (numEnrolled >= maxEnrollments) {
			if(log.isDebugEnabled()) log.debug("Attempted to join a section with no spaces");
			JsfUtil.addErrorMessage(JsfUtil.getLocalizedMessage("student_view_membership_full", new String[] {section.getTitle()}));
			return;
		}
		try {
			getSectionManager().switchSection(sectionUuid);
		} catch (RoleConfigurationException rce) {
			JsfUtil.addErrorMessage(JsfUtil.getLocalizedMessage("role_config_error"));
		}
	}

	public List getSections() {
		return sections;
	}
	public String getRowClasses() {
		return rowClasses;
	}
	public boolean isSortAscending() {
		return sortAscending;
	}
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSectionFilter() {
		return sectionFilter;
	}
	public void setSectionFilter(String sectionFilter) {
		this.sectionFilter = sectionFilter;
	}
	public boolean isExternallyManaged() {
		return externallyManaged;
	}
	public boolean isJoinAllowed() {
		return joinAllowed;
	}
	public boolean isSwitchAllowed() {
		return switchAllowed;
	}
	public String getInstructions() {
		return instructions;
	}
}
