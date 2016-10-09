package api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;

public class ApiMain extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.executeCommand("run", ApiMain.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new FastApi());
        vertx.deployVerticle(new SlowApi());
    }
}
