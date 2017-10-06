package com.creeps.hkthn;

import java.util.ArrayList;
import java.util.Arrays;

import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.ScoredCategory;

public class RelevanceTester {
	
	private ArrayList<Item> items;
	
	RelevanceTester(ArrayList<Item> items)
	{
		this.items = items;
	}
	
	ArrayList<Item> relevantItems()
	{
		
		TextRazor client = new TextRazor("ab1e2fcc7a9381f1711217c6aca4b225ab44482d567171870bc47bf4");

		client.setClassifiers(Arrays.asList("textrazor_newscodes"));//Classify the string using the newscodes
		client.addExtractor("entities");
		client.setEnrichmentQueries(Arrays.asList("fbase:/location/location/geolocation>/location/geocode/latitude", "fbase:/location/location/geolocation>/location/geocode/longitude"));//
		
		AnalyzedText response1;
		
		for(int i=0;i<items.size();i++)
		{
			try {
				response1 = client.analyze(items.get(i).getDescription());//Paragraph to be analyzed
			
			for (ScoredCategory category : response1.getResponse().getCategories()) {
				
				if(permittedCategories.contains())
				{
					
				}
				
			}
			catch(Exception e) {
				
			}
		
		
		return items;
	}
	

}
