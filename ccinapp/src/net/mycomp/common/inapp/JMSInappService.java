package net.mycomp.common.inapp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import net.mycomp.common.service.IDaoService;

import org.apache.activemq.ScheduledMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("jmsInappService")
public class JMSInappService {


	private static final Logger logger = Logger.getLogger(JMSInappService.class);

	@Autowired
	@Qualifier("inappProcessRequestJMSTemplate")
	private JmsTemplate inappProcessRequestJMSTemplate;
	
	@Autowired
	private IDaoService daoService;
	
	public boolean saveInappProcessRequest(final InappProcessRequest inappProcessRequest) {
	
		try{
			inappProcessRequestJMSTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message=session.createObjectMessage(inappProcessRequest);
				//message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1*60*1000);
				return message;
			}
		});
		}catch(Exception ex){
			logger.error("saveInappProcessRequest::::::::::::: ",ex);
			boolean save=daoService.updateObject(inappProcessRequest);			
			return save;
		}
		return true;
	}
	
	
}
