/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drools.tx.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import org.drools.tx.core.impl.BaseNotificationServiceImpl;
import org.drools.tx.model.Line;
import org.drools.tx.model.Transaction;
import org.drools.tx.utils.TransactionFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.kie.api.cdi.KBase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;

/**
 *
 * @author salaboy
 */
@RunWith( Arquillian.class )
public class LabellingRulesTest {

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create( JavaArchive.class )
                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" )
                .addAsResource( "data/data-mini.csv", "data/data-mini.csv" )
                .addAsManifestResource( "META-INF/kmodule.xml", "kmodule.xml" );
        System.out.println( jar.toString( true ) );
        return jar;
    }

    @Inject
    @KBase( "labelKbase" )
    private KieBase kBase;

    @Test
    @Ignore
    public void hello() {
        assertNotNull( kBase );
        KieSession kSession = kBase.newKieSession();
        kSession.setGlobal( "notification", new BaseNotificationServiceImpl() );
        assertNotNull( kSession );
        List<Transaction> txs = createTXs();
        for ( Transaction tx : txs ) {
            kSession.insert( tx );

            for ( Line l : tx.getLines() ) {
                kSession.insert( l );
            }
        }

        kSession.openLiveQuery( "getUnlabelledTXs", null, new ViewChangedEventListener() {
            @Override
            public void rowInserted( Row row ) {
                //System.out.println( ">>> Row Inserted: " + row );
            }

            @Override
            public void rowDeleted( Row row ) {
                //System.out.println( ">>> Row Deleted: " + row );
            }

            @Override
            public void rowUpdated( Row row ) {
                //System.out.println( ">>> Row Updated: " + row );
            }
        } );
        int fireAllRules = kSession.fireAllRules();
        assertEquals( 3, fireAllRules );

        for ( Transaction tx : txs ) {
            //assertEquals( 2, tx.getLabels().size() );
            System.out.println( "TX Labels: " + tx.getFormatedLabels() );
        }

        QueryResults queryResults = kSession.getQueryResults( "getUnlabelledTXs", null );
        assertEquals( 0, queryResults.size() );
    }

    @Test
    public void realDataTest() throws Exception {
        assertNotNull( kBase );
        KieSession kSession = kBase.newKieSession();
        kSession.setGlobal( "notification", new BaseNotificationServiceImpl() );
        assertNotNull( kSession );
       
        List<Transaction> txs = TransactionFactory.createTxsFromCSV( "data/data-mini.csv"  );
        System.out.println( "Total TXS: " + txs.size() );
        for ( Transaction tx : txs ) {
            kSession.insert( tx );

            for ( Line l : tx.getLines() ) {
                kSession.insert( l );
            }
        }

        kSession.openLiveQuery( "getUnlabelledTXs", null, new ViewChangedEventListener() {
            @Override
            public void rowInserted( Row row ) {
                //System.out.println( ">>> Row Inserted: " + row );
            }

            @Override
            public void rowDeleted( Row row ) {
                //System.out.println( ">>> Row Deleted: " + row );
            }

            @Override
            public void rowUpdated( Row row ) {
                //System.out.println( ">>> Row Updated: " + row );
            }
        } );
        int fireAllRules = kSession.fireAllRules();
//        assertEquals( 669, fireAllRules );

        for ( Transaction tx : txs ) {
            //assertEquals( 2, tx.getLabels().size() );
            System.out.println( "TX Labels: " + tx.getFormatedLabels() );
        }

        QueryResults queryResults = kSession.getQueryResults( "getUnlabelledTXs", null );
        System.out.println( "Unlabelled: " + queryResults.size() );
        Iterator<QueryResultsRow> iterator = queryResults.iterator();
        while ( iterator.hasNext() ) {
            QueryResultsRow next = iterator.next();
            Transaction tx = ( Transaction ) next.get( "$tx" );
            System.out.println( "Unlabelled TX: " + tx );
        }

//        assertEquals( 0, queryResults.size() );
    }

    private List<Transaction> createTXs() {
        List<Transaction> txs = new ArrayList<>();
        Transaction tx = new Transaction();
        tx.setClientId( "111" );
        tx.setRetailerId( "AAA" );
        tx.setTxDate( new Date() );
        List<Line> lines = new ArrayList<>();
        lines.add( new Line( "C000", "Coffee", 2.0, 5.75 ) );
        tx.setLines( lines );
        tx.setBasketTotal( calculateBasketTotal( lines ) );
        txs.add( tx );
        tx = new Transaction();
        tx.setClientId( "222" );
        tx.setRetailerId( "AAA" );
        tx.setTxDate( new Date() );
        lines = new ArrayList<>();
        lines.add( new Line( "C001", "ACoffeeLatte", 1.0, 5.75 ) );
        tx.setLines( lines );
        tx.setBasketTotal( calculateBasketTotal( lines ) );
        txs.add( tx );

        return txs;

    }

    private Double calculateBasketTotal( List<Line> lines ) {
        Double total = 0.0;
        for ( Line l : lines ) {
            total += l.getQuantity() * l.getTotalToPay();
        }
        return total;
    }

}
