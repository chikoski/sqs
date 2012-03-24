package net.sqs2.omr.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.net.NetworkUtil;

public class HttpUtil {

	public static boolean isMSIE(HttpServletRequest req) {
		return 0 <= req.getHeader("User-Agent").indexOf("MSIE");
	}

	public static void setDownloadHeader(HttpServletResponse res, String filename) {
		// res.setContentType("text/plain; charset=Shift-JIS");
		res.setContentType("application/download; name=\"" + filename + "\"");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
	}
	
	static abstract class Authenticator{
		public abstract boolean isAuthorized(HttpServletRequest req);
	}
	
	public static class DomainAuthenticator extends Authenticator{
		
		Set<String> allowedDomainSet;
		 public DomainAuthenticator(Set<String> allowedDomainSet){
			 this.allowedDomainSet = allowedDomainSet;
		 }
		
		public boolean isAuthorized(HttpServletRequest req) {
			if(req.getRemoteAddr().equals("127.0.0.1")){
				return true;
			}
			if(allowedDomainSet == null){
				return true;
			}else{
				return allowedDomainSet.contains(req.getRemoteAddr());	
			}
		}
	}
	
	public static class NetmaskAuthenticator extends Authenticator{
		int netmaskLength;
		
		 public NetmaskAuthenticator(int netmaskLength){
			 this.netmaskLength = netmaskLength;
		 }

		 public boolean isAuthorized(HttpServletRequest req){
			if(req.getRemoteAddr().equals("127.0.0.1")){
				return true;
			}
			return isLocalNetworkMatched(req.getRemoteAddr(), netmaskLength);
		}
	
		 public static boolean isLocalNetworkMatched(String remoteAddr, int netMaskLength) {
			if (remoteAddr == null) {
				Logger.getLogger("HttpUtil").info("remoteAddr == null");
				return false;
			}
			try {
				return isLocalNetworkMatched(InetAddress.getByName(remoteAddr), NetworkUtil.Inet4.getLocalhost(),
						netMaskLength);
			} catch (IOException ex) {
				return false;
			}
		}
	
		public static boolean isLocalNetworkMatched(InetAddress remoteAddr, InetAddress localAddr, int bitMaskLength) {
			byte[] remoteAddressBytes = remoteAddr.getAddress();
			byte[] localAddressBytes = localAddr.getAddress();

			if (remoteAddressBytes.length != localAddressBytes.length || localAddressBytes.length < bitMaskLength) {
				return false;
			}

			for (int i = bitMaskLength / 8 - 1; 0 <= i; i--) {
				if (remoteAddressBytes[i] != localAddressBytes[i]) {
					return false;
				}
			}

			byte lastRemoteByte = remoteAddressBytes[bitMaskLength / 8];
			byte lastLocalByte = localAddressBytes[bitMaskLength / 8];

			for (int i = 7; bitMaskLength - bitMaskLength % 8 < i; i--) {
				if ((lastRemoteByte & (1 << i)) == (lastLocalByte & (1 << i))) {
					return false;
				}
			}
			
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public static void logRequestedURI(HttpServletRequest req) {
		StringBuilder ret = new StringBuilder();
		char delim = '?';
		try {
			ret.append(req.getRequestURI()).append("?");
		} catch (Exception ignore) {
		}
		for (Object o : req.getParameterMap().entrySet()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) o;
			for (String value : entry.getValue()) {
				if (delim == '&') {
					ret.append('&');
				}
				delim = '&';
				ret.append(entry.getKey());
				ret.append("=");
				ret.append(value);
			}
		}
		Logger.getLogger(HttpUtil.class.getName()).info(ret.toString());
	}

}
