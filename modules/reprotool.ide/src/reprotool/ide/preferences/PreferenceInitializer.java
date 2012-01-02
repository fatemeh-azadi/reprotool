package reprotool.ide.preferences;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import reprotool.ide.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		store.setDefault(PreferenceConstants.P_STRING, "Default value");

		// get path /reprotool
		String rootPath = new java.io.File(getRootPath()).getParentFile().getParent();

		store.setDefault(PreferenceConstants.EXTERNAL_WORDNET, false);

		store.setDefault(PreferenceConstants.MODEL_LOC, rootPath);
		store.setDefault(PreferenceConstants.MXPOST_MODEL, rootPath + File.separator + "modules" + File.separator
				+ "reprotool.tools.mxpost" + File.separator + "data" + File.separator + "tagger.project");
		store.setDefault(PreferenceConstants.WORDNET_DICT, rootPath + File.separator + "modules" + File.separator
				+ "WordNet-3.0" + File.separator + "dict");

		// parser settings
		store.setDefault(PreferenceConstants.PARSER_MODEL, rootPath + File.separator + "modules" + File.separator
				+ "reprotool.tools.dbparser" + File.separator + "data" + File.separator + "wsj-02-21.obj.gz");
		store.setDefault(PreferenceConstants.PARSER_SETTINGS, rootPath + File.separator + "modules" + File.separator
				+ "reprotool.tools.dbparser" + File.separator + "data" + File.separator + "collins.properties");
		// lemmatizer settings
		store.setDefault(PreferenceConstants.LEMMATIZER_MODEL, rootPath + File.separator + "modules" + File.separator
				+ "reprotool.tools.anna" + File.separator + "data" + File.separator + "lemma-eng.model");
	}
	
	/**
	 * Returns the reprotool.ide directory
	 *
	 * @return the reprotool.ide directory
	 */
	public static String getRootPath() {
		// URL to the root ("/") of the plugin-path
		URL pluginURL = Activator.getDefault().getBundle().getEntry("/");
		// resolving URL
        try {
        	pluginURL = FileLocator.toFileURL(pluginURL);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
		return pluginURL.getPath();
	}	

}
