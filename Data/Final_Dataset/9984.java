package org.jboss.resteasy.plugins.server.vertx;

import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class VertxSecurityContext implements SecurityContext
{
   protected final Principal principal;
   protected final SecurityDomain domain;
   protected final String authScheme;
   protected final boolean isSecure;

   public VertxSecurityContext(Principal principal, SecurityDomain domain, String authScheme, boolean secure)
   {
      this.principal = principal;
      this.domain = domain;
      this.authScheme = authScheme;
      isSecure = secure;
   }

   public VertxSecurityContext()
   {
      this(null, null, null, false);
   }

   @Override
   public Principal getUserPrincipal()
   {
      return principal;
   }

   @Override
   public boolean isUserInRole(String role)
   {
      if (domain == null) return false;
      return domain.isUserInRole(principal, role);
   }

   @Override
   public boolean isSecure()
   {
      return isSecure;
   }

   @Override
   public String getAuthenticationScheme()
   {
      return authScheme;
   }
}
