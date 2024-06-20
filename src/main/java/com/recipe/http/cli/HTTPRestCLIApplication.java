package com.recipe.http.cli;

import com.recipe.http.client.RESTClient;

public class HTTPRestCLIApplication {

    private RESTClient restClient;



    public RESTClient getRestClient() {
        if (restClient == null) {
            restClient = new RESTClient();
        }

        return restClient;
    }

    public void setRestClient(RESTClient restClient) {
        this.restClient = restClient;
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }

        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();

        String serverURL = args[0];
        String CRUDMethod = args[1];

        if (serverURL != null && !serverURL.isEmpty()) {

            RESTClient restClient = new RESTClient();
            restClient.setServerURL(serverURL);

            cliApp.setRestClient(restClient);

            if (serverURL.matches(".*\\/recipe\\/[A-Za-z0-9]+") && CRUDMethod.equalsIgnoreCase("GET")) {
                cliApp.getRestClient().getGETResponseFromHTTPRequest("recipe/{recipeName}");
            } else if(serverURL.matches(".*\\/recipe\\/[A-Za-z0-9]+") && CRUDMethod.equalsIgnoreCase("DELETE")) {
                cliApp.getRestClient().getDELETEResponseFromHTTPRequest("recipe/{recipeName}");
            } else if (serverURL.contains("recipe")) {
                cliApp.getRestClient().getGETResponseFromHTTPRequest("recipe");
            } else if (serverURL.contains("recipes")) {
                cliApp.getRestClient().getGETResponseFromHTTPRequest("recipes");
            } else {
                System.out.println("other URL");
            }
        }

    }
}
