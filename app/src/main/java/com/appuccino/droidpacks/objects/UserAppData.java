package com.appuccino.droidpacks.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class UserAppData {

    public int[] boughtAppIDs;
    public UserPackData[] boughtAppPacks;

    //@JsonProperty("example") is used for Jackson for JSON
    public UserAppData(
            @JsonProperty("boughtAppIDs") int[] list,
            @JsonProperty("boughtAppPacks") UserPackData[] packData){
        boughtAppIDs = list;
        boughtAppPacks = packData;
    }

    public static UserAppData appDataFromJSON(String json){
        if (json == null || json.isEmpty()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        UserAppData myObject;
        try {
            myObject = mapper.readValue(json, UserAppData.class);
            return myObject;
        }
        catch (  JsonParseException e) {
            e.printStackTrace();
            return null;
        }
        catch (  Exception e) {
            e.printStackTrace();
            throw new RuntimeException("bad input " + json);
        }
    }

    @Override
    public String toString(){
        //ObjectWriter ow = new ObjectMapper().writer();//.withDefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            //return ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
