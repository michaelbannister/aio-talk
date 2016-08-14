package api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;

public class FastApi extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", FastApi.class.getCanonicalName());
    }

    private static final int PORT = 8500;

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
             .requestHandler(request -> {
                 request.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "text/plain")
                        .end("HELLO!");
             })
             .listen(PORT, this::listenHandler);
    }

    private void listenHandler(AsyncResult<HttpServer> result) {
        if (result.succeeded()) {
            System.out.println("FastApi listening on port " + PORT);
        } else {
            System.err.println("FastApi failed to start listening on port " + PORT);
            result.cause().printStackTrace();
            vertx.close();
        }
    }
}
