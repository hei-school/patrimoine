package school.hei.patrimoine.compiler;

import school.hei.patrimoine.cas.PatrimoineCresusCas;
import school.hei.patrimoine.modele.Patrimoine;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PatrimoineCompiler {
  public static List<Patrimoine> stringCompiler(String javaSource) throws Exception{

    PatrimoineCresusCas patrimoineCresusCas =
            new PatrimoineCresusCas();

    Patrimoine patrimoineCresus = patrimoineCresusCas.get();

    String javaFile = "src/main/java/school/hei/patrimoine/compiler/dynamic/DynamicClass.java";
    String classOutputDir = "src/main/java/school/hei/patrimoine/compiler/dynamic";

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

    return (List<Patrimoine>) method.invoke(null, patrimoineCresus);
  }
}
