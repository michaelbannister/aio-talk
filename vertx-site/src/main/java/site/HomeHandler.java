package site;

import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

public class HomeHandler {
    private final HttpClient client;
    private final HandlebarsTemplateEngine hbsEngine;

    public HomeHandler(HttpClient client, HandlebarsTemplateEngine hbsEngine) {
        this.client = client;
        this.hbsEngine = hbsEngine;
    }

    void get(RoutingContext context) {
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
