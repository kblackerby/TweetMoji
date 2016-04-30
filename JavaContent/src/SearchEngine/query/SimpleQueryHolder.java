package query;

public class SimpleQueryHolder {
	
	private String mainQuery;
	private String minTotalScore;
	private String maxTotalScore;
	private String minTextScore;
	private String maxTextScore;
	private String minEmojiScore;
	private String maxEmojiScore;
	
	public void setMainQuery(String mainQuery) {
		this.mainQuery = mainQuery;
	}
	public void setMinTotalScore(double minTotalScore) {
		this.minTotalScore = Double.toString(minTotalScore);
	}
	public void setMaxTotalScore(double maxTotalScore) {
		this.maxTotalScore = Double.toString(maxTotalScore);
	}
	public void setMinTextScore(double minTextScore) {
		this.minTextScore = Double.toString(minTextScore);
	}
	public void setMaxTextScore(double maxTextScore) {
		this.maxTextScore = Double.toString(maxTextScore);
	}
	public void setMinEmojiScore(double minEmojiScore) {
		this.minEmojiScore = Double.toString(minEmojiScore);
	}
	public void setMaxEmojiScore(double maxEmojiScore) {
		this.maxEmojiScore = Double.toString(maxEmojiScore);
	}
	public String getMainQuery() {
		return mainQuery;
	}
	public String getMinTotalScore() {
		return minTotalScore;
	}
	public String getMaxTotalScore() {
		return maxTotalScore;
	}
	public String getMinTextScore() {
		return minTextScore;
	}
	public String getMaxTextScore() {
		return maxTextScore;
	}
	public String getMinEmojiScore() {
		return minEmojiScore;
	}
	public String getMaxEmojiScore() {
		return maxEmojiScore;
	}
	
	
	
	
	

}
