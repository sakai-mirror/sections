<f:view>
<h:form id="overviewForm">

    <sakai:flowState bean="#{overviewBean}"/>

    <h:panelGroup rendered="#{ ! overviewBean.externallyManaged}">
        <x:aliasBean alias="#{viewName}" value="overview">
            <%@include file="/inc/navMenu.jspf"%>
        </x:aliasBean>
    </h:panelGroup>
    
    <h:messages globalOnly="true"/>
    
    <x:dataTable cellpadding="0" cellspacing="0"
        id="sectionsTable"
        value="#{overviewBean.sections}"
        var="section"
        sortColumn="#{overviewBean.sortColumn}"
        sortAscending="#{overviewBean.sortAscending}"
        rowClasses="#{overviewBean.rowClasses}">

        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="title" immediate="true" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_name}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.title}"/>

            <f:verbatim>
                <br/>&nbsp;&nbsp;&nbsp;&nbsp;
            </f:verbatim>
            
            <h:panelGroup rendered="#{ ! overviewBean.externallyManaged && overviewBean.instructorFeaturesEnabled}">
                <h:commandLink action="editSection" value="#{msgs.overview_link_edit}">
                    <f:param name="sectionUuid" value="#{section.uuid}"/>
                </h:commandLink>
                
                <h:outputFormat value="#{msgs.overview_link_sep_char}"/>
            </h:panelGroup>

            <h:commandLink
                action="editManagers"
                value="#{msgs.overview_link_managers}"
                rendered="#{overviewBean.instructorFeaturesEnabled}">
                    <f:param name="sectionUuid" value="#{section.uuid}"/>
            </h:commandLink>
            
            <h:panelGroup rendered="#{ ! overviewBean.externallyManaged}">
                <h:outputFormat value="#{msgs.overview_link_sep_char}"/>

                <h:commandLink action="editStudents" value="#{msgs.overview_link_students}">
                    <f:param name="sectionUuid" value="#{section.uuid}"/>
                </h:commandLink>
            </h:panelGroup>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="time" immediate="true" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_day_time}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.meetingTimes}"/>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="managers" immediate="true" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_managers}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.instructorNames}"/>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="location" immediate="true" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_location}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.location}"/>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="max" immediate="true" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_max_size}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.maxEnrollments}"/>
        </h:column>
        <h:column>
            <f:facet name="header">
                <x:commandSortHeader columnName="available" immediate="true" arrow="true">
                    <h:outputText value="#{msgs.overview_table_header_available}" />
                </x:commandSortHeader>
            </f:facet>
            <h:outputText value="#{section.spotsAvailable}"/>
        </h:column>
    </x:dataTable>

</h:form>
</f:view>
