package ninja.seppli.lsp.lspserverpoc.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ninja.seppli.jsonrpc.base.RequestHandler;
import ninja.seppli.jsonrpc.exception.InvalidRequestException;

public class Server {
	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ServerSocket serverSocket;

	private String host;
	private int port;

	private Thread thread;

	private PrintWriter out;

	private RequestHandler requestHandler;

	private Consumer<RequestHandler> requestHandlerInitListener;

	public Server(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		if (thread != null && thread.isAlive()) {
			throw new IllegalStateException("Server is already running");
		}
		thread = new Thread(this::run);
		thread.start();
	}

	public void stop() {
		thread.interrupt();
	}

	private void run() {
		try {
			serverSocket = new ServerSocket(port);
			logger.info("Server is running on {}:{}", host, port);
			while (!thread.isInterrupted()) {
				Socket socket = serverSocket.accept();
				logger.info("Found client");
				initRequestHandler();
				out = new PrintWriter(socket.getOutputStream());
				Reader reader = new InputStreamReader(socket.getInputStream());
				StringBuilder currentStr = new StringBuilder();
				char[] buffer = new char[1024];
				int readChars = -1;
				int bodySize = -1;
				ParserState state = ParserState.HEADER;
				while ((readChars = reader.read(buffer)) >= 0) {
					for (int i = 0; i < readChars; i++) {
						char current = buffer[i];
						switch (state) {
						case HEADER:
							if (current != '\r' && current != '\n') {
								currentStr.append(current);
							}
							if (current == '\n') {
								String header = currentStr.toString().trim();
								if (header.isEmpty()) {
									state = ParserState.BODY;
								} else {
									bodySize = parseHeader(header);
									currentStr = new StringBuilder();
								}
							}
							break;

						case BODY:
							currentStr.append(current);
							bodySize--;
							if (bodySize == 0) {
								parseBody(currentStr.toString());
								currentStr = new StringBuilder();
								state = ParserState.HEADER;
							}
						default:
							break;
						}
					}
				}
				logger.info("lost client");
			}
		} catch (IOException e) {
			logger.error("An error occured in the server run method", e);
		}
	}

	private void initRequestHandler() {
		requestHandler = new RequestHandler();
		if (requestHandlerInitListener != null) {
			requestHandlerInitListener.accept(requestHandler);
		}
	}

	private int parseHeader(String line) {
		String[] splitted = line.split(":");
		if (splitted.length == 1) {
			logger.warn("Invalid Header line \"{}\"", line);
			return -1;
		}
		if (!"Content-Length".equals(splitted[0].trim())) {
			logger.warn("Invalid Header \"{}\"", splitted[0].trim());
			return -1;
		}
		try {
			return Integer.parseInt(splitted[1].trim());
		} catch (NumberFormatException e) {
			logger.error("Invalid Content-Length header with the size \"{}\"", splitted[1].trim(), e);
			return -1;
		}
	}

	private void parseBody(String body) {

		// logger.info("Received Body: {}", body);
		try {
			String response = requestHandler.handel(body);
			// request was notification
			if (response == null) {
				return;
			}
			byte[] reponseBytes = response.getBytes(StandardCharsets.UTF_8);
			out.append("Content-Length: " + reponseBytes.length + "\r\n");
			out.append("\r\n");
			out.append(response);
			out.flush();
			// logger.info("responsed with: {}", response);
		} catch (InvalidRequestException e) {
			logger.error("Couldn't handle rpc request", e);
		}
	}


	public void setRequestHandlerInitListener(Consumer<RequestHandler> requestHandlerInitListener) {
		this.requestHandlerInitListener = requestHandlerInitListener;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private enum ParserState {
		HEADER, BODY
	}
}
