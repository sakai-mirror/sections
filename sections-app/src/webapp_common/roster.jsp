<f:view>
<h:form id="rosterForm">

    <sakai:flowState bean="#{rosterBean}"/>

    <x:aliasBean alias="#{viewName}" value="roster">
        <%@include file="/inc/navMenu.jspf"%>
    </x:aliasBean>
    
    <x:div styleClass="portletBody">
        <h2><h:outputText value="#{msgs.roster_page_header}"/></h2>
        
        <%@include file="/inc/globalMessages.jspf"%>
    
    <h:panelGrid styleClass="sectionContainerNav" columns="2" columnClasses="sectionLeftNav,sectionRightNav">
        <x:div>
            <h:inputText id="search" value="#{rosterBean.searchText}"
                onfocus="clearIfDefaultString(this, '#{msgs.roster_search_text}')"/>
            <h:commandButton value="#{msgs.roster_search_button}" actionListener="#{rosterBean.search}"/>
            <h:commandButton value="#{msgs.roster_clear_button}" actionListener="#{rosterBean.clearSearch}"/>
        </x:div>
        
        <x:div>
            <sakai:pager id="pager" totalItems="#{rosterBean.enrollmentsSize}" firstItem="#{rosterBean.firstRow}" pageSize="#{rosterBean.maxDisplayedRows}" textStatus="#{msgs.roster_pager_status}" />
        </x:div>
    </h:panelGrid>
            
        <x:dataTable cellpadding="0" cellspacing="0"
            id="sectionsTable"
            value="#{rosterBean.enrollments}"
            var="enrollment"
            binding="#{rosterBean.rosterDataTable}"
            sortColumn="#{rosterBean.sortColumn}"
            sortAscending="#{rosterBean.sortAscending}"
            styleClass="listHier narrowTable">
            <h:column>
                <f:facet name="header">
                    <x:commandSortHeader columnName="studentName" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.roster_table_header_name}" />
                    </x:commandSortHeader>
                </f:facet>
                <h:commandLink action="editStudentSections" value="#{enrollment.user.sortName}">
                    <f:param name="studentUuid" value="#{enrollment.user.userUuid}"/>
                </h:commandLink>
            </h:column>
            <h:column>
                <f:facet name="header">
                    <x:commandSortHeader columnName="displayId" immediate="true" arrow="true">
                        <h:outputText value="#{msgs.roster_table_header_id}" />
                    </x:commandSortHeader>
                </f:facet>
                <h:outputText value="#{enrollment.user.displayId}"/>
            </h:column>
            
            <%/* A dynamic number of section columns will be appended here by the backing bean */%>
        
        </x:dataTable>
    </x:div>
</h:form>
</f:view>
