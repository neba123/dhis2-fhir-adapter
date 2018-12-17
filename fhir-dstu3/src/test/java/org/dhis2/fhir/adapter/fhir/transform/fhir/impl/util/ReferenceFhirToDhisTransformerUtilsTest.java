package org.dhis2.fhir.adapter.fhir.transform.fhir.impl.util;

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

import ca.uhn.fhir.context.FhirContext;
import org.dhis2.fhir.adapter.fhir.metadata.model.FhirResourceType;
import org.dhis2.fhir.adapter.fhir.metadata.model.RemoteSubscription;
import org.dhis2.fhir.adapter.fhir.metadata.model.RemoteSubscriptionResource;
import org.dhis2.fhir.adapter.fhir.metadata.model.SubscriptionFhirEndpoint;
import org.dhis2.fhir.adapter.fhir.metadata.repository.RemoteSubscriptionResourceRepository;
import org.dhis2.fhir.adapter.fhir.model.FhirVersion;
import org.dhis2.fhir.adapter.fhir.repository.RemoteFhirResourceRepository;
import org.dhis2.fhir.adapter.fhir.script.ScriptExecution;
import org.dhis2.fhir.adapter.fhir.script.ScriptExecutionContext;
import org.dhis2.fhir.adapter.fhir.transform.fhir.FhirToDhisTransformerContext;
import org.dhis2.fhir.adapter.fhir.transform.fhir.model.FhirRequest;
import org.dhis2.fhir.adapter.fhir.transform.fhir.model.ResourceSystem;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for {@link ReferenceFhirToDhisTransformerUtils}.
 *
 * @author volsch
 */
public class ReferenceFhirToDhisTransformerUtilsTest
{
    @Mock
    private ScriptExecutionContext scriptExecutionContext;

    @Mock
    private RemoteSubscriptionResourceRepository subscriptionResourceRepository;

    @Mock
    private RemoteFhirResourceRepository remoteFhirResourceRepository;

    @Mock
    private FhirToDhisTransformerContext context;

    @Mock
    private FhirRequest request;

    @Mock
    private ScriptExecution scriptExecution;

    @Mock
    private Map<String, Object> variables;

    @Mock
    private IBaseReference reference;

    @InjectMocks
    private ReferenceFhirToDhisTransformerUtils utils;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void getResourceNull()
    {
        Assert.assertNull( utils.getResource( null, "PATIENT", false ) );
    }

    @Test
    public void getIncludedResource()
    {
        final Patient resource = new Patient();
        final IIdType id = new IdType( "Patient", "123" );
        Mockito.doReturn( resource ).when( reference ).getResource();
        Assert.assertSame( resource, utils.getResource( reference, "PATIENT", false ) );
    }

    @Test
    public void getIncludedResourceNoType()
    {
        final Patient resource = new Patient();
        final IIdType id = new IdType( null, "123" );
        Mockito.doReturn( resource ).when( reference ).getResource();
        Assert.assertSame( resource, utils.getResource( reference, "PATIENT", false ) );
    }

    @Test
    public void getIncludedResourceNoRequestType()
    {
        final Patient resource = new Patient();
        final IIdType id = new IdType( "Patient", "123" );
        Mockito.doReturn( resource ).when( reference ).getResource();
        Assert.assertSame( resource, utils.getResource( reference, null, false ) );
    }

    @Test
    public void getResource()
    {
        final SubscriptionFhirEndpoint subscriptionFhirEndpoint = new SubscriptionFhirEndpoint();
        final UUID remoteSubscriptionId = UUID.randomUUID();
        final RemoteSubscription remoteSubscription = new RemoteSubscription();
        remoteSubscription.setId( remoteSubscriptionId );
        remoteSubscription.setFhirVersion( FhirVersion.DSTU3 );
        remoteSubscription.setFhirEndpoint( subscriptionFhirEndpoint );
        final FhirContext fhirContext = FhirContext.forDstu3();
        final UUID remoteSubscriptionResourceId = UUID.randomUUID();
        final RemoteSubscriptionResource remoteSubscriptionResource = new RemoteSubscriptionResource();
        remoteSubscriptionResource.setId( remoteSubscriptionResourceId );
        remoteSubscriptionResource.setRemoteSubscription( remoteSubscription );
        final ResourceSystem resourceSystem = new ResourceSystem( FhirResourceType.ORGANIZATION, "http://test.com", "OT_", null );
        Mockito.doReturn( scriptExecution ).when( scriptExecutionContext ).getScriptExecution();
        Mockito.doReturn( variables ).when( scriptExecution ).getVariables();
        Mockito.doReturn( context ).when( variables ).get( Mockito.eq( "context" ) );
        Mockito.doReturn( request ).when( context ).getFhirRequest();
        Mockito.doReturn( remoteSubscriptionResourceId ).when( request ).getRemoteSubscriptionResourceId();
        Mockito.doReturn( FhirVersion.DSTU3 ).when( request ).getVersion();
        Mockito.doReturn( Optional.of( resourceSystem ) ).when( request ).getOptionalResourceSystem( FhirResourceType.ORGANIZATION );
        Mockito.doReturn( Optional.of( remoteSubscriptionResource ) ).when( subscriptionResourceRepository ).findOneByIdCached( Mockito.eq( remoteSubscriptionResourceId ) );
        Mockito.doReturn( Optional.of( fhirContext ) ).when( remoteFhirResourceRepository ).findFhirContext( Mockito.eq( FhirVersion.DSTU3 ) );

        final Patient resource = new Patient();
        final IIdType id = new IdType( "Patient", "123" );
        resource.setId( id );
        final Reference reference = new Reference( id );

        Mockito.doReturn( Optional.of( resource ) ).when( remoteFhirResourceRepository )
            .find( Mockito.eq( remoteSubscriptionId ), Mockito.eq( FhirVersion.DSTU3 ),
                Mockito.same( subscriptionFhirEndpoint ), Mockito.eq( "Patient" ), Mockito.eq( "123" ) );

        final Resource result = (Resource) utils.getResource( reference, null, false );
        Assert.assertTrue( result instanceof Patient );
        Assert.assertEquals( id, result.getIdElement() );
        Assert.assertNotSame( resource, result );

        Mockito.verify( remoteFhirResourceRepository ).find( Mockito.eq( remoteSubscriptionId ), Mockito.eq( FhirVersion.DSTU3 ),
            Mockito.same( subscriptionFhirEndpoint ), Mockito.eq( "Patient" ), Mockito.eq( "123" ) );
    }

    @Test
    public void getResourceRefreshed()
    {
        final SubscriptionFhirEndpoint subscriptionFhirEndpoint = new SubscriptionFhirEndpoint();
        final UUID remoteSubscriptionId = UUID.randomUUID();
        final RemoteSubscription remoteSubscription = new RemoteSubscription();
        remoteSubscription.setId( remoteSubscriptionId );
        remoteSubscription.setFhirVersion( FhirVersion.DSTU3 );
        remoteSubscription.setFhirEndpoint( subscriptionFhirEndpoint );
        final FhirContext fhirContext = FhirContext.forDstu3();
        final UUID remoteSubscriptionResourceId = UUID.randomUUID();
        final RemoteSubscriptionResource remoteSubscriptionResource = new RemoteSubscriptionResource();
        remoteSubscriptionResource.setId( remoteSubscriptionResourceId );
        remoteSubscriptionResource.setRemoteSubscription( remoteSubscription );
        final ResourceSystem resourceSystem = new ResourceSystem( FhirResourceType.ORGANIZATION, "http://test.com", "OT_", null );
        Mockito.doReturn( scriptExecution ).when( scriptExecutionContext ).getScriptExecution();
        Mockito.doReturn( variables ).when( scriptExecution ).getVariables();
        Mockito.doReturn( context ).when( variables ).get( Mockito.eq( "context" ) );
        Mockito.doReturn( request ).when( context ).getFhirRequest();
        Mockito.doReturn( remoteSubscriptionResourceId ).when( request ).getRemoteSubscriptionResourceId();
        Mockito.doReturn( FhirVersion.DSTU3 ).when( request ).getVersion();
        Mockito.doReturn( Optional.of( resourceSystem ) ).when( request ).getOptionalResourceSystem( FhirResourceType.ORGANIZATION );
        Mockito.doReturn( Optional.of( remoteSubscriptionResource ) ).when( subscriptionResourceRepository ).findOneByIdCached( Mockito.eq( remoteSubscriptionResourceId ) );
        Mockito.doReturn( Optional.of( fhirContext ) ).when( remoteFhirResourceRepository ).findFhirContext( Mockito.eq( FhirVersion.DSTU3 ) );

        final Patient resource = new Patient();
        final IIdType id = new IdType( "Patient", "123" );
        resource.setId( id );
        final Reference reference = new Reference( id );

        Mockito.doReturn( Optional.of( resource ) ).when( remoteFhirResourceRepository )
            .findRefreshed( Mockito.eq( remoteSubscriptionId ), Mockito.eq( FhirVersion.DSTU3 ),
                Mockito.same( subscriptionFhirEndpoint ), Mockito.eq( "Patient" ), Mockito.eq( "123" ) );

        final Resource result = (Resource) utils.getResource( reference, null, true );
        Assert.assertTrue( result instanceof Patient );
        Assert.assertEquals( id, result.getIdElement() );
        Assert.assertNotSame( resource, result );

        Mockito.verify( remoteFhirResourceRepository ).findRefreshed( Mockito.eq( remoteSubscriptionId ), Mockito.eq( FhirVersion.DSTU3 ),
            Mockito.same( subscriptionFhirEndpoint ), Mockito.eq( "Patient" ), Mockito.eq( "123" ) );
    }

    @Test
    public void initReference()
    {
        final SubscriptionFhirEndpoint subscriptionFhirEndpoint = new SubscriptionFhirEndpoint();
        final UUID remoteSubscriptionId = UUID.randomUUID();
        final RemoteSubscription remoteSubscription = new RemoteSubscription();
        remoteSubscription.setId( remoteSubscriptionId );
        remoteSubscription.setFhirVersion( FhirVersion.DSTU3 );
        remoteSubscription.setFhirEndpoint( subscriptionFhirEndpoint );
        final FhirContext fhirContext = FhirContext.forDstu3();
        final UUID remoteSubscriptionResourceId = UUID.randomUUID();
        final RemoteSubscriptionResource remoteSubscriptionResource = new RemoteSubscriptionResource();
        remoteSubscriptionResource.setId( remoteSubscriptionResourceId );
        remoteSubscriptionResource.setRemoteSubscription( remoteSubscription );
        final ResourceSystem resourceSystem = new ResourceSystem( FhirResourceType.ORGANIZATION, "http://test.com", "OT_", null );
        Mockito.doReturn( scriptExecution ).when( scriptExecutionContext ).getScriptExecution();
        Mockito.doReturn( variables ).when( scriptExecution ).getVariables();
        Mockito.doReturn( context ).when( variables ).get( Mockito.eq( "context" ) );
        Mockito.doReturn( request ).when( context ).getFhirRequest();
        Mockito.doReturn( remoteSubscriptionResourceId ).when( request ).getRemoteSubscriptionResourceId();
        Mockito.doReturn( FhirVersion.DSTU3 ).when( request ).getVersion();
        Mockito.doReturn( Optional.of( resourceSystem ) ).when( request ).getOptionalResourceSystem( FhirResourceType.ORGANIZATION );
        Mockito.doReturn( Optional.of( remoteSubscriptionResource ) ).when( subscriptionResourceRepository ).findOneByIdCached( Mockito.eq( remoteSubscriptionResourceId ) );
        Mockito.doReturn( Optional.of( fhirContext ) ).when( remoteFhirResourceRepository ).findFhirContext( Mockito.eq( FhirVersion.DSTU3 ) );

        final Patient resource = new Patient();
        final IIdType id = new IdType( "Patient", "123" );
        resource.setId( id );
        final Reference reference = new Reference( id );

        Mockito.doReturn( Optional.of( resource ) ).when( remoteFhirResourceRepository )
            .find( Mockito.eq( remoteSubscriptionId ), Mockito.eq( FhirVersion.DSTU3 ),
                Mockito.same( subscriptionFhirEndpoint ), Mockito.eq( "Patient" ), Mockito.eq( "123" ) );

        utils.initReference( reference, "PATIENT" );
        Assert.assertTrue( reference.getResource() instanceof Patient );
        Assert.assertEquals( id, reference.getResource().getIdElement() );
        Assert.assertNotSame( resource, reference.getResource() );

        Mockito.verify( remoteFhirResourceRepository ).find( Mockito.eq( remoteSubscriptionId ), Mockito.eq( FhirVersion.DSTU3 ),
            Mockito.same( subscriptionFhirEndpoint ), Mockito.eq( "Patient" ), Mockito.eq( "123" ) );
    }
}