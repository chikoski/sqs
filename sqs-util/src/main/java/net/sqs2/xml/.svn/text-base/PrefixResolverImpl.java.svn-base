/**
 * 
 */
package net.sqs2.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;

import org.apache.xml.utils.PrefixResolver;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PrefixResolverImpl implements PrefixResolver {

	private Map<String, String> nsMap;

	public PrefixResolverImpl(Element root) {
		this(root, "");
	}

	public PrefixResolverImpl(Element root, String defaultPrefix) {
		nsMap = new HashMap<String, String>();
		NamedNodeMap attrs = root.getAttributes();
		String xmlns = "xmlns";
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String[] name = attr.getNodeName().split(":");
			if (xmlns.equals(name[0])) {
				String nsURI = attr.getNodeValue();
				if (name.length == 1) {
					nsMap.put(defaultPrefix, nsURI);
					// System.err.println("NS : "+ defaultPrefix+" = "+nsURI);
				} else {
					nsMap.put(name[1], nsURI);
					// System.err.println("NS : "+ name[1]+" = "+nsURI);
				}
			}
		}
	}

	public String getNamespaceForPrefix(String prefix) {
		if (nsMap.containsKey(prefix)) {
			return nsMap.get(prefix);
		}
		return XMLConstants.NULL_NS_URI;
	}

	public String getNamespaceForPrefix(String prefix, Node context) {
		throw new UnsupportedOperationException();
	}

	public String getBaseIdentifier() {
		throw new UnsupportedOperationException();
	}

	public boolean handlesNullPrefixes() {
		return false;
	}
}
