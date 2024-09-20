package school.hei.patrimoine.compiler;

import school.hei.patrimoine.cas.PatrimoineCresusCas;
import school.hei.patrimoine.cas.PatrimoineRicheCas;
import school.hei.patrimoine.modele.Patrimoine;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PatrimoineCompiler {
  public static Patrimoine stringCompiler(String javaSource) throws Exception{

    PatrimoineRicheCas patrimoineRicheCas = new PatrimoineRicheCas();

    Patrimoine patrimoineRiche = patrimoineRicheCas.get();

    String javaFile = "out/production/classes/DynamicClass.java";
    String classOutputDir = "out/production/classes/";

    Files.write(Paths.get(javaFile), javaSource.getBytes());

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    int result = compiler.run(null, null, null, "-d", classOutputDir, javaFile);

    if (result != 0) {
      throw new RuntimeException("Compilation failed.");
    }

    File outputDir = new File(classOutputDir);
    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{outputDir.toURI().toURL()});
    Class<?> dynamicClass = Class.forName("DynamicClass", true, classLoader);
    Method method = dynamicClass.getMethod("compileCode", Patrimoine.class);

    return (Patrimoine) method.invoke(null, patrimoineRiche);
  }
}
