package net.sqs2.omr.result.servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.net.NetworkUtil;

public class HttpUtil {
	
	public static boolean isMSIE(HttpServletRequest req){
		return 0 <= req.getHeader("User-Agent").indexOf("MSIE");
	}
	
	public static void setDownloadHeader(HttpServletResponse res, String filename) {
		//res.setContentType("text/plain; charset=Shift-JIS");
		res.setContentType("application/download; name=\""+filename+"\"");
		res.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
	}
	
	public static boolean isLocalNetworkAccess(HttpServletRequest req){
		int DEFAULT_NETMASK_LENGTH = 24;
		return isLocalNetworkMatched(req.getRemoteAddr(), DEFAULT_NETMASK_LENGTH); 
	}

	public static boolean isLocalNetworkMatched(String remoteAddr, int netMaskLength){
		if(remoteAddr == null){
			Logger.getAnonymousLogger().info("remoteAddr == null");
			return false;
		}
		try{
			return isLocalNetworkMatched(InetAddress.getByName(remoteAddr), NetworkUtil.getInet4Localhost(), netMaskLength);	
		}catch(IOException ex){
			return false;
		}
	}

	public static boolean isLocalNetworkMatched(InetAddress remoteAddr, InetAddress localAddr, int bitMaskLength){
		byte[] remoteAddressBytes = remoteAddr.getAddress();
		byte[] localAddressBytes = localAddr.getAddress();

		if(remoteAddressBytes.length != localAddressBytes.length || localAddressBytes.length < bitMaskLength){
			return false;
		}
			
		for(int i = bitMaskLength / 8 - 1; 0 <= i ; i--){
			if(remoteAddressBytes[i] != localAddressBytes[i]){
				return false;
			}
		}
		
		byte lastRemoteByte = remoteAddressBytes[bitMaskLength/8];
		byte lastLocalByte = localAddressBytes[bitMaskLength/8];
			
		for(int i = 7; bitMaskLength - bitMaskLength % 8 < i ; i--){
			if((lastRemoteByte & (1 << i)) == (lastLocalByte & (1 << i))){
				return false;
			}
		}
		return true;
	}

}
