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

import java.util.HashSet;
import java.util.Set;

public class Line {

    private String code;
    private String itemDescription;
    private Double quantity;
    private Double totalToPay;
    private Set<String> labels = new HashSet<>();

    public Line( String code, String itemDescription, Double quantity, Double totalToPay ) {
        this.code = code;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.totalToPay = totalToPay;
    }

    public String getCode() {
        return code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription( String itemDescription ) {
        this.itemDescription = itemDescription;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity( Double quantity ) {
        this.quantity = quantity;
    }

    public Double getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay( Double totalToPay ) {
        this.totalToPay = totalToPay;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels( Set<String> labels ) {
        this.labels = labels;
    }

    public void addLabel( String label ) {
        if ( this.labels == null ) {
            this.labels = new HashSet<>();
        }
        this.labels.add( label );
    }

    @Override
    public String toString() {
        return "Line{" + "code=" + code + ", itemDescription=" + itemDescription + ", quantity=" + quantity + ", totalToPay=" + totalToPay + ", labels=" + labels + '}';
    }

}
