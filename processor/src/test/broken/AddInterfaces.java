package mofokom.slee;

public class AddInterfaces {

    private int tally;
    private Trees trees;
    private TreeMaker make;
    private Name.Table names;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        trees = Trees.instance(env);
        Context context = ((JavacProcessingEnvironment) env).getContext();
        make = TreeMaker.instance(context);
        names = Name.Table.instance(context);
        tally = 0;
    }

    private void addInterfaces(RoundEnvironment roundEnv) {
        for (Element each : roundEnv.getRootElements()) {
            if (each.getKind() == ElementKind.CLASS) {
                com.sun.tools.javac.tree.JCTree tree = (Jcom.sun.tools.javac.tree.JCTree) trees.getTree(each);
                TreeTranslator visitor = new Inliner();
                tree.accept(visitor);
            }
        }
    }

}
