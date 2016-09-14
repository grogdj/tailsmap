/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drools.tx.tests;

import java.util.List;
import org.drools.tx.core.api.LabellingCoreService;
import org.drools.tx.core.impl.BaseNotificationServiceImpl;
import org.drools.tx.core.impl.LabellingCoreServiceImpl;
import org.drools.tx.model.Transaction;
import org.drools.tx.utils.TransactionFactory;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class CoreServiceTest {

    @Test
    public void realDataTest() throws Exception {
        LabellingCoreService labelService = LabellingCoreServiceImpl.getInstance();

        labelService.setNotificationService( new BaseNotificationServiceImpl() );
        labelService.init();

        List<Transaction> txs = TransactionFactory.createTxsFromCSV( "data/data-mini.csv" );
        System.out.println( "Total TXS: " + txs.size() );

        for ( Transaction tx : txs ) {
            labelService.label( tx );
        }

    }

}
