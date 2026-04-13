package sfs.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class JwsService {

    private static final String SECRET = "ToJestBardzoTajnyKluczDoPodpisuDanychJWS123!";

    // podpis etag na podstawie id i wersji obiektu
    public String createSignature(String id, Long version) {
        try {
            // create nagłowlak JWS
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            // payload np: "id=123,version=5"
            String payloadContent = id + "," + version;
            Payload payload = new Payload(payloadContent);

            // towrzenie obiekt JWS
            JWSObject jwsObject = new JWSObject(header, payload);

            // podpisanie
            JWSSigner signer = new MACSigner(SECRET);
            jwsObject.sign(signer);

            return jwsObject.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("Błąd generowania podpisu JWS", e);
        }
    }

    public boolean verifySignature(String rawToken, String id, Long version) {
        try {
            String token = extractToken(rawToken);

            JWSObject jwsObject = JWSObject.parse(token);

            // weryfikacja podpisu
            JWSVerifier verifier = new MACVerifier(SECRET);
            if (!jwsObject.verify(verifier)) {
                return false;
            }

            // co jest w payload
            String payloadContent = jwsObject.getPayload().toString();
            String expectedContent = id + "," + version;

            return payloadContent.equals(expectedContent);

        } catch (ParseException | JOSEException e) {
            System.err.println("Błąd weryfikacji JWS: " + e.getMessage());
            return false;
        }
    }

    // wyciaganie tokena
    public String extractToken(String header) {
        if (header == null) return null;
        return header.replace("\"", "");
    }
}