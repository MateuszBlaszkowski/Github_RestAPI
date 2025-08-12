package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    static HttpResponse<String> getJson(String url) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }


    public static void main(String[] args) throws IOException, InterruptedException {

            System.out.println("Type username:");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.next();

            JSONArray array = new JSONArray(getJson("https://api.github.com/users/"+username+"/repos").body());

            for(int i =0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                if(object.get("fork").toString().equals("false")){
                   String name = object.getString("name");
                   JSONObject owner = object.getJSONObject("owner");
                   String owner_login = owner.getString("login");
                   JSONArray branches = new JSONArray(getJson("https://api.github.com/repos/"+owner_login+"/"+name+"/branches").body());
                   System.out.println("Repository name: " + name + "\nOwner Login: " + owner_login + "\nBranches:");

                   for(int j = 0; j<branches.length(); j++)
                   {
                        JSONObject branch = branches.getJSONObject(j);
                        String branchName = branch.getString("name");
                        JSONObject commit = branch.getJSONObject("commit");
                        String sha = commit.getString("sha");
                        System.out.println((j+1)+". name: "+branchName + ", sha: " + sha);
                   }
                   System.out.println("---------------");
               }
            }

    }
}