<f:view>
<h:form id="editSectionForm">

    <sakai:flowState bean="#{editSectionBean}"/>

    <x:aliasBean alias="#{viewName}" value="editSection">
        <%@include file="/inc/navMenu.jspf"%>
    </x:aliasBean>
    
    <x:div styleClass="portletBody">
        <h2><h:outputText value="#{msgs.edit_section_page_header}"/></h2>
        
        <%@include file="/inc/globalMessages.jspf"%>
    
        <h:panelGrid columns="2">
            <h:outputLabel for="titleInput" value="#{msgs.section_title}" styleClass="formLabel"/>
            <h:panelGroup>
                <h:inputText id="titleInput" required="true" value="#{editSectionBean.title}"/>
                <h:message for="titleInput" styleClass="validationEmbedded"/>
            </h:panelGroup>
            
            <h:outputLabel for="monday" value="#{msgs.section_days}" styleClass="formLabel"/>
            <h:panelGroup>
                <h:selectBooleanCheckbox id="monday" value="#{editSectionBean.monday}"/>
                <h:outputLabel for="monday" value="#{msgs.day_of_week_monday}"/>
    
                <h:selectBooleanCheckbox id="tuesday" value="#{editSectionBean.tuesday}"/>
                <h:outputLabel for="tuesday" value="#{msgs.day_of_week_tuesday}"/>
    
                <h:selectBooleanCheckbox id="wednesday" value="#{editSectionBean.wednesday}"/>
                <h:outputLabel for="wednesday" value="#{msgs.day_of_week_wednesday}"/>
    
                <h:selectBooleanCheckbox id="thursday" value="#{editSectionBean.thursday}"/>
                <h:outputLabel for="thursday" value="#{msgs.day_of_week_thursday}"/>
    
                <h:selectBooleanCheckbox id="friday" value="#{editSectionBean.friday}"/>
                <h:outputLabel for="friday" value="#{msgs.day_of_week_friday}"/>
    
                <h:selectBooleanCheckbox id="saturday" value="#{editSectionBean.saturday}"/>
                <h:outputLabel for="saturday" value="#{msgs.day_of_week_saturday}"/>
    
                <h:selectBooleanCheckbox id="sunday" value="#{editSectionBean.sunday}"/>
                <h:outputLabel for="sunday" value="#{msgs.day_of_week_sunday}"/>
            </h:panelGroup>
    
            <h:outputLabel for="startTime" value="#{msgs.section_start_time}" styleClass="formLabel"/>
            <h:panelGrid columns="3">
                <h:panelGroup>
                    <h:inputText id="startTime" value="#{editSectionBean.startTime}">
                        <f:convertDateTime type="time" pattern="h:mm"/>
                    </h:inputText>
                    <h:message for="startTime" styleClass="validationEmbedded"/>
                </h:panelGroup>
                <h:outputText value="#{msgs.section_start_time_example}"/>
                <h:selectOneRadio value="#{editSectionBean.startTimeAm}">
                    <f:selectItem itemValue="true" itemLabel="#{msgs.time_of_day_am_cap}"/>
                    <f:selectItem itemValue="false" itemLabel="#{msgs.time_of_day_pm_cap}"/>
                </h:selectOneRadio>
            </h:panelGrid>
    
            <h:outputLabel for="endTime" value="#{msgs.section_end_time}" styleClass="formLabel"/>
            <h:panelGrid columns="3">
                <h:panelGroup>
                    <h:inputText id="endTime" value="#{editSectionBean.endTime}">
                        <f:convertDateTime type="time" pattern="h:mm"/>
                    </h:inputText>
                    <h:message for="endTime" styleClass="validationEmbedded"/>
                </h:panelGroup>
                <h:outputText value="#{msgs.section_end_time_example}"/>
                <h:selectOneRadio value="#{editSectionBean.endTimeAm}">
                    <f:selectItem itemValue="true" itemLabel="#{msgs.time_of_day_am_cap}"/>
                    <f:selectItem itemValue="false" itemLabel="#{msgs.time_of_day_pm_cap}"/>
                </h:selectOneRadio>
            </h:panelGrid>
    
            <h:outputLabel for="maxEnrollmentInput" value="#{msgs.section_max_size}" styleClass="formLabel"/>
            <h:panelGroup>
                <h:inputText
                    id="maxEnrollmentInput"
                    value="#{editSectionBean.maxEnrollments}">
                    <f:validateLongRange minimum="0" />
                </h:inputText>
                <h:message for="maxEnrollmentInput" styleClass="validationEmbedded"/>
            </h:panelGroup>
    
            <h:outputLabel value="#{msgs.section_location}" for="location" styleClass="formLabel"/>
            <h:inputText id="location" value="#{editSectionBean.location}" styleClass="formLabel"/>
        </h:panelGrid>
    
        <h:commandButton action="#{editSectionBean.update}" value="Update"/>
        
        <h:commandButton action="deleteSection" value="Delete">
            <f:param name="sectionUuid" value="#{editSectionBean.sectionUuid}"/>
        </h:commandButton>
        
        <h:commandButton action="overview" value="Cancel" immediate="true" />
    </x:div>    
</h:form>
</f:view>
