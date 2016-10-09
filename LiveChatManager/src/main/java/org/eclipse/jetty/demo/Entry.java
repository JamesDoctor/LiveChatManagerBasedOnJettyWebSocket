package org.eclipse.jetty.demo;

import java.io.IOException;

import org.eclipse.jetty.server.Server;

public class Entry {
	
	private static void stopServer(final Server server) {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occurs in stopping server: " + e.getMessage());
		}
	}
	
	private static void joinServer(final Server server) {
		try {
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occurs in server.join(): " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		final Server serverForCustomer = EventServer.createServer(8080, "/Customer", false);
		if (serverForCustomer == null) {
			System.out.println("Fail to create server for customer");
			return;
		}
		final Server serverForCompanyAgent = EventServer.createServer(8180, "/CompanyAgent", true);
		if (serverForCompanyAgent == null) {
			System.out.println("Fail to create server for company agent");
			try {
				serverForCustomer.stop();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Exception occurs in stopping server for customer: " + e.getMessage());
			}
			return;
		}
		
		try {
			serverForCustomer.start();
		} catch (Exception e) {
			System.out.println("Exception occurs in starting server for customer: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		try {
			serverForCompanyAgent.start();
		} catch (Exception e) {
			System.out.println("Exception occurs in starting server for company agent: " + e.getMessage());
			e.printStackTrace();
			stopServer(serverForCustomer);
			joinServer(serverForCustomer);
			return;
		}
		
		System.out.println("Press any key to stop processing...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.in.read();
				} catch (IOException e) {
					System.out.println("Exception occurs in reading standard IO: " + e.getMessage());
					e.printStackTrace();
				}
				stopServer(serverForCustomer);
				stopServer(serverForCompanyAgent);
			}
			
		}).start();

		joinServer(serverForCustomer);
		System.out.println("Server for customer closed.");
		joinServer(serverForCompanyAgent);
		System.out.println("Server for company agent closed.");
		System.out.println("Exit.");
	}

}
