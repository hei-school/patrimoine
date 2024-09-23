package school.hei.patrimoine.compiler;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import school.hei.patrimoine.modele.Patrimoine;

public class PatrimoineCompiler implements BiFunction<String, String, Patrimoine> {

  private static final String CLASS_OUTPUT_DIR = "out/production/classes/";

  @Override
  public Patrimoine apply(String className, String javaSource) {
    try {
      String javaFile = CLASS_OUTPUT_DIR + className + ".java";

      Files.write(Paths.get(javaFile), javaSource.getBytes());

      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      int result = compiler.run(null, null, null, "-d", CLASS_OUTPUT_DIR, javaFile);

      if (result != 0) {
        throw new RuntimeException("Compilation failed.");
      }

      File outputDir = new File(CLASS_OUTPUT_DIR);
      URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {outputDir.toURI().toURL()});
      Class<?> dynamicClass = Class.forName(className, true, classLoader);

      if (!Supplier.class.isAssignableFrom(dynamicClass)) {
        throw new RuntimeException("The compiled class does not implement Supplier<Patrimoine>.");
      }

      Supplier<Patrimoine> instance =
              (Supplier<Patrimoine>) dynamicClass.getDeclaredConstructor().newInstance();

      return instance.get();
    } catch (Exception e) {
      throw new RuntimeException("Error during compilation or execution", e);
    }
  }
}
