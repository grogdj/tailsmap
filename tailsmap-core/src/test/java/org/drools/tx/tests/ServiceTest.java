
package org.drools.tx.tests;

//
//package org.drols.tx.tests;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//import org.drools.tx.endpoint.api.TransactionsEndpoint;
//import org.drools.tx.endpoint.impl.TransactionsEndpointImpl;
//import org.drools.tx.model.Transaction;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.container.test.api.RunAsClient;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.wildfly.swarm.jaxrs.JAXRSArchive;
//
//@RunWith( Arquillian.class )
//public class ServiceTest {
//
//    private Client client;
//    private ResteasyWebTarget target;
//    private static final String APP_URL = "http://localhost:8080/";
//
//    @Deployment
//    public static Archive createDeployment() throws IllegalArgumentException, Exception {
//        JAXRSArchive deployment = ShrinkWrap.create( JAXRSArchive.class );
//
//        deployment.setContextRoot( "/api" );
//        deployment.addClass( TransactionsEndpoint.class );
//        deployment.addClass( TransactionsEndpointImpl.class );
//        deployment.addPackage( "org.drools.tx.model" );
//        deployment.addAsManifestResource( "META-INF/kmodule.xml", "kmodule.xml" );
//        deployment.addAsResource( "rules/labelling.drl",  "rules/labelling.drl");
//        deployment.addAllDependencies()
//                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" );
//
//        
//        return deployment;
//    }
//
//    @Before
//    public void setup() throws Exception {
//        client = ClientBuilder.newBuilder().build();
//        final WebTarget webtarget = client.target( APP_URL );
//        target = ( ResteasyWebTarget ) webtarget;
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        client.close();
//    }
//
//    @Test
//    @RunAsClient
//    public void processTransactionTest() {
//        TransactionsEndpoint proxy = target.proxy( TransactionsEndpoint.class );
//        Transaction tx = new Transaction();
//        proxy.processTx( tx );
//
//    }
//
//}
