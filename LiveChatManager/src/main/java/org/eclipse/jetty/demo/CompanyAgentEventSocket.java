package org.eclipse.jetty.demo;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events")
public class CompanyAgentEventSocket {
	private CommunicationBridge.Communication communication;
	
    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        System.out.println("Company Agent Socket Connected: " + sess);
        communication = CommunicationBridge.addCompanyAgentSession(sess);
    }
    
    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println("Company Agent Socket received TEXT message: " + message);
        if (communication != null) {
        	communication.sendMsgToCustomer(message);
        }
    }
    
    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
    }
    
    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}