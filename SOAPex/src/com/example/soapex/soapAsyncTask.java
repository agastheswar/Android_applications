package com.example.soapex;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class soapAsyncTask extends AsyncTask<Void, Void, String>{
	
	private static String SOAP_ACTION = "http://www.webserviceX.NET/GetAtomicNumber";
	private static final String TAG = "GetAtomicNumber";
	private static String NAMESPACE = "http://www.webserviceX.NET/";
	private static String METHOD_NAME = "GetAtomicNumber";

	private static String URL = "http://www.webservicex.net/periodictable.asmx";
	
	
	protected String doInBackground(Void... params) {
		String response= " ";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("ElementName","helium");
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.debug = true;
		
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "HTTP REQUEST:\n" + androidHttpTransport.requestDump);
        Log.d(TAG, "HTTP RESPONSE:\n" + androidHttpTransport.responseDump);
		

		 if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
	            SoapObject soapObject = (SoapObject) envelope.bodyIn;
	            Log.d(TAG, soapObject.toString());
	            response = parseSOAP(soapObject);
	            Log.d(TAG, response);
	        } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
	            SoapFault soapFault = (SoapFault) envelope.bodyIn;
	            try {
					throw new Exception(soapFault.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		 return response;
	}

	private String parseSOAP(SoapObject soapObject) {
		SoapObject ipNode = (SoapObject) soapObject.getProperty("GetGeoIPContextResult");
		boolean sucess = Boolean.parseBoolean(ipNode.getPrimitivePropertySafelyAsString("Success"));
		String atomic_number = ipNode.getPrimitivePropertyAsString("GetAtomicNumberResult");
//		String countryName = ipNode.getPrimitivePropertyAsString("CountryName");
//		String countryCode = ipNode.getPrimitivePropertyAsString("CountryCode");
//		String returnCode = ipNode.getPrimitivePropertyAsString("ReturnCode");
		
		StringBuilder sb = new StringBuilder();
		sb.append("Atomic Number is");
		sb.append(atomic_number);
//		sb.append("\nCountry name is ");
//		sb.append(countryName);
//		sb.append("\nCountry Code is ");
//		sb.append(countryCode);
		return sb.toString();
	}

	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	protected void onPostExecute(String result) {
		
		System.out.println(result);
	}
}
