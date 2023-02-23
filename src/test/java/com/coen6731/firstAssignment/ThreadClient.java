package com.coen6731.firstAssignment;



        import com.coen6731.firstAssignment.controller.AudioController;
        import com.coen6731.firstAssignment.model.Audio;
        import com.fasterxml.jackson.databind.ObjectMapper;

        import org.springframework.http.HttpHeaders;
        import org.springframework.http.MediaType;
        import org.springframework.http.ResponseEntity;
        import org.springframework.mock.web.MockHttpServletRequest;
        import org.springframework.mock.web.MockHttpServletResponse;
        import org.springframework.web.reactive.function.client.WebClient;
        import reactor.core.publisher.Mono;


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


        Audio audio = new Audio("Neha Chaudhary", "Song 1", 1, 2020, 100, 1000);
        WebClient webClient = WebClient.create(URL);
        String responseBody = webClient.post()
                .uri("/")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(audio), Audio.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(responseBody);
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
