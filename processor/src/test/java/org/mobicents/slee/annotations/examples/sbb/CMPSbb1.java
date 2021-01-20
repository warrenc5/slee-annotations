package org.mobicents.slee.annotations.examples.sbb;

import javax.slee.annotation.CMPField;
import javax.slee.annotation.Sbb;
import javax.slee.facilities.TimerID;

@Sbb(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb", localInterface = SimpleExampleSbbLocalObject.class)

public abstract class CMPSbb1 {

    @CMPField
    public TimerID timer;
}
