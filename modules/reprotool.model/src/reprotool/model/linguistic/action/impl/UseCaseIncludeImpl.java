/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package reprotool.model.linguistic.action.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import reprotool.model.linguistic.action.ActionPackage;
import reprotool.model.linguistic.action.UseCaseInclude;

import reprotool.model.linguistic.actionpart.ActionPart;
import reprotool.model.linguistic.actionpart.ActionpartPackage;
import reprotool.model.linguistic.actionpart.Text;
import reprotool.model.usecase.UseCase;
import reprotool.model.linguistic.actionpart.UseCaseIncludePart;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Use Case Include</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link reprotool.model.linguistic.action.impl.UseCaseIncludeImpl#getText <em>Text</em>}</li>
 *   <li>{@link reprotool.model.linguistic.action.impl.UseCaseIncludeImpl#getIncludeTarget <em>Include Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UseCaseIncludeImpl extends EObjectImpl implements UseCaseInclude {
	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected Text text;
	/**
	 * The cached value of the '{@link #getIncludeTarget() <em>Include Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncludeTarget()
	 * @generated
	 * @ordered
	 */
	protected UseCase includeTarget;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UseCaseIncludeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActionPackage.Literals.USE_CASE_INCLUDE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Text getText() {
		if (text != null && text.eIsProxy()) {
			InternalEObject oldText = (InternalEObject)text;
			text = (Text)eResolveProxy(oldText);
			if (text != oldText) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActionPackage.USE_CASE_INCLUDE__TEXT, oldText, text));
			}
		}
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Text basicGetText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setText(Text newText) {
		Text oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActionPackage.USE_CASE_INCLUDE__TEXT, oldText, text));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UseCase getIncludeTarget() {
		return includeTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIncludeTarget(UseCase newIncludeTarget, NotificationChain msgs) {
		UseCase oldIncludeTarget = includeTarget;
		includeTarget = newIncludeTarget;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET, oldIncludeTarget, newIncludeTarget);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIncludeTarget(UseCase newIncludeTarget) {
		if (newIncludeTarget != includeTarget) {
			NotificationChain msgs = null;
			if (includeTarget != null)
				msgs = ((InternalEObject)includeTarget).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET, null, msgs);
			if (newIncludeTarget != null)
				msgs = ((InternalEObject)newIncludeTarget).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET, null, msgs);
			msgs = basicSetIncludeTarget(newIncludeTarget, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET, newIncludeTarget, newIncludeTarget));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET:
				return basicSetIncludeTarget(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActionPackage.USE_CASE_INCLUDE__TEXT:
				if (resolve) return getText();
				return basicGetText();
			case ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET:
				return getIncludeTarget();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ActionPackage.USE_CASE_INCLUDE__TEXT:
				setText((Text)newValue);
				return;
			case ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET:
				setIncludeTarget((UseCase)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ActionPackage.USE_CASE_INCLUDE__TEXT:
				setText((Text)null);
				return;
			case ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET:
				setIncludeTarget((UseCase)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ActionPackage.USE_CASE_INCLUDE__TEXT:
				return text != null;
			case ActionPackage.USE_CASE_INCLUDE__INCLUDE_TARGET:
				return includeTarget != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ActionPart.class) {
			switch (derivedFeatureID) {
				case ActionPackage.USE_CASE_INCLUDE__TEXT: return ActionpartPackage.ACTION_PART__TEXT;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ActionPart.class) {
			switch (baseFeatureID) {
				case ActionpartPackage.ACTION_PART__TEXT: return ActionPackage.USE_CASE_INCLUDE__TEXT;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //UseCaseIncludeImpl