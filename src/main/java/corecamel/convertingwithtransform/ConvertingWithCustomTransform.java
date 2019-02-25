package corecamel.convertingwithtransform;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import static org.apache.camel.builder.Builder.body;

public class ConvertingWithCustomTransform {

        public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
            from("file:inbox?noop=true")
                .transform(new Expression() {
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                    String body = exchange.getIn().getBody(String.class);
                    body = body.replaceAll(" ", "<br/>");
                    body = "<body>" + body + "</body>";
                    return (T) body;
                    }
                })
                .to("file:outbox");
            }
        }
        );
        context.start();
        Thread.sleep(10000); // need sleep to keep JVM running until the job is done
        context.stop();
    }

}
