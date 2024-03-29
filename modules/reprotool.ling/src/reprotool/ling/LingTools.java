package reprotool.ling;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.statushandlers.StatusManager;

import reprotool.ling.analyser.Analyser;
import reprotool.ling.analyser.FindConstituent;
import reprotool.ling.analyser.MatchSentence;
import reprotool.ling.tools.Lemmatizer;
import reprotool.ling.tools.Parser;
import reprotool.ling.tools.Tagger;
import reprotool.ling.tools.Tokenizer;
import reprotool.model.usecase.UseCase;
import reprotool.model.usecase.UseCaseStep;

/**
 * @author ofiala
 * 
 */
public class LingTools {
	
	
	/**
	 * Ling tools pipeline
	 * 
	 * @param originalSentence
	 * @return parserSentence
	 */
	public static String parseLingSentence(String originalSentence) {
		
 		String tokenizedSentence = "";
		String taggedSentence = "";
		String parserSentence = "";
		
		// calling tokenizer 
		tokenizedSentence = Tokenizer.getTokens(originalSentence);
		// calling tagger 
		taggedSentence = Tagger.mxposToLisp(Tagger.getMXPOST(tokenizedSentence));
		// calling parser 
		parserSentence = Parser.getString(taggedSentence);
		
		return parserSentence;
	}	
	
	/**
	 * Runs all linguistic tools
	 * 
	 * @param originalSentence
	 * @return
	 */
	public static Sentence parseSentence(String originalSentence) {
		
 		String tokenizedSentence = "";
 		String lemmatizedSentence = "";
 		String taggedSentence = "";
		String parsedSentence = "";
		
		// calling tokenizer 
		tokenizedSentence = Tokenizer.getTokens(originalSentence.trim());
		System.out.println("Tokenizer: " + tokenizedSentence);
		
		// calling tagger 
		taggedSentence = Tagger.mxposToLisp(Tagger.getMXPOST(tokenizedSentence));
		System.out.println("Tagger: " + taggedSentence);
		
		// calling parser 
		parsedSentence = Parser.getString(taggedSentence);
		System.out.println("Parser: " + parsedSentence);
		
		// parsing into a Sentence object
		Sentence sentence = Parser.parseSentence(parsedSentence);
		
		// calling lemmatizer 
		sentence = Lemmatizer.getSentence(sentence);
		for(Word word : sentence.getWords()) {
			lemmatizedSentence += " " + word.getLemma();
			lemmatizedSentence = lemmatizedSentence.trim();
		}
		System.out.println("Lemmatizer: " + lemmatizedSentence);
 		
	    // set passive - better to do here
	    sentence.setPassive(FindConstituent.getPassive(sentence));   
		System.out.println("isPassive: " + sentence.isPassive());
	    
		return sentence;
	}
	
	/**
	 * Runs all linguistic tools
	 * 
	 * @param editingDomain
	 * @param ucs
	 * @return CompoundCommand all commands at model objects
	 */
	public static CompoundCommand analyseUseCaseStep(EditingDomain editingDomain, UseCaseStep ucs) {
		// gets UseCaseStep string 
		String sentenceString = ucs.getContent();
		// empty sentence - some problems?
		if(sentenceString.isEmpty()) {
			return new CompoundCommand();
		}
		
		// gets sentence object
		//Sentence sentence = LingTools.parseSentence(sentenceString);
		
		MessageConsoleStream consoleOut = Activator.getDefault().findConsole().newMessageStream();
		consoleOut.println("[Ling]");
		consoleOut.println("[Ling] " + "Starting liguistic analysis for sentence \"" +
				sentenceString + "\"");
		
		LingJob job = new LingJob("Linguistics analyse", sentenceString);
		job.schedule();	

		while(job.getState() != Job.NONE){
			try {
				//Thread.currentThread();
				Thread.sleep(100);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		System.out.println("LingJob final state: " + job.getState());
		consoleOut.println("[Ling] " + "Analysis finished.");
		
		Sentence sentence = job.getSentence();
		if(sentence.getSentenceString() != null && !sentenceString.equals(sentence.getSentenceString()))
			sentenceString = sentence.getSentenceString();
		
		try {
			// match sentence object to original string
			if(!MatchSentence.matchSentence(sentenceString, sentence)){
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error during matching UseCaseStep.content to Sentence object", null);
				StatusManager.getManager().handle(status, StatusManager.LOG);
			}
			// detect all striped sentence regions like "name" 
			MatchSentence.matchSentenceRegions(sentenceString, sentence);
		} catch (NullPointerException e){
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "NullPointerException during matching UseCaseStep.content to Sentence object and detecting regions", e);
			StatusManager.getManager().handle(status, StatusManager.LOG);
		}
		CompoundCommand command = Analyser.analyseTree(editingDomain, ucs, sentence);
		System.out.println("Linguistics - created command - END.");
		
		return command;
	}	
	
	/**
	 * Runs all linguistic tools for all UseCaseSteps in UseCase
	 * 
	 * @param editingDomain
	 * @param ucs
	 * @return CompoundCommand all commands at model objects
	 */
	public static CompoundCommand analyseUseCase(EditingDomain editingDomain, UseCase uc) {
		System.out.println("Linguistic analysis of UseCase started.");
		// gets UseCaseSteps 
		EList<UseCaseStep> lucs = uc.getAllUseCaseStepsShortcut();
		
		// empty list - some problems?
		if(lucs.isEmpty()) {
			return new CompoundCommand();
		}
		
		CompoundCommand command = new CompoundCommand();
		for(UseCaseStep ucs : lucs) {
			if(!ucs.getContent().isEmpty()) {
				command.append(analyseUseCaseStep(editingDomain, ucs));	
			}
		}	
		return command;
	}
	
	// job inicialization
	public static void initJob() {
		//nic tam neni
		//System.out.println(Activator.getContext()..toString());
		
		Job job = new Job("Linguistics tools initialization") {
		    @Override
		    public IStatus run(IProgressMonitor monitor) {
		    	setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
		    	try{
		    		String initSentence = "Inicialization sentence.";
		    		// local console initialization
					MessageConsoleStream consoleOut = Activator.getDefault().findConsole().newMessageStream();
					consoleOut.println("[Ling] Initialization started.");
		    		monitor.beginTask("External tools", 100);
		    		monitor.worked(1);	
		    		
		    		// tools subtasks
		    		monitor.subTask("Anna lemmatizer initialization....");	
		    		consoleOut.println("[Ling] Anna lemmatizer initialization...");
		    		Lemmatizer.start();
		    		monitor.worked(5);
		    		
		    		monitor.subTask("MXPost tagger initialization....");
		    		consoleOut.println("[Ling] MXPost tagger initialization...");
		    		Tagger.start();
		    		// memory for tagger init
		    		Tagger.getMXPOST(initSentence);
		    		monitor.worked(5);	
		    		
		    		monitor.subTask("Parser initialization....");
		    		consoleOut.println("[Ling] Dan Bikel Parser initialization...");
		    		monitor.worked(10);
		    		Parser.start();	
		    		
		    		consoleOut.println("[Ling] Analyzing initialization sentence...");
		    		LingJob job = new LingJob("Linguistics analyse initialization", "Initialization sentence.");
		    		job.schedule();	
		    		consoleOut.println("[Ling] Initialization finished.");
		    		
			    	if(monitor.isCanceled()){
			    		return Status.CANCEL_STATUS;
			    	}
			    	monitor.worked(100);
		    	} catch (Exception e){
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error during ling tools initialization", e);
					StatusManager.getManager().handle(status, StatusManager.LOG);		    		
		    	}
		    	finally{
		    		monitor.done();
		    	}
		    	return Status.OK_STATUS;
		    }
		};
		IAction showReportAction = new Action("Results") {
			public void run() {
				// all tools 
				MessageBox mb = new MessageBox( PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
				mb.setText("Linguistics tools initialization");
				mb.setMessage(
						"Tagger - inicialization: " + (Tagger.isInicialized() ? "OK" : "Error" ) + " | running: " + (Tagger.isRunning() ? "YES" : "NO" ) + "\n" +
						"Parser - inicialization: " + (Parser.isInicialized() ? "OK" : "Error" ) + " | running: " + (Parser.isReady() ? "OK" : "Error" ) + "\n" +
						"Lemmatizer - inicialization: " + (Lemmatizer.isInicialized() ? "OK" : "Error" ) + "\n"
						);
				mb.open();
			}
		};
		job.setProperty(IProgressConstants.ACTION_PROPERTY, showReportAction);		
		job.schedule();	
	}	

}

