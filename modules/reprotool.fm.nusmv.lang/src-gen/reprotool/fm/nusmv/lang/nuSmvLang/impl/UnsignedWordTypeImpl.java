/**
 * <copyright>
 * </copyright>
 *
 */
package reprotool.fm.nusmv.lang.nuSmvLang.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import reprotool.fm.nusmv.lang.nuSmvLang.NuSmvLangPackage;
import reprotool.fm.nusmv.lang.nuSmvLang.UnsignedWordType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unsigned Word Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link reprotool.fm.nusmv.lang.nuSmvLang.impl.UnsignedWordTypeImpl#getUWordNumber <em>UWord Number</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnsignedWordTypeImpl extends SimpleTypeImpl implements UnsignedWordType
{
  /**
   * The default value of the '{@link #getUWordNumber() <em>UWord Number</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUWordNumber()
   * @generated
   * @ordered
   */
  protected static final String UWORD_NUMBER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUWordNumber() <em>UWord Number</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUWordNumber()
   * @generated
   * @ordered
   */
  protected String uWordNumber = UWORD_NUMBER_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected UnsignedWordTypeImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return NuSmvLangPackage.Literals.UNSIGNED_WORD_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUWordNumber()
  {
    return uWordNumber;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUWordNumber(String newUWordNumber)
  {
    String oldUWordNumber = uWordNumber;
    uWordNumber = newUWordNumber;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, NuSmvLangPackage.UNSIGNED_WORD_TYPE__UWORD_NUMBER, oldUWordNumber, uWordNumber));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case NuSmvLangPackage.UNSIGNED_WORD_TYPE__UWORD_NUMBER:
        return getUWordNumber();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case NuSmvLangPackage.UNSIGNED_WORD_TYPE__UWORD_NUMBER:
        setUWordNumber((String)newValue);
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
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case NuSmvLangPackage.UNSIGNED_WORD_TYPE__UWORD_NUMBER:
        setUWordNumber(UWORD_NUMBER_EDEFAULT);
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
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case NuSmvLangPackage.UNSIGNED_WORD_TYPE__UWORD_NUMBER:
        return UWORD_NUMBER_EDEFAULT == null ? uWordNumber != null : !UWORD_NUMBER_EDEFAULT.equals(uWordNumber);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (uWordNumber: ");
    result.append(uWordNumber);
    result.append(')');
    return result.toString();
  }

} //UnsignedWordTypeImpl