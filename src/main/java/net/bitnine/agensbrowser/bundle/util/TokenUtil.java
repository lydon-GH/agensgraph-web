//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.bitnine.agensbrowser.bundle.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.bitnine.agensbrowser.bundle.message.ClientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil implements Serializable {
    private static final long serialVersionUID = -3301605591108950415L;
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_USERIP = "addr";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";
    @Value("${agens.jwt.secret}")
    private String secret;
    @Value("${agens.jwt.expiration}")
    private Long expiration;

    public TokenUtil() {
    }

    public String getUserNameFromToken(String token) {
        String userName;
        try {
            Claims claims = this.getClaimsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception var4) {
            userName = null;
        }

        return userName;
    }

    public String getUserIpFromToken(String token) {
        String userIp;
        try {
            Claims claims = this.getClaimsFromToken(token);
            userIp = new String((String)claims.get("addr"));
        } catch (Exception var4) {
            userIp = null;
        }

        return userIp;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            Claims claims = this.getClaimsFromToken(token);
            created = new Date((Long)claims.get("created"));
        } catch (Exception var4) {
            created = null;
        }

        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = this.getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception var4) {
            expiration = null;
        }

        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            Claims claims = this.getClaimsFromToken(token);
            audience = (String)claims.get("audience");
        } catch (Exception var4) {
            audience = null;
        }

        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        } catch (Exception var4) {
            claims = null;
        }

        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.expiration * 1000L);
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return lastPasswordReset != null && created.before(lastPasswordReset);
    }

    private static final String generateAudience(Device device) {
        String audience = "unknown";
        if (device.isNormal()) {
            audience = "web";
        } else if (device.isTablet()) {
            audience = "tablet";
        } else if (device.isMobile()) {
            audience = "mobile";
        }

        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = this.getAudienceFromToken(token);
        return "tablet".equals(audience) || "mobile".equals(audience);
    }

    public String generateToken(ClientDto client) {
        Map<String, Object> claims = new HashMap();
        claims.put("sub", client.getUserName());
        claims.put("addr", client.getUserIp());
        claims.put("audience", generateAudience(client.getDevice()));
        claims.put("created", new Date());
        return this.generateToken((Map)claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate()).signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        Date created = this.getCreatedDateFromToken(token);
        return !this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!this.isTokenExpired(token) || this.ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = this.getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = this.generateToken((Map)claims);
        } catch (Exception var4) {
            refreshedToken = null;
        }

        return refreshedToken;
    }

    public Boolean validateToken(String token, ClientDto client) {
        String userName = this.getUserNameFromToken(token);
        String userIp = this.getUserIpFromToken(token);
        String audience = this.getAudienceFromToken(token);
        return client.getUserName().equals(userName) && client.getUserIp().equals(userIp) && generateAudience(client.getDevice()).equals(audience) && !this.isTokenExpired(token) ? true : false;
    }
}
