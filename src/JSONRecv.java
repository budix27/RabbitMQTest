import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.DeliverCallback;

public class JSONRecv 
{
 private final static String QUEUE_NAME = "AUSYDWS166113_JMeter_bus_1";
 private ConnectionFactory factory = null;
 private JSONParser parser;
 
 public JSONRecv() 
 {
  parser = new JSONParser();
 }

 public void run () throws Exception
 {
  factory = new ConnectionFactory();
     factory.setHost("localhost");
     //factory.setPort(5676);
     Connection connection = factory.newConnection();
     Channel channel = connection.createChannel();
     
     channel.basicQos(1);

     channel.queueDeclare(QUEUE_NAME, true, false, false, null);
     System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
     
     DeliverCallback deliverCallback = (consumerTag, delivery) -> {
         String message = new String(delivery.getBody());
         JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(message);
			System.out.println(" [x] Received '" + obj.toJSONString() + "'");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
     };
     channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
     
     /*
     QueueingConsumer consumer = new QueueingConsumer(channel);
     channel.basicConsume(QUEUE_NAME, true, consumer);
     
     while (true) 
     {
       QueueingConsumer.Delivery delivery = consumer.nextDelivery();
       String message = new String(delivery.getBody()); 
       JSONObject obj = (JSONObject) parser.parse(message);
       
       System.out.println(" [x] Received '" + obj.toJSONString() + "'");
     }  
     */
 }
 
 /**
  * @param args
  * @throws Exception 
  */
 public static void main(String[] args) throws Exception 
 {
  // TODO Auto-generated method stub
  JSONRecv test = new JSONRecv();
  test.run();
 }

}  