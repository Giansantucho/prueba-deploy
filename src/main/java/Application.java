import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Application {
    private static Javalin app = null;
    private static final String UPLOAD_DIR = "src/main/resources/uploads";

    public static Javalin app() {
        if (app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }

    public static void main(String[] args) {

        app = Javalin.create(javalinConfig -> {
            javalinConfig.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });

            // Configurar recursos estáticos
            javalinConfig.staticFiles.add("/static", Location.CLASSPATH);



        }).start(8080);

        // Crear el directorio de imágenes si no existe
        initializeUploadDirectory();

        // Configurar rutas
        Router.init(getEntityManager());
        configureImageRoutes(app);
    }

    private static EntityManager getEntityManager() {
        // Configuración dinámica de la conexión usando el archivo application.properties
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.connection.url", ConfigUtil.getProperty("db.url"));
        properties.put("hibernate.connection.username", ConfigUtil.getProperty("db.username"));
        properties.put("hibernate.connection.password", ConfigUtil.getProperty("db.password"));
        properties.put("hibernate.dialect", ConfigUtil.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", ConfigUtil.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", ConfigUtil.getProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", ConfigUtil.getProperty("hibernate.hbm2ddl.auto"));

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("db", properties);
        return emf.createEntityManager();
    }

    private static void initializeUploadDirectory() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    private static void configureImageRoutes(Javalin app) {
        app.get("/images/{filename}", ctx -> {
            String filePath = "src/main/resources/uploads/ofertas/" + ctx.pathParam("filename");
            try {
                // Leer el archivo completo como un arreglo de bytes
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

                if (fileBytes.length > 0) {
                    ctx.contentType("image/*");
                    ctx.result(fileBytes); // Enviar los bytes directamente
                } else {
                    ctx.status(404).result("Imagen vacía");
                }
            } catch (IOException e) {
                e.printStackTrace();
                ctx.status(500).result("Error al cargar la imagen");
            }
        });
    }
}
