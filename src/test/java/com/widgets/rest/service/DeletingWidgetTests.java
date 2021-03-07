package com.widgets.rest.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeletingWidgetTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deleteWidgetValidatesPath() throws Exception {
        WidgetTestUtils.deleteWidget(this.mockMvc, "NOT-A-UUID")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteWidgetNonExistingWidget() throws Exception {
        JSONObject widgetUpdateDto = new JSONObject();

        WidgetTestUtils.updateWidget(this.mockMvc, UUID.randomUUID().toString(), widgetUpdateDto)
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteWidgetWorks() throws Exception {
        String createResponse = WidgetTestUtils.createWidget(this.mockMvc, WidgetTestUtils.widgetCreateBody())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UUID widgetId = WidgetTestUtils.getWidgetId(new JSONObject(createResponse));

        WidgetTestUtils.deleteWidget(this.mockMvc, widgetId.toString())
                .andExpect(status().isOk());

        WidgetTestUtils.getWidget(this.mockMvc, widgetId.toString())
                .andExpect(status().isNotFound());
    }
}
