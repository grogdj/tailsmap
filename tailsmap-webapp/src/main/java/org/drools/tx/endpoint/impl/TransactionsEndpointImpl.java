///*
// * Copyright 2016 Red Hat, Inc. and/or its affiliates.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.drools.tx.endpoint.impl;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.enterprise.context.ApplicationScoped;
//import org.drools.tx.endpoint.api.TransactionsEndpoint;
//import org.drools.tx.model.Transaction;
//import org.kie.api.KieServices;
//import org.kie.api.runtime.KieSession;
//import org.kie.api.runtime.ObjectFilter;
//
//@ApplicationScoped
//public class TransactionsEndpointImpl implements TransactionsEndpoint {
//
//    private KieSession currentSession;
//
//    @PostConstruct
//    public void init() {
//        configure();
//    }
//
//    private void configure() {
//        KieServices ks = KieServices.Factory.get();
//        currentSession = ks.newKieClasspathContainer().getKieBase( "myKbase" ).newKieSession();
//        currentSession.setGlobal( "out", "initial" );
//        currentSession.insert( new Transaction() );
//        currentSession.fireAllRules();
//    }
//
//    public void reset() {
//        configure();
//    }
//
//    @Override
//    public void processTx( Transaction tx ) {
//        currentSession.insert( tx );
//        int fireAllRules = currentSession.fireAllRules();
//        System.out.println( "Rules Fired: " + fireAllRules );
//    }
//
//    @Override
//    public List<Transaction> getTransactions() {
//        Collection<? extends Object> objects = currentSession.getObjects( new ObjectFilter() {
//            @Override
//            public boolean accept( Object o ) {
//                if ( o instanceof Transaction ) {
//                    return true;
//                }
//                return false;
//            }
//        } );
//        return new ArrayList<Transaction>( ( Collection<? extends Transaction> ) objects);
//    }
//
//}
