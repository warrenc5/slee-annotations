package mobi.mofokom.javax.slee.annotations.examples.sbb;

import mobi.mofokom.javax.slee.annotation.CMPField;
import mobi.mofokom.javax.slee.annotation.Sbb;

@Sbb(name = "SecondExampleAnnotatedSbb", vendor = "ISV1", version = "1.0", alias = "SecondSbb")
public abstract class CMPSbb2 {

    @CMPField
    public String timer;

}
