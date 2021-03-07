package com.widgets.rest.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreatingWidgetTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createWidgetValidatesNull() throws Exception {
        JSONObject widgetCreateDto = WidgetTestUtils.widgetCreateBody();
        widgetCreateDto.remove("x");

        WidgetTestUtils.createWidget(this.mockMvc, widgetCreateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWidgetValidatesNegative() throws Exception {
        JSONObject widgetCreateDto = WidgetTestUtils.widgetCreateBody();
        widgetCreateDto.put("width", -1);

        WidgetTestUtils.createWidget(this.mockMvc, widgetCreateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWidgetValidatesZero() throws Exception {
        JSONObject widgetCreateDto = WidgetTestUtils.widgetCreateBody();
        widgetCreateDto.put("height", 0);

        WidgetTestUtils.createWidget(this.mockMvc, widgetCreateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWidgetWorks() throws Exception {
        JSONObject widgetCreateDto = WidgetTestUtils.widgetCreateBody();
        widgetCreateDto.put("z", 1000);

        String createResponse = WidgetTestUtils.createWidget(this.mockMvc, widgetCreateDto)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject responseObj = new JSONObject(createResponse);
        assertThat(WidgetTestUtils.getWidgetAttribute(responseObj, "xCoordinate"), equalTo(10));
        assertThat(WidgetTestUtils.getWidgetAttribute(responseObj, "yCoordinate"), equalTo(1000));
        assertThat(WidgetTestUtils.getWidgetAttribute(responseObj, "zIndex"), equalTo(1000));
        assertThat(WidgetTestUtils.getWidgetAttribute(responseObj, "width"), equalTo(1));
        assertThat(WidgetTestUtils.getWidgetAttribute(responseObj, "height"), equalTo(1));
    }

}
