package cl.duoc.cordillera.bffgateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador SPA: redirige todas las rutas no-API al index.html del frontend.
 * El build de React (npm run build) debe copiarse a:
 *   bff-gateway/src/main/resources/static/
 * Spring Boot sirve automáticamente los archivos estáticos desde ese directorio.
 *
 * Flujo en producción:
 *   Browser → http://localhost:8081/dashboard → index.html (React Router toma control)
 *   Browser → http://localhost:8081/api/v1/kpis → KpisProxyController → kpi-service:8084
 */
@Controller
public class SpaController {

    /**
     * Captura cualquier ruta que no contenga un punto (extensión de archivo)
     * y que no sea una ruta de API, devolviendo index.html para que React Router
     * maneje la navegación del lado del cliente.
     */
    @RequestMapping(value = { "/", "/{path:[^\\.]*}", "/{path:[^\\.]*}/**" })
    public String index() {
        return "forward:/index.html";
    }
}
