package com.afour.tad.actuator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

public class ActuatorStartUpJob implements Job{
	private final Logger LOGGER = LoggerFactory.getLogger(ActuatorStartUpJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		LOGGER.info("----------------------Sending start request to the Actuator----------------------");
		this.sendMqttRequest();
		LOGGER.info("-------------------Start request sent successfully to the Actuator---------------");
		
	}
	public void sendMqttRequest() {
		Properties prop = new Properties();
		
		try{
			prop.load(new FileInputStream("src/main/resources/mqtt.properties"));
			
			String topic        =  prop.getProperty("topic");
			String content      =  prop.getProperty("content");
			int qos             =  Integer.parseInt(prop.getProperty("qos"));
			String broker       =  prop.getProperty("broker");
			String clientId     =  prop.getProperty("clientId");
			
			MemoryPersistence persistence = new MemoryPersistence();
			  MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
	            MqttConnectOptions connOpts = new MqttConnectOptions();
	            connOpts.setCleanSession(true);
	            LOGGER.info("-> Trying to connect to broker: "+broker);
	            sampleClient.connect(connOpts);
	            LOGGER.info("-> Connected");
	            LOGGER.info("-> Publishing message: "+content);
	            MqttMessage message = new MqttMessage(content.getBytes());
	            message.setQos(qos);
	            sampleClient.publish(topic, message);
	            LOGGER.info("-> Message published");
	            sampleClient.disconnect();
	            LOGGER.info("-> Disconnected from the broker");
	            //System.exit(0);
			
		}catch(MqttException me) {
			LOGGER.warn("reason : " + me.getReasonCode());
			LOGGER.warn("msg : " + me.getMessage());
			LOGGER.warn("loc : " + me.getLocalizedMessage());
			LOGGER.warn("cause : " + me.getCause());
			LOGGER.warn("excep : " + me);
            me.printStackTrace();
        } catch (IOException  e) {
        	LOGGER.warn("Unable to load the Properties file for the Actuator.");
			e.printStackTrace();
		}
	}
	
}