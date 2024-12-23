package ptithcm.datt.WarehouseManager.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.config.SecurityConfig;
import ptithcm.datt.WarehouseManager.repository.StaffRepository;
import ptithcm.datt.WarehouseManager.request.AccountRequest;
import ptithcm.datt.WarehouseManager.request.AuthenticationRequest;
import ptithcm.datt.WarehouseManager.request.IntrospectRequest;
import ptithcm.datt.WarehouseManager.request.StaffRequest;
import ptithcm.datt.WarehouseManager.response.AuthenticationResponse;
import ptithcm.datt.WarehouseManager.response.IntrospectResponse;
import ptithcm.datt.WarehouseManager.model.Account;
import ptithcm.datt.WarehouseManager.model.Role;
import ptithcm.datt.WarehouseManager.model.Staff;
import ptithcm.datt.WarehouseManager.repository.AccountRepository;
import ptithcm.datt.WarehouseManager.repository.RoleRepository;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MailService emailService;
    @NonFinal
    protected static final String SIGNER_KEY = "CYG0G6yaS0rd/FExfwMHVIeyPmwv/FZphommJCN8CwCVFv/IwJIAWiMcPxWzYkF6";
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    StaffService staffService;
    @Autowired
    SecurityConfig securityConfig;
    UserDetails userDetails;

    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public Account createAccount(AccountRequest accountDTO) {
        Role role = roleRepository.findById(accountDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setRole(role);

        Staff newStaff = new Staff();
        newStaff.setStaffName(accountDTO.getUsername());
        newStaff.setEmail(accountDTO.getEmail());
        Staff savedStaff = staffService.createStaff(newStaff);
        account.setStaff(savedStaff);

        account.setCreateAt(LocalDateTime.now());
        return accountRepository.save(account);
    }



    public List<Account> addStaff(List<StaffRequest> staffRequests) throws MessagingException {
        List<Account> accounts = new ArrayList<>();

        for (StaffRequest staffRequest : staffRequests) {
            Role role = roleRepository.findByRoleName(staffRequest.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            if (accountRepository.existsByEmail(staffRequest.getEmail())) {
                throw new RuntimeException("Email đã tồn tại trên hệ thống: " + staffRequest.getEmail());
            }

            Account account = new Account();
            account.setCreateAt(LocalDateTime.now());
            account.setUsername(staffRequest.getUsername());
            account.setEmail(staffRequest.getEmail());
            account.setStatus("active");

            String randomPassword = generateRandomPassword();
            account.setPassword(passwordEncoder.encode(randomPassword));
            account.setRole(role);

            Staff newStaff = new Staff();
            newStaff.setStaffName(staffRequest.getStaffName());
            newStaff.setPhoneNumber(staffRequest.getPhoneNumber());
            newStaff.setEmail(staffRequest.getEmail());
            newStaff.setImg(staffRequest.getPicture());
            newStaff.setAddress(staffRequest.getAddress());
            newStaff.setDob(staffRequest.getDob());
            newStaff.setHiredDate(LocalDateTime.now());
            newStaff.setIsEnable(true);

            Staff savedStaff = staffService.createStaff(newStaff);
            account.setStaff(savedStaff);

            Account savedAccount = accountRepository.save(account);
            accounts.add(savedAccount);

            // Send email with unencoded password
            String subject = "Welcome to Warehouse - Account Information";
            String content = "<!DOCTYPE html>"
                    + "<html lang=\"vi\">"
                    + "<head>"
                    + "    <meta charset=\"UTF-8\">"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "    <title>Thông Tin Tài Khoản Nhân Viên</title>"
                    + "    <style>"
                    + "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; }"
                    + "        .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                    + "        .header { text-align: center; margin-bottom: 20px; }"
                    + "        .header h1 { color: #333333; font-size: 24px; }"
                    + "        .content { font-size: 16px; color: #333333; line-height: 1.5; }"
                    + "        .account-info { margin: 20px 0; font-weight: bold; text-align: center; color: #007bff; }"
                    + "        .footer { font-size: 14px; color: #888888; text-align: center; margin-top: 20px; }"
                    + "        .footer a { color: #007bff; text-decoration: none; }"
                    + "        .footer a:hover { text-decoration: underline; }"
                    + "    </style>"
                    + "</head>"
                    + "<body>"
                    + "    <div class=\"container\">"
                    + "        <div class=\"header\">"
                    + "            <h1>Thông Tin Tài Khoản Nhân Viên</h1>"
                    + "        </div>"
                    + "        <div class=\"content\">"
                    + "            <p>Chào <strong>" + staffRequest.getEmail() + "</strong>,</p>"
                    + "            <p>Tài khoản của bạn đã được tạo thành công. Dưới đây là thông tin đăng nhập:</p>"
                    + "            <div class=\"account-info\">"
                    + "                <p><strong>Tên đăng nhập:</strong> " + staffRequest.getUsername() + "</p>"
                    + "                <p><strong>Mật khẩu:</strong> " + randomPassword + "</p>"
                    + "            </div>"
                    + "            <p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu tiên.</p>"
                    + "        </div>"
                    + "        <div class=\"footer\">"
                    + "            <p>Trân trọng,</p>"
                    + "            <p><strong>WAREHOUSE</strong><br>"
                    + "            <a href=\"mailto:thithuytran74@gmail.com\">thithuytran74@gmail.com</a><br>"
                    + "            <a href=\"#\">CLOTHING STORE</a></p>"
                    + "        </div>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";

            sendMail(staffRequest.getEmail(), subject, content);
        }

        return accounts;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));

        // Sử dụng PasswordEncoder để kiểm tra mật khẩu
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!authenticated) {
            throw new BadCredentialsException("Mật khẩu không chính xác");
        }

        // Lấy danh sách quyền của người dùng từ cơ sở dữ liệu
        List<GrantedAuthority> authorities = getAuthoritiesForUser(account.getUsername());

        // Tạo UserDetails từ account và authorities
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                authorities // Chuyển authorities vào UserDetails
        );

        // Tạo Authentication object với userDetails đã được khởi tạo
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo JWT token
        String token = generateToken(request.getUsername(), authentication);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    private List<GrantedAuthority> getAuthoritiesForUser(String username) {
        List<Role> roles = roleRepository.findByAccounts_Username(username);
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            System.out.println("Danh sách quyền: " + role.getRoleName()); // Print the role name or any relevant property
        }
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return authorities;
    }

    private String generateToken(String username, Authentication auth) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String authoritiesString = populateAuthorities(authorities);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("datn.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("username", username)
                .claim("authorities", authoritiesString)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.err.println("Cannot create token: " + e.getMessage());
            throw new RuntimeException("Cannot create token", e);
        }
    }

    // Phương thức chuyển đổi danh sách quyền thành chuỗi
    public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exporyTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && exporyTime.after(new Date()))
                .build();

    }

    public void sendMail(String email, String subject, String content) throws MessagingException {
        emailService.sendMail(email, subject, content);
    }

    public boolean checkIfAccountExists(String username) {
        return accountRepository.existsByUsername(username);
    }



}
