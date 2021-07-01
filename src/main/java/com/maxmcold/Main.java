package com.maxmcold;
import com.maxmcold.logger.Logger;
import com.maxmcold.utils.Time;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.core.domain.IssueBean;
import de.micromata.jira.rest.core.domain.JqlSearchResult;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.jql.*;
import de.micromata.jira.rest.core.misc.RestPathConstants;
import de.micromata.jira.rest.core.util.RestException;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static String URL_TO_JIRA_SERVER ="https://poste-digital.atlassian.net/";
    private static String USERNAME ="massimo.delvecchio@posteitaliane.it";
    private static String PASSWORD ="VEaBqNSp90frVLwz9VZJ9F24";
    private static JiraRestClient jiraRestClient;
    public static void main(String[] args){

        Time time = new Time();

        time.getMonthEnd(1);
        Logger.info("From: "+ time.getCurrentMonthStart()+" to "+ time.getCurrentMonthEnd()+"\n");
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        //ProxyHost proxy = new ProxyHost("proxy", 3128);
        URI uri = null;
        try {
            uri = new URI(URL_TO_JIRA_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        jiraRestClient = new JiraRestClient(executorService);
        try {
            jiraRestClient.connect(uri, USERNAME, PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
        try {
            for (ProjectBean tmp : getAllProjects()){
                Logger.info(tmp.getKey()+"\n");


            }
        } catch (RestException e) {
            Logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            Logger.error(e.getLocalizedMessage());
        } catch (ExecutionException e) {
            Logger.error(e.getLocalizedMessage());
        } catch (InterruptedException e) {
            Logger.error(e.getLocalizedMessage());
        }
        */
        String jql = "project = \"ON\" and issuetype=Story ORDER BY created DESC";
        JqlSearchResult jsb = searchByJQL(jql);
       List<IssueBean> issues = jsb.getIssues();

       Logger.info("Total issues: " + jsb.getTotal() +"\n");

       for (IssueBean ib : issues) {
           Logger.info(ib.getFields().getSummary()+"\n");

       }




    }

    public static void getIssueKeyWithFields() throws RestException, IOException, ExecutionException, InterruptedException {
        List<String> field = new ArrayList<>();
        field.add(EField.SUMMARY.getField());
        field.add(EField.DESCRIPTION.getField());
        List<String> expand = new ArrayList<>();
        expand.add(EField.RENDEREDFIELDS.getField());
        expand.add(EField.TRANSITIONS.getField());
        expand.add(EField.CHANGELOG.getField());
        final Future<IssueBean> future = jiraRestClient.getIssueClient().getIssueByKey("ISSUEKEY_TO_SEARCH", field, expand);
        IssueBean issue = future.get();

    }
    public static List<ProjectBean> getAllProjects() throws RestException, IOException, ExecutionException, InterruptedException {
        final Future<List<ProjectBean>> future = jiraRestClient.getProjectClient().getAllProjects();
        final List<ProjectBean> projectBeans = future.get();
        return projectBeans;

    }

    public static JqlSearchResult searchByJQL(String jql){


        JqlSearchBean jsb = new JqlSearchBean();
        jsb.setJql(jql);
        jsb.addField(EField.ISSUE_KEY, EField.STATUS, EField.DUE, EField.SUMMARY, EField.ISSUE_TYPE, EField.PRIORITY, EField.UPDATED, EField.TRANSITIONS);
        jsb.addExpand(EField.TRANSITIONS);
        jsb.addExpand(EField.DESCRIPTION);

        Future<JqlSearchResult> future = jiraRestClient.getSearchClient().searchIssues(jsb);
        JqlSearchResult jqlSearchResult = null;
        try {
            jqlSearchResult = future.get();
        } catch (InterruptedException e) {
            Logger.error(e.getLocalizedMessage());
        } catch (ExecutionException e) {
            Logger.error(e.getLocalizedMessage());
        }
        return jqlSearchResult;
    }
}
