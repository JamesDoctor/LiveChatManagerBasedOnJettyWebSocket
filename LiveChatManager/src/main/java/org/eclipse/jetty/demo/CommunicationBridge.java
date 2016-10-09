package org.eclipse.jetty.demo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.json.JSONObject;

public class CommunicationBridge {
	public static class Communication {
		private static final String BROADCAST_CUSOMTER = "all";
		private static final String SERVER_MSG_CUSTOMER = "server";
		
		private final String company;
		private final Session companyAgent;
		private final Map<String, Session> customers = new ConcurrentHashMap<>();
		
		private static void sendMsg(final String msg, final Session session) {
			if ((msg == null) || (msg.length() == 0)) return;
			
			try {
				session.getBasicRemote().sendText(msg);
			} catch (final Exception e) {
				System.out.println("Exception occurs when sending message: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		public Communication(final String companyName, final Session session) {
			company = companyName;
			companyAgent = session;
		}
		
		public void sendMsgToCustomer(final String customerId, final String msg) {
			if (Utils.isEmptyStr(customerId) || Utils.isEmptyStr(msg)) {
				return;
			}
			
			if (customerId.equals(SERVER_MSG_CUSTOMER)) return; // for server only.
			if (customerId.equals(BROADCAST_CUSOMTER)) { // broadcast to all customers
				for (final Map.Entry<String, Session> entry : customers.entrySet()) {
					sendMsg(company + ": " + msg, entry.getValue());
				}
				return;
			}
			final Session customer = customers.get(customerId);
			if (customer == null) {
				System.out.println("Customer " + customerId + " does not exist!");
				return;
			}
			sendMsg(company + ": " + msg, customer);
		}
		
		public void addCustomer(final Session session) {
			customers.put(session.getId(), session);
			System.out.println("Added customer session for ID: " + session.getId());
		}

		public void sendMsgToCompanyAgent(final CustomerMsg msg) {
			sendMsg(new JSONObject(msg).toString(), companyAgent);
		}
	}
	
    private static final Map<String, Communication> companyAgentSessions =
    		new ConcurrentHashMap<>();
    
    private static void closeSession(final Session session) {
		try {
			session.close();
		} catch (IOException e) {
			System.out.println("Exception occurs when closing the session: " + e.getMessage());;
			e.printStackTrace();
		}
    }
    
    public static String getCompanyFromSession(final Session session) {
    	final List<String> companies = session.getRequestParameterMap().get("Company");
    	if ((companies == null) || (companies.size() == 0)) {
    		return null;
    	}
    	return companies.get(0);
    }
    
    public static Communication addCompanyAgentSession(final Session session) {
    	final String company = getCompanyFromSession(session);
    	if ((company == null) || (company.length() == 0)) {
    		System.out.println("No company information from company agent session!");
    		closeSession(session);
    		return null;
    	}
    	final Communication com = new Communication(company, session);
    	companyAgentSessions.put(company, com);
    	System.out.println("Company Agent communication added for " + company);
    	return com;
    }
    
    public static Communication addCustomerSession(final Session session) {
    	final String company = getCompanyFromSession(session);
    	if ((company == null) || (company.length() == 0)) {
    		System.out.println("No company information from customer session!");
    		closeSession(session);
    	}
    	final Communication com = companyAgentSessions.get(company);
    	if (com == null)  {
    		System.out.println("No company agent session found!");
    		closeSession(session);
    		return null;
    	}
    	com.addCustomer(session);
    	return com;
    }
}
