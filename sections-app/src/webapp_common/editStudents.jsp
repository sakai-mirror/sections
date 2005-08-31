<f:view>
<h:form id="memberForm">

    <x:aliasBean alias="#{viewName}" value="editStudents">
        <%@include file="/inc/navMenu.jspf"%>
    </x:aliasBean>

    <sakai:flowState bean="#{editStudentsBean}"/>

    <h:panelGrid columns="3">
    
        <h:panelGroup>
            <h:selectOneMenu value="#{editStudentsBean.availableSectionUuid}" valueChangeListener="#{editStudentsBean.processChangeSection}" onchange="this.form.submit()">
                <f:selectItems value="#{editStudentsBean.availableSectionItems}"/>
            </h:selectOneMenu>
            
            <f:verbatim>
                <br/>
            </f:verbatim>
            
        	<h:selectManyListbox id="availableUsers" size="20" style="width:200px;">
        		<f:selectItems value="#{editStudentsBean.availableUsers}"/>
        	</h:selectManyListbox>
        </h:panelGroup>
    
        <%@include file="/inc/transferButtons.jspf"%>
    	
        <h:panelGroup>
            <h:outputText value="#{editStudentsBean.sectionTitle}"/>
        
            <f:verbatim>
                <br/>
            </f:verbatim>
        
        	<h:selectManyListbox id="selectedUsers" size="20" style="width:200px;">
                <f:selectItems value="#{editStudentsBean.selectedUsers}"/>
        	</h:selectManyListbox>
        </h:panelGroup>

    </h:panelGrid>
    
    <h:commandButton
        action="#{editStudentsBean.update}"
        onclick="highlightUsers()"
        value="#{msgs.edit_student_update}"/>

    <h:commandButton
        action="#{overview}"
        value="#{msgs.edit_student_cancel}"/>

</h:form>
</f:view>
