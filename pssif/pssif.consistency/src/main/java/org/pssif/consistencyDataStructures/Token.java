package org.pssif.consistencyDataStructures;

/**
 * @author Andreas
 * 
 *         this class represents a simple token implementation. Each token holds
 *         a word as a String and an according wordWeigth (plus variables
 *         necessary for computing the weight)
 * 
 */
public class Token {

	public Token(String string) {
		this.word = string;
		this.documentCounter = 0;
		this.wordWeigth = 0;
	}

	/**
	 * the word held by this tolen
	 */
	private String word;

	/**
	 * the computed tf-idf weight of this token
	 */
	private double wordWeigth;

	/**
	 * counts in how many documents(nodes) this token appears
	 */
	private int documentCounter;

	/**
	 * variables describing temporary results for the vsm
	 */
	private double idf, tf;

	/**
	 * @return the word
	 */
	public String getWord() {
		return this.word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the documentCounter
	 */
	public int getDocumentCounter() {
		return documentCounter;
	}

	/**
	 * @param documentCounter
	 *            the documentCounter to set
	 */
	public void setDocumentCounter(int documentCounter) {
		this.documentCounter = documentCounter;
	}

	/**
	 * @return the idf
	 */
	public double getIdf() {
		return idf;
	}

	/**
	 * @param idf
	 *            the idf to set
	 */
	public void setIdf(double idf) {
		this.idf = idf;
	}

	/**
	 * @return the tf
	 */
	public double getTf() {
		return tf;
	}

	/**
	 * @param tf
	 *            the tf to set
	 */
	public void setTf(double tf) {
		this.tf = tf;
	}

	/**
	 * @return the wordWeigth
	 */
	public double getWordWeigth() {
		return wordWeigth;
	}

	/**
	 * this method increases the DocumentCounter by +1
	 */
	public void incrementDocumentCounter() {
		int actCounter = getDocumentCounter();
		actCounter++;
		this.setDocumentCounter(actCounter);
	}

	/**
	 * This method coputes the tf-idf word weight for this token (relative to
	 * the document corpus)
	 */
	public void computeWordWeight() {
		double weight = 0;

		weight = tf * idf;

		this.wordWeigth = weight;
	}

}
