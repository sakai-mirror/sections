<f:view>
<div class="portletBody">
<h:form id="studentViewForm">
<%/*
    Due to the limited screen real estate available in sakai iframes, several
    columns have been removed from this view.  The standalone version has
    retained these extra columns.  When the portal learns to live without iframes,
    the standalone version of this page should replace this one.
*/%>

    <sakai:flowState bean="#{studentViewBean}"/>

        <h3><h:outputText value="#{msgs.student_view_page_header}"/></h3>
    
        <x:div styleClass="instructions" rendered="#{not empty studentViewBean.instructions}">
            <h:outputText value="#{studentViewBean.instructions}"/>
        </x:div>

        <%@include file="/inc/globalMessages.jspf"%>

        <h:panelGrid styleClass="sectionContainerNav" columns="2" columnClasses="sectionLeftNav,sectionRightNav">
            <x:div>
                <h:outputText
                    value="#{msgs.student_view_view}"/>
            
                <h:selectOneMenu value="#{studentViewBean.sectionFilter}" onchange="this.form.submit()">
                    <f:selectItem itemLabel="#{msgs.student_view_all}" itemValue="ALL"/>
                    <f:selectItem itemLabel="#{msgs.student_view_my}" itemValue="MY"/>
                </h:selectOneMenu>
            </x:div>

            <x:div>
                <h:outputText
                    value="#{msgs.student_view_sections_in_bold}"
                    styleClass="studentSectionInfo"
                    rendered="#{not empty studentViewBean.sections}"/>
            </x:div>
        </h:panelGrid>

        <x:dataTable cellpadding="0" cellspacing="0"
            id="studentViewSectionsTable"
            value="#{studentViewBean.sections}"
            var="section"
            sortColumn="#{studentViewBean.sortColumn}"
            sortAscending="#{studentViewBean.sortAscending}"
            styleClass="listHier"
            rowClasses="#{studentViewBean.rowClasses}">
    
            <h:column>
                <f:facet name="header">
                    <x:commandSortHeader columnName="title" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.student_view_header_title}" />
                    </x:commandSortHeader>
                </f:facet>
                <h:outputText value="#{section.title}"/>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <x:commandSortHeader columnName="instructor" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.student_view_header_instructor}" />
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
	                	<h:outputText value="#{msgs.student_view_header_day}" />
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
                    <x:commandSortHeader columnName="meetingTimes" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.student_view_header_time}" />
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
                    <x:commandSortHeader columnName="location" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.student_view_header_location}" />
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
                    <x:commandSortHeader columnName="available" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.student_view_header_available}" />
                    </x:commandSortHeader>
                </f:facet>
            	<h:outputText value="#{section.spotsAvailable}"/>
            </h:column>

            <h:column rendered="#{!studentViewBean.externallyManaged}">
                <h:commandLink
                    value="#{msgs.student_view_join}"
                    actionListener="#{studentViewBean.processJoinSection}"
                    rendered="#{section.joinable && studentViewBean.joinAllowed}">
                    <f:param name="sectionUuid" value="#{section.uuid}"/>
                </h:commandLink>
                <h:commandLink
                    value="#{msgs.student_view_switch}"
                    actionListener="#{studentViewBean.processSwitchSection}"
                    rendered="#{section.switchable && studentViewBean.switchAllowed}">
                    <f:param name="sectionUuid" value="#{section.uuid}"/>
                </h:commandLink>
                <h:outputText
                    value="#{msgs.student_view_full}"
                    rendered="#{section.full}"/>
                <h:outputText
                    value="#{msgs.student_view_member}"
                    rendered="#{section.member}"/>
            </h:column>
    
        </x:dataTable>

        <x:div styleClass="verticalPadding" rendered="#{empty studentViewBean.sections}">
            <h:outputText value="#{msgs.no_sections_available}"/>
        </x:div>

</h:form>
</div>
</f:view>
