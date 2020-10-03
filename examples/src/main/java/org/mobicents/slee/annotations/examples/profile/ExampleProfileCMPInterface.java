/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.annotations.examples.profile;

import javax.slee.annotation.ProfileCMPField;
import javax.slee.profile.Profile;

/**
 *
 * @author wozza
 */
public interface ExampleProfileCMPInterface extends Profile {
    
	@ProfileCMPField
	public String getX();
	public void setX(String x);

	@ProfileCMPField
	public String getAnother1();
	public void setAnother1(String x);

}
