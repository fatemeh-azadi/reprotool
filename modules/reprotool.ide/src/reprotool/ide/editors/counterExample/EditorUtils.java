package reprotool.ide.editors.counterExample;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import reprotool.ide.editors.project.UseCaseEditorInput;
import reprotool.ide.editors.usecase.UsecaseEMFEditor;
import reprotool.model.usecase.Scenario;
import reprotool.model.usecase.UseCase;
import reprotool.model.usecase.UseCaseStep;

public class EditorUtils {
	
	static public void openUseCaseEditor(IWorkbenchPage page, UseCase useCase, UseCaseStep ucStep,
			IEditorInput input, EditingDomain domain) {
		UseCaseEditorInput useCaseEditorInput = new UseCaseEditorInput(EcoreUtil.getURI(useCase), input, domain);

		IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
				.findEditor("org.eclipselabs.reprotool.ide.UseCaseEmfEditor");

		try {
			IEditorPart editor = page.openEditor(useCaseEditorInput, desc.getId());
			if (editor instanceof UsecaseEMFEditor) {
				UsecaseEMFEditor ucEditor = (UsecaseEMFEditor) editor;
				
				IEditorInput ucInput = ucEditor.getEditorInput();
				Assert.isTrue(ucInput instanceof UseCaseEditorInput);
				UseCaseEditorInput ucEditorInput = (UseCaseEditorInput) ucInput;

				EObject object = ucEditor.getEditingDomain().getResourceSet().getEObject(ucEditorInput.getUseCaseURI(), true);
				Assert.isTrue(object instanceof UseCase);
				UseCase editorUC = (UseCase) object;
				UseCaseStep editorStep = findUCStep(editorUC.getMainScenario(), ucStep.getLabel());
								
				ucEditor.setLTSSelection(new StructuredSelection(editorStep));
			}
		} catch (PartInitException e1) {
			e1.printStackTrace();
		}
	}

	private static UseCaseStep findUCStep(Scenario scenario, String label) {
		for (UseCaseStep step: scenario.getSteps()) {
			if ((step.getLabel() != null) && (step.getLabel().equals(label))) {
				return step;
			}
			
			if (!step.getExtensions().isEmpty()) {
				for (Scenario ext: step.getExtensions()) {
					UseCaseStep s = null;
					if ((s = findUCStep(ext, label)) != null) {
						return s;
					}
				}
			}
			
			if (!step.getVariations().isEmpty()) {
				for (Scenario var: step.getVariations()) {
					UseCaseStep s = null;
					if ((s = findUCStep(var, label)) != null) {
						return s;
					}
				}
			}
		}
		
		return null;
	}	

}