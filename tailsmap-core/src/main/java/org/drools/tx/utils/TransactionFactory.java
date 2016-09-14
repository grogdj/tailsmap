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

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.drools.tx.model.Line;
import org.drools.tx.model.Transaction;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.joda.ParseDateTime;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.xmlpull.v1.XmlPullParserException;

public class TransactionFactory {
    
    public static Transaction createTxFromString( String txString ) {
        
        return new Transaction();
        
    }
    
    public static List<Transaction> createTxsFromCSV( String csvPath ) throws FileNotFoundException, IOException, XmlPullParserException {
        List<Transaction> txs = new ArrayList<>();
        InputStream resourceAsStream = TransactionFactory.class.getClassLoader().getResourceAsStream( csvPath );
        
        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader( new InputStreamReader( resourceAsStream ), CsvPreference.STANDARD_PREFERENCE );

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader( true );
            final CellProcessor[] processors = getProcessors();
            
            Map<String, Object> transactions;
            while ( ( transactions = mapReader.read( header, processors ) ) != null ) {
                
                String request = ( String ) transactions.get( "Request" );
                Transaction tx = new Transaction();
                XML xml = new XMLDocument( request );
                String clientId = xml.xpath( "//clientId/text()" ).get( 0 );
                String apiKey = xml.xpath( "//apiKey/text()" ).get( 0 );
                List<String> values = xml.xpath( "//detail/value/text()" );
                String value = "0.0";
                if(values != null && values.size() > 0){
                    value = values.get( 0 );
                }
                List<String> lineNos = xml.xpath( "//detail/basket/item/line_no/text()" );
                List<String> productCodes = xml.xpath( "//detail/basket/item/product_code/text()" );
                List<String> descriptions = xml.xpath( "//detail/basket/item/description/text()" );
                List<String> quantities = xml.xpath( "//detail/basket/item/quantity/text()" );
                List<String> totalToPays = xml.xpath( "//detail/basket/item/total_to_pay/text()" );
                List<Line> lines = new ArrayList<>();
                for ( int i = 0; i < lineNos.size(); i++ ) {
                    Line line = new Line( productCodes.get( i ), descriptions.get( i ),
                            Double.parseDouble( quantities.get( i ) ), Double.parseDouble( totalToPays.get( i ) ) );
                    lines.add( line );
                }
                tx.setBasketTotal( Double.parseDouble( value ) );
                tx.setLines( lines );
                tx.setRetailerId( apiKey );
                tx.setClientId( clientId );
                Long txId = ( Long ) transactions.get( "Id" );
                DateTime txDate = ( DateTime ) transactions.get( "Date" );
                tx.setTxDate( txDate.toDate() );
                tx.setId( txId );
                txs.add( tx );
            }
            
        } finally {
            if ( mapReader != null ) {
                mapReader.close();
            }
        }
        
        return txs;
        
    }
    
    private static CellProcessor[] getProcessors() {
        
        DateTimeFormatter fmt = DateTimeFormat.forPattern( "yyyy-MM-dd HH:mm:ss.SSSSSSZ" );
        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // request
            new ParseLong(), // id
            new ParseDateTime( fmt ) //date "2016-03-03 00:00:06.054883+00" -> 
        };
        
        return processors;
    }
    
}
