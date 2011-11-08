package reprotool.fm.nusmv.commands;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.SaveOptions.Builder;

import reprotool.fm.nusmv.lang.NuSmvInputLanguageStandaloneSetup;
import reprotool.fm.nusmv.lang.nuSmvInputLanguage.FairnessExpression;
import reprotool.fm.nusmv.lang.nuSmvInputLanguage.FormalParameter;
import reprotool.fm.nusmv.lang.nuSmvInputLanguage.InitConstraint;
import reprotool.fm.nusmv.lang.nuSmvInputLanguage.Model;
import reprotool.fm.nusmv.lang.nuSmvInputLanguage.Module;
import reprotool.fm.nusmv.lang.nuSmvInputLanguage.NuSmvInputLanguageFactory;
import reprotool.model.swproj.SoftwareProject;

public class SwprojToSMVXtextExample implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if( ! (sel instanceof TreeSelection) )
			return null;
		
		TreeSelection tsel = (TreeSelection) sel;
		IFile ifile = (IFile) tsel.getFirstElement();
		final ResourceSet rs = new ResourceSetImpl();
		
		// Converting IFile to URI: see http://wiki.eclipse.org/index.php/EMF/FAQ#How_do_I_map_between_an_EMF_Resource_and_an_Eclipse_IFile.3F
		final URI uri = URI.createPlatformResourceURI(ifile.getFullPath().toString(), true);
		final Resource resource = rs.getResource(uri, true);
		
		EObject rootEObj = resource.getContents().get(0);
		
		if( ! (rootEObj instanceof SoftwareProject) ) {
			System.out.println("NOT a SWPROJ : " + rootEObj);
			return null;
		}

		SoftwareProject swproj = (SoftwareProject) rootEObj;
		System.out.println("FOUND SWPROJ : " + swproj);
		
		String fileName = CommonPlugin.resolve(uri).path() + ".nusmv";
		System.out.println("Will be saved to : " + fileName);
		
		// creating an instance of the generated NuSMV model (generated by Xtext using a grammar)
		// -----------------------------------------------
		NuSmvInputLanguageFactory factory = NuSmvInputLanguageFactory.eINSTANCE;
		Model nusmvModel = factory.createModel();
		
		// example main MODULE
		Module mainModule = factory.createModule();
		mainModule.setName("main");	
		nusmvModel.getModules().add(mainModule);
		
		// example other MODULE
		Module otherModule = factory.createModule();
		otherModule.setName("other");	
		nusmvModel.getModules().add(otherModule);
		// some params
		FormalParameter formalParam = factory.createFormalParameter();
		formalParam.setId("someParam");
		otherModule.getParams().add(formalParam);
		
		// example FAIRNESS
		FairnessExpression fairness = factory.createFairnessExpression();
		fairness.setFairnessExpr("p=p1");
		fairness.setSemicolon(true);
		mainModule.getModuleElement().add(fairness);

		// example INIT
		InitConstraint initConstraint = factory.createInitConstraint();
		initConstraint.setInitExpression("nieco=12");
		mainModule.getModuleElement().add(initConstraint);
		
		// serialization of the model
		// -----------------------------------------------
		NuSmvInputLanguageStandaloneSetup.doSetup(); // activates the correct parser/serializer
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource res = resourceSet.createResource(URI.createFileURI(fileName));
		
		res.getContents().add(nusmvModel);

		try {
			// we need to setup options that enable formatting of the result code
			Builder builder = SaveOptions.newBuilder();
			builder.format();
			
			// serializing the code into a file and formatting
			res.save( builder.getOptions().toOptionsMap() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub
	}

}