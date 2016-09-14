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

package org.drools.tx.core.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.inject.Singleton;
import org.drools.tx.core.api.LabellingCoreService;
import org.drools.tx.core.api.NotificationService;
import org.drools.tx.model.Line;
import org.drools.tx.model.Location;
import org.drools.tx.model.Transaction;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Row;
import org.kie.api.runtime.rule.ViewChangedEventListener;
import org.xmlpull.v1.XmlPullParserException;

@Singleton
public class LabellingCoreServiceImpl implements LabellingCoreService {

    private KieSession kSession;
    private NotificationService notificationService;
    private static final LabellingCoreService INSTANCE = new LabellingCoreServiceImpl();

    public static LabellingCoreService getInstance() {
        return INSTANCE;
    }

    private LabellingCoreServiceImpl() {
        System.out.println( "New Instance of LabellingCoreServiceImpl : " + this.hashCode() );

    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    @Override
    public void setNotificationService( NotificationService notificationService ) {
        this.notificationService = notificationService;
    }

    public void init() {
        KieServices kServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kServices.getKieClasspathContainer();
        kSession = kieClasspathContainer.getKieBase( "labelKbase" ).newKieSession();
        kSession.setGlobal( "notification", this.notificationService );
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
    }

    public void addLocation(Location loc){
        kSession.insert( loc );
        int fireAllRules = kSession.fireAllRules();
        System.out.println( " Fired Rules Loc: " + fireAllRules );
    }
    
    @Override
    public void label( Transaction tx ) throws IOException, FileNotFoundException, XmlPullParserException {

        kSession.insert( tx );
        for ( Line l : tx.getLines() ) {
            kSession.insert( l );
        }
        int fireAllRules = kSession.fireAllRules();
        System.out.println( " Fired Rules Label: " + fireAllRules );
    }
}
