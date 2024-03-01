/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import commons.EmailRequestBody;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

/**
 * Utility class for interacting with the server.
 */
public class ServerUtils {

	private static final String SERVER = "http://localhost:8080/";

	/**
	 * Sends invites via email.
	 *
	 * @param requestBody The request body containing email information.
	 * @return The response from the server.
	 */
	public Response sendInvites(EmailRequestBody requestBody) {
		return ClientBuilder.newClient()
				.target(SERVER).path("api/email/invites")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(requestBody, APPLICATION_JSON));
	}

	/**
	 * Updates an event.
	 *
	 * @param event The event to update.
	 * @return The updated event.
	 */
	public Event updateEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("/api/events/" + event.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(event, APPLICATION_JSON), Event.class);
	}

	/**
	 * Sends reminders via email.
	 *
	 * @param emailRequestBody The request body containing email information for reminders.
	 * @return The response from the server.
	 */
	public Response sendReminder(EmailRequestBody emailRequestBody) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/email/reminders")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(emailRequestBody, APPLICATION_JSON));
	}
}
