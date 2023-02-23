package com.coen6731.firstAssignment;



        import com.coen6731.firstAssignment.controller.AudioController;
        import com.coen6731.firstAssignment.model.Audio;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import jakarta.servlet.ServletException;
        import org.springframework.http.ResponseEntity;
        import org.springframework.mock.web.MockHttpServletRequest;
        import org.springframework.mock.web.MockHttpServletResponse;
        import org.springframework.web.reactive.function.client.WebClient;


        import java.io.IOException;
        import java.util.Map;

        import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadClient implements Runnable{


    private Thread t;

    String name;
    String URL;
    Integer n;

    public ThreadClient(String name, String URL, Integer n) {
        this.name = name;
        this.URL = URL;
        this.n = n;

        System.out.println("Creating Thread"+ name);
    }

    @Override
    public void run() {

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

        System.out.println("Client"+name+"Post Response: "+responseBody);


        for(int j=0;j<n;j++){


		String ARTIST_NAME = "Tyler Swift";
		String PROPERTY_NAME = "track number";

		WebClient client = WebClient.create();
		String urlWithParams = URL + "/" + ARTIST_NAME + "/propertyName?key=" + PROPERTY_NAME;
		String responseContent = client.get()
				.uri(urlWithParams)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		System.out.println("Client"+name+"Get response"+responseContent);
        }

        System.out.println("Thread"+ name+"exiting");

    }

    public void start() {


        System.out.println("Starting Thread"+name);
        if (t == null) {
            t = new Thread (this, name);
            t.start ();
        }
    }

    public void join() throws InterruptedException {
        t.join();
    }
}
