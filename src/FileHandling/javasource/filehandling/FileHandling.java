package filehandling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.util.LimitedInputStream;
import org.apache.commons.io.IOUtils;

import com.mendix.core.Core;
import com.mendix.core.objectmanagement.member.MendixBoolean;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import system.proxies.FileDocument;

public class FileHandling {
	public static void base64DecodeToFile(IContext context, String encoded, FileDocument targetFile) throws Exception
	{
		if (targetFile == null)
			throw new IllegalArgumentException("Source file is null");
		if (encoded == null)
			throw new IllegalArgumentException("Source data is null");
		
		byte [] decoded = Base64.decodeBase64(encoded.getBytes());
		Core.storeFileDocumentContent(context, targetFile.getMendixObject(), new ByteArrayInputStream(decoded));
	}
	
	public static String base64EncodeFile(IContext context, FileDocument file) throws IOException
	{
		if (file == null)
			throw new IllegalArgumentException("Source file is null");
		if (!file.getHasContents())
			throw new IllegalArgumentException("Source file has no contents!");
		InputStream f = Core.getFileDocumentContent(context, file.getMendixObject());
		return new String(Base64.encodeBase64(IOUtils.toByteArray(f)));		
	}
	
	public static Boolean duplicateFileDocument(IContext context, IMendixObject toClone, IMendixObject target) throws Exception
    {
        if (toClone == null || target == null)
            throw new Exception("No file to clone or to clone into provided");

        MendixBoolean hasContents = (MendixBoolean) toClone.getMember(context, FileDocument.MemberNames.HasContents.toString());
        if (!hasContents.getValue(context))
        	return false;

        InputStream inputStream = Core.getFileDocumentContent(context, toClone); 

        try {
            Core.storeFileDocumentContent(context, target, (String) toClone.getValue(context, system.proxies.FileDocument.MemberNames.Name.toString()),  inputStream); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try {
                if(inputStream != null)
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

	public static Boolean duplicateImage(IContext context, IMendixObject toClone, IMendixObject target, int thumbWidth, int thumbHeight) throws Exception
	{
	      if (toClone == null || target == null)
	          throw new Exception("No file to clone or to clone into provided");

	      MendixBoolean hasContents = (MendixBoolean) toClone.getMember(context, FileDocument.MemberNames.HasContents.toString());
	      if (!hasContents.getValue(context))
	          return false;

	      InputStream inputStream = Core.getImage(context, toClone, false); 

	      try {
	        Core.storeImageDocumentContent(context, target, inputStream, thumbWidth, thumbHeight);

	          return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	      finally {
	          try {
	            if(inputStream!= null)
	              inputStream.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	      }
	      return true;
	  }
	
	public static Boolean storeURLToFileDocument(IContext context, String url, IMendixObject __document, String filename) throws Exception
	{
        if (__document == null || url == null || filename == null)
            throw new Exception("No document, filename or URL provided");
        
        final int MAX_REMOTE_FILESIZE = 1024 * 1024 * 200; //maxium of 200 MB
        URL imageUrl = new URL(url);
        URLConnection connection = imageUrl.openConnection();
        //we connect in 20 seconds or not at all
        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);
        connection.connect();

        //check on forehand the size of the remote file, we don't want to kill the server by providing a 3 terabyte image. 
        if (connection.getContentLength() > MAX_REMOTE_FILESIZE) { //maximum of 200 mb 
            throw new IllegalArgumentException("MxID: importing image, wrong filesize of remote url: " + connection.getContentLength()+ " (max: " + String.valueOf(MAX_REMOTE_FILESIZE)+ ")");
        } else if (connection.getContentLength() < 0) {
            // connection has not specified content length, wrap stream in a LimitedInputStream
            LimitedInputStream limitStream = new LimitedInputStream(connection.getInputStream(), MAX_REMOTE_FILESIZE) {                
                @Override
                protected void raiseError(long pSizeMax, long pCount) throws IOException {
                    throw new IllegalArgumentException("MxID: importing image, wrong filesize of remote url (max: " + String.valueOf(MAX_REMOTE_FILESIZE)+ ")");                    
                }
            };
            Core.storeFileDocumentContent(context, __document, filename, limitStream);
        } else {
            // connection has specified correct content length, read the stream normally
            //NB; stream is closed by the core
            Core.storeFileDocumentContent(context, __document, filename, connection.getInputStream());
        }
        
        return true;
	}

    public static Long getFileSize(IContext context, IMendixObject document)
    {
        final int BUFFER_SIZE = 4096;
        long size = 0;

        if (context != null) {
            InputStream inputStream = null;
            byte[] buffer = new byte[BUFFER_SIZE];
            
            try {
                inputStream = Core.getFileDocumentContent(context, document);
                int i;
                while ((i = inputStream.read(buffer)) != -1) 
                    size += i;
            } catch (IOException e) {
                Core.getLogger("FileUtil").error(
                        "Couldn't determine filesize of FileDocument '" + document.getId()); 
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
        
        return size;
    }
    public static String stringFromFile(IContext context, FileDocument source) throws IOException
	{
		if (source == null)
			return null;
		InputStream f = Core.getFileDocumentContent(context, source.getMendixObject());
		return org.apache.commons.io.IOUtils.toString(f);
	}

	public static void stringToFile(IContext context, String value, FileDocument destination) 
	{
		if (destination == null)
			throw new IllegalArgumentException("Destination file is null");
		if (value == null)
			throw new IllegalArgumentException("Value to write is null");
		Core.storeFileDocumentContent(context, destination.getMendixObject(), IOUtils.toInputStream(value));
	}
}
