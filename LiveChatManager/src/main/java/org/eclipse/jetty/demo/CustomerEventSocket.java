package org.eclipse.jetty.demo;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.ClientEndpoint;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events")
public class CustomerEventSocket {
	private CommunicationBridge.Communication communication;
	private String id;
	
    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        System.out.println("Customer Socket Connected: " + sess);
        communication = CommunicationBridge.addCustomerSession(sess);
        if (communication != null) {
            id = sess.getId();
        	communication.sendMsgToCompanyAgent(CustomerMsg.buildOpenMsg(id));
        }
    }
    
    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println("Customer Socket Received TEXT message: " + message);
        if (communication != null) {
        	communication.sendMsgToCompanyAgent(CustomerMsg.buildCommonMsg(id, message));
        }
    }
    
    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
        if (communication != null) {
        	communication.sendMsgToCompanyAgent(CustomerMsg.buildCloseMsg(id));
        	communication.removeCustomer(id);
        }
    }
    
    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
        if (communication != null) {
        	communication.sendMsgToCompanyAgent(CustomerMsg.buildErrorMsg(id));
        }
    }
}

