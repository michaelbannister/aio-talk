package api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;

public class FastApi extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", FastApi.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
             .requestHandler(request -> {
                 request.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "text/plain")
                        .end("HELLO!");
             })
             .listen(8500, this::listenHandler);
    }

    private void listenHandler(AsyncResult<HttpServer> result) {
        if (result.succeeded()) {
            System.out.println("RemoteApi listening on port 8500");
        } else {
            System.err.println("RemoteApi failed to start listening on port 8500");
            result.cause().printStackTrace();
            vertx.close();
        }
    }
}
