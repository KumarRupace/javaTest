package com.customer.journey.exception;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.customer.journey.util.CustomerConstants;

/**
 * @author Jhonson
 *
 */
public class ApplicationExceptionDetail  {

	    private String errTimestamp;
	    private String errMessage;
	    private String errDetail;
	    private String errUri;

	    public ApplicationExceptionDetail(String message, String details, String uri) {	        
	         this.errTimestamp =  getDate();
	         this.errMessage = message;
	         this.errDetail = details;	
	         this.errUri = uri;
	    } 	  
	    
	    public String getErrTimestamp() {
			return errTimestamp;
		}

		public void setErrTimestamp(String errTimestamp) {
			this.errTimestamp = errTimestamp;
		}

		public String getErrMessage() {
			return errMessage;
		}

		public void setErrMessage(String errMessage) {
			this.errMessage = errMessage;
		}

		public String getErrDetail() {
			return errDetail;
		}

		public void setErrDetail(String errDetail) {
			this.errDetail = errDetail;
		}
		
		public String getErrUri() {
			return errUri;
		}

		public void setErrUri(String errUri) {
			this.errUri = errUri;
		}

		protected String getDate() {
			return new SimpleDateFormat(CustomerConstants.ERR_DATE_FORMAT).format(new Date());
	    }
	}
