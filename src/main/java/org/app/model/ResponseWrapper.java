package org.app.model;



import java.util.List;

/**
 * Wraps response of
 * csv files
 *
 * Created by lilit on 3/3/18.
 */
public class ResponseWrapper {

    private List<Customer> customers;
    private List<Facility> facilities;

    private ResponseWrapper(Builder builder) {
        this.customers = builder.customers;
        this.facilities = builder.facilities;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public static class Builder{
        private List<Customer> customers;
        private List<Facility> facilities;
        public Builder(){
        }
        public Builder customers(List<Customer> val){
                this.customers=val;
                return this;
        }
        public Builder facilities(List<Facility> val){
            this.facilities=val;
            return this;
        }
        public ResponseWrapper build(){
            return new ResponseWrapper(this);
        }
    }



}
