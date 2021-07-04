package ninja.seppli.lsp.lspserverpoc;

import ninja.seppli.jsonrpc.base.RequestHandler;
import ninja.seppli.lsp.lspserverpoc.lsp.LspListener;
import ninja.seppli.lsp.lspserverpoc.server.Server;

public class LspServerMockMain {
	private LspListener listener;

	public LspServerMockMain() {
		Server server = new Server("localhost", 8081);
		server.setRequestHandlerInitListener(this::setupRequestHandler);
		server.start();
	}

	private void setupRequestHandler(RequestHandler handler) {
		this.listener = new LspListener();
		handler.addClassDispatcher(listener);
	}

	public static void main(String[] args) {

		new LspServerMockMain();
	}
}
