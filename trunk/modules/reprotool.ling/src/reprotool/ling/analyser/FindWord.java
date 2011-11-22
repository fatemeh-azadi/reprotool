package reprotool.ling.analyser;

import java.util.ArrayList;

import reprotool.ling.ParseTreeNode;
import reprotool.ling.SentenceNode;
import reprotool.ling.Word;

/**
 * Class for finding selected words in a tree
 * 
 * @author ofiala
 *
 */
public class FindWord {

	/**
	 * Find all nouns in given tree
	 * @param rootNode SentenceNode object to analyze
	 * @return found ArrayList<Word> or null
	 */
	public static ArrayList<Word> getNouns(SentenceNode rootNode) {
		// result list
		ArrayList<Word> resultWords = new ArrayList<Word>();
		// go through all children
		for(ParseTreeNode pnode : rootNode.getChildren()) {
			if (pnode instanceof Word) {
				// need noun
				if (((Word) pnode).isNoun())
					resultWords.add((Word)pnode);
			} else { // node - go deeper
				// recursion is there - add all of them
				for (Word word : getNouns((SentenceNode)pnode)) {
					resultWords.add(word);
				}
			}
		}
		return resultWords;
	}

	/**
	 * Find all verbs in given tree
	 * @param rootNode SentenceNode object to analyze
	 * @return found ArrayList<Word> or null
	 */
	public static ArrayList<Word> getVerbs(SentenceNode rootNode) {
		// result list
		ArrayList<Word> resultWords = new ArrayList<Word>();
		// go through all children
		for(ParseTreeNode pnode : rootNode.getChildren()) {
			if (pnode instanceof Word) {
				// need noun
				if (((Word) pnode).isVerb())
					resultWords.add((Word)pnode);
			} else { // node - go deeper
				// recursion is there
				for (Word word : getVerbs((SentenceNode)pnode)) {
					resultWords.add(word);
				}
			}
		}
		return resultWords;
	}
	
}
