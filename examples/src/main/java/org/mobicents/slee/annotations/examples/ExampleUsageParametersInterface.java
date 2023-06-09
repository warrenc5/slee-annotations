
package org.mobicents.slee.annotations.examples;

import mobi.mofokom.javax.slee.annotation.UsageParameter;
import mobi.mofokom.javax.slee.annotation.UsageParametersInterface;
import javax.slee.usage.SampleStatistics;


@UsageParametersInterface
public interface ExampleUsageParametersInterface {

	@UsageParameter(notificationsEnabled=true)
	public void incrementX(long i);
    public long getX();
	
	public void incrementY(long i);
    public long getY();

	@UsageParameter(notificationsEnabled=true)
	public void sampleWastedTime(long s);
    public SampleStatistics getWastedTime();
	
	
}
