package client.utils;

import commons.Event;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketUtils {
    private StompSession session;
    private List<Consumer<Event>> eventListener;

    public WebSocketUtils() {
        eventListener = new ArrayList<>();
    }
    public void addEventListener(Consumer<Event> listener){
        eventListener.add(listener);
    }
    public void connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            this.session = stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
            registerForUpdates("/topic/events",Event.class, event ->{
                eventListener.forEach(listener ->listener.accept(event));
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void registerForUpdates(String dest, Class<T> type, Consumer<T> action) {
        session.subscribe(dest, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                action.accept((T)payload);
            }
        });
    }
}
