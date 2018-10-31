package org.dhis2.fhir.adapter.fhir.metadata.model;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Contains the subscription status of a single FHIR resource type.
 *
 * @author volsch
 */
@Entity
@Table( name = "fhir_remote_subscription_resource" )
public class RemoteSubscriptionResource extends BaseMetadata implements Serializable
{
    private static final long serialVersionUID = -6797001318266984453L;

    private FhirResourceType fhirResourceType;
    private String fhirCriteriaParameters;
    private String description;
    private RemoteSubscription remoteSubscription;
    private RemoteSubscriptionResourceUpdate resourceUpdate;
    private String fhirSubscriptionId;

    @Basic
    @Column( name = "fhir_resource_type", nullable = false, length = 30 )
    @Enumerated( EnumType.STRING )
    public FhirResourceType getFhirResourceType()
    {
        return fhirResourceType;
    }

    public void setFhirResourceType( FhirResourceType fhirResourceType )
    {
        this.fhirResourceType = fhirResourceType;
    }

    @Basic
    @Column( name = "fhir_criteria_parameters", length = 200 )
    public String getFhirCriteriaParameters()
    {
        return fhirCriteriaParameters;
    }

    public void setFhirCriteriaParameters( String fhirCriteriaParameters )
    {
        this.fhirCriteriaParameters = fhirCriteriaParameters;
    }

    @Basic
    @Column( name = "description", length = -1 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @ManyToOne( optional = false )
    @JoinColumn( name = "remote_subscription_id", referencedColumnName = "id", nullable = false )
    public RemoteSubscription getRemoteSubscription()
    {
        return remoteSubscription;
    }

    public void setRemoteSubscription( RemoteSubscription remoteSubscription )
    {
        this.remoteSubscription = remoteSubscription;
    }

    @RestResource( exported = false )
    @OneToOne( mappedBy = "remoteSubscriptionResource", cascade = { CascadeType.REMOVE, CascadeType.PERSIST } )
    @JsonIgnore
    public RemoteSubscriptionResourceUpdate getResourceUpdate()
    {
        return resourceUpdate;
    }

    public void setResourceUpdate( RemoteSubscriptionResourceUpdate resourceUpdate )
    {
        this.resourceUpdate = resourceUpdate;
    }

    @Basic
    @Column( name = "fhir_subscription_id", length = 100 )
    public String getFhirSubscriptionId()
    {
        return fhirSubscriptionId;
    }

    public void setFhirSubscriptionId( String fhirSubscriptionId )
    {
        this.fhirSubscriptionId = fhirSubscriptionId;
    }
}
