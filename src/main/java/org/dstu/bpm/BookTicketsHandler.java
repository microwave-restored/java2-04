package org.dstu.bpm;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Scanner;

@Component
@ExternalTaskSubscription("book-tickets")
public class BookTicketsHandler implements ExternalTaskHandler {
    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Map<String, Object> vars = externalTask.getAllVariables();
        Object[] moviesList = (Object[]) vars.get("moviesList");
        Scanner scanner = new Scanner(System.in);

        System.out.println("---------------------------------");
        System.out.println("Movies List:");
        System.out.println("---------------------------------");

        for (Object movie : moviesList) {
            if (movie instanceof Map) {
                Map<String, Object> movieData = (Map<String, Object>) movie;
                String title = (String) movieData.get("title");
                Long startAt = (Long) movieData.get("startAt");
                Integer duration = (Integer) movieData.get("duration");
                String hall = (String) movieData.get("hall");

                System.out.println("Movie: " + title);
                System.out.println("Start Time: " + startAt);
                System.out.println("Duration: " + duration);
                System.out.println("Hall: " + hall);
                System.out.println("---------------------------------");
            }
        }

        System.out.print("Enter the title of the movie you want to book: ");
        String selectedMovie = scanner.nextLine().trim();
        String getURL = "http://localhost:8090/movie/book/" + selectedMovie;

        RestTemplate rt = new RestTemplate();

        Map<String, Object> movie = rt.getForObject(getURL, Map.class);
        if (movie != null && !movie.isEmpty()) {
            System.out.println("Movie found. Here is your ticket");
            System.out.println("Title: " + movie.get("title"));
            System.out.println("Start Time: " + movie.get("startAt"));
            System.out.println("Duration: " + movie.get("duration"));
            System.out.println("Hall: " + movie.    get("hall"));
            System.out.println("---------------------------------");
        } else {
            System.out.println("Movie not found.");
        }

        externalTaskService.complete(externalTask);
    }
}
