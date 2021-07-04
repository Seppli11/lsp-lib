package ninja.seppli.lsp.lspserverpoc.lsp;

public class Position {
	public int line;
	public int character;

	@Override
	public String toString() {
		return "Pos [" + line + ":" + character + "]";
	}
}
