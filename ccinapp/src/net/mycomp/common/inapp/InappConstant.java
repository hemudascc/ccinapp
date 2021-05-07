package net.mycomp.common.inapp;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public interface InappConstant {

	 static final Logger logger = Logger
			.getLogger(InappConstant.class.getName());
	
	public static AtomicInteger inappProcessRequestId=new AtomicInteger(0);
	
	public static final String INAPP="INAPP";
	
	public static final int INAPP_COLLECENT_ADNETWORK_ID=44;
	public static final int INAPP_GLOBOCOM_ADNETWORK_ID=45;
	public static final int INAPP_AUDIENCE_NEST_ADNETWORK_ID=46;
	public static final int INAPP_GLOBOCOM_DUPLICATE_ADNETWORK_ID=47;
	public static final int INAPP_TRAFFIC_ADNETWORK_ID=48;
	public static final int INAPP_XCELL_ADNETWORK_ID=49;
	public static final int INAPP_31_TOE_ADNETWORK_ID=50;
	public static final int INAPP_MPLUS_ADNETWORK_ID=51;
	public static final int INAPP_MACLATO_ADNETWORK_ID=52;
	public static final int INAPP_MOBILARTS_ADNETWORK_ID=53;
	public static final int INAPP_SMILE_DIGITAL_ADNETWORK_ID=54;
	public static final int INAPP_SAINT_ADNETWORK_ID=55;
	public static final int INAPP_INNOVERA_SOLUTIONS_ADNETWORK_ID=56;
	public static final int INAPP_HALAWA_ADNETWORK_ID=57;
	public static final int INAPP_GULFTECH_ADNETWORK_ID=58;
	public static final int INAPP_SARAS_MEDIA_ADNETWORK_ID=59;
	public static final int INAPP_ADWELLY_MEDIA_ADNETWORK_ID=60;
	public static final int INAPP_OUTRIX_WAVE_ADNETWORK_ID=61;
}
