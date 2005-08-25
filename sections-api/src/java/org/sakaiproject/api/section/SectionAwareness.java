/**********************************************************************************
*
* $Id: $
*
***********************************************************************************
*
* Copyright (c) 2005 The Regents of the University of California, The Regents of the University of Michigan,
*                  Board of Trustees of the Leland Stanford, Jr., University, and The MIT Corporation
* 
* Licensed under the Educational Community License Version 1.0 (the "License");
* By obtaining, using and/or copying this Original Work, you agree that you have read,
* understand, and will comply with the terms and conditions of the Educational Community License.
* You may obtain a copy of the License at:
* 
*      http://cvs.sakaiproject.org/licenses/license_1_0.html
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
**********************************************************************************/
package org.sakaiproject.api.section;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.sakaiproject.api.section.coursemanagement.CourseSection;
import org.sakaiproject.api.section.facade.Role;

/**
 * <p>
 * Provides section awareness to tools needing read-only access to section
 * information, such as section membership.
 * </p>
 * 
 * @author <a href="mailto:ray@media.berkeley.edu">Ray Davis</a>
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 */
public interface SectionAwareness {

    /**
     * Gets the sections associated with this site context.
     * 
	 * @param siteContext The site context
	 * 
     * @return The Set of
     * {@link org.sakaiproject.api.section.coursemanagement.CourseSection CourseSections}
     * associated with this site context.
     */
    public Set getSections(String siteContext);

    /**
     * Gets the list of section categories.  In sakai 2.1, there will be only a
     * single set of categories.  They will not be configurable on a per-course
     * or per-context bases.
     * 
     * @return A List of unique Strings that identify the available section
     * categories.  These should be internationalized for display using
     * {@link SectionAwareness#getCategoryName(String, Locale) getCategoryName}.
     */
    public List getSectionCategories();
    
    /**
     * Gets a {@link org.sakaiproject.api.section.coursemanagement.CourseSection CourseSection}
     * by its uuid.
     * 
     * @param sectionUuid
     * 
     * @return
     */
    public CourseSection getSection(String sectionUuid);

    /**
     * Gets the site membership for a given context.
     * 
	 * @param siteContext The site context
	 * 
     * @return A {@link java.util.List List} of
     * {@link org.sakaiproject.api.section.coursemanagement.ParticipationRecord
     * ParticipationRecords} representing the users in the given site, playing
     * the given {@link org.sakaiproject.api.section.facade.Role Role}.
     * 
     */
    public List getSiteMembersInRole(String siteContext, Role role);

    /**
     * Finds site members in the given context and {@link org.sakaiproject.api.section.facade.Role Role}
     * with a matching name or display id.  Pattern matching is TBD, but will
     * probably match in any of the following cases:
     * 
     * <ul>
     * 	<li>Display Name = pattern*</li>
     * 	<li>Sort Name = pattern*</li>
     * 	<li>Display Id (installation defined, either Email or enterprise id) = pattern*</li>
     * </ul>
     * 
	 * @param siteContext The site context
     * @param role The role the user must play in this context
     * @param pattern The pattern the user's name or id must match
     * 
     * @return A {@link java.util.List List} of
     * {@link org.sakaiproject.api.section.coursemanagement.ParticipationRecord
     * ParticipationRecords} representing the users in the given site, playing
     * the given role, that match the string pattern.
     */
    public List findSiteMembersInRole(String siteContext, Role role, String pattern);
    
    /**
     * Checks whether a user plays a particular {@link org.sakaiproject.api.section.facade.Role Role}
     * in a given site context.
     * 
	 * @param siteContext The site context
     * @param userUuid The user's unique id
     * @param role The role we're checking
     * 
     * @return Whether this user plays this role in this context.
     */
    public boolean isSiteMemberInRole(String siteContext, String userUuid, Role role);

    /**
     * Gets the full membership of the given section.
     * 
     * @param sectionId
     * 
     * @return A {@link java.util.List List} of
     * {@link org.sakaiproject.api.section.coursemanagement.ParticipationRecord
     * ParticipationRecords} representing the users in a
     * {@link org.sakaiproject.api.section.coursemanagement.CourseSection CourseSection}.
     */
    public List getSectionMembers(String sectionId);

    /**
     * Gets the members of a given section that play a given role in the section.
     * 
     * @param sectionUuid
     * @param role
     * 
     * @return A {@link java.util.List List} of
     * {@link org.sakaiproject.api.section.coursemanagement.ParticipationRecord
     * ParticipationRecords} representing the users in a
     * {@link org.sakaiproject.api.section.coursemanagement.CourseSection CourseSection}
     * that play a given {@link org.sakaiproject.api.section.facade.Role Role}.
     */
    public List getSectionMembersInRole(String sectionUuid, Role role);

    /**
     * Checks whether a user plays a particular {@link org.sakaiproject.api.section.facade.Role Role}
     * in a section.
     * 
     * @param sectionId
     * @param personId
     * @param role
     * 
     * @return Whether the user plays a particular role in a section.
     */
    public boolean isSectionMemberInRole(String sectionId, String personId, Role role);

    /**
     * Lists the sections in this context that are a member of the given category.
     * 
	 * @param siteContext The site context
     * @param categoryId
     * 
     * @return A List of {@link org.sakaiproject.api.section.coursemanagement.CourseSection CourseSections}
     */
    public List getSectionsInCategory(String siteContext, String categoryId);

    /**
     * Gets the localized name of a given category.
     * 
     * @param categoryId A string identifying the category
     * @param locale The locale of the client
     * 
     * @return An internationalized string to display for this category.
     * 
     */
    public String getCategoryName(String categoryId, Locale locale);
    
}


/**********************************************************************************
 * $Id: $
 *********************************************************************************/
