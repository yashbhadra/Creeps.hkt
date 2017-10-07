package com.creeps.hkthn;

public class TopicRequest {

	Item item;
	String groupName;
	public TopicRequest(Item item, String groupName) {
		super();
		this.item = item;
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		return "{\"to\": \"/topics/"+groupName+"\",\"data\":{\"message\":\""+item.getTitle()+"\"}}";
	}
	
	
	
}
