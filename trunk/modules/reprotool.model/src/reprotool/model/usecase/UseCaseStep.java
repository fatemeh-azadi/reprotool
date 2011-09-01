/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package reprotool.model.usecase;

import org.eclipse.emf.common.util.EList;

import reprotool.model.linguistic.action.Action;
import reprotool.model.linguistic.actionpart.Text;
import reprotool.model.linguistic.parsetree.SentenceNode;
import reprotool.model.swproj.ReqCover;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Use Case Step</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link reprotool.model.usecase.UseCaseStep#getParsedSentence <em>Parsed Sentence</em>}</li>
 *   <li>{@link reprotool.model.usecase.UseCaseStep#getID <em>ID</em>}</li>
 *   <li>{@link reprotool.model.usecase.UseCaseStep#getSentenceNodes <em>Sentence Nodes</em>}</li>
 *   <li>{@link reprotool.model.usecase.UseCaseStep#getExtensions <em>Extensions</em>}</li>
 *   <li>{@link reprotool.model.usecase.UseCaseStep#getVariations <em>Variations</em>}</li>
 *   <li>{@link reprotool.model.usecase.UseCaseStep#getAction <em>Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep()
 * @model
 * @generated
 */
public interface UseCaseStep extends ReqCover {
	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getLabel();

	/**
	 * Returns the value of the '<em><b>Parsed Sentence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parsed Sentence</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parsed Sentence</em>' containment reference.
	 * @see #setParsedSentence(SentenceNode)
	 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep_ParsedSentence()
	 * @model containment="true"
	 * @generated
	 */
	SentenceNode getParsedSentence();

	/**
	 * Sets the value of the '{@link reprotool.model.usecase.UseCaseStep#getParsedSentence <em>Parsed Sentence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parsed Sentence</em>' containment reference.
	 * @see #getParsedSentence()
	 * @generated
	 */
	void setParsedSentence(SentenceNode value);

	/**
	 * Returns the value of the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ID</em>' attribute.
	 * @see #setID(String)
	 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep_ID()
	 * @model id="true"
	 * @generated
	 */
	String getID();

	/**
	 * Sets the value of the '{@link reprotool.model.usecase.UseCaseStep#getID <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ID</em>' attribute.
	 * @see #getID()
	 * @generated
	 */
	void setID(String value);

	/**
	 * Returns the value of the '<em><b>Sentence Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link reprotool.model.linguistic.actionpart.Text}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sentence Nodes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sentence Nodes</em>' containment reference list.
	 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep_SentenceNodes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Text> getSentenceNodes();

	/**
	 * Returns the value of the '<em><b>Extensions</b></em>' containment reference list.
	 * The list contents are of type {@link reprotool.model.usecase.Guard}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extensions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extensions</em>' containment reference list.
	 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep_Extensions()
	 * @model containment="true"
	 * @generated
	 */
	EList<Guard> getExtensions();

	/**
	 * Returns the value of the '<em><b>Variations</b></em>' containment reference list.
	 * The list contents are of type {@link reprotool.model.usecase.Guard}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variations</em>' containment reference list.
	 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep_Variations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Guard> getVariations();

	/**
	 * Returns the value of the '<em><b>Action</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Action</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Action</em>' containment reference.
	 * @see #setAction(Action)
	 * @see reprotool.model.usecase.UsecasePackage#getUseCaseStep_Action()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Action getAction();

	/**
	 * Sets the value of the '{@link reprotool.model.usecase.UseCaseStep#getAction <em>Action</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Action</em>' containment reference.
	 * @see #getAction()
	 * @generated
	 */
	void setAction(Action value);

} // UseCaseStep