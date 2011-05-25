package reprotool.ide.adapter;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import reprotool.model.usecase.UseCaseStep;

public class UseCaseStepPropertySource implements IPropertySource {
	private static final String ID_LABEL = "label";
	private static final String ID_SENTENCE = "sentence";
	private static final String ID_ACTION_TYPE = "actionType";
	private static final String ID_TOKEN = "token";
	
	private UseCaseStep step;
	private IPropertyDescriptor[] propertyDescriptors;
	
	public UseCaseStepPropertySource(UseCaseStep step) {
		this.step = step;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
            IPropertyDescriptor label = new PropertyDescriptor(ID_LABEL, "Label");
            IPropertyDescriptor sentence = new PropertyDescriptor(ID_SENTENCE, "Sentence");
            IPropertyDescriptor actionType = new PropertyDescriptor(ID_ACTION_TYPE, "Action type");
            IPropertyDescriptor token = new PropertyDescriptor(ID_TOKEN, "Token");
            propertyDescriptors = new IPropertyDescriptor[] { label, sentence, actionType, token };
	    }
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(ID_ACTION_TYPE)) {
			// TODO
			return "internal";
		} else if (id.equals(ID_SENTENCE)) {
			return step.getSentence();
		} else if (id.equals(ID_LABEL)) {
			return step.getLabel();
		} else if (id.equals(ID_TOKEN)) {
			return "#tokenText";
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}

}
