package com.creeps.hkthn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;
import com.textrazor.annotations.ScoredCategory;
import com.textrazor.annotations.Sentence;
import com.textrazor.annotations.Topic;
import com.textrazor.annotations.Word;

public class RelevanceTester {
	
	private ArrayList<Item> items;
	private ArrayList<String> permittedCategories;
	
	private ArrayList<EntityLocation> entityLocations;
	
	RelevanceTester(ArrayList<Item> items)
	{
		this.items = items;
		permittedCategories=new ArrayList<String>();
		
		permittedCategories.add("03015001");
		permittedCategories.add("03007000");
		permittedCategories.add("03000000");
		permittedCategories.add("03006001");
		permittedCategories.add("03004000");
		
		entityLocations = new ArrayList<EntityLocation>();

		
	}
	
	ArrayList<EntityLocation> relevantItems()
	{
		
		TextRazor client = new TextRazor("d0f9e08b81b50646d78f3ee7c202bf27f627c264d0069f964e61d7ab");

		client.setClassifiers(Arrays.asList("textrazor_newscodes"));//Classify the string using the newscodes
		client.addExtractor("entities");
		client.setEnrichmentQueries(Arrays.asList("fbase:/location/location/geolocation>/location/geocode/latitude", "fbase:/location/location/geolocation>/location/geocode/longitude"));//
	
		AnalyzedText response1=null;
		
		int size = items.size();

		for(int i=0;i<size;i++)
		{
			int j=0;
			try {
				
				response1 = client.analyze(items.get(i).getTitle());//Paragraph to be analyzed

				synchronized(response1) {
			
				for (ScoredCategory category : response1.getResponse().getCategories()) {
				
					if(category!=null) {
					if(permittedCategories.toString().toLowerCase().contains(category.getCategoryId())){
						System.out.println("Accepted"+category.getLabel()+" "+category.getCategoryId());
						j++;
						continue;
					}
					else {

						System.out.println("Rejected"+category.getLabel()+" "+category.getCategoryId());
						items.remove(j);

					}
					
					}
				}
			}
				}
			catch(Exception e) {}
			
			
		}
	
			try {
				for(int g=0;g<items.size();g++) {
					response1 = client.analyze(items.get(g).getTitle());
			
					if (response1.getResponse().getEntities() != null) {
						// TODO Check why textrazor returns the same entity multiple times in some cases
						for (Entity entity : response1.getResponse().getEntities()) {
							
							Map<String, List<String>>entityData=entity.getData();
							List<String> latitudeValues = entityData.get("fbase:/location/location/geolocation>/location/geocode/latitude");
							List<String> longitudeValues = entityData.get("fbase:/location/location/geolocation>/location/geocode/longitude");
						
							if ((null != latitudeValues && null != longitudeValues)) {
								System.out.println(entity.getEntityEnglishId());
								System.out.print(latitudeValues.toString()+longitudeValues.toString());
								entityLocations.add(new EntityLocation(g,entity.getEntityEnglishId(),latitudeValues.get(0),longitudeValues.get(0)));
							
							}
						}
					}
				}
				
			}
			catch(Exception e) 
			{
							e.printStackTrace();
			}
			
			return entityLocations;
		
	}}
