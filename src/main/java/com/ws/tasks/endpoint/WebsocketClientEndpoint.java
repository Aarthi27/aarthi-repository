package com.ws.tasks.endpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.web.socket.WebSocketHttpHeaders;

import com.ws.helper.APIInfo;


@ClientEndpoint(configurator = WebsocketClientEndpoint.AuthorizationConfigurator.class)
public class WebsocketClientEndpoint {

	Session userSession = null;

	public WebsocketClientEndpoint() {

	}

	public WebsocketClientEndpoint(URI endpoint) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(WebsocketClientEndpoint.class, endpoint);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("opening websocket "+LocalDateTime.now());
		this.userSession = session;
		
		  try { 
			  userSession.getBasicRemote().sendText(
		  "{\"type\":\"subscribe\",\"chan_name\":\"orderbook\",\"subchan_name\":\"" +
		  "btc-eur" + "\"}"); } catch (IOException e) { e.printStackTrace(); }
		 
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket : "+LocalDateTime.now()+" "+ reason.getReasonPhrase() + " " + reason.getCloseCode());
		this.userSession = null;
	}

	
	@OnError
	public void onError(Session session, Throwable t) {
		t.printStackTrace();
	}

	
	@OnMessage
	public void onMessage(String msg) {
		//System.out.println("Message from Server" + msg);
	}

	public static class AuthorizationConfigurator extends ClientEndpointConfig.Configurator {
		public void beforeRequest(Map<String, List<String>> headers) {

			System.out.println("Inside before Request");
			APIInfo apiInfo = APIInfo.getInstance();

			headers.put(WebSocketHttpHeaders.AUTHORIZATION, asList(apiInfo.getAuthHeaderInfo()));
			headers.put(WebSocketHttpHeaders.DATE, asList(apiInfo.getDate()));
			headers.put("ApiKey", asList(apiInfo.getApiKey()));
		}

		private List<String> asList(String content) {
			List<String> list = new ArrayList<>();
			list.add(content);
			return list;
		}
	}

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		new WebsocketClientEndpoint(new URI(APIInfo.getInstance().getWebsocketURL()));
		CountDownLatch latch =new CountDownLatch(1);
		latch.await();
	}
}
