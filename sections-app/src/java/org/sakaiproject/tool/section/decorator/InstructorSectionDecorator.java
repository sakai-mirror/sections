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

package org.sakaiproject.tool.section.decorator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.section.coursemanagement.CourseSection;
import org.sakaiproject.tool.section.jsf.JsfUtil;

/**
 * Decorates a CourseSection for use in the instructor's (and TA's) page views.
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class InstructorSectionDecorator extends CourseSectionDecorator
	implements Serializable, Comparable {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(InstructorSectionDecorator.class);

	public static final int NAME_TRUNCATION_LENGTH = 20;
	
	protected List instructorNames;
	protected int totalEnrollments;
	protected String spotsAvailable;
	private boolean flaggedForRemoval;
	
	public InstructorSectionDecorator(CourseSection courseSection, String categoryForDisplay,
			List instructorNames, int totalEnrollments) {
		super(courseSection, categoryForDisplay);
		this.instructorNames = instructorNames;
		this.totalEnrollments = totalEnrollments;
		populateSpotsAvailable(courseSection);
	}

	protected void populateSpotsAvailable(CourseSection courseSection) {
		if(courseSection.getMaxEnrollments() == null) {
			spotsAvailable = JsfUtil.getLocalizedMessage("section_max_size_unlimited");
		} else {
			int spots = courseSection.getMaxEnrollments().intValue() - totalEnrollments;
			// Allow negative values to be displayed
			spotsAvailable = Integer.toString(spots);
		}
	}

	public InstructorSectionDecorator() {
		// Needed for serialization
	}
	
	public List getInstructorNames() {
		return instructorNames;
	}
	public String getSpotsAvailable() {
		return spotsAvailable;
	}
	public boolean isFlaggedForRemoval() {
		return flaggedForRemoval;
	}
	public void setFlaggedForRemoval(boolean flaggedForRemoval) {
		this.flaggedForRemoval = flaggedForRemoval;
	}
	
	public int compareTo(Object o) {
		return this.getTitle().compareTo(((InstructorSectionDecorator)o).getTitle());
	}
		
	public static final Comparator getFieldComparator(final String fieldName,
			final boolean sortAscending, final List categoryNames, final List categoryIds) {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				if(o1 instanceof InstructorSectionDecorator && o2 instanceof InstructorSectionDecorator) {
					InstructorSectionDecorator section1 = (InstructorSectionDecorator)o1;
					InstructorSectionDecorator section2 = (InstructorSectionDecorator)o2;

					// First compare the category name, then compare the time
					int categoryNameComparison = categoryNameComparison(section1,
							section2, categoryNames, categoryIds);
					if(categoryNameComparison == 0) {
						// These are in the same category, so compare by the field
						Comparable object1;
						Comparable object2;
						try {
							object1 = (Comparable)PropertyUtils.getProperty(section1, fieldName);
							object2 = (Comparable)PropertyUtils.getProperty(section2, fieldName);
						} catch (Exception e) {
							if(log.isDebugEnabled()) log.debug("Could not read field " +
									fieldName + " on objects" + o1 + " and " + o2);
							return 0;
						}
						if(object1 == null && object2 != null) {
							return sortAscending? -1 : 1 ;
						}
						if(object2 == null && object1 != null) {
							return sortAscending? 1 : -1 ;
						}
						if(object1 == null && object2 == null) {
							if(fieldName.equals("title")) {
								return 0;
							} else {
								return getFieldComparator("title", sortAscending, categoryNames, categoryIds).compare(o1, o2);
							}
						}
						int comparison = object1.compareTo(object2);
						return sortAscending ? comparison : (-1 * comparison);
					}
					// These are in different categories, so sort them by category name
					return categoryNameComparison;
				}
				if(log.isDebugEnabled()) log.debug("One of these is not an InstructorSectionDecorator: "
						+ o1 + "," + o2);
				return 0;
			}
		};
	}

	public static final Comparator getManagersComparator(final boolean sortAscending,
			final List categoryNames, final List categoryIds) {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				if(o1 instanceof InstructorSectionDecorator && o2 instanceof InstructorSectionDecorator) {
					InstructorSectionDecorator section1 = (InstructorSectionDecorator)o1;
					InstructorSectionDecorator section2 = (InstructorSectionDecorator)o2;

					// First compare the category name, then compare the time
					int categoryNameComparison = categoryNameComparison(section1,
							section2, categoryNames, categoryIds);
					if(categoryNameComparison == 0) {
						// These are in the same category, so compare by the list of managers
						List managers1 = section1.getInstructorNames();
						List managers2 = section2.getInstructorNames();
						if(managers1.isEmpty() && ! managers2.isEmpty()) {
							return sortAscending? -1 : 1 ;
						}
						if(managers2.isEmpty() && ! managers1.isEmpty()) {
							return sortAscending? 1 : -1 ;
						}
						if(managers1.isEmpty() && managers2.isEmpty()) {
							return 0;
						}
						int managersComparison = managers1.get(0).toString().compareTo(managers2.get(0));
						return sortAscending ? managersComparison : (-1 * managersComparison);
					}
					// These are in different categories, so sort them by category name
					return categoryNameComparison;
				}
				if(log.isDebugEnabled()) log.debug("One of these is not an InstructorSectionDecorator: "
						+ o1 + "," + o2);
				return 0;
			}
		};
	}

	public static final Comparator getAvailableEnrollmentsComparator(final boolean sortAscending,
			final List categoryNames, final List categoryIds) {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				if(o1 instanceof InstructorSectionDecorator && o2 instanceof InstructorSectionDecorator) {
					InstructorSectionDecorator section1 = (InstructorSectionDecorator)o1;
					InstructorSectionDecorator section2 = (InstructorSectionDecorator)o2;

					// First compare the category name, then compare available spots
					int categoryNameComparison = categoryNameComparison(section1,
							section2, categoryNames, categoryIds);
					if(categoryNameComparison == 0) {
						// These are in the same category, so compare by available enrollments
						Integer maxEnrollments1 = section1.getMaxEnrollments();
						Integer maxEnrollments2 = section2.getMaxEnrollments();
						if(maxEnrollments1 == null && maxEnrollments2 != null) {
							return sortAscending? 1 : -1 ;
						}
						if(maxEnrollments2 == null && maxEnrollments1 != null) {
							return sortAscending? -1 : 1 ;
						}
						if(maxEnrollments1 == null && maxEnrollments2 == null) {
							return 0;
						}
						int availEnrollmentComparison =
							(maxEnrollments1.intValue() - section1.totalEnrollments) -
							(maxEnrollments2.intValue() - section2.totalEnrollments);
						return sortAscending ? availEnrollmentComparison : (-1 * availEnrollmentComparison);
					}
					// These are in different categories, so sort them by category name
					return categoryNameComparison;
				}
				if(log.isDebugEnabled()) log.debug("One of these is not an InstructorSectionDecorator: "
						+ o1 + "," + o2);
				return 0;
			}
		};
	}

	protected static final int categoryNameComparison(InstructorSectionDecorator section1,
			InstructorSectionDecorator section2, List categoryNames, List categoryIds) {
		String section1Name = (String)categoryNames.get(categoryIds.indexOf(section1.getCategory()));
		String section2Name = (String)categoryNames.get(categoryIds.indexOf(section2.getCategory()));
		return section1Name.compareTo(section2Name);
	}

}



/**********************************************************************************
 * $Id$
 *********************************************************************************/
