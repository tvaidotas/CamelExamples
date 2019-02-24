package camel.ftptojmswithprocessor;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class FtpToJMSExample {

    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        // add the broker as a component
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // make sure activemq is started ./activemq start
        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // connect to FTP server
                // copy all the files in root directory and put them as messages onto the queue
                from("ftp://test.rebex.net?username=demo&password=password")
                    .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(
                            "We just downloaded: " + exchange.getIn().getHeader("CamelFileName")
                        );
                    }
                })
                .to("activemq:queue:orders"); // connected to activeMQ and put message into it
            }
        });

        context.start();
        Thread.sleep(10000); // need sleep to keep JVM running until the job is done
        context.stop();
    }

}
