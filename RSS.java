package com.creeps.hkthn;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RSS {

	final String title;
    final String link;
    final String description;
    final String language;
    final String copyright;
    final String pubDate;

    ArrayList<Item> items = new ArrayList<Item>();
    

    public RSS(String title, String link, String description, String language,
            String copyright, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public List<Item> getMessages() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String toString()
    {
    	ObjectMapper mapper = new ObjectMapper();

    	//Object to JSON in String
    	try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return "Failed";
    }

	
}
