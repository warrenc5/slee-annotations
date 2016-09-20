package mofokom.slee.aspect;

public privileged aspect DebugAspect {
    pointcut myClass(): within(*) ;
    pointcut myConstructor(): myClass() && execution(new(..));
    pointcut myMethod(): myClass() && execution(* *(..));
    pointcut myField(): myClass() && (get(* *.*) || set(* *.*));

    before (): myConstructor() {
        System.err.println("**** " + thisJoinPointStaticPart.getSignature());
    }
    after(): myConstructor() {
        System.err.println("****" + thisJoinPointStaticPart.getSignature());
    }

    before (): myMethod() {
        System.err.println("****" + thisJoinPointStaticPart.getSignature());
    }
    after(): myMethod() {
        System.err.println("****" + thisJoinPointStaticPart.getSignature());
    }
    after(): myField() {
        System.err.println("****" + thisJoinPointStaticPart.getSignature());
    }

}
        