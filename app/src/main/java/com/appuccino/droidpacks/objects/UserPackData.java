package com.appuccino.droidpacks.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserPackData {
    public int id;
    public boolean gold;

    //@JsonProperty("example") is used for Jackson for JSON
    public UserPackData(
            @JsonProperty("id") int i,
            @JsonProperty("gold") boolean g){
        id = i;
        gold = g;
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
