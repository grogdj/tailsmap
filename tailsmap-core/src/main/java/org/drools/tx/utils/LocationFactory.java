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

package org.drools.tx.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.drools.tx.model.Location;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.xmlpull.v1.XmlPullParserException;

public class LocationFactory {

    public static Location createLocationFromString( String txString ) {

        return new Location();

    }

    public static List<Location> createLocationsFromCSV( String csvPath ) throws FileNotFoundException, IOException, XmlPullParserException {
        List<Location> locations = new ArrayList<>();
        InputStream resourceAsStream = LocationFactory.class.getClassLoader().getResourceAsStream( csvPath );

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader( new InputStreamReader( resourceAsStream ), CsvPreference.STANDARD_PREFERENCE );

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader( true );
            final CellProcessor[] processors = getProcessors();

            Map<String, Object> transactions;
            while ( ( transactions = mapReader.read( header, processors ) ) != null ) {

                String id = ( String ) transactions.get( "api_key" );
                String name = ( String ) transactions.get( "name" );
                String[] latLon = (( String ) transactions.get( "position_maximum" )).split( ",");
                Location loc = new Location();
                loc.setId( id );
                loc.setName( name );
                loc.setLat( Double.parseDouble(latLon[0]) );
                loc.setLon( Double.parseDouble( latLon[1]) );

                locations.add( loc );
            }

        } finally {
            if ( mapReader != null ) {
                mapReader.close();
            }
        }

        return locations;

    }

    private static CellProcessor[] getProcessors() {

        DateTimeFormatter fmt = DateTimeFormat.forPattern( "yyyy-MM-dd HH:mm:ss.SSSSSSZ" );
        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // internal_name
            new NotNull(), // api_key
            new NotNull(), // name
            new NotNull(), // position_max
            new NotNull(), // position_min
        };

        return processors;
    }

}
