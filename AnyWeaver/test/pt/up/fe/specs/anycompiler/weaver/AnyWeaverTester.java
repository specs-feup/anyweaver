package pt.up.fe.specs.anycompiler.weaver;


import org.lara.interpreter.tester.WeaverTester;

public class AnyWeaverTester extends WeaverTester<AnyWeaver, AnyWeaverTester> {

    public AnyWeaverTester(String basePackage) {
        super(AnyWeaver.class, basePackage);

        // Set custom settings
        //getCustomData().
    }
}
