package mobi.mofokom.javax.slee.annotations.examples.sbb;

import mobi.mofokom.javax.slee.annotation.CMPField;
import javax.slee.facilities.TimerID;
import mobi.mofokom.javax.slee.annotation.Sbb;

@Sbb(name = "ExistingSbbWithNewCmpField", vendor = "ISV1", version = "1.0")
public abstract class ExistingSbbWithNewCmpField extends BaseSbb {

    @CMPField
    public TimerID timer2;

}
