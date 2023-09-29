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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackerrankApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackerrankApplication.class, args);

		String competition = "English Premier League";
		int year = 2014;

		//String competition = "UEFA Champions League";
		//int year = 2011;

		System.out.println("RTA FINAL: " + CalculateGoalService.getWinnerTotalGoals(competition, year));

	}



}
