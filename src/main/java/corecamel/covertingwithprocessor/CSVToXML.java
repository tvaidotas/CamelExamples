package corecamel.covertingwithprocessor;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CSVToXML {

    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
            from("file:inboxcsv?noop=true")
                .process(new CSVToXMLProcessor())
                .to("file:outboxcsv?fileName=emp.xml");
            }
        }
        );
        context.start();
        Thread.sleep(10000); // need sleep to keep JVM running until the job is done
        context.stop();
    }

}
