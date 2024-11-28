package com.project.shopapp.configuaration;

import com.project.shopapp.dto.PaymentInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayConfig {
    @Getter
    @Value("${payment.vnPay.url}")
    String vnp_PayUrl;

    @Value("${payment.vnPay.returnUrl}")
    String vnp_ReturnUrl;

    @Value("${payment.vnPay.tmnCode}")
    String vnp_TmnCode;

    @Getter
    @Value("${payment.vnPay.secretKey}")
    String secretKey;

    @Value("${payment.vnPay.version}")
    String vnp_Version;

    @Value("${payment.vnPay.command}")
    String vnp_Command;

    @Value("${payment.vnPay.orderType}")
    String orderType;

    public Map<String, String> getVNPayConfig() {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        return vnpParamsMap;
    }
    static final Random RANDOM = new Random();

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException("Key or Data is null for HMAC SHA512");
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error generating HMAC SHA512 hash", ex);
        }
    }

    public static String getRandomNumber(int len) {
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public String getPaymentURL(PaymentInfo paymentInfo) {
        Map<String, String> params = getVNPayConfig();
        ZoneId zone = TimeZone.getTimeZone("GMT+7").toZoneId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        params.put("vnp_TxnRef", paymentInfo.getReference());
        params.put("vnp_OrderInfo", paymentInfo.getDescription());
        params.put("vnp_Amount", paymentInfo.getAmount().setScale(0, RoundingMode.DOWN).toString());
        params.put("vnp_CreateDate", formatter.format(paymentInfo.getCreatedAt().atZone(zone).toLocalDateTime()));
        params.put("vnp_ExpireDate", formatter.format(paymentInfo.getExpiredAt().atZone(zone).toLocalDateTime()));
        params.put("vnp_IpAddr", paymentInfo.getIpAddress());

        String query = buildQuery(params, true);
        String checksum = VNPayConfig.hmacSHA512(getSecretKey(), buildQuery(params, false));
        query += "&vnp_SecureHash=" + checksum;

        return getVnp_PayUrl() + "?" + query;
    }

    private String buildQuery(Map<String, String> params, boolean encodeKey) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    Charset charset = StandardCharsets.US_ASCII;
                    String key = encodeKey ? URLEncoder.encode(e.getKey(), charset) : e.getKey();
                    return key + "=" + URLEncoder.encode(e.getValue(), charset);
                })
                .collect(Collectors.joining("&"));
    }

}

