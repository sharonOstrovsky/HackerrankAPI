package com.example.Hackerrank;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class CalculateGoalService {

    public static int getWinnerTotalGoals(String competition, int year) {

        String competitionNameChanged = getChangedString(competition);
        String winner = getWinnerTeam(competitionNameChanged, year);
        int cantPages = getCantPages(winner, competitionNameChanged, year);

        int goalsTeam1 = getTotalGoals(winner, competitionNameChanged, year, cantPages, "team1");
        int goalsTeam2 = getTotalGoals(winner, competitionNameChanged, year, cantPages, "team2");

        System.out.println();
        System.out.println("GOLESS TEAM 1:" + goalsTeam1);
        System.out.println("GOLESS TEAM 2:" + goalsTeam2);
        System.out.println();

        return goalsTeam1 + goalsTeam2;
    }


    public static String getWinnerTeam(String competitionName, int competitionYear) {

        String winner = "";
        HttpClient httpClient = HttpClients.createDefault();


        String url = "https://jsonmock.hackerrank.com/api/football_competitions?name=" + competitionName + "&year=" + competitionYear;

        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {

                String jsonResponse = EntityUtils.toString(response.getEntity());

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();

                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                for (JsonElement element : dataArray) {
                    JsonObject dataObject = element.getAsJsonObject();

                    String name = dataObject.get("name").getAsString();
                    String country = dataObject.get("country").getAsString();
                    int year = dataObject.get("year").getAsInt();
                    winner = dataObject.get("winner").getAsString();
                    String runnerup = dataObject.get("runnerup").getAsString();

                    System.out.println("Name: " + name);
                    System.out.println("Country: " + country);
                    System.out.println("Year: " + year);
                    System.out.println("Winner: " + winner);
                    System.out.println("Runner-Up: " + runnerup);
                }


            } else {
                System.err.println("[getWinnerTeam] Error, api response status is: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return winner;
    }

    public static int getCantPages(String winner, String competitionName, int competitionYear) {

        int totalPages = 0;

        HttpClient httpClient = HttpClients.createDefault();
        String url = "https://jsonmock.hackerrank.com/api/football_matches?competition=" + competitionName + "&year=" + competitionYear + "&team1=" + winner + "&page=1";

        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);


            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String jsonResponse = EntityUtils.toString(response.getEntity());

                JsonParser parser = new JsonParser();
                JsonObject jsonObjectTeam1 = parser.parse(jsonResponse).getAsJsonObject();

                totalPages = jsonObjectTeam1.get("total_pages").getAsInt();
                System.out.println("Total Pages: " + totalPages);


            } else {
                System.err.println("[getCantPages] Error, api response status is: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPages;

    }

    public static int getTotalGoals(String winner, String competitionName, int competitionYear, int cantPages, String team) {
        HttpClient httpClient = HttpClients.createDefault();
        int totalGoals = 0;

        for (int i = 1; i <= cantPages; i++) {


            String url = "https://jsonmock.hackerrank.com/api/football_matches?competition=" + competitionName + "&year=" + competitionYear + "&" + team + "=" + winner + "&page=" + i;

            HttpGet httpGet = new HttpGet(url);

            System.out.println("-----" + team.toUpperCase() + "------");
            System.out.println("PAGE: " + i);
            System.out.println();

            try {
                HttpResponse response = httpClient.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());

                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();


                    JsonArray dataArray = jsonObject.getAsJsonArray("data");


                    for (JsonElement element : dataArray) {
                        JsonObject matchObject = element.getAsJsonObject();

                        String competition = matchObject.get("competition").getAsString();
                        int year = matchObject.get("year").getAsInt();
                        String round = matchObject.get("round").getAsString();
                        String team1 = matchObject.get("team1").getAsString();
                        String team2 = matchObject.get("team2").getAsString();
                        int team1goals = matchObject.get("team1goals").getAsInt();
                        int team2goals = matchObject.get("team2goals").getAsInt();

                        if(team.equals("team1")){
                            totalGoals = totalGoals + team1goals;
                        }else{
                            totalGoals = totalGoals + team2goals;
                        }


                        System.out.println("Competition: " + competition);
                        System.out.println("Year: " + year);
                        System.out.println("Round: " + round);
                        System.out.println("Team 1: " + team1);
                        System.out.println("Team 2: " + team2);
                        System.out.println("Team 1 Goals: " + team1goals);
                        System.out.println("Team 2 Goals: " + team2goals);
                        System.out.println();
                    }
                } else {
                    System.err.println("[getCompetitionTotalGoals] Error, api response status is: " + statusCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return totalGoals;
    }


    public static String getChangedString(String competition) {
        return competition.replace(" ", "%20");

    }
}
