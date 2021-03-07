package com.widgets.rest.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class WidgetTestUtils {

    public static JSONObject widgetCreateBody() throws Exception {
        JSONObject widgetCreateDto = new JSONObject();
        widgetCreateDto.put("x", 10);
        widgetCreateDto.put("y", 1000);
        widgetCreateDto.put("z", -1);
        widgetCreateDto.put("width", 1);
        widgetCreateDto.put("height", 1);
        return widgetCreateDto;
    }

    public static ResultActions createWidget(MockMvc mockMvc, JSONObject widgetCreateDto) throws Exception {
        return  mockMvc.perform(
                post("/api/widgets/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(widgetCreateDto.toString())
        );
    }

    public static  ResultActions updateWidget(MockMvc mockMvc, String widgetId, JSONObject widgetUpdateDto) throws Exception {
        return  mockMvc.perform(
                patch("/api/widgets/"+ widgetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(widgetUpdateDto.toString())
        );
    }

    public static  ResultActions deleteWidget(MockMvc mockMvc, String widgetId) throws Exception {
        return  mockMvc.perform(
                delete("/api/widgets/"+ widgetId)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    public static  ResultActions getWidget(MockMvc mockMvc, String widgetId) throws Exception {
        return  mockMvc.perform(
                get("/api/widgets/"+ widgetId)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    public static  ResultActions listWidgets(MockMvc mockMvc) throws Exception {
        return  mockMvc.perform(
                get("/api/widgets/")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }


    public static UUID getWidgetId(JSONObject responseObj) throws Exception {
        return UUID.fromString(responseObj.getString("widgetId"));
    }

    public static Integer getWidgetAttribute(JSONObject responseObj, String key) throws JSONException {
        return responseObj.getInt(key);
    }
}
