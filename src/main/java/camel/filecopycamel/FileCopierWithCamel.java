package camel.filecopycamel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FileCopierWithCamel {

    public static void main(String args[]) throws Exception {
        copyFiles();
    }

    private static void copyFiles() throws Exception{
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:inbox?noop=true").to("file:outbox");
            }
        });
        context.start();
        Thread.sleep(10000); // need sleep to keep JVM running until the job is done
        context.stop();
    }

}
