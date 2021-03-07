package com.widgets.rest.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetWidgetTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getWidgetValidatesPath() throws Exception {
        WidgetTestUtils.getWidget(this.mockMvc, "NOT-A-UUID")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWidgetNonExistingWidget() throws Exception {
        WidgetTestUtils.getWidget(this.mockMvc, UUID.randomUUID().toString())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getWidgetWorks() throws Exception {
        String createResponse = WidgetTestUtils.createWidget(this.mockMvc, WidgetTestUtils.widgetCreateBody())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UUID widgetId = WidgetTestUtils.getWidgetId(new JSONObject(createResponse));


        String getResponse = WidgetTestUtils.getWidget(this.mockMvc, widgetId.toString())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONObject getResponseObj = new JSONObject(getResponse);
        assertThat(WidgetTestUtils.getWidgetId(getResponseObj), equalTo(widgetId));
        assertThat(WidgetTestUtils.getWidgetAttribute(getResponseObj, "xCoordinate"), equalTo(10));
        assertThat(WidgetTestUtils.getWidgetAttribute(getResponseObj, "yCoordinate"), equalTo(1000));
        assertThat(WidgetTestUtils.getWidgetAttribute(getResponseObj, "zIndex"), equalTo(-1));
        assertThat(WidgetTestUtils.getWidgetAttribute(getResponseObj, "width"), equalTo(1));
        assertThat(WidgetTestUtils.getWidgetAttribute(getResponseObj, "height"), equalTo(1));
    }
}
