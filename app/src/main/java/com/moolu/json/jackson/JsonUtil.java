package com.moolu.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Nanan on 3/4/2015.
 */
public class JsonUtil {
    public static ObjectMapper mapper = new ObjectMapper();
    public static JsonFactory jsonFactory = new MappingJsonFactory(mapper);
    static{
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }



    public static <T> T fromJson(String json,Class<T> clas) throws IOException {
        return mapper.readValue(json, clas);
    }

    public static <T> ResponseDo<T> getResult(String json,Class<T> clas) throws IOException {
        ResponseDo<T> res = new ResponseDo<T>();
        if(json == null){
            res.setResultCode(400);
            return res;
        }
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonNode jsonNode = mapper.readTree(jsonParser);
        JsonNode node = jsonNode.get("content");
        JsonNode resultCode = jsonNode.get("resultCode");
        if(resultCode != null){
            res.setResultCode(resultCode.asInt(200));
        }else{
            res.setResultCode(400);
        }
        JsonNode message = jsonNode.get("message");
        if(message != null){
            res.setMessage(message.toString());
        }

        if(node != null){
            res.setResult(mapper.readValue(node.toString(), clas));
        }
        return res;
    }
}
