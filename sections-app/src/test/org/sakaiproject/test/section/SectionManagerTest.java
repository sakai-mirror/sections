/**********************************************************************************
*
* $Id$
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
package org.sakaiproject.test.section;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.section.SectionAwareness;
import org.sakaiproject.api.section.coursemanagement.Course;
import org.sakaiproject.api.section.coursemanagement.CourseSection;
import org.sakaiproject.api.section.coursemanagement.EnrollmentRecord;
import org.sakaiproject.api.section.coursemanagement.ParticipationRecord;
import org.sakaiproject.api.section.coursemanagement.SectionEnrollments;
import org.sakaiproject.api.section.coursemanagement.User;
import org.sakaiproject.api.section.exception.MembershipException;
import org.sakaiproject.api.section.facade.Role;
import org.sakaiproject.api.section.facade.manager.Context;
import org.sakaiproject.test.section.manager.CourseManager;
import org.sakaiproject.test.section.manager.UserManager;
import org.sakaiproject.tool.section.facade.impl.standalone.AuthnTestImpl;
import org.sakaiproject.tool.section.manager.SectionManager;

/**
 * Each test method is isolated in its own transaction, which is rolled back when
 * the method exits.  Since we can not assume that data will exist, we need to use
 * the SectionManager api to insert data before retrieving it with SectionAwareness.
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class SectionManagerTest extends SectionsTestBase{
	private static final Log log = LogFactory.getLog(SectionManagerTest.class);
	
	private AuthnTestImpl authn;
	private Context context;
	private SectionManager secMgr;
	private SectionAwareness secAware;
	private CourseManager courseMgr;
	private UserManager userMgr;

    protected void onSetUpInTransaction() throws Exception {
    	authn = (AuthnTestImpl)applicationContext.getBean("org.sakaiproject.api.section.facade.manager.Authn");
    	context = (Context)applicationContext.getBean("org.sakaiproject.api.section.facade.manager.Context");
        secMgr = (SectionManager)applicationContext.getBean("org.sakaiproject.tool.section.manager.SectionManager");
        courseMgr = (CourseManager)applicationContext.getBean("org.sakaiproject.test.section.manager.CourseManager");
        userMgr = (UserManager)applicationContext.getBean("org.sakaiproject.test.section.manager.UserManager");
        secAware = secMgr.getSectionAwareness();
    }

    public void testSectionMembership() throws Exception {

    	// TODO This test has become totally unruly.  Split it up, even though it will lead to a lot of duplication
    	
    	String siteContext = context.getContext();
    	List categories = secAware.getSectionCategories();
    	
    	// Add a course and a section to work from
    	Course newCourse = courseMgr.createCourse(siteContext, "A course", false, false, false);
    	Course course = secMgr.getCourse(siteContext);
    	
    	// Assert that the correct course was retrieved
    	Assert.assertTrue(newCourse.equals(course));
    	
    	String firstCategory = (String)categories.get(0);
    	String secondCategory = (String)categories.get(1);
    	String thirdCategory = (String)categories.get(2);
    	CourseSection sec1 = secMgr.addSection(course.getUuid(), "A section", firstCategory, 10, null, null, false, null, false, false,  false, false,  false, false, false, false);
    	CourseSection sec2 = secMgr.addSection(course.getUuid(), "Another section", firstCategory, 10, null, null, false, null, false, false,  false, false,  false, false, false, false);
    	CourseSection sec3 = secMgr.addSection(course.getUuid(), "A different kind of section", secondCategory, 10, null, null, false, null, false, false,  false, false,  false, false, false, false);
    	CourseSection sec4 = secMgr.addSection(course.getUuid(), "Barely even a section", thirdCategory, 10, null, null, false, null, false, false,  false, false,  false, false, false, false);
    	
		// Load students
		User student1 = userMgr.createUser("student1", "Joe Student", "Student, Joe", "jstudent");
		User student2 = userMgr.createUser("student2", "Jane Undergrad", "Undergrad, Jane", "jundergrad");

		// Load TAs
		User ta1 = userMgr.createUser("ta1", "Mike Grad", "Grad, Mike", "mgrad");
		User ta2 = userMgr.createUser("ta2", "Sara Postdoc", "Postdoc, Sara", "spostdoc");
		
		// Load instructors
		User instructor1 = userMgr.createUser("instructor1", "Bill Economist", "Economist, Bill", "beconomist");
		User instructor2 = userMgr.createUser("instructor2", "Amber Philosopher", "Philosopher, Amber", "aphilosopher");

		// Load other people
		User otherPerson = userMgr.createUser("other1", "Other Person", "Person, Other", "operson");

		// Load enrollments into the course
		ParticipationRecord siteEnrollment1 = courseMgr.addEnrollment(student1, course);
		ParticipationRecord siteEnrollment2 = courseMgr.addEnrollment(student2, course);
		ParticipationRecord siteEnrollment3 = courseMgr.addEnrollment(otherPerson, course);
		
		// Load enrollments into sections
		ParticipationRecord sectionEnrollment1 = secMgr.addSectionMembership("student1", Role.STUDENT, sec1.getUuid());
		ParticipationRecord sectionEnrollment2 = secMgr.addSectionMembership("student2", Role.STUDENT, sec1.getUuid());
		
		// Load TAs into the course
		ParticipationRecord siteTaRecord1 = courseMgr.addTA(ta1, course);
		ParticipationRecord siteTaRecord2 = courseMgr.addTA(ta2, course);
		
		// Load TAs into the sections
		ParticipationRecord sectionTaRecord1 = secMgr.addSectionMembership("ta1", Role.TA, sec1.getUuid());
		ParticipationRecord sectionTaRecord2 = secMgr.addSectionMembership("ta2", Role.TA, sec1.getUuid());
		
		// Load instructors into the courses
		ParticipationRecord siteInstructorRecord1 = courseMgr.addInstructor(instructor1, course);
				
		// Assert that an student who joins a section is returned as a member of that section
		authn.setUserUuid("other1");
		EnrollmentRecord sectionEnrollment3 = secMgr.joinSection(sec1.getUuid());

		List enrollments = secAware.getSectionMembersInRole(sec1.getUuid(), Role.STUDENT);
		Assert.assertTrue(enrollments.contains(sectionEnrollment3));
		
		// Assert that an enrolled student can not add themselves again
		authn.setUserUuid("student1");
		boolean joinSectionErrorThrown = false;
		try {
			secMgr.joinSection(sec1.getUuid());
		} catch (MembershipException me) {
			joinSectionErrorThrown = true;
		}
		Assert.assertTrue(joinSectionErrorThrown);
		
		// Assert that a student can switch between sections only within the same category
		secMgr.switchSection(sec2.getUuid());
		boolean switchingErrorThrown = false;
		try {
			secMgr.switchSection(sec3.getUuid());
		} catch(MembershipException me) {
			switchingErrorThrown = true;
		}
		Assert.assertTrue(switchingErrorThrown);
		
		// Add otherPerson to the section in the third category.  This is the only enrollment in this section or category.
		secMgr.addSectionMembership(otherPerson.getUserUuid(), Role.STUDENT, sec4.getUuid());
		
		// Assert that the third category's unsectioned students returns the two students
		List unsectionedEnrollments = secMgr.getUnsectionedEnrollments(course.getUuid(), thirdCategory);
		List unsectionedStudents = new ArrayList();
		for(Iterator iter = unsectionedEnrollments.iterator(); iter.hasNext();) {
			unsectionedStudents.add(((ParticipationRecord)iter.next()).getUser());
		}
		
		Assert.assertTrue(unsectionedStudents.contains(student1));
		Assert.assertTrue(unsectionedStudents.contains(student2));
		Assert.assertTrue(! unsectionedStudents.contains(otherPerson));
		
		// Assert that an instructor can not be added to a section
		boolean instructorErrorThrown = false;
		try {
			secMgr.addSectionMembership(instructor1.getUserUuid(), Role.INSTRUCTOR, sec1.getUuid());
		} catch (MembershipException me) {
			instructorErrorThrown = true;
		}
		Assert.assertTrue(instructorErrorThrown);

		// Assert that setting the entire membership of a section is successful
		Set set = new HashSet();
		set.add(student1.getUserUuid());
		set.add(student2.getUserUuid());
		secMgr.setSectionMemberships(set, Role.STUDENT, sec4.getUuid());
		List sectionMemberships = secAware.getSectionMembersInRole(sec4.getUuid(), Role.STUDENT);
		Set sectionMembers = new HashSet();
		for(Iterator iter = sectionMemberships.iterator(); iter.hasNext();) {
			sectionMembers.add(((ParticipationRecord)iter.next()).getUser());
		}
		Assert.assertTrue(sectionMembers.contains(student1));
		Assert.assertTrue(sectionMembers.contains(student2));
		// otherPerson was originally in the section, but wasn't included in the set operation
		Assert.assertTrue( ! sectionMembers.contains(otherPerson));
		
		// Drop a student from a section and ensure the enrollments reflect the drop (remember... student1 switched to sec2!)
		secMgr.dropSectionMembership(student1.getUserUuid(), sec2.getUuid());
		List sec2Members = secAware.getSectionMembers(sec2.getUuid());
		Assert.assertTrue( ! sec2Members.contains(sectionEnrollment1));
				
		// Check whether the total enrollments in the course and in the sections is accurate
		Assert.assertTrue(secMgr.getTotalEnrollments(course.getUuid()) == 3);
		Assert.assertTrue(secMgr.getTotalEnrollments(sec1.getUuid()) == 2);
		
		// Ensure that a section can be updated
		secMgr.updateSection(sec1.getUuid(), "New title", secondCategory, 10, null, null, false, null, false, false, false, false, false, false, false, false);
		CourseSection updatedSec = secAware.getSection(sec1.getUuid());
		Assert.assertTrue(updatedSec.getTitle().equals("New title"));
		Assert.assertTrue(updatedSec.getCategory().equals(secondCategory));
		sec1 = updatedSec;
		
		// Ensure that disbanding a section actually removes it from the course
		secMgr.disbandSection(sec4.getUuid());
		Assert.assertTrue( ! secAware.getSections(siteContext).contains(sec4));
		
		
		// Assert that the correct enrollment records are returned for a student in a course
		ParticipationRecord enrollment1 = secMgr.addSectionMembership(student1.getUserUuid(), Role.STUDENT, sec1.getUuid());
		ParticipationRecord enrollment2 = secMgr.addSectionMembership(student1.getUserUuid(), Role.STUDENT, sec3.getUuid());
		Set myEnrollments = secMgr.getSectionEnrollments(student1.getUserUuid(), course.getUuid());
		
		Assert.assertTrue(myEnrollments.contains(enrollment1));
		Assert.assertTrue(myEnrollments.contains(enrollment2));
		Assert.assertTrue(myEnrollments.size() == 2);

		// Assert that a valid SectionEnrollments object (just a convenient data structure) can be obtained
		Set studentUuids = new HashSet();
		studentUuids.add("student1");
		studentUuids.add("student2");
		studentUuids.add("other1");
		SectionEnrollments secEnrollments = secMgr.getSectionEnrollments(siteContext, studentUuids);

		// Student 1 is enrolled in section 3
		Assert.assertTrue(secEnrollments.getSection("student1", firstCategory) == null);
		Assert.assertTrue(secEnrollments.getSection("student1", secondCategory).equals(sec3));
		
		// Student 2 is enrolled in section 1
		Assert.assertTrue(secEnrollments.getSection("student2", firstCategory) == null);
		Assert.assertTrue(secEnrollments.getSection("student2", secondCategory).equals(sec1));

		// Other person is enrolled in section 1
		Assert.assertTrue(secEnrollments.getSection("other1", firstCategory) == null);
		Assert.assertTrue(secEnrollments.getSection("other1", secondCategory).equals(sec1));
		
		// Change the self reg and self switching flags
		secMgr.setSelfRegistrationAllowed(course.getUuid(), true);
		Assert.assertTrue(secMgr.isSelfRegistrationAllowed(course.getUuid()));

		secMgr.setSelfRegistrationAllowed(course.getUuid(), false);
		Assert.assertTrue( ! secMgr.isSelfRegistrationAllowed(course.getUuid()));

		secMgr.setSelfSwitchingAllowed(course.getUuid(), true);
		Assert.assertTrue(secMgr.isSelfSwitchingAllowed(course.getUuid()));

		secMgr.setSelfSwitchingAllowed(course.getUuid(), false);
		Assert.assertTrue( ! secMgr.isSelfSwitchingAllowed(course.getUuid()));

    }
}



/**********************************************************************************
 * $Id$
 *********************************************************************************/
