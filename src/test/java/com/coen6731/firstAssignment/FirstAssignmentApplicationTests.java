package com.coen6731.firstAssignment;


import com.coen6731.firstAssignment.controller.AudioController;
import com.coen6731.firstAssignment.model.Audio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FirstAssignmentApplicationTests {



	@Test
	void contextLoads() {
	}

	@Test
	void testAudioPost() throws Exception{
	WebClient webClient = WebClient.create("http://localhost:8080");
		Audio newAudio= new Audio("eminem", "slim shady", 1, 2020, 100, 1000);
		AudioController audioController=new AudioController();
		ResponseEntity<Map<String, Object>> data=audioController.doPost(newAudio);
		Map<String, Object> responseBody = data.getBody();
		Map<String, Object> audioDB = (Map<String, Object>) responseBody.get("audioDB");
		Audio postedElement = new ObjectMapper().convertValue(responseBody.get("postedElement"), Audio.class);

		assertEquals("slim shady", postedElement.getTrack_title());
		assertEquals(1, postedElement.getTrack_number());
		assertEquals(2020, postedElement.getYear());
		assertEquals(100, postedElement.getReviews_count());
		assertEquals(1000, postedElement.getCopies_sold());

		System.out.println(responseBody);


	}
//
//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	@Test
//	public void testGetAudioProperty() throws Exception {
//
//		String artistName = "Tyler Swift";
//		String key = "artist name";
//
//		ResponseEntity<Map> response = restTemplate.getForEntity("/audios/{artistName}/propertyName?key={key}",
//				Map.class, artistName, key);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//
//		Map<String, Object> responseBody = response.getBody();
//
//		assertNotNull(responseBody);
//		assertEquals(artistName, responseBody.get("singleElementResponse"));
//
//		Map<String, Audio> audioDB = (Map<String, Audio>) responseBody.get("audioDB");
//		assertNotNull(audioDB);
//
//		int totalCopiesSold = (int) responseBody.get("totalCopiesSold");
//		assertEquals(6000, totalCopiesSold);
//		System.out.println(responseBody);
//	}


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
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter number of client you want to create: ");
//		int no_of_clients= sc.nextInt();
//		System.out.println();
//		System.out.println("Enter number of GET request you want to send for each client : \n 1) 2 \n 2) 5 \n 3) 10");
//        int no_of_gets= sc.nextInt();
//        sc.close();
		int no_of_clients=10;
		int no_of_gets=2;
		Instant start_time= Instant.now();
		List<ThreadClient> clients= new ArrayList<>();
		for (int i=0; i<no_of_clients; i++){

			ThreadClient R1= new ThreadClient(Integer.toString(i), URL, no_of_gets);
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

		System.out.println("Time Taken"+timeTaken);

	}

}







