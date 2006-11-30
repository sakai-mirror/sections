<f:view>
<div class="portletBody">
<h:form id="overviewForm">

    <sakai:flowState bean="#{overviewBean}"/>

    <x:aliasBean alias="#{viewName}" value="overview">
        <%@include file="/inc/navMenu.jspf"%>
    </x:aliasBean>

    <h3><h:outputText value="#{msgs.overview_page_header}"/></h3>

    <%@include file="/inc/globalMessages.jspf"%>

    <x:aliasBean alias="#{filterBean}" value="#{overviewBean}">
        <%@include file="/inc/sectionFilter.jspf"%>
    </x:aliasBean>

    <sec:sectionTable cellpadding="0" cellspacing="0"
        id="sectionsTable"
        value="#{overviewBean.sections}"
        var="section"
        sortColumn="#{preferencesBean.overviewSortColumn}"
        sortAscending="#{preferencesBean.overviewSortAscending}"
        rules="rows"
        styleClass="listHier narrowTable"
        columnClasses="leftIndent,left,left,left,center,center,center"
        >
    
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="title" immediate="false" arrow="true">
                <h:outputText value="#{msgs.overview_table_header_name}" />
                </x:commandSortHeader>
            </f:facet>
            <x:div>
                <h:outputText value="#{section.title}"/>
            </x:div>
            <x:div styleClass="itemAction">
                <h:panelGroup rendered="#{ ! overviewBean.externallyManaged && overviewBean.sectionManagementEnabled}">
                    <h:commandLink action="editSection" value="#{msgs.overview_link_edit}">
                        <f:param name="sectionUuid" value="#{section.uuid}"/>
                    </h:commandLink>
                    <h:outputFormat value=" #{msgs.overview_link_sep_char} "/>
                </h:panelGroup>
    
                <h:commandLink
                    action="editManagers"
                    value="#{msgs.overview_link_managers}"
                    rendered="#{overviewBean.sectionTaManagementEnabled}">
                        <f:param name="sectionUuid" value="#{section.uuid}"/>
                </h:commandLink>
    
                <h:panelGroup rendered="#{ ! overviewBean.externallyManaged}">
                    <h:outputFormat
                        value=" #{msgs.overview_link_sep_char} "
                        rendered="#{overviewBean.sectionTaManagementEnabled}"/>
        
                    <h:commandLink
                        action="editStudents"
                        value="#{msgs.overview_link_students}"
                        rendered="#{overviewBean.sectionEnrollmentMangementEnabled}">
                            <f:param name="sectionUuid" value="#{section.uuid}"/>
                    </h:commandLink>
                </h:panelGroup>
            </x:div>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="managers" immediate="false" arrow="true">
                <h:outputText value="#{msgs.overview_table_header_managers}" />
                </x:commandSortHeader>
            </f:facet>
            <x:dataList id="instructorName"
                var="instructorName"
                value="#{section.instructorNames}"
                layout="simple">
                    <x:div>
                        <h:outputText value="#{instructorName}" />
                    </x:div>
            </x:dataList>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="meetingDays" immediate="false" arrow="true">
                	<h:outputText value="#{msgs.overview_table_header_day}" />
                </x:commandSortHeader>
            </f:facet>
            <x:dataList id="meetingDayList" value="#{section.decoratedMeetings}" var="meeting" layout="simple">
	            <x:div>
	            	<h:outputText value="#{meeting.abbreviatedDays}"/>
	            </x:div>
            </x:dataList>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="meetingTimes" immediate="false" arrow="true">
                	<h:outputText value="#{msgs.overview_table_header_time}" />
                </x:commandSortHeader>
            </f:facet>
            <x:dataList id="meetingTimeList" value="#{section.decoratedMeetings}" var="meeting" layout="simple">
	            <x:div>
		            <h:outputText value="#{meeting.times}"/>
	            </x:div>
            </x:dataList>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="location" immediate="false" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_location}" />
                </x:commandSortHeader>
            </f:facet>
            <x:dataList id="meetingLocationList" value="#{section.decoratedMeetings}" var="meeting" layout="simple">
	            <x:div>
	            	<h:outputText value="#{meeting.location}"/>
	            </x:div>
            </x:dataList>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="totalEnrollments" immediate="false" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_current_size}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.totalEnrollments}"/>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="available" immediate="false" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_available}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.spotsAvailable}"/>
        </h:column>
        <h:column rendered="#{overviewBean.deleteRendered}">
            <f:facet name="header">
                <h:outputText value="#{msgs.overview_table_header_remove}" />
            </f:facet>
            <h:selectBooleanCheckbox id="remove" value="#{section.flaggedForRemoval}"/>
        </h:column>
    </sec:sectionTable>

    <x:div styleClass="verticalPadding" rendered="#{empty overviewBean.sections}">
        <h:outputText value="#{msgs.no_sections_available}"/>
        <h:outputText value="#{msgs.no_sections_instructions}" rendered="#{overviewBean.sectionManagementEnabled}"/>
    </x:div>

    <x:div rendered="#{overviewBean.deleteRendered}" styleClass="verticalPadding">
        <%/* Add space before the buttons */%>
    </x:div>

    <x:div styleClass="act">
        <h:commandButton
            action="#{overviewBean.confirmDelete}"
            value="#{msgs.overview_delete}"
            rendered="#{overviewBean.deleteRendered}"
            styleClass="active"/>
    
        <h:commandButton
            action="overview"
            value="#{msgs.overview_cancel}"
            rendered="#{overviewBean.deleteRendered}"/>
    </x:div>

</h:form>
</div>
</f:view>
