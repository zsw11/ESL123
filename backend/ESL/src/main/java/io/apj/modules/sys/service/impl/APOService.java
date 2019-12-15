package io.apj.modules.sys.service.impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("APOService")
public class APOService {
	@Value("${APOIP}")
	private String ldapUrl;

	public void setLdapUrl(String url) {
		this.ldapUrl = url;
	}

	public boolean login(String username, String passwd) throws Exception {
		Hashtable<String, String> HashEnv = new Hashtable<String, String>();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		HashEnv.put(Context.SECURITY_PRINCIPAL, username);
		HashEnv.put(Context.SECURITY_CREDENTIALS, passwd);
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		HashEnv.put(Context.PROVIDER_URL, ldapUrl);

		try {
			InitialLdapContext ctx = new InitialLdapContext(HashEnv, null);
			return true;
		} catch (Exception e) {
			throw e;
		}

	}

//	public static void main(String[] args) {
//		APOService service = new APOService();
//		try {
//			service.login("apo\\cn000303", "123456");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
