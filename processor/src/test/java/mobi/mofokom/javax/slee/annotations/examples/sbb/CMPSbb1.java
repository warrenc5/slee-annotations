package mobi.mofokom.javax.slee.annotations.examples.sbb;

import mobi.mofokom.javax.slee.annotation.CMPField;
import mobi.mofokom.javax.slee.annotation.Sbb;
import javax.slee.facilities.TimerID;
import org.mobicents.slee.annotations.examples.sbb.SimpleExampleSbbLocalObject;

@Sbb(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb", localInterface = SimpleExampleSbbLocalObject.class)

public abstract class CMPSbb1 {

    @CMPField
    public TimerID timer;

}
