package com.weatherforecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BitbucketRepositorySearch {
    public static void main(String[] args) {
        String repositoryOwner = "your-username";
        String repositoryName = "your-repo";
        String searchWord = "your-search-word";
        String accessToken = "your-access-token";

        String baseUrl = "https://api.bitbucket.org/2.0/repositories";
        String branchesUrl = baseUrl + "/" + repositoryOwner + "/" + repositoryName + "/refs/branches";

        try {
            List<String> branchNames = getBranchNames(branchesUrl, accessToken);
            List<SearchResult> searchResults = new ArrayList<>();

            for (String branchName : branchNames) {
                String filesUrl = baseUrl + "/" + repositoryOwner + "/" + repositoryName + "/src/" + branchName;
                List<String> fileUrls = getFileUrls(filesUrl, accessToken);

                for (String fileUrl : fileUrls) {
                    String fileContent = getFileContent(fileUrl, accessToken);

                    if (fileContent.contains(searchWord)) {
                        SearchResult result = new SearchResult(branchName, fileUrl);
                        searchResults.add(result);
                    }
                }
            }

            // Process search results as needed
            for (SearchResult result : searchResults) {
                System.out.println("Branch: " + result.getBranchName());
                System.out.println("File: " + result.getFileUrl());
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getBranchNames(String url, String accessToken) throws IOException {
        String response = sendGetRequest(url, accessToken);
        // Extract branch names from the response JSON and return as a list
        // ...
    }

    public static List<String> getFileUrls(String url, String accessToken) throws IOException {
        String response = sendGetRequest(url, accessToken);
        // Extract file URLs from the response JSON and return as a list
        // ...
    }

    public static String getFileContent(String url, String accessToken) throws IOException {
        return sendGetRequest(url, accessToken);
    }

    public static String sendGetRequest(String url, String accessToken) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("Request failed. Response Code: " + responseCode);
        }
    }

    static class SearchResult {
        private final String branchName;
        private final String fileUrl;

        public SearchResult(String branchName, String fileUrl) {
            this.branchName = branchName;
            this.fileUrl = fileUrl;
        }

        public String getBranchName() {
            return branchName;
        }

        public String getFileUrl() {
            return fileUrl;
        }
    }
}
