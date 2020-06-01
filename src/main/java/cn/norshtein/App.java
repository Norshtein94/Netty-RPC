package cn.norshtein;

import cn.norshtein.configuration.SpringConfig;
import cn.norshtein.server.RpcNettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcNettyServer server = context.getBean(RpcNettyServer.class);
        server.start(8080);
    }
}
