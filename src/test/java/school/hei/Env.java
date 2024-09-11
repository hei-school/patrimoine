package school.hei;

public class Env {
  public static final boolean IS_LOCAL_ENV = "local".equals(System.getenv("ENV"));
}
