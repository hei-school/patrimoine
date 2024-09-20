package school.hei.patrimoine.compiler;

import school.hei.patrimoine.modele.Patrimoine;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class PatrimoineCompiler {
  public static Patrimoine stringCompiler(String className, String javaSource) throws Exception{

    String javaFile = "out/production/classes/" + className + ".java";
    String classOutputDir = "out/production/classes/";

    Files.write(Paths.get(javaFile), javaSource.getBytes());

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    int result = compiler.run(null, null, null, "-d", classOutputDir, javaFile);

    if (result != 0) {
      throw new RuntimeException("Compilation failed.");
    }

    File outputDir = new File(classOutputDir);
    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{outputDir.toURI().toURL()});
    Class<?> dynamicClass = Class.forName(className, true, classLoader);

    if (!Supplier.class.isAssignableFrom(dynamicClass)) {
      throw new RuntimeException("La classe compilée n'implémente pas Supplier<Patrimoine>.");
    }

    Supplier<Patrimoine> instance = (Supplier<Patrimoine>) dynamicClass.getDeclaredConstructor().newInstance();

    return instance.get();
  }
}
