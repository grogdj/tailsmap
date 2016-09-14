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

package org.drools.tx.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transaction {

    private Long id;
    private String clientId;
    private String retailerId;
    private Date txDate;
    private String currency;
    private Double basketTotal;
    private List<Line> lines;

    public Transaction() {
    }

    public Transaction( String clientId, String retailerId, Date txDate, String currency, Double basketTotal, List<Line> lines ) {
        this.clientId = clientId;
        this.retailerId = retailerId;
        this.txDate = txDate;
        this.currency = currency;
        this.basketTotal = basketTotal;
        this.lines = lines;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId( String clientId ) {
        this.clientId = clientId;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId( String retailerId ) {
        this.retailerId = retailerId;
    }

    public Date getTxDate() {
        return txDate;
    }

    public void setTxDate( Date txDate ) {
        this.txDate = txDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency( String currency ) {
        this.currency = currency;
    }

    public Double getBasketTotal() {
        return basketTotal;
    }

    public void setBasketTotal( Double basketTotal ) {
        this.basketTotal = basketTotal;
    }

    public void setLines( List<Line> lines ) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Set<String> getLabels() {
        Set<String> labels = new HashSet<>();
        for ( Line l : this.lines ) {
            labels.addAll( l.getLabels() );
        }
        return labels;
    }

    public String getFormatedLabels() {
        String formatedLabels = "[";
        Set<String> labels = getLabels();
        for ( String l : labels ) {
            formatedLabels += "\"" + l + "\",";
        }
        formatedLabels = formatedLabels.substring( 0, formatedLabels.length()-1 );
        formatedLabels += "]";
        return formatedLabels;
    }

    @Override
    public String toString() {
        return "Transaction{" + "clientId=" + clientId + ", tillTransactionId=" + retailerId + ", tillTimestamp=" + txDate + ", currency=" + currency + ", basketTotal=" + basketTotal + ", lines=" + lines + '}';
    }

}
