package ninja.seppli.lsp.lspserverpoc.lsp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ninja.seppli.jsonrpc.annotation.RpcMethod;
import ninja.seppli.jsonrpc.base.Response;

public class LspListener {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public LspListener() {

	}

	@RpcMethod("initialize")
	public Map<String, Object> initialize(Map<String, Object> param) {
		logger.info("received initialize: {}", param);
		Map<String, Object> response = new HashMap<>();

		Map<String, Object> serverCapabilities = new HashMap<>();
		Map<String, Object> textDocumentSync = new HashMap<>();
		textDocumentSync.put("openClose", true);
		textDocumentSync.put("change", 2);
		serverCapabilities.put("textDocumentSync", textDocumentSync);
		response.put("capabilities", serverCapabilities);

		Map<String, Object> serverInfoMap = new HashMap<>();
		serverInfoMap.put("name", "Poc LSP Server");
		serverInfoMap.put("version", "0.1");
		response.put("serverInfo", serverInfoMap);
		return response;
	}

	@RpcMethod("textDocument/didOpen")
	public Response didOpen(DidOpenTextDocumentParams param) {
		logger.info("didOpen:  " + param.textDocument);
		return Response.NOTIFICATION_RESPONSE;
	}

	@RpcMethod("textDocument/didChange")
	public Response didChange(DidChangeTextDocumentParams param) {
		logger.info("changes: " + Arrays.toString(param.contentChanges));
		return Response.NOTIFICATION_RESPONSE;
	}
}
