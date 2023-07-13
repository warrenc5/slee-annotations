package mobi.mofokom.javax.slee.annotations.examples.sbb;

import mobi.mofokom.javax.slee.annotation.CMPField;
import mobi.mofokom.javax.slee.annotation.Sbb;
import javax.slee.facilities.TimerID;
import mobi.mofokom.javax.slee.annotation.SbbRef;
import org.mobicents.slee.annotations.examples.sbb.SimpleExampleSbbLocalObject;

@Sbb(name = "SimpleExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SimpleSbb", localInterface = SimpleExampleSbbLocalObject.class, sbbRefs = {
    @SbbRef(name = "SecondExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SecondSbb")
})

public abstract class CMPSbb1 extends BaseSbb {

    @CMPField
    public TimerID timer;

}
