package com.cs201.g1t1.spatial;

import com.cs201.g1t1.model.Business;

public class BusinessGeo implements Dimensional {

    private Business business;

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public BusinessGeo(Business business) {
        this.business = business;
    }

    @Override
    public Double[] getCoords() {
        Double[] coords = new Double[2];
        coords[0] = business.getLongitude();
        coords[1] = business.getLatitude();
        return coords;
    }

}
