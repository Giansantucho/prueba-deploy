import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {

    private static Properties properties = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo cargar el archivo de configuración");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
