package com.widgets.rest.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdatingWidgetTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void patchWidgetValidatesPath() throws Exception {
        WidgetTestUtils.updateWidget(this.mockMvc, "NOT-A-UUID", new JSONObject())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchWidgetValidatesNull() throws Exception {
        JSONObject widgetUpdateDto = new JSONObject();
        widgetUpdateDto.put("width", -1);

        this.mockMvc.perform(
                patch("/api/widgets/"+ UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchWidgetValidatesNegative() throws Exception {
        JSONObject widgetUpdateDto = new JSONObject();
        widgetUpdateDto.put("width", -1);

        WidgetTestUtils.updateWidget(this.mockMvc, UUID.randomUUID().toString(), widgetUpdateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchWidgetValidatesZero() throws Exception {
        JSONObject widgetUpdateDto = new JSONObject();
        widgetUpdateDto.put("height", 0);

        WidgetTestUtils.updateWidget(this.mockMvc, UUID.randomUUID().toString(), widgetUpdateDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchWidgetNonExistingWidget() throws Exception {
        JSONObject widgetUpdateDto = new JSONObject();

        WidgetTestUtils.updateWidget(this.mockMvc, UUID.randomUUID().toString(), widgetUpdateDto)
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchWidgetWorks() throws Exception {
        String createResponse = WidgetTestUtils.createWidget(this.mockMvc, WidgetTestUtils.widgetCreateBody())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UUID widgetId = WidgetTestUtils.getWidgetId(new JSONObject(createResponse));

        JSONObject widgetUpdateDto = new JSONObject();
        widgetUpdateDto.put("x", 1);
        widgetUpdateDto.put("width", 1);

        String patchResponse = WidgetTestUtils.updateWidget(this.mockMvc, widgetId.toString(), widgetUpdateDto)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject patchResponseObj = new JSONObject(patchResponse);

        assertThat(WidgetTestUtils.getWidgetId(patchResponseObj), equalTo(widgetId));
        assertThat(WidgetTestUtils.getWidgetAttribute(patchResponseObj, "xCoordinate"), equalTo(1));
        assertThat(WidgetTestUtils.getWidgetAttribute(patchResponseObj, "width"), equalTo(1));
    }
}
