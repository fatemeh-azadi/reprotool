package reprotool.ide.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;

import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import reprotool.ide.service.Service;
import reprotool.model.specification.UseCase;
import reprotool.model.specification.UseCaseStep;

public class UcStepView extends ViewPart {
	
	public static final String ID = "cz.cuni.mff.reprotool.ide.uc_step_view";
	
	public Label lblStepSentence = null;
	public Label lblActorDesc = null;
	
	public UcStepView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
        parent.setLayout(new FormLayout());
        
        Group grpStep = new Group(parent, SWT.NONE);
        grpStep.setText("Step");
        FillLayout fl_grpStep = new FillLayout(SWT.HORIZONTAL);
        fl_grpStep.marginWidth = 5;
        fl_grpStep.marginHeight = 5;
        grpStep.setLayout(fl_grpStep);
        FormData fd_grpStep = new FormData();
        fd_grpStep.left = new FormAttachment(0, 10);
        fd_grpStep.right = new FormAttachment(100, -10);
        fd_grpStep.top = new FormAttachment(0, 10);
        grpStep.setLayoutData(fd_grpStep);
        
        lblStepSentence = new Label(grpStep, SWT.WRAP);
        lblStepSentence.setText("No step selected");

        Group grpAnalysisResult = new Group(parent, SWT.NONE);
        grpAnalysisResult.setText("Analysis result");
        grpAnalysisResult.setLayout(new FillLayout(SWT.VERTICAL));
        FormData fd_grpAnalysisResult = new FormData();
        fd_grpAnalysisResult.top = new FormAttachment(grpStep, 10);
        fd_grpAnalysisResult.left = new FormAttachment(0, 10);
        fd_grpAnalysisResult.right = new FormAttachment(100, -10);
        grpAnalysisResult.setLayoutData(fd_grpAnalysisResult);
        
        Composite c_actiontype = new Composite(grpAnalysisResult, SWT.NONE);
        RowLayout rl_actiontype = new RowLayout(SWT.HORIZONTAL);
        rl_actiontype.spacing = 5;
        rl_actiontype.center = true;
        c_actiontype.setLayout(rl_actiontype);
        
        Label lblStepIsA = new Label(c_actiontype, SWT.NONE);
        lblStepIsA.setText("Step is a");
        Combo combo = new Combo(c_actiontype, SWT.NONE);
        combo.setEnabled(false);
        combo.setLayoutData(new RowData(150, 26));
        combo.setText("internal");
        Label lblAction = new Label(c_actiontype, SWT.NONE);
        lblAction.setText("action");
        
        Composite c_actor = new Composite(grpAnalysisResult, SWT.NONE);
        RowLayout rl_actor = new RowLayout(SWT.HORIZONTAL);
        rl_actor.marginHeight = 5;
        rl_actor.marginBottom = 0;
        rl_actor.center = true;
        rl_actor.fill = true;
        rl_actor.pack = false;
        rl_actor.spacing = 5;
        c_actor.setLayout(rl_actor);
        Label lblActor = new Label(c_actor, SWT.NONE);
        lblActor.setText("Primary actor:");
        lblActorDesc = new Label(c_actor, SWT.WRAP);
        lblActorDesc.setLayoutData(new RowData(16, SWT.DEFAULT));
        
        
        Group grpToken = new Group(parent, SWT.NONE);
        grpToken.setText("Token");
        FormData fd_grpToken = new FormData();
        fd_grpToken.top = new FormAttachment(grpAnalysisResult, 10);
        FillLayout fl_grpToken = new FillLayout(SWT.HORIZONTAL);
        fl_grpToken.marginWidth = 10;
        fl_grpToken.marginHeight = 10;
        grpToken.setLayout(fl_grpToken);
        fd_grpToken.left = new FormAttachment(0, 10);
        fd_grpToken.right = new FormAttachment(100, -10);
        grpToken.setLayoutData(fd_grpToken);

        Label lbltokentext = new Label(grpToken, SWT.WRAP);
        lbltokentext.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseDown(MouseEvent e) {
        		TokenWizard tw = new TokenWizard(parent.getShell(), SWT.NONE);
        		tw.open();
        	}
        });
        lbltokentext.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.NORMAL));
        lbltokentext.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
        lbltokentext.setText("#tokenText");
	}
	
	public void setStep(UseCaseStep s) {
		lblStepSentence.setText(s.getDesc());
		// TODO reference from UCStep to UseCase?
		for (UseCase u : Service.INSTANCE.getSoftwareProject().getUseCases()) {
			if (u.getUseCaseSteps().contains(s)) {
				lblActorDesc.setText(u.getPrimaryActor().getName());
				break;
			}
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
}