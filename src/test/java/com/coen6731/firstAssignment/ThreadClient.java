package com.coen6731.firstAssignment;



        import com.coen6731.firstAssignment.model.Audio;

        import com.fasterxml.jackson.databind.util.TypeKey;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.MediaType;
        import org.springframework.web.reactive.function.client.WebClient;
        import reactor.core.publisher.Mono;


        import java.io.FileWriter;
        import java.io.IOException;
        import java.time.Duration;
        import java.time.Instant;
        import java.util.HashMap;

        import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadClient implements Runnable{

//    HashMap<Integer, Duration> getTime=new HashMap<Integer, Duration>();//Creating HashMap
//    HashMap<Integer, Duration> postTime=new HashMap<Integer, Duration>();

    private Thread t;

    String name;
    String URL;
    Integer n;

    HashMap<Integer, Duration> getTime;

    HashMap<Integer, Duration> postTime;
    public ThreadClient(){}

    public ThreadClient(String name, String URL, Integer n, HashMap<Integer, Duration> getTime,HashMap<Integer, Duration> postTime) throws IOException {
        this.name = name;
        this.URL = URL;
        this.n = n;
        this.getTime=getTime;
        this.postTime=postTime;

        System.out.println("Creating Thread"+ name);
    }

    @Override
    public void run() {

        Instant start_timePost= Instant.now();
        Audio audio = new Audio("Neha Chaudhary", "Song 1", 1, 2020, 100, 1000,"bad liar");
        WebClient webClient = WebClient.create(URL);
        String responseBody = webClient.post()
                .uri("/")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(audio), Audio.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Client"+name+" post response"+responseBody);


        Instant end_timePost= Instant.now();

        Duration timeTakenPost= Duration.between(start_timePost,end_timePost);


        Instant start_timeGet= Instant.now();
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

        Instant end_timeGet= Instant.now();
        Duration timeTakenGet= Duration.between(start_timeGet,end_timeGet).dividedBy(2);

        getTime.put(Integer.parseInt(name), timeTakenGet);
        postTime.put(Integer.parseInt(name), timeTakenPost);
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
