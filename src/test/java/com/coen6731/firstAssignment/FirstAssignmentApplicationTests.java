package com.coen6731.firstAssignment;


import com.coen6731.firstAssignment.controller.AudioController;
import com.coen6731.firstAssignment.model.Audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@SpringBootTest
class FirstAssignmentApplicationTests extends ThreadClient  {




	@Test
	void contextLoads() {
	}




	@Test
	void testAudiopost() throws Exception {
		Audio audio = new Audio("Neha Chaudhary", "Song 1", 1, 2020, 100, 1000,"bad liar");
		WebClient webClient = WebClient.create("http://155.248.226.228:8080");
		String responseBody = webClient.post()
				.uri("/audios/")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(audio), Audio.class)
				.retrieve()
				.bodyToMono(String.class)
				.block();

		System.out.println(responseBody);
	}


	@Test
	void testAudioGet() throws Exception{

		String URL = "http://localhost:8080/audios";
		String ARTIST_NAME = "Tyler Swift";
		String PROPERTY_NAME = "track number";

		WebClient client = WebClient.create();
		String urlWithParams = URL + "/" + ARTIST_NAME + "/propertyName?key=" + PROPERTY_NAME;
		String responseContent = client.get()
				.uri(urlWithParams)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		System.out.println(responseContent);
	}


	@Test
	void testAudio() throws Exception{
		String URL =  "http://localhost:8080/audios";

		HashMap<Integer, Duration> getTime=new HashMap<Integer, Duration>();//Creating HashMap
		HashMap<Integer, Duration> postTime=new HashMap<Integer, Duration>();
		int no_of_clients=10;
		int no_of_gets=2;
		Instant start_time= Instant.now();
		List<ThreadClient> clients= new ArrayList<>();
		for (int i=0; i<no_of_clients; i++){

			ThreadClient R1= new ThreadClient(Integer.toString(i), URL, no_of_gets, getTime, postTime);
			R1.start();
			clients.add(R1);


		}

		for(int i=0; i<no_of_clients; i++){
			try{
				clients.get(i).join();
			} catch (InterruptedException e){
				System.out.println("Thread Interrupted");
			}

		}
		Instant end_time= Instant.now();

		Duration timeTaken= Duration.between(start_time,end_time);
		ThreadClient th = new ThreadClient();
		System.out.println("Time Taken"+timeTaken);


		// add the following code to write getTime to CSV
		String getTimeFilename = "getTime_" + no_of_clients + ".csv";
		writeHashMapToCSV(getTimeFilename, getTime);

		// add the following code to write postTime to CSV
		String postTimeFilename = "postTime_" + no_of_clients + ".csv";
		writeHashMapToCSV(postTimeFilename, postTime);






	}

	public void writeHashMapToCSV(String filename, HashMap<Integer, Duration> hashMap) {
		// specify the file name and path to save the CSV file



		// create a FileWriter object to write to the CSV file
		try (FileWriter writer = new FileWriter(filename)) {

			// write the header row
			writer.append("Client name,time\n");

			// iterate through the HashMap and write each row to the CSV file
			for (Integer key : hashMap.keySet()) {
				Duration value = hashMap.get(key);
				writer.append(key + "," + value + "\n");
			}

			System.out.println("HashMap converted to CSV successfully!");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}







