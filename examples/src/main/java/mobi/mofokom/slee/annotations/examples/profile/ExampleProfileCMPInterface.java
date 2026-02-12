/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mobi.mofokom.slee.annotations.examples.profile;

import mobi.mofokom.javax.slee.annotation.ProfileCMPField;

/**
 *
 * @author wozza
 */
public interface ExampleProfileCMPInterface {
    
	@ProfileCMPField
	public String getX();
	public void setX(String x);

	@ProfileCMPField
	public String getAnother1();
	public void setAnother1(String x);

}
