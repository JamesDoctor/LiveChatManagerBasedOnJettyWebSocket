package org.eclipse.jetty.demo;

public final class Utils {
	private Utils() {
	}
	
	public static boolean isEmptyStr(final String str) {
		return (str == null) || (str.length() == 0);
	}

}
