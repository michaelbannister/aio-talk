package api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;

public class SlowApi extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", SlowApi.class.getCanonicalName());
    }

    private static final int PORT = 8501;
    
    @Override
    public void start() throws Exception {
        int delayMillis = Integer.getInteger("delay", 5000);

        vertx.createHttpServer()
             .requestHandler(request -> {
                 vertx.setTimer(delayMillis , (h) -> {
                     request.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("message", "HELLO!").toString());
                 });
             })
             .listen(PORT, this::listenHandler);
    }

    private void listenHandler(AsyncResult<HttpServer> result) {
        if (result.succeeded()) {
            System.out.println("SlowApi listening on port " + PORT);
        } else {
            System.err.println("SlowApi failed to start listening on port " + PORT);
            result.cause().printStackTrace();
            vertx.close();
        }
    }
}
