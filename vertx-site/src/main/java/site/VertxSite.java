package site;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

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

        Router router = Router.router(vertx);
        router.route().handler(this::getGreeting);
        router.exceptionHandler(vertx.exceptionHandler());

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

    private void getGreeting(RoutingContext context) {
        client.getNow("/", resp -> {
            if (resp.statusCode() == 200) {
                resp.bodyHandler(body -> {
                    String message = body.toJsonObject().getString("message");
                    context.data().put("greeting", message);
                    hbsEngine.render(context, "templates/home", res -> {
                        if (res.succeeded()) {
                            context.response().putHeader("Content-Type", "text/html;charset=UTF-8");
                            context.response().end(res.result());
                        } else {
                            context.fail(res.cause());
                        }
                    });
                });
            } else {
                context.fail(resp.statusCode());
            }
        });
    }
}
