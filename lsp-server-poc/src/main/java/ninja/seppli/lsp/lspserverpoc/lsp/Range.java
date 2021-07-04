package ninja.seppli.lsp.lspserverpoc.lsp;

public class Range {
	public Position start;
	public Position end;

	@Override
	public String toString() {
		return "Range[ " + start + " - " + end + "]";
	}
}
