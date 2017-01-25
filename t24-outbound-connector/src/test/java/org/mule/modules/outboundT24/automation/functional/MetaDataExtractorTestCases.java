package org.mule.modules.outboundT24.automation.functional;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;

import com.temenos.adapter.common.runtime.outbound.T24ServiceXmlMetadataImpl;
import com.temenos.adapter.mule.T24outbound.config.ConnectorConfig;
import com.temenos.adapter.mule.T24outbound.config.RuntimeConfigSelector;
import com.temenos.adapter.mule.T24outbound.metadata.extract.T24OutboundDesignTimeMetaDataExtractor;
import com.temenos.adapter.mule.T24outbound.utils.IoResourseUtil;

public class MetaDataExtractorTestCases {
	private T24OutboundDesignTimeMetaDataExtractor extractor;
	private ConnectorConfig config;
	
	@Before
	public void setup() throws ConnectionException {
		config = new ConnectorConfig();
		config.setAgentHost("localhost");
		config.setPort(4447);
		config.setT24RunTime(RuntimeConfigSelector.TAFJ);
		
		config.setAgentUser("INPUTT");
		config.setFolder("D:/Schemas4");
		config.connect("INPUTT", "http://localhost:9089/axis2/services/IntegrationLandscapeServiceWS?wsdl", "C:/Users/petar.zhivkov/AnypointStudio/workspace/inboundflow/src/main/api/UserCredentials_1.txt");
		
		config.initIntegrationServiceLandscape("http://localhost:9089/axis2/services/IntegrationLandscapeServiceWS?wsdl");
		//boolean conStatus = cf.isConnected();
		extractor = new T24OutboundDesignTimeMetaDataExtractor(config);
		extractor.setDebug(true);
	}
	
	@Test
	public void verifyExtractor(){
		
		List<String> serviceNames = null;
		try{
			serviceNames = extractor.getServiceNames();

			IoResourseUtil ioProcessor = new IoResourseUtil();
			
			String tryMyDirector = ioProcessor.createDirectory(config.getFolder(), "Outbound");
			
			extractor.saveMetaDataFiles(tryMyDirector); 
			
		}catch(RuntimeException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(serviceNames);
		
		T24ServiceXmlMetadataImpl xmlMetaData =  extractor.getModelArtifacts("Currency list", config.getFolder());
		assertNotNull(xmlMetaData);

	}
	

}
