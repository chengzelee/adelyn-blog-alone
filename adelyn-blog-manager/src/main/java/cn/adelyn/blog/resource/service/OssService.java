package cn.adelyn.blog.resource.service;

import cn.adelyn.blog.resource.config.OssConfig;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class OssService {

    private final OSS ossClient;
    private final OssConfig ossConfig;

    public void putObject(String objectAbsolutePath, InputStream inputStream) {
        try {
            ossClient.putObject(ossConfig.getBucketName(), objectAbsolutePath, inputStream);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.", oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.", ce);
        }
    }

    public URL generatePutObjectUrl(String objectAbsolutePath, long validTime) {
        // 设置请求头。
        Map<String, String> headers = new HashMap<>();
        /*// 指定Object的存储类型。
        headers.put(OSSHeaders.STORAGE_CLASS, StorageClass.Standard.toString());
        // 指定ContentType。
        headers.put(OSSHeaders.CONTENT_TYPE, "text/txt");*/

        // 设置用户自定义元数据。
        Map<String, String> userMetadata = new HashMap<>();
        /*userMetadata.put("key1","value1");
        userMetadata.put("key2","value2");*/

        URL signedUrl = null;
        try {
            // 指定生成的签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + validTime);

            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ossConfig.getBucketName(), objectAbsolutePath, HttpMethod.PUT);
            // 设置过期时间。
            request.setExpiration(expiration);

            // 将请求头加入到request中。
            request.setHeaders(headers);
            // 添加用户自定义元数据。
            request.setUserMetadata(userMetadata);

            // 通过HTTP PUT请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.", oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.", ce);
        }

        return signedUrl;
    }

    public void putObjWithCallBack(String objectAbsolutePath, InputStream inputStream) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), objectAbsolutePath, inputStream);

            // 上传回调参数。
            Callback callback = new Callback();
            callback.setCallbackUrl(ossConfig.getCallBackUrl());
            //（可选）设置回调请求消息头中Host的值，即您的服务器配置Host的值。
            // callback.setCallbackHost("yourCallbackHost");
            // 设置发起回调时请求body的值。
            callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
            // 设置发起回调请求的Content-Type。
            callback.setCalbackBodyType(Callback.CalbackBodyType.JSON);
            // 设置发起回调请求的自定义参数，由Key和Value组成，Key必须以x:开始。
            callback.addCallbackVar("x:path", objectAbsolutePath);
            putObjectRequest.setCallback(callback);

            ossClient.putObject(putObjectRequest);
        }  catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.", oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.", ce);
        }
    }

    public byte[] getObject(String objectAbsolutePath) {
        byte[] objectBytes = new byte[0];
        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元数据。
            OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), objectAbsolutePath);
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            objectBytes = content.readAllBytes();
            content.close();
            return objectBytes;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.", oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.", ce);
        } catch (IOException e) {
            log.error("", e);
        }

        return objectBytes;
    }

    /**
     * 自定义域名转发，避免强制下载
     * https://help.aliyun.com/zh/oss/support/0048-00000113?spm=a2c4g.11186623.0.i13#main-2292898
     */
    public URL generateGetObjectUrl(String objectAbsolutePath, long validTime) {
        URL signedUrl = null;
        try {
            // 指定生成的签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + validTime);

            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(ossConfig.getBucketName(), objectAbsolutePath, HttpMethod.GET);
            // 设置过期时间。
            request.setExpiration(expiration);

            // 通过HTTP GET请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);

            signedUrl = replaceDomain(signedUrl, ossConfig.getAdelynOssDomain());
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.", oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.", ce);
        } catch (MalformedURLException urle) {
            log.error("replace url domain err", urle);
        }

        return signedUrl;
    }

    public void deleteObject(String objectAbsolutePath) {
        try {
            // 删除文件。
            ossClient.deleteObject(ossConfig.getBucketName(), objectAbsolutePath);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.", oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.", ce);
        }
    }

    private URL replaceDomain(URL originalURL, String newDomain) throws MalformedURLException {
        String file = originalURL.getFile();
        String newUrlString = newDomain + file;

        return URI.create(newUrlString).toURL();
    }
}
