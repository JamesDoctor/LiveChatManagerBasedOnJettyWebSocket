package org.eclipse.jetty.demo;

import lombok.Data;

@Data
public class CustomerMsg {
	private static final int OPEN = 0;
	private static final int MSG = 1;
	private static final int CLOSE = 2;
	private static final int ERROR = 3;
	
    private final String msg;
    private final int type;
    private final String customerid;
    
    public static CustomerMsg buildOpenMsg(final String customerId) {
    	return new CustomerMsg(null, OPEN, customerId);
    }
    
    public static CustomerMsg buildCloseMsg(final String customerId) {
    	return new CustomerMsg(null, CLOSE, customerId);
    }
    
    public static CustomerMsg buildErrorMsg(final String customerId) {
    	return new CustomerMsg(null, ERROR, customerId);
    }
    
    public static CustomerMsg buildCommonMsg(final String customerId, final String msg) {
    	return new CustomerMsg(msg, MSG, customerId);
    }
}
