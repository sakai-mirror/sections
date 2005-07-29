/**********************************************************************************
*
* $Id: $
*
***********************************************************************************
*
* Copyright (c) 2003, 2004, 2005 The Regents of the University of Michigan, Trustees of Indiana University,
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
package org.sakaiproject.component.section;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.section.SectionAwareness;
import org.sakaiproject.api.section.facade.Role;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

public class SectionAwarenessHibernateImpl extends HibernateDaoSupport
        implements SectionAwareness {
	
	private Log log = LogFactory.getLog(SectionAwarenessHibernateImpl.class);

	/**
	 * A list (to be configured via dependency injection) containing category
	 * name keys, which should be localized with a properties file by the UI.
	 */
	protected List sectionCategoryList;
	
    public List getSections(final String contextId) {
    	if(log.isDebugEnabled()) log.debug("Getting sections for context " + contextId);
        HibernateCallback hc = new HibernateCallback(){
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("from CourseSectionImpl as section where section.courseOffering.context=:context");
                q.setParameter("context", contextId);
                return q.list();
            }
        };
        return (List)getHibernateTemplate().execute(hc);
    }

	public List getSectionCategories() {
		return sectionCategoryList;
	}

	public List getMembers(String contextId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getMembersInRoles(String contextId, Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isMemberInRole(String contextId, String personId, Role role) {
		// TODO Auto-generated method stub
		return false;
	}

	public List getSectionMembers(String sectionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSectionMembersInRole(String sectionId, Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSectionMemberInRole(String sectionId, String personId, Role role) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSectionName(String sectionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSectionCategory(String sectionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSectionsInCategory(String contextId, String categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategoryName(String categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	// Dependency injection

	public void setSectionCategoryList(List sectionCategoryList) {
		this.sectionCategoryList = sectionCategoryList;
	}

}


/**********************************************************************************
 * $Id: $
 *********************************************************************************/
