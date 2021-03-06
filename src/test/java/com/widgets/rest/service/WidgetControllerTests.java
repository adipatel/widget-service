package com.widgets.rest.service;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WidgetControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void listWidgetsWorks() throws Exception {
		this.mockMvc
				.perform(get("/api/widgets").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotImplemented());
	}

	@Test
	public void getWidgetByIdWorks() throws Exception {
		this.mockMvc
				.perform(get("/api/widgets/"+ UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotImplemented());
	}

	@Test
	public void getWidgetByIdValidates() throws Exception {
		this.mockMvc
				.perform(get("/api/widgets/anonUUID").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deleteWidgetByIdWorks() throws Exception {
		this.mockMvc
				.perform(delete("/api/widgets/"+ UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotImplemented());
	}

	@Test
	public void deleteWidgetByIdValidates() throws Exception {
		this.mockMvc
				.perform(delete("/api/widgets/anonUUID").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createWidgetValidatesNull() throws Exception {
		JSONObject widgetCreateDto = new JSONObject();
		widgetCreateDto.put("x", null);
		widgetCreateDto.put("y", 1000);
		widgetCreateDto.put("z", -1);
		widgetCreateDto.put("width", 1);
		widgetCreateDto.put("height", 1);

		this.mockMvc
				.perform(
						post("/api/widgets/")
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetCreateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createWidgetValidatesNegative() throws Exception {
		JSONObject widgetCreateDto = new JSONObject();
		widgetCreateDto.put("x", 10);
		widgetCreateDto.put("y", 1000);
		widgetCreateDto.put("z", -1);
		widgetCreateDto.put("width", -1);
		widgetCreateDto.put("height", 1);

		this.mockMvc
				.perform(
						post("/api/widgets/")
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetCreateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createWidgetValidatesZero() throws Exception {
		JSONObject widgetCreateDto = new JSONObject();
		widgetCreateDto.put("x", 10);
		widgetCreateDto.put("y", 1000);
		widgetCreateDto.put("z", -1);
		widgetCreateDto.put("width", 0);
		widgetCreateDto.put("height", 1);

		this.mockMvc
				.perform(
						post("/api/widgets/")
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetCreateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createWidgetWorks() throws Exception {
		JSONObject widgetCreateDto = new JSONObject();
		widgetCreateDto.put("x", 10);
		widgetCreateDto.put("y", 1000);
		widgetCreateDto.put("z", -1);
		widgetCreateDto.put("width", 1);
		widgetCreateDto.put("height", 1);

		this.mockMvc
				.perform(
						post("/api/widgets/")
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetCreateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isNotImplemented());
	}

	@Test
	public void patchWidgetValidatesPath() throws Exception {
		JSONObject widgetCreateDto = new JSONObject();
		widgetCreateDto.put("x", 10);

		this.mockMvc
				.perform(
						patch("/api/widgets/nonUUID")
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetCreateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void patchWidgetValidatesNegative() throws Exception {
		JSONObject widgetUpdateDto = new JSONObject();
		widgetUpdateDto.put("x", 10);
		widgetUpdateDto.put("width", -1);

		this.mockMvc
				.perform(
						patch("/api/widgets/"+ UUID.randomUUID())
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetUpdateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void patchWidgetValidatesZero() throws Exception {
		JSONObject widgetUpdateDto = new JSONObject();
		widgetUpdateDto.put("x", 10);
		widgetUpdateDto.put("width", 0);

		this.mockMvc
				.perform(
						patch("/api/widgets/"+ UUID.randomUUID())
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetUpdateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void patchWidgetWorks() throws Exception {
		JSONObject widgetUpdateDto = new JSONObject();
		widgetUpdateDto.put("x", 1);
		widgetUpdateDto.put("width", 1);

		this.mockMvc
				.perform(
						patch("/api/widgets/"+ UUID.randomUUID())
								.contentType(MediaType.APPLICATION_JSON)
								.content(widgetUpdateDto.toString())
				)
				.andDo(print())
				.andExpect(status().isNotImplemented());
	}
}
