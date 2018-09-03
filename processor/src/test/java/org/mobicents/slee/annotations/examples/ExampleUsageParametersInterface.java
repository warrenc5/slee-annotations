
package org.mobicents.slee.annotations.examples;

import javax.slee.annotation.UsageParameter;
import javax.slee.annotation.UsageParametersInterface;


@UsageParametersInterface
public interface ExampleUsageParametersInterface {

	public void incrementX(long i);
	
	@UsageParameter(notificationsEnabled=true)
	public void incrementY(long i);

	public void sampleWastedTime(long s);
	
	
}
