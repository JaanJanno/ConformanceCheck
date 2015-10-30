package ee.ut.jaanjanno.conformancechecker.petrinet;

public class Place implements Node {
	
	@SuppressWarnings("unused")
	private String name;
	private int tokens;
	
	public Place(String name) {
		this.name = name;
		this.tokens = 0;
	}
	
	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public int getTokens() {
		return tokens;
	}
	
	public void consumeToken() {
		tokens --;
	}
	
	public void produceToken() {
		tokens ++;
	}

}