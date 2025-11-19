package com.wrlus.jadx;

public class McpServerMain {
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 8651;

	public static void main(String[] args) {
		String host = System.getenv("JADX_DAEMON_MCP_HOST");
		String portString = System.getenv("JADX_DAEMON_MCP_PORT");
		int port = DEFAULT_PORT;

		if (host == null) host = DEFAULT_HOST;
		if (portString != null) port = Integer.parseInt(portString);

		McpServer server = new McpServer(host, port);
		server.start();
	}
}
