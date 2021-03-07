package com.widgets.rest.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ListWidgetTests {

    @Autowired
    private MockMvc mockMvc;
    @Test
    public void getWidgetWorks() throws Exception {
        WidgetTestUtils.listWidgets(this.mockMvc)
                .andExpect(status().isOk());
    }

    @Test
    public void getWidgetPreservesOrder() throws Exception {
        UUID widgetA = createWidget(-10);
        UUID widgetB = createWidget(-20);
        UUID widgetC = createWidget(-30);

        String listResponse = WidgetTestUtils.listWidgets(this.mockMvc)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONArray listResponseObj = new JSONArray(listResponse);
        assertThat(WidgetTestUtils.getWidgetId(listResponseObj.getJSONObject(0)), equalTo(widgetC));
        assertThat(WidgetTestUtils.getWidgetId(listResponseObj.getJSONObject(1)), equalTo(widgetB));
        assertThat(WidgetTestUtils.getWidgetId(listResponseObj.getJSONObject(2)), equalTo(widgetA));
    }

    private UUID createWidget(Integer zIndex) throws Exception {
        JSONObject widgetCreateDto = WidgetTestUtils.widgetCreateBody();
        widgetCreateDto.put("z", zIndex);
        String createResponse = WidgetTestUtils.createWidget(this.mockMvc, widgetCreateDto)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return WidgetTestUtils.getWidgetId(new JSONObject(createResponse));
    }
}
