package util;

import com.duytran.kdtrace.entity.HFUserContext;
import model.UserContext;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class Util {

    /**
     * Serialize user
     *
     * @param userContext
     */
    public static void writeUserContext(UserContext userContext) {
        ObjectOutputStream out = null;
        FileOutputStream file = null;
        try {
            String directoryPath = "msp/" + userContext.getAffiliation();
            String filePath = directoryPath + "/" + userContext.getName() + ".context";
            File directory = new File(directoryPath);
            if (!directory.exists())
                directory.mkdirs();

            file = null;
            file = new FileOutputStream(filePath);
            out = new ObjectOutputStream(file);

            out.writeObject(userContext);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (file != null)
                    file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deserialize user
     *
     * @param affiliation
     * @param username
     * @return
     * @throws Exception
     */
    public static UserContext readUserContext(String affiliation, String username) {
        UserContext uContext = null;
        FileInputStream fileStream = null;
        ObjectInputStream in = null;
        try {
            String filePath = "msp/" + affiliation + "/" + username + ".context";
            File file = new File(filePath);
            if (file.exists()) {
                fileStream = new FileInputStream(filePath);
                in = new ObjectInputStream(fileStream);
                uContext = (UserContext) in.readObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileStream != null)
                    fileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return uContext;
        }

    }
    // Filter channel name when create new channel
    public static String organizationFilter(String org) {
        if (org.equalsIgnoreCase("citynow"))
            return "OrgCitynow";
        if (org.equalsIgnoreCase("ctc"))
            return "OrgCTC";
        if (org.equalsIgnoreCase("ivs"))
            return "ivs";
        if (org.equalsIgnoreCase("almex"))
            return "almex";
        return org;
    }


    public static String convertToProjectPrivilegesString(Map<String, Set<String>> maps) {
        StringBuilder stringBuilder = new StringBuilder();
        maps.forEach((s, strings) ->
                strings.forEach(s1 -> {
                if (stringBuilder.length()>0)
                    stringBuilder.append(",");
                stringBuilder.append(s).append("-").append(s1);
            })
        );
        return stringBuilder.toString();
    }

    private static Enrollment generateEnrollment(byte[] encodedKey, String cert) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        return new X509Enrollment(privateKey, cert);
    }

    public static UserContext toUserContextFromHFUserContext(HFUserContext hfUserContext) {
        UserContext userContext = new UserContext();
        userContext.setName(hfUserContext.getName());
        userContext.setAccount(hfUserContext.getAccount());
        userContext.setAffiliation(hfUserContext.getAffiliation());
        userContext.setMspId(hfUserContext.getMspId());
        byte[] encodedPrivateKey = hfUserContext.getEncodedPrivateKey();
        String cert = hfUserContext.getCertificate();
        Enrollment enrollment;
        try {
            enrollment = generateEnrollment(encodedPrivateKey, cert);
            userContext.setEnrollment(enrollment);
            return userContext;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
