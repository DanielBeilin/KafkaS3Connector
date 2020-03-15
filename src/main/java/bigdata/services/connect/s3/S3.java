package bigdata.services.connect.s3;

import java.util.Map;
import java.util.Objects;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.S3ClientOptions;

public class S3 {
    public static AmazonS3 s3client(Map<String,String> config) {

        String s3AccessKey = config.get("s3.accessKey");
        String s3SecretKey = config.get("s3.secretKey");

        AmazonS3 s3Client;
        /*
            Checks if either the property access key or secret key not defined
             If the credentials not supplied use the default supply chain
             (Env + Java properties + profile + instance role)
        */
        if ((s3AccessKey != null) && !Objects.equals(s3AccessKey, "") &&
                (s3SecretKey != null) && !Objects.equals(s3SecretKey, ""))  {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
             s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();
        } else s3Client = AmazonS3ClientBuilder.standard().build();

        // If the user defined an explicit s3 endpoint
        String s3Endpoint = config.get("s3.endpoint");
        if ((s3Endpoint != null) && !Objects.equals(s3Endpoint, "")) {
            s3Client.setEndpoint(s3Endpoint);
        }


        // If the user defined the path style for
        boolean s3PathStyle = Boolean.parseBoolean(config.get("s3.path_style"));
        if (s3PathStyle) {
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
        }
        return s3Client;
    }

}

