package ninja.seppli.lsp.lspserverpoc.lsp;

public class TextDocumentItem {
	public String uri;
	public String text;

	@Override
	public String toString() {
		return "TextDocumentItem [ " + text + " @" + uri + "]";
	}
}
