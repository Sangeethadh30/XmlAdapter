package com.adapter.process;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.adapter.model.RequestModel;
import com.adapter.model.ResponseTagModel;
public class XmlProcessor implements ItemProcessor<RequestModel,ResponseTagModel>{

	private static final Logger log = LoggerFactory.getLogger(XmlProcessor.class);
    /**
     * Implements the process reading and extracting XML elements
     * @param RequestModel - RequestModel to process
     * @return ResponseTagModel - Processed model
     */

	@Override
	public ResponseTagModel process(RequestModel arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("process");
		return null;
	}

}
