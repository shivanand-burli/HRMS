package com.hrms.app.service;

import java.time.Duration;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;

import com.hrms.app.config.ValidationError;
import com.hrms.app.dao.LoginRes;
import com.hrms.app.util.Constants;
import com.hrms.app.util.Role;
import com.hrms.app.util.Validator;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

@Default
@ApplicationScoped
public class AuthService {

    @Inject
    JWTParser parser;

    public LoginRes generateToken(String companyId, String employeeId, Role role) {
        final JwtClaimsBuilder accessTokenBuilder = Jwt.issuer(Constants.JWT_ISSUER)
                .upn(Constants.JWT_UPN)
                .claim(Constants.X_CMP_ID, companyId)
                .groups(role.name())
                .expiresIn(Duration.ofMinutes(15));

        if (employeeId != null) {
            accessTokenBuilder.claim(Constants.X_EMP_ID, employeeId);
        }

        final JwtClaimsBuilder refreshTokenBuilder = Jwt.issuer(Constants.JWT_ISSUER)
                .upn(Constants.JWT_UPN)
                .claim(Constants.X_CMP_ID, companyId)
                .claim(Constants.RT_GRP_ID, Role.COMPANY.equals(role) ? Constants.RT_COMPANY : Constants.RT_EMPLOYEE)
                .expiresIn(Duration.ofDays(20));

        if (employeeId != null) {
            refreshTokenBuilder.claim(Constants.X_EMP_ID, employeeId);
        }

        return new LoginRes(accessTokenBuilder.sign(), refreshTokenBuilder.sign());
    }

    public LoginRes regenerateToken(final String refreshToken) {
        Validator.requiresNonNull(refreshToken, "refreshToken");

        // Have to extact Info from refresh token if valid!
        final JsonWebToken parsedRefreshToken = parseToken(
                refreshToken.startsWith("Bearer") ? refreshToken.replace("Bearer ", "") : refreshToken);

        final JwtClaimsBuilder accessTokenBuilder = Jwt.issuer(parsedRefreshToken.getIssuer())
                .upn(parsedRefreshToken.getName());

        // ADD GROUP:
        final Object rtGroup = parsedRefreshToken.getClaim(Constants.RT_GRP_ID);
        if (Validator.isNull(rtGroup)) {
            throw new ValidationError(Constants.INVALID_RT_TOKEN);
        }

        switch (rtGroup.toString()) {
            case Constants.RT_COMPANY:
                accessTokenBuilder.groups(Role.COMPANY.name());
                break;
            case Constants.RT_EMPLOYEE:
                accessTokenBuilder.groups(Role.EMPLOYEE.name());
                break;
            default:
                throw new ValidationError(Constants.INVALID_RT_TOKEN);
        }

        // ADD CLAIMS:
        final Object companyId = parsedRefreshToken.getClaim(Constants.X_CMP_ID);
        if (Validator.isNonNull(companyId)) {
            accessTokenBuilder.claim(Constants.X_CMP_ID, companyId);
        }

        final Object employeeId = parsedRefreshToken.getClaim(Constants.X_EMP_ID);
        if (Validator.isNonNull(employeeId)) {
            accessTokenBuilder.claim(Constants.X_EMP_ID, employeeId);
        }

        // Done
        return new LoginRes(accessTokenBuilder.sign(), refreshToken);
    }

    private JsonWebToken parseToken(String token) {
        try {
            return parser.parse(token); // throws ParseException if invalid
        } catch (ParseException e) {
            throw new ValidationError("Session expired. Login back!"); // invalid token or expired
        }
    }

    // Hash password for storage
    public String hashPass(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Verify password at login
    public void verifyPass(String plainPassword, String hashedPassword) {
        if (!BCrypt.checkpw(plainPassword, hashedPassword)) {
            throw new ValidationError("Invalid Password!");
        }
    }
}
