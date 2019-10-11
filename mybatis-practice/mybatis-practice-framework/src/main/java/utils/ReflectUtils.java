package utils;

public class ReflectUtils {

  public static Class<?> resolveClass(String parameterType) {
    try {
      return Class.forName(parameterType);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
