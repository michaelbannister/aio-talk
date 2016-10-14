package site;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

import static io.vertx.core.http.HttpMethod.GET;

public class VertxSite extends AbstractVerticle {

    private HandlebarsTemplateEngine hbsEngine;
    private HttpClient client;

    public static void main(String[] args) {
        Launcher.executeCommand("run", VertxSite.class.getCanonicalName());
    }

    @Override
    public void start(Future<Void> startup) throws Exception {
        int port = Integer.getInteger("server.port", 8080);

        hbsEngine = HandlebarsTemplateEngine.create();
        client = vertx.createHttpClient(new HttpClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(Integer.getInteger("api.port", 8500))
                .setKeepAlive(false)
                .setMaxPoolSize(5000));

        HomeHandler homeHandler = new HomeHandler(client, hbsEngine);

        Router router = Router.router(vertx);
        router.exceptionHandler(vertx.exceptionHandler());

        router.route(GET, "/").handler(homeHandler::get);

        vertx.createHttpServer()
             .requestHandler(router::accept)
             .listen(port, res -> {
                 if (res.failed()) {
                     System.err.print("Failed to listen on port " + port);
                     startup.fail(res.cause());
                 } else {
                     startup.complete();
                 }
             });
    }

}
