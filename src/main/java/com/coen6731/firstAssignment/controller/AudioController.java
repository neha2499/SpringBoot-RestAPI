package com.coen6731.firstAssignment.controller;

import com.coen6731.firstAssignment.model.Audio;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/audios")
@Api(value = "Audio API", tags = { "Audio" })
public class AudioController {

    ConcurrentHashMap<String, Audio> audioDB = new ConcurrentHashMap<>();

    public AudioController() {

        Audio audio_1 = new Audio("Tyler Swift", "Song 1", 1, 2020, 100, 1000);
        Audio audio_2 = new Audio("Rema", "Song 2", 2, 2021, 200, 2000);
        Audio audio_3 = new Audio("Ed Sheeran", "Song 3", 3, 2022, 300, 3000);

        audioDB.put("id_1", audio_1);
        audioDB.put("id_2", audio_2);
        audioDB.put("id_3", audio_3);

    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get a single audio item by id")
    public Audio getAudio(
            @ApiParam(value = "The Id of the audio item you want to retrieve", required = true)
            @PathVariable String id) {
        return audioDB.get(id);
    }

    @GetMapping("/{artistName}/propertyName")
    @ApiOperation(value = "Get a single property of the selected Audio item along with all audio items")
    public ResponseEntity<Map<String, Object>> getAudioProperty(
            @ApiParam(value = "The artist name of the Audio item to retrieve", required = true)
            @PathVariable String artistName,
            @ApiParam(value = "The name of the property to retrieve", required = true)
            @RequestParam String key) {


        String value = null;


        Audio audioSelected = null;

        //only return the first Audio object that it encounters with the matching artist name.
        for (Audio audio : audioDB.values()) {

            if (audio.getArtist_name().equals(artistName)) {
                audioSelected = audio;
                break;
            }
        }

        if (audioSelected == null) {
            return ResponseEntity.notFound().build();


        } else {
//


            if (Objects.equals(key, "artist name")) {
                value = audioSelected.getArtist_name();
            } else if (Objects.equals(key, "track title")) {
                value = audioSelected.getTrack_title();
            } else if (Objects.equals(key, "track number")) {
                value = String.valueOf(audioSelected.getTrack_number());
            } else if (Objects.equals(key, "year")) {
                value = String.valueOf(audioSelected.getYear());
            } else if (Objects.equals(key, "reviews count")) {
                value = String.valueOf(audioSelected.getReviews_count());
            } else {
                value = String.valueOf(audioSelected.getCopies_sold());
            }

            int totalCopiesSold = 0;

            for (Audio audio : audioDB.values()) {
                totalCopiesSold = totalCopiesSold + audio.getCopies_sold();
            }

            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(audioDB);
            Map<String, Object> responseMap = new ConcurrentHashMap<>();
            responseMap.put("singleElementResponse", value);
            responseMap.put("audioDB", audioDB);
            responseMap.put("totalCopiesSold", totalCopiesSold);

            // Return the Map object as a JSON response.
            return ResponseEntity.ok(responseMap);

        }


    }

    @PostMapping("/")
    @ApiOperation(value = "Create a new Audio item")
    public ResponseEntity<Map<String, Object>> doPost(
            @ApiParam(value = "The new Audio item to create", required = true)
            @RequestBody Audio newAudio){

        if (newAudio== null) {
            return ResponseEntity.notFound().build();

    }
        String size = String.valueOf(audioDB.size()+1);
        String id = "id_" + size;
        audioDB.put(id, newAudio);
        int totalCopiesSold= 0;

        for (Audio audioFor : audioDB.values()){
            totalCopiesSold=totalCopiesSold+audioFor.getCopies_sold();
        }
        Map<String, Object> responseMap = new ConcurrentHashMap<>();
        responseMap.put("postedElement", newAudio);
        responseMap.put("audioDB", audioDB);
        responseMap.put("totalCopiesSold", totalCopiesSold);

        // Return the Map object as a JSON response.
        return ResponseEntity.ok(responseMap);


}

    public void init() {
    }
}