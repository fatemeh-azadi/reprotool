package reprotool.ide.editors;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import reprotool.ide.adapter.UseCaseContentOutlinePage;
import reprotool.ide.commands.ClipboardHandler;
import reprotool.model.usecase.Scenario;
import reprotool.model.usecase.UseCase;
import reprotool.model.usecase.UseCaseStep;
import reprotool.model.usecase.impl.UsecaseFactoryImpl;

public class UseCaseEditor extends EditorPart {

	public static final String ID = "cz.cuni.mff.reprotool.ide.editors.UseCaseEditor"; //$NON-NLS-1$

	// the usecase to edit
	private UseCase usecase = null;

	private ResourceSet resourceSet = null;

	private TreeViewer treeViewer = null;
	private SourceViewer sentenceText = null;

	private boolean dirty = false;

	private PropertySheetPage propertySheetPage;
	private UseCaseContentOutlinePage outlinePage;
	
	/**
	 * Stores the object in the clipboard and implements cut/copy/paste actions
	 */
	private Clipboard clipboard;
	public class Clipboard {
		boolean isVariation; // when pasting a Scenario, we have to remember if it was a variation or an extension
		EObject clipboardItem;
		public void doCut() {
			if (getSelectedObject() == null)
				return;
			EObject selected = (EObject)getSelectedObject();
			clipboardItem = EcoreUtil.copy(selected);
			if (selected instanceof Scenario)
				isVariation = ((UseCaseStep)selected.eContainer()).getVariation().contains(selected);
			runCommand("org.eclipse.ui.edit.delete");
		}
		
		public void doCopy() {
			if (getSelectedObject() == null)
				return;
			EObject selected = (EObject)getSelectedObject();
			if (selected instanceof Scenario)
				isVariation = ((UseCaseStep)selected.eContainer()).getVariation().contains(selected);
			clipboardItem = EcoreUtil.copy(selected);
		}
		
		public void doPaste() {
			if (clipboardItem == null)
				return;
			saveUndoState();
			
			EObject selected = (EObject)getSelectedObject();
			if (clipboardItem instanceof Scenario) {
				if (selected instanceof Scenario)
					insertScenario((UseCaseStep)selected.eContainer(), (Scenario)clipboardItem);
				else if (selected instanceof UseCaseStep)
					insertScenario((UseCaseStep)selected, (Scenario)clipboardItem);
			} else if (clipboardItem instanceof UseCaseStep) {
				if (selected instanceof Scenario)
					((Scenario)selected).getSteps().add((UseCaseStep)clipboardItem);
				else if (selected instanceof UseCaseStep) {
					Scenario parent = (Scenario)selected.eContainer();
					// add new step just after the selected step
					parent.getSteps().add(parent.getSteps().indexOf(selected)+1, (UseCaseStep)clipboardItem);
				}
			}
			
			clipboardItem = EcoreUtil.copy(clipboardItem);
			setDirty();
		}
		
		private void insertScenario(UseCaseStep step, Scenario scen) {
			if (isVariation)
				step.getVariation().add(scen);
			else
				step.getExtension().add(scen);
		}
	};
	public Clipboard getClipboard() {
		return clipboard;
	}

	// global actions for toolbar contribution
	private IAction undoAction;
	private IAction redoAction;
	private UndoStack undoStack;

	/**
	 * Maintains a stack of undo and redo states of the usecase.
	 */
	class UndoStack {
		/** Maximum amount of saved usecase states */
		static final int MAX_UNDO_DEPTH = 20;
		private LinkedList<UseCase> undoStack;
		private LinkedList<UseCase> redoStack;

		public UndoStack() {
			undoStack = new LinkedList<UseCase>();
			redoStack = new LinkedList<UseCase>();
			undoAction.setEnabled(false);
			redoAction.setEnabled(false);
		}

		/**
		 * Save the state of the usecase for undo operation
		 */
		public void snapshot() {
			undoStack.add(EcoreUtil.copy(usecase));
			if (undoStack.size() > MAX_UNDO_DEPTH)
				undoStack.removeFirst();
			redoStack.clear();
			undoAction.setEnabled(true);
			redoAction.setEnabled(false);
		}

		public void undo() {
			if (undoStack.isEmpty())
				return;
			redoStack.addFirst(usecase);
			usecase = EcoreUtil.copy(undoStack.removeLast());
			undoAction.setEnabled(!undoStack.isEmpty());
			redoAction.setEnabled(true);
			treeViewer.setInput(usecase);
			treeViewer.getTree().setFocus();
			setDirty();
		}

		public void redo() {
			if (redoStack.isEmpty())
				return;
			undoStack.add(usecase);
			usecase = EcoreUtil.copy(redoStack.removeFirst());
			redoAction.setEnabled(!redoStack.isEmpty());
			undoAction.setEnabled(true);
			treeViewer.setInput(usecase);
			treeViewer.getTree().setFocus();
			setDirty();
		}
	}

	/**
	 * Saves a snapshot of the usecase for undo operation - to be called before
	 * any change to the usecase
	 */
	public void saveUndoState() {
		undoStack.snapshot();
	}

	public static UseCaseEditor getActiveUseCaseEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();

		IEditorPart editor = page.getActiveEditor();
		if (editor != null && editor instanceof UseCaseEditor)
			return (UseCaseEditor) editor;
		else
			return null;
	}

	public UseCaseEditor() {
	}

	private void runCommand(String command) {
		IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(command, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public UseCase getEditedUseCase() {
		return usecase;
	}

	public UseCaseStep getSelectedStep() {
		Object element = getSelectedObject();

		if (element instanceof UseCaseStep)
			return (UseCaseStep) element;

		return null;
	}

	public Object getSelectedObject() {
		if (treeViewer.getSelection().isEmpty())
			return null;

		return ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
	}

	public void showSelectedStep() {
		if (treeViewer.getSelection().isEmpty())
			return;
		runCommand("commands.showStep");
	}
	
	public void deleteItem(Object item) {
		if (item instanceof UseCaseStep) {
			UseCaseStep step = (UseCaseStep)item;
			Scenario scen = (Scenario)step.eContainer();
			scen.getSteps().remove(step);
			checkEmptyScenario(scen);
		} else if (item instanceof Scenario) {
			UseCaseStep parent = (UseCaseStep)((EObject)item).eContainer();
			parent.getExtension().remove(item);
			parent.getVariation().remove(item);
		}
		sentenceText.setDocument(new Document());
		refresh();
	}

	private void checkEmptyScenario(Scenario scen) {
		// remove empty variation / extension
		if (scen.getSteps().isEmpty()) {
			if (scen.eContainer() instanceof UseCaseStep) {
				UseCaseStep parent = (UseCaseStep)scen.eContainer();
				parent.getExtension().remove(scen);
				parent.getVariation().remove(scen);
			}
		}
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		treeViewer = new TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		treeViewer.setAutoExpandLevel(4);

		Tree tree = treeViewer.getTree();
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100, -110);
		fd_tree.right = new FormAttachment(100, 0);
		fd_tree.top = new FormAttachment(0);
		fd_tree.left = new FormAttachment(0);
		tree.setLayoutData(fd_tree);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		

		initializeDragAndDrop();
		
		sentenceText = new SourceViewer(container, null, SWT.V_SCROLL);
		
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(100, -50);
		fd_text.right = new FormAttachment(100, 0);
		fd_text.top = new FormAttachment(100, -105);
		fd_text.left = new FormAttachment(0);
		sentenceText.getTextWidget().setLayoutData(fd_text);
		
		initializeSentenceEditor();
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				Object selection = getSelectedObject();
				if (selection == null)
					return;
				showSelectedStep();
				
				IDocument doc = new Document();
				if (selection instanceof UseCaseStep)
					doc.set(getSelectedStep().getSentence());
				else
					doc.set(((Scenario) getSelectedObject()).getDescription());
				
				AnnotationModel annotations = new AnnotationModel();
				annotations.connect(doc);
				sentenceText.setDocument(doc, annotations);

				// XXX for testing - add example annotation to the second word
				if (doc.get().split(" ").length > 1) {
					int start = doc.get().indexOf(" ")+1;
					int end = doc.get().indexOf(" ", start + 1);
					if (end == -1)
						end = doc.get().length();
					Annotation a = new Annotation(ANNOTATION_TYPE, false, "hidden text");
					annotations.addAnnotation(a, new Position(start, end - start));
				}
			}
		});

		TreeColumn trclmnLabel = new TreeColumn(tree, SWT.NONE);
		trclmnLabel.setWidth(100);
		trclmnLabel.setText("Label");

		TreeColumn trclmnStepText = new TreeColumn(tree, SWT.NONE);
		trclmnStepText.setWidth(300);
		trclmnStepText.setText("Step text");

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		TreeColumn trclmnType = treeViewerColumn.getColumn();
		trclmnType.setWidth(139);
		trclmnType.setText("Type");

		Composite composite = new Composite(container, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(sentenceText.getTextWidget(), 5);
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		composite.setLayout(null);

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.stepUp");
			}
		});
		button.setBounds(7, 7, 70, 29);
		button.setText("Up");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.stepDown");
			}
		});
		button_1.setBounds(83, 7, 70, 29);
		button_1.setText("Down");

		Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.newStep");
			}
		});
		button_2.setBounds(159, 7, 70, 29);
		button_2.setText("Add");

		Button button_3 = new Button(composite, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("org.eclipse.ui.edit.delete");
			}
		});
		button_3.setBounds(235, 7, 70, 29);
		button_3.setText("Delete");

		UseCaseStepTreeProvider provider = new UseCaseStepTreeProvider();
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(provider);

		TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
		treeColumn.setWidth(24);
		treeColumn.setText("*");

		Menu menu = new Menu(tree);
		tree.setMenu(menu);

		MenuItem mntmNewStep = new MenuItem(menu, SWT.NONE);
		mntmNewStep.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.newStep");
			}
		});
		mntmNewStep.setText("New step");

		MenuItem mntmDuplicateStep = new MenuItem(menu, SWT.NONE);
		mntmDuplicateStep.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.duplicateStep");
			}
		});
		mntmDuplicateStep.setText("Duplicate step");

		MenuItem mntmAddExtension = new MenuItem(menu, SWT.NONE);
		mntmAddExtension.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.addExtension");
			}
		});
		mntmAddExtension.setText("Add extension");

		MenuItem mntmAddVariation = new MenuItem(menu, SWT.NONE);
		mntmAddVariation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("commands.addVariation");
			}
		});
		mntmAddVariation.setText("Add variation");

		MenuItem mntmDeleteStep = new MenuItem(menu, SWT.NONE);
		mntmDeleteStep.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("org.eclipse.ui.edit.delete");
			}
		});
		mntmDeleteStep.setText("Delete step");
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmCut = new MenuItem(menu, SWT.NONE);
		mntmCut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("org.eclipse.ui.edit.cut");
			}
		});
		mntmCut.setText("Cut");
		
		MenuItem mntmCopy = new MenuItem(menu, SWT.NONE);
		mntmCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("org.eclipse.ui.edit.copy");
			}
		});
		mntmCopy.setText("Copy");
		
		MenuItem mntmPaste = new MenuItem(menu, SWT.NONE);
		mntmPaste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runCommand("org.eclipse.ui.edit.paste");
			}
		});
		mntmPaste.setText("Paste");

		clipboard = new Clipboard();
		
		initializeGlobalActions();
		undoStack = new UndoStack();
		IActionBars bars = getEditorSite().getActionBars();
		bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
		bars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
		
		IHandlerService hs = (IHandlerService) getSite().getService(IHandlerService.class);
		hs.activateHandler("org.eclipse.ui.edit.copy", new ClipboardHandler());
		hs.activateHandler("org.eclipse.ui.edit.cut", new ClipboardHandler());
		hs.activateHandler("org.eclipse.ui.edit.paste", new ClipboardHandler());

		setTitle();

		getSite().setSelectionProvider(treeViewer);
		treeViewer.setInput(usecase);
	}
	
	private void initializeDragAndDrop() {
		Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		int operations = DND.DROP_MOVE;

		final DragSource source = new DragSource(treeViewer.getTree(), operations);
		source.setTransfer(types);

		DropTarget target = new DropTarget(treeViewer.getTree(), operations);
		target.setTransfer(types);
		target.addDropListener(new ViewerDropAdapter(treeViewer) {
			@Override
			public void drop(DropTargetEvent event) {
				int location = determineLocation(event);
				EObject src = (EObject)getSelectedObject();
				EObject target = (EObject)determineTarget(event);
				switch (location) {
				case 1: // Dropped before the target
					if (src instanceof UseCaseStep && target instanceof UseCaseStep && !isTransitiveParent(src, target)) {
						saveUndoState();
						deleteItem(src);
						Scenario parent = (Scenario)((UseCaseStep)target).eContainer();
						parent.getSteps().add(parent.getSteps().indexOf(target), (UseCaseStep)src);
						setDirty();
					}
					break;
				case 2: // Dropped after the target
					if (src instanceof Scenario || isTransitiveParent(src, target))
						return;
					saveUndoState();
					deleteItem(src);
					if (target instanceof UseCaseStep) {
						Scenario parent = (Scenario)((UseCaseStep)target).eContainer();
						parent.getSteps().add(parent.getSteps().indexOf(target)+1, (UseCaseStep)src);
					} else if (target instanceof Scenario)
						((Scenario)target).getSteps().add(0, (UseCaseStep)src);
					setDirty();
					break;
				case 3: // Dropped on the target
					if (src == target || src.eContainer() == target || isTransitiveParent(src, target))
						return;
					if  (src instanceof Scenario && target instanceof UseCaseStep) {
						saveUndoState();
						Scenario scen = (Scenario)src;
						UseCaseStep parent = (UseCaseStep)scen.eContainer();
						if (parent.getVariation().contains(scen)) {
							((UseCaseStep)target).getVariation().add(scen);
							parent.getVariation().remove(scen);
						} else {
							((UseCaseStep)target).getExtension().add(scen);
							parent.getExtension().remove(scen);
						}
						setDirty();
					} else if (src instanceof UseCaseStep && target instanceof Scenario) {
						if (isTransitiveParent(src, target))
							return;
						saveUndoState();
						((Scenario)target).getSteps().add(EcoreUtil.copy((UseCaseStep)src));
						deleteItem(src);
						setDirty();
					}
					break;
				case 4: // Dropped into nothing
					if (src instanceof Scenario)
						return;
					saveUndoState();
					UseCaseStep step = (UseCaseStep)src;
					deleteItem(step);
					usecase.getMainScenario().getSteps().add(step);
					setDirty();
					break;
				}
			}

			@Override
			public boolean performDrop(Object data) {
				return true;
			}

			@Override
			public boolean validateDrop(Object target, int operation, TransferData transferType) {
				EObject src = (EObject)getSelectedObject();
				if (src instanceof Scenario && target instanceof Scenario || isTransitiveParent(src, (EObject)target))
					return false;
				return true;
			}
		});
	}
	
	private boolean isTransitiveParent(EObject source, EObject target) {
		EObject parent = target;
		while (parent != null) {
			if (parent == source)
				return true;
			parent = parent.eContainer();
		}
		return false;
	}

	private static final String ANNOTATION_TYPE = "reprotool.ide.tag";
	private static final String KEY_TAG_COLOR_PREF = "tagColor";
	private static final String KEY_TAG_HIGHLIGHT_PREF = "tagHighlight";
	private static final String KEY_TAG_TEXT_PREF = "tagText";
	
	private void initializeSentenceEditor() {
		StyledText styledText = sentenceText.getTextWidget();
		styledText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Object selected = getSelectedObject();
				if (selected instanceof UseCaseStep) {
					UseCaseStep step = (UseCaseStep)selected;
					if (step.getSentence() == null)
						step.setSentence("");
					if (! step.getSentence().equals(sentenceText.getDocument().get())) {
						saveUndoState();
						step.setSentence(sentenceText.getDocument().get());
					}
				} else if (selected instanceof Scenario) {
					Scenario scen = (Scenario)selected;
					if (scen.getDescription() == null)
						scen.setDescription("");
					if (! scen.getDescription().equals(sentenceText.getDocument().get())) {
						saveUndoState();
						scen.setDescription(sentenceText.getDocument().get());
					}
				}
				treeViewer.refresh();
			}
		});
		sentenceText.configure(new SourceViewerConfiguration());

		SourceViewerDecorationSupport svds = new SourceViewerDecorationSupport(sentenceText, null, null, EditorsPlugin.getDefault().getSharedTextColors());
		AnnotationPreference ap = new AnnotationPreference();
		ap.setColorPreferenceKey(KEY_TAG_COLOR_PREF);
		ap.setHighlightPreferenceKey(KEY_TAG_HIGHLIGHT_PREF);
		ap.setTextPreferenceKey(KEY_TAG_TEXT_PREF);
		ap.setAnnotationType(ANNOTATION_TYPE);
		svds.setAnnotationPreference(ap);
		svds.install(EditorsPlugin.getDefault().getPreferenceStore());
	}

	private void initializeGlobalActions() {
		undoAction = new Action() {
			@Override
			public void run() {
				undoStack.undo();
			}
		};
		redoAction = new Action() {
			@Override
			public void run() {
				undoStack.redo();
			}
		};
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	public void setSelection(Object selection) {
		treeViewer.setSelection(new TreeSelection(new TreePath(new Object[] { selection })));
		refreshPropertySheet();
	}

	public void refresh() {
		treeViewer.refresh();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Resource resource = resourceSet.getResource(URI.createURI(getInputFilePath()), true);
		try {
			final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
			saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
			resource.save(saveOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	private String getInputFilePath() {
		return ((FileEditorInput) getEditorInput()).getFile().getFullPath().toString();
	}

	private void loadUseCase() throws PartInitException {
		Resource resource = resourceSet.getResource(URI.createURI(getInputFilePath()), true);

		if (resource.getContents().isEmpty() || !(resource.getContents().get(0) instanceof UseCase))
			throw new PartInitException("File does not contain a use case");

		usecase = (UseCase) resource.getContents().get(0);
		
		if (usecase.getMainScenario() == null)
			usecase.setMainScenario(new UsecaseFactoryImpl().createScenario());
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Initialize the editor part
		setSite(site);
		setInputWithNotify(input);

		if (!(input instanceof FileEditorInput))
			throw new PartInitException("UseCaseEditor input must be FileEditorInput");

		resourceSet = new ResourceSetImpl();
		loadUseCase();
	}

	/**
	 * Displays the use case name in the editor tab title
	 */
	private void setTitle() {
		if (usecase.getName() == null || usecase.getName().equals(""))
			setPartName("Unnamed use case");
		else
			setPartName(usecase.getName());
	}

	private void setDirty(boolean state) {
		dirty = state;
		firePropertyChange(PROP_DIRTY);
		refresh();
	}

	public void setDirty() {
		setDirty(true);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	// this method is here only to save the propertySheetPage reference to allow
	// manual refresh of the property view (refreshPropertySheet() method)
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			if (propertySheetPage == null)
				propertySheetPage = new PropertySheetPage();
			return propertySheetPage;
		}
		
		if (IContentOutlinePage.class.equals(key)) {
			if (outlinePage  == null) {
				outlinePage = new UseCaseContentOutlinePage(this.usecase);
			}	
			return outlinePage;
		} 
		
		return super.getAdapter(key);
	}

	private void refreshPropertySheet() {
		if (propertySheetPage != null)
			propertySheetPage.refresh();
	}
	
	
}
