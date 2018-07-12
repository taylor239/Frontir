

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import java.io.DataOutputStream;
import java.util.Enumeration;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import java.nio.*;
import java.nio.charset.Charset;
import java.nio.charset.*;

/**
 * Servlet implementation class FrontingServlet
 */
@WebServlet(
		asyncSupported = true, 
		urlPatterns =
		{ 
				"/FrontingServlet", 
				"/*"
		})
public class FrontingServlet extends HttpServlet
    {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontingServlet()
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		System.out.println(request.getMethod());
		String method = request.getMethod();
		System.out.println(request.getRequestURI());
		System.out.println(request.getContextPath());
		String fullURL = request.getRequestURI();
		fullURL = fullURL.substring(request.getContextPath().length() + 1);
		System.out.println(fullURL);
		
		//Map<String, String> parameters = new HashMap();
		//fullURL = "http://www.google.com/";
		
		URL theURL = new URL(fullURL);
		URLConnection theConnection = (URLConnection)theURL.openConnection();
		
		
		if(theConnection instanceof HttpURLConnection)
		{
			HttpURLConnection theHTTPConnection = (HttpURLConnection)theConnection;
			theHTTPConnection.setRequestMethod(method);
			
			Enumeration<String>headerNames = request.getHeaderNames();
			//if(headerNames != null)
			{
				//while(headerNames.hasMoreElements())
				//{
				//	String curHeader = headerNames.nextElement();
				//	System.out.println(curHeader + ": " + request.getHeader(curHeader));
					//theHTTPConnection.setRequestProperty(curHeader, request.getHeader(curHeader));
				//}
			}
			
			theHTTPConnection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(theHTTPConnection.getOutputStream());
			out.flush();
			out.close();
			
			int status = theHTTPConnection.getResponseCode();
			System.out.println("Status: " + status);
		}
		else
		{
			theConnection.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(theConnection.getOutputStream());
			out.flush();
			out.close();
		}
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		InputStream is = theConnection.getInputStream();
		int nRead;
		byte[] curData = new byte[4096];
		while((nRead = is.read(curData, 0, curData.length)) != -1)
		{
			buffer.write(curData, 0, nRead);
		}
		buffer.flush();
		byte[] theOutput = buffer.toByteArray();
		String theOutputString = "";
		
		boolean isText = true;
		try
		{
			ByteBuffer bb = ByteBuffer.wrap(theOutput);
			CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
			CharBuffer cb = decoder.decode(bb);
			theOutputString = cb.toString();
		}
		catch(Exception e)
		{
			System.out.println("Not text response");
			isText = false;
		}
		
		if(!isText)
		{
			response.getOutputStream().write(theOutput);
			response.getOutputStream().close();
		}
		else
		{
			String sig = "\n<script>alert(\"Fronted by Frontir\")</script>";
			response.getWriter().append(theOutputString + sig);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		doGet(request, response);
	}

}
