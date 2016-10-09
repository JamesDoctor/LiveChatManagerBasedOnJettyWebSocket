package org.eclipse.jetty.demo;

import org.json.JSONObject;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ServerMsg {
	private static final String CUSTOMER_ID = "customerId";
	private static final String MESSAGE = "msg";
	
	private final String customerId;
	private final String msg;
	
	public static ServerMsg buildServerMsg(final String msg) {
		if ((msg == null) || (msg.length() == 0)) return null;
		try {
			final JSONObject obj = new JSONObject(msg);
			return new ServerMsg(obj.getString(CUSTOMER_ID), obj.getString(MESSAGE));
		} catch (final Exception e) {
			System.out.println("Exception in parsing \"" + msg + "\": " + e.getMessage());
			return null;
		}
	}
}
