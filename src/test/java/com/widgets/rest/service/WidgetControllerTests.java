package com.widgets.rest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
				.perform(get("/widgets"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void getWidgetByIdWorks() throws Exception {
		this.mockMvc
				.perform(get("/widget/"+ UUID.randomUUID())
						.param("name", "Widget name"))
				.andDo(print())
				.andExpect(status().isOk());
	}

}
