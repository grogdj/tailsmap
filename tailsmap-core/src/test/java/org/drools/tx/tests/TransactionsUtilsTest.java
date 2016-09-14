/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drools.tx.tests;

import java.util.List;
import org.drools.tx.model.Transaction;
import org.drools.tx.utils.TransactionFactory;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class TransactionsUtilsTest {

    @Test
    public void hello() throws Exception {
        List<Transaction> txs = TransactionFactory.createTxsFromCSV( "data/data-mini.csv" );

        System.out.println( "Size: " + txs.size() );

        for ( Transaction tx : txs ) {
            System.out.println( "TX: " + tx );

        }
    }
}
