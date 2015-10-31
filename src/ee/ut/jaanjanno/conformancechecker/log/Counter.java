package ee.ut.jaanjanno.conformancechecker.log;

public class Counter {
	
	public int missingTokens = 0;
	public int remainingTokens = 1;
	public int consumedTokens = 0;
	public int producedTokens = 1;
	
	public int getMissingTokens() {
		return missingTokens;
	}
	public int getRemainingTokens() {
		return remainingTokens;
	}
	public int getConsumedTokens() {
		return consumedTokens;
	}
	public int getProducedTokens() {
		return producedTokens;
	}

}
