/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.drools.tx.tests;

import java.util.List;
import org.drools.tx.model.Location;
import org.drools.tx.utils.LocationFactory;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class LocationUtilsTest {

    @Test
    public void hello() throws Exception {
        List<Location> locations = LocationFactory.createLocationsFromCSV( "data/ret-loc.csv" );

        System.out.println( "Size: " + locations.size() );

        for ( Location loc : locations ) {
            System.out.println( "Loc: " + loc );

        }
    }
}
