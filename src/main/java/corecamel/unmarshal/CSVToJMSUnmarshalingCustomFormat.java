package corecamel.unmarshal;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class CSVToJMSUnmarshalingCustomFormat {

    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

        // add the broker as a component
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
              public void configure() {
                  CsvDataFormat myCsv = new CsvDataFormat()
                  .setDelimiter(',') // values separated by comma
                  .setHeader(new String[]{"id", "name", "amount"});
                  from("file:inboxcsv?noop=true&fileName=employees.csv")
                  .marshal(myCsv)
                  .to("activemq:queue:csvrecord");
              }
        });
        context.start();
        Thread.sleep(10000); // need sleep to keep JVM running until the job is done
        context.stop();
    }

}
