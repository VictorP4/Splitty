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

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Utility class for interacting with the server.
 */
public class ServerUtils {

	private static String SERVER = "";
	private static StompSession session;
	private String PORT = "";

	public void setSERVER(String SERVER) {
		this.SERVER = SERVER;
	}

	/**
	 * This method creates a get request to the server entered by the user.
	 *
	 * @param userUrl a string representing the url
	// * @param port the port
	 * @return a Response object
	 */
	public Response checkServer(String userUrl) {
		this.SERVER = "http://" + userUrl;
		//this.PORT = port;

		session = connect("ws://" + userUrl + "/websocket");
		Response response =  ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/connection")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get();


		return response;
	}
	private StompSession connect(String url) {
		var client = new StandardWebSocketClient();
		var stomp = new WebSocketStompClient(client);
		stomp.setMessageConverter(new MappingJackson2MessageConverter());
		try {
			return stomp.connect(url, new StompSessionHandlerAdapter() {
			}).get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		throw new IllegalStateException();
	}
	/**
	 * This method starts the websockets on a specific port, not working for the moment.
	 * @param url - the url of the websocket
	 * @return the session
	 */
	public StompSession startWebSockets(String url){
		this.session = connect("ws://" + url +"/websocket");
		return session;
	}

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

    public Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

	public Event getEvent(long id) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + id)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get().readEntity(Event.class);
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

	/**
	 *
	 * @param expense expense to be added
	 * @return html response of the successful post request
	 */
	public Expense addExpense(Expense expense, Long id){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/"+id+"/expenses")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
	}
	public Participant addParticipant(Participant participant){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(participant,APPLICATION_JSON), Participant.class);
	}
	public Participant updateParticipant(Participant participant){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants/"+participant.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(participant,APPLICATION_JSON), Participant.class);
	}
	public Response deleteParticipant(Participant participant){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants/"+participant.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}

	/**
	 * Fetches the tags from the server.
	 *
	 * @return The list of tags.
	 */
	public List<Tag> getTags(Long eventId) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/events/" + eventId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get().readEntity(Event.class).getTags();
	}
	public Tag addTag(Tag tag){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/tags")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.post(Entity.entity(tag,APPLICATION_JSON), Tag.class);
	}
	public Response removeTag(Tag tag){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/tags/"+tag.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.delete();
	}
	public Tag updateTag(Tag tag){
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/tags/"+tag.getId())
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.put(Entity.entity(tag,APPLICATION_JSON), Tag.class);
	}
}
