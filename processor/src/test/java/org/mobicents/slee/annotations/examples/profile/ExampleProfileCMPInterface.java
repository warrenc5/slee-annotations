/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.annotations.examples.profile;

import javax.slee.annotation.ProfileCMPField;

/**
 *
 * @author wozza
 */
public interface ExampleProfileCMPInterface {
    
	@ProfileCMPField
	public String getX();
	public void setX(String x);

	@ProfileCMPField
	public String getAnother();
	public void setAnother(String x);

}
