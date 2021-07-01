package com.maxmcold.utils;

public class JQLBuilder {
    public JQLBuilder(){

    }

    public String getMonthlyTransitionByStatus(String prjKey, String statusFrom, String statusTo){
        String jql = "Project = "+
                prjKey+
                " and status changed from \""+
                statusFrom+
                "\" to \"" +
                statusTo +
                "\" during ("
                ;
        return jql;
    }
}
