package com.nwt.juber.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
public class TimeEstimator {

	public int estimateTime(Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude) {
		// http://router.project-osrm.org/route/v1/driving/13.388860,52.517037;13.397634,52.529407?overview=false
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://router.project-osrm.org/route/v1/driving/" + startLongitude + "," + startLatitude + ";" + endLongitude + "," + endLatitude;
		String res = restTemplate.getForObject(url, String.class);
		try {
			JsonElement jelement = new JsonParser().parse(res);
			System.out.println(jelement);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray routes = jobject.getAsJsonArray("routes");
			Double duration = routes.get(0).getAsJsonObject().get("duration").getAsDouble();
			System.out.println(duration + " " + duration / 60);
			return duration.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 30 * 60; // 30 minutes maximum for Novi Sad

		}

	}
}
