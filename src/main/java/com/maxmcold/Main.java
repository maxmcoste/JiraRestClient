package com.maxmcold;
import com.maxmcold.logger.Logger;
import de.micromata.jira.rest.JiraRestClient;
import de.micromata.jira.rest.core.domain.IssueBean;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.jql.EField;
import de.micromata.jira.rest.core.jql.JqlConstants;
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
    private static String PASSWORD ="Beatrice13!";
    private static JiraRestClient jiraRestClient;
    public static void main(String[] args){
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

        try {
            for (ProjectBean tmp : getAllProjects()){
                Logger.info(tmp.getKey());


            }
        } catch (RestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
}
