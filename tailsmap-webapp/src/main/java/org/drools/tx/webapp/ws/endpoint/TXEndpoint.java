/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.tx.webapp.ws.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.drools.tx.core.api.LabellingCoreService;
import org.drools.tx.core.api.NotificationService;
import org.drools.tx.core.impl.LabellingCoreServiceImpl;
import org.drools.tx.model.Location;
import org.drools.tx.model.Transaction;
import org.drools.tx.utils.LocationFactory;
import org.drools.tx.utils.TransactionFactory;
import org.xmlpull.v1.XmlPullParserException;

@ServerEndpoint( value = "/txs" )
public class TXEndpoint implements NotificationService {

    private static final Logger logger = Logger.getLogger( "TXEndpoint" );

    private static final Set<Session> peers = Collections.synchronizedSet( new HashSet() );

    private static LabellingCoreService labellingService = LabellingCoreServiceImpl.getInstance();

    public TXEndpoint() {
        System.out.println( "This TXEndpoint instance: " + this.hashCode() );
        init();

    }

    public void init() {

        labellingService.setNotificationService( this );
        labellingService.init();
        List<Location> locations;
        try {
            locations = LocationFactory.createLocationsFromCSV( "data/ret-loc.csv" );
            for ( Location loc : locations ) {
                labellingService.addLocation( loc );
            }
        } catch ( IOException | XmlPullParserException ex ) {
            Logger.getLogger( TXEndpoint.class.getName() ).log( Level.SEVERE, null, ex );
        }

        Thread thread = new Thread( new Runnable() {
            public void run() {
                try {
                    List<Transaction> txs = TransactionFactory.createTxsFromCSV( "data/data-mini.csv" );
                    long lastTimeStamp = 0;
                    for ( int i = 0; i < txs.size(); i++ ) {
                        try {
                            long sleep = 0;
                            if(lastTimeStamp != 0){
                                sleep = lastTimeStamp / 10;
                            }
                            System.out.println( " Sleeping For: " + sleep);
                            Thread.sleep( sleep );
                            labellingService.label( txs.get( i ) );
                            lastTimeStamp = txs.get( i + 1 ).getTxDate().getTime() - txs.get( i ).getTxDate().getTime();
                        } catch ( Exception ex ) {
                            Logger.getLogger( TXEndpoint.class.getName() ).log( Level.SEVERE, null, ex );
                        }
                    }
                } catch ( IOException | XmlPullParserException ex ) {
                    Logger.getLogger( TXEndpoint.class.getName() ).log( Level.SEVERE, null, ex );
                }
            }
        } );

        thread.start();

    }

    @OnOpen
    public void openConnection( Session session ) {
        /* Register this connection in the queue */
        System.out.println( "New Connection Opend for Session: " + session.toString() );
        peers.add( session );
        logger.log( Level.INFO, "Connection opened." );
    }

    @OnClose
    public void closedConnection( Session session ) {
        /* Remove this connection from the queue */
        System.out.println( "Closing connection for Session: " + session.toString() );
        peers.remove( session );
        logger.log( Level.INFO, "Connection closed." );
    }

    @OnError
    public void error( Session session, Throwable t ) {
        /* Remove this connection from the queue */
        System.out.println( "Error for Session: " + session.toString() );
        t.printStackTrace();
        peers.remove( session );
        logger.log( Level.INFO, t.toString() );
        logger.log( Level.INFO, "Connection error." );
    }

    @Override
    public void notify( String message ) {
        System.out.println( "Notify to websockets: " + peers.size() );
        try {
            /* Send updates to all open WebSocket sessions */
            for ( Session session : peers ) {
                System.out.println( "Notifying message to the websockets: " + message );
                session.getBasicRemote().sendText( message );
                logger.log( Level.INFO, "Sent: {0}", message );
            }
        } catch ( IOException e ) {
            logger.log( Level.INFO, e.toString() );
        }
    }
}
