package reprotool.ling.tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.Bundle;

import reprotool.ling.Activator;
import reprotool.ling.LingFactory;
import reprotool.ling.NodeType;
import reprotool.ling.POSType;
import reprotool.ling.ParseTreeNode;
import reprotool.ling.Sentence;
import reprotool.ling.SentenceNode;
import reprotool.ling.SentenceType;
import reprotool.ling.Tool;
import reprotool.ling.Word;
import danbikel.lisp.Sexp;
import danbikel.parser.Settings;

/*
 * 
 * Parser
 * D:\Projects\ReProTool\dbparser>java -server -cp D:\Projects\ReProTool\dbparser\dbparser.jar -Dparser.settingsFile=collins.properties danbikel.parser.Parser -isD:\Projects\ReProTool\dbparser\wsj-02-21.obj.gz -sa D:\Projects\ReProTool\dbparser\test.txt
 * 
 * Trainer
 * D:\Projects\ReProTool\dbparser>java -Xms800m -Xmx800m -cp dbparser.jar -Dparser.settingsFile=collins.properties danbikel.parser.Trainer -it -l wsj-02-21.observed.gz -od wsj-02-21.obj.gz
 */

/**
 * @author ofiala
 *
 */
public class Parser extends Tool {
	
	// number of attempts to load a model (sometime external tool fails during loading)
	static int max_model_reloads = 5;
	static boolean runs = false;
	static danbikel.parser.Parser parser = null;
	
	/**
	 * Parse trees of each sentence
	 *
	 * @return String parsed_tree 
	 */	
	public String run(String text) {
		return getString(text);
	}
	
    /**
     * Parse sentence into tree in string format
     * 
     * @param originalText Sentence from linguistics tagger
     * @return Sentence whole Sentence object with tree and array
     */
    public static String getString(String originalText) {
    	String parsedText = "";
    	
    	// bad input
    	if (originalText == null || originalText.isEmpty() || originalText.equals("(( ()) )"))
    		return "";
    	
    	// running at blank data
    	if(originalText.isEmpty()) {
    		return parsedText;
    	}
    	
    	if (runs){ // get response from tool
    		
    		// validation of state
    		boolean alive = false;
    		try{
    			alive = parser.alive();
    		} catch (RemoteException e){
    			if(!start()){    			
    				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error during parser model initialization", e);
    				StatusManager.getManager().handle(status, StatusManager.LOG);
    			}
    		}
    		
    		// is still running in memory
    		if (!alive){
    			start();
    		}    		
    	} else { // load models first time
    		start();
    	}
    	
    	// parsing
		parsedText = parse(originalText); 
    	
    	// creating tree objects
        return parsedText;
    }   
    
	/**
	 * Is still running?
	 * 
	 * @return
	 */
	public static boolean isReady(){
		try {
			return parser.alive();
		} catch (NullPointerException e) {
			// parser is not initialized
			return false;
		} catch (RemoteException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Parser is not responding", e);
			StatusManager.getManager().handle(status, StatusManager.LOG);
			return false;
		}
	}
	
	/**
	 * Was running?
	 * 
	 * @return
	 */
	public static boolean isInicialized(){
		return runs;
	}
	
	
	public static synchronized boolean start () {
    	String settingsFile = "";
    	String modelFile = "";
    	
    	Bundle bundle = Platform.getBundle("reprotool.tools.dbparser");
		// bundle not found - package damaged
		if(bundle == null) {
			System.err.println("Bundle \"reprotool.tools.dbparser\" not found!");
			return false;
		}
    	URL modelFileURL = bundle.getEntry("data/wsj-02-21.obj.gz");
    	URL settingsFileURL = bundle.getEntry("data/collins.properties");
	    try {
	    	modelFile = new File(FileLocator.resolve(modelFileURL).toURI()).toString();
			settingsFile = new File(FileLocator.resolve(settingsFileURL).toURI()).toString();
		} catch (URISyntaxException e1) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error during locating parser files", e1);
			StatusManager.getManager().handle(status, StatusManager.LOG);
		} catch (IOException e1) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error during accessing parser files", e1);
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}

    	try {
			Settings.load(settingsFile);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Parser error during loading settings file", e);
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}		

    	
    	for (int i = 0; i < max_model_reloads ; i++) {
    		if(runs == false) {
				try {
					parser = new danbikel.parser.Parser(modelFile);
					runs = true;			
				} catch (Exception e) {
					try {
						parser.die(true);
					} catch (RemoteException e2) {
						// just for case, no need to report again
						runs = false;
					}
					// for benchmark purpose - getting manager can fail outside plugins 					
					try {
						IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Parser error during loading model file", e);
						StatusManager.getManager().handle(status, StatusManager.LOG);
					} catch (AssertionFailedException e1) {}
				}
    		}
    	}
		
		return runs;		
	}

	
	/**
	 * Main parsing function
	 * 
	 * @param originalText test to parse
	 * @return parsed string
	 */
	private static String parse(String originalText) {
    	Sexp result = null;

    	// remove bad parenthesis
    	originalText = originalText.replaceAll("\\(\\( \\(NN\\)\\)", "");
    	originalText = originalText.replaceAll("\\(\\) \\(NN\\)\\)", "");
    
		// parsing sentence
		try {
			result = parser.parse(Sexp.read(originalText).list());
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Parser error during parsing sentence", e);
			StatusManager.getManager().handle(status, StatusManager.LOG);
			return "";
		}
		
		// internal parser error
		if (result == null)
			return "";
		else
			return result.toString();	
	}
	
 	/**
	 * Parse trees of each sentence
	 *
	 * @param parsedText Result of this.getString method
	 * @return SentenceNode parsed_tree 
	 */	
    
    public static Sentence parseSentence(String parsedText) {	
    	// root node    	
		LingFactory  factory = LingFactory.eINSTANCE;
		SentenceNode rootNode = factory.createSentenceNode();
		Sentence sentence = factory.createSentence();
		
    	if (parsedText.isEmpty()){
    		sentence.setSentenceTree(rootNode);
	    	return sentence;
    	}

    	Word curWord = null;
    	ParseTreeNode curNode = rootNode;
				
    	// removing head S (sentence) node - it is already root node
    	if((parsedText.startsWith("(S") || parsedText.startsWith("(X")) && parsedText.endsWith(")")) {
    		parsedText = parsedText.substring(2, parsedText.length()-1).trim();
    	}    	
    	
		// string preparation
		//parsedText = Pattern.compile("[(]").matcher(parsedText).replaceAll("( ");
    	parsedText = Pattern.compile("[)]").matcher(parsedText).replaceAll(" )");	
		// whitespaces cleanup
		parsedText = Pattern.compile("[\\s]+").matcher(parsedText).replaceAll(" ");
		
		boolean atWord = false;
	    for (String symbol : parsedText.split("\\s")) {
	    	
	    	if(symbol.startsWith("(")) { // start node
	    		symbol = symbol.substring(1).toUpperCase();
	    		//NodeType node = NodeType.fromString(symbol);
	    		
	    		// converting to ENUM
	    		NodeType node = NodeType.get(symbol);
	    		if(node == null){
	    			if (symbol.startsWith("S")) 
	    				node = NodeType.S;
	    			else
	    				node = NodeType.X;
	    		}

	    		switch(node){
	    		case NP:
	    			SentenceNode nounPhrase = factory.createSentenceNode();
	    			nounPhrase.setType(SentenceType.NOUN_PHRASE);
	    			curNode.getChildren().add(nounPhrase);
	    			nounPhrase.setParent(curNode);
	    			curNode = nounPhrase;
	    			break;
	    		case VP:
	    			SentenceNode verbPhrase = factory.createSentenceNode();
	    			verbPhrase.setType(SentenceType.VERB_PHRASE);
	    			curNode.getChildren().add(verbPhrase);
	    			verbPhrase.setParent(curNode);
	    			curNode = verbPhrase;
	    			break;	
	    		case PP:
	    			SentenceNode prepositionalPhrase = factory.createSentenceNode();
	    			prepositionalPhrase.setType(SentenceType.PREPOSITION_PHRASE);
	    			curNode.getChildren().add(prepositionalPhrase);
	    			prepositionalPhrase.setParent(curNode);
	    			curNode = prepositionalPhrase;
	    			break;
	    			
	    		case S:
	    			// new sentence approaching
	    			SentenceNode sentencePhrase = factory.createSentenceNode();
	    			sentencePhrase.setType(SentenceType.SENTENCE_PHRASE);
	    			curNode.getChildren().add(sentencePhrase);
	    			sentencePhrase.setParent(curNode);
	    			curNode = sentencePhrase;
	    			break;	   			
	    		case ADJP:
	    			// Adjective Phrase - usually within VP or NP - stay in parent node?
	    			SentenceNode adjectivePhrase = factory.createSentenceNode();
	    			adjectivePhrase.setType(SentenceType.ADJECTIVE_PHRASE);
	    			curNode.getChildren().add(adjectivePhrase);
	    			adjectivePhrase.setParent(curNode);
	    			curNode = adjectivePhrase;
	    			break;
	    		case QP:
	    			// Quantifier Phrase - within NP - stay in parent node
	    			break;
	    		case ADVP:
	    			// Adverb Phrase - stay in parent node
	    			break;	    			
	    		case FRAG:
	    			// sentence fragment
	    			break;
	    		case PRN:
	    			// parenthetical
	    			break;
	    			
				default:
					// preparation for new WORD (at POS)
					curWord = factory.createWord();
					// set POS of word
					try {
						curWord.setPOS(POSType.get(symbol));
					} catch (NullPointerException e) {
						curWord.setPOS(POSType.UNDEFINED);
					}

					// there are no parent roots -> finish sentence
					if (curNode == null) {
						break;
					}
					// text will be rewrited
					curWord.setText(symbol);
					curWord.setParent(curNode);
					curNode.getChildren().add(curWord);
					sentence.getWords().add(curWord);
					atWord = true;
					break;
	    		}
	    	} else if(symbol.startsWith(")")) { // end node
	    		
	    		if(!atWord){	//    			
	    			curNode = curNode.getParent();
	    			//there are no parent roots -> finish sentence
	    			if(curNode == null) {
	    				break;
	    			}
	    		} else {
	    			atWord = false;
	    		}
	    	} else { // parsing WORD
	    		if(symbol.equals("X") && curWord.getPOS() == POSType.UNDEFINED)
	    			curNode.getChildren().remove(curWord);
	    		else {
	    			curWord.setText(symbol);
	    			curWord.setInterpunction(Pattern.matches("[,.!?\\-\"';]", symbol));
	    			// finding digits - all from Label1 to 99IMG
	    			curWord.setNumeral(Pattern.matches("([a-zA-Z]*\\d+)|(\\d+[a-zA-Z]*)", symbol));
	    		}
	    	}  		
	    }
	    	
	    // connect sentence and tree
	    sentence.setSentenceTree(rootNode);
	    
	    // verification
	    System.out.println("ParsedTree: " + treeToString(rootNode));
	    
    	return sentence;
    }
    
	/**
	 * Parse trees of each sentence
	 *
	 * @param parsedText Result of this.getString method
	 * @return SentenceNode parsed_tree 
	 */	
    public static SentenceNode parseTree(String parsedText) {
    	Sentence sentence = Parser.parseSentence(parsedText);
    	return sentence.getSentenceTree();    	
    }
    
    /**
     * Function for converting tree to string
     * mainly for verification purpose
     * 
     * @param rootNode input tree
     * @return output string in parser output string format
     */
    public static String treeToString(SentenceNode rootNode) {
    	// result
    	String result = "";
    	
    	if (rootNode != null) {
    		result += " (" + rootNode.getType().getLiteral() + " ";
    		for(ParseTreeNode node : rootNode.getChildren()) {
    			if (node instanceof SentenceNode)
    				result += treeToString((SentenceNode)node);
    			else 
    				result += "(" + ((Word)node).getPOS().getLiteral() + " " + ((Word)node).getText() + ") ";
    		}
    		result += ") ";
    	}
    	
    	return result.trim();
    }
}
