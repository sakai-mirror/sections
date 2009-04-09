/**********************************************************************************
 * $URL:$
 * $Id:$
 ***********************************************************************************
 *
 * Copyright (c) 2008 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.sakaiproject.tool.section.jsf;

import org.apache.myfaces.component.html.ext.HtmlDataTable;

/**
 * Author:Louis Majanja <louis@media.berkeley.edu>
 * Date: Jan 18, 2007
 * Time: 1:07:09 PM
 */
public class RowGroupDataTable extends HtmlDataTable {

    public String category;
    public static final String COMPONENT_TYPE = "org.sakaiproject.tool.section.jsf.RowGroupDataTable";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    public static final String DEFAULT_RENDERER_TYPE = "org.sakaiproject.tool.section.jsf.RowGroupDataTableRenderer";


    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }
}
