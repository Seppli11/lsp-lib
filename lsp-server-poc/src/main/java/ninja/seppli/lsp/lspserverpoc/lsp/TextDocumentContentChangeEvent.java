package ninja.seppli.lsp.lspserverpoc.lsp;

public class TextDocumentContentChangeEvent {
	public Range range;
	public String text;

	@Override
	public String toString() {
		return "TextDocumentChange [ " + range + " -> " + text + "]";
	}
}
