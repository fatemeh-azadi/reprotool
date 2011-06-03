package reprotool.ide.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import reprotool.ide.Activator;
import reprotool.ling.wordnet.WordNet;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class LingToolsPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private StringFieldEditor fieldDict; 

	
	public LingToolsPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Linguistics tools settings");
	}
	

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.MODEL_LOC, 
				"&Model location:", getFieldEditorParent()));

		addField(new DirectoryFieldEditor(PreferenceConstants.MXPOST_MODEL, 
				"&MXPOS Tagger model:", getFieldEditorParent()));
		
		fieldDict = new DirectoryFieldEditor(PreferenceConstants.WORDNET_DICT, 
				"&WordNet dictionary:", getFieldEditorParent());
		addField(fieldDict);
	}

	
	protected void checkState() {
        super.checkState();
        if(fieldDict.getStringValue()!= null && !fieldDict.getStringValue().equals("")){
            if(WordNet.validateDictionary(fieldDict.getStringValue())){
            	setErrorMessage(null);
                setValid(true);            	
            } else {
                setErrorMessage("Folder must contain WordNet dictionary!");
                setValid(false);           	
            }
        }else{
              setErrorMessage("Folder name cannot be blank!");
              setValid(false);
        }
	}
	
	public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        if (event.getProperty().equals(FieldEditor.VALUE)) {
                  checkState();
        }        
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}