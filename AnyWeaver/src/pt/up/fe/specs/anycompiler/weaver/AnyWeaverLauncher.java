package pt.up.fe.specs.anycompiler.weaver;

import pt.up.fe.specs.lara.WeaverLauncher;
import pt.up.fe.specs.util.SpecsSystem;

public class AnyWeaverLauncher {

    public static void main(String[] args) {

        SpecsSystem.programStandardInit();

        var isSucess = new WeaverLauncher(new AnyWeaver()).launch(args);

    }
}
