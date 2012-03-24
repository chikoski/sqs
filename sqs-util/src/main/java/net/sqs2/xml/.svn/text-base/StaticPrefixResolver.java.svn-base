/*
 * 

 StaticPrefixResolver.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package net.sqs2.xml;

import java.util.HashMap;
import java.util.Map;

import org.apache.xml.utils.PrefixResolver;
import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public class StaticPrefixResolver implements PrefixResolver {

	Map<String, String> prefixMap = null;

	private static final String EMPTY_STRING = "";

	public static final String S_XMLNAMESPACEURI = "http://www.w3.org/XML/1998/namespace";

	public static final String XML_PREFIX = "xml";

	public static final String S_XMLNSNAMESPACEURI = "http://www.w3.org/2000/xmlns/";

	public static final String XMLNS_PREFIX = "xmlns";

	public StaticPrefixResolver(Map<String, String> prefixMap) {
		if (prefixMap != null) {
			this.prefixMap = new HashMap<String, String>(prefixMap);
		} else {
			this.prefixMap = new HashMap<String, String>();
		}
		this.prefixMap.put(XML_PREFIX, S_XMLNAMESPACEURI);
		this.prefixMap.put(XMLNS_PREFIX, S_XMLNSNAMESPACEURI);
	}

	/**
	 * Return the base identifier for the document.
	 */
	public String getBaseIdentifier() {
		return (String) prefixMap.get(EMPTY_STRING);
	}

	/**
	 * Retrieve the URI corresponding to the supplied prefix.
	 * 
	 * @param prefix
	 *            the prefix to match to a URI.
	 */
	public String getNamespaceForPrefix(String prefix) {
		return (String) prefixMap.get(prefix);
	}

	/**
	 * Retrieve a namespace in context. Note that this is meaningless for our
	 * implementation, as the prefixes are static for the entire document and
	 * independent of any local declarations.
	 * 
	 * @param prefix
	 *            the prefix to query
	 * @param n
	 *            the context Node (ignored)
	 */
	public String getNamespaceForPrefix(String prefix, Node n) {
		return getNamespaceForPrefix(prefix);
	}

	public boolean handlesNullPrefixes() {
		return false;
	}
}
