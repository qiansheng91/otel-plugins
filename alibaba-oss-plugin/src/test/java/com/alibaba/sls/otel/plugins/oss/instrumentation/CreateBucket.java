package com.alibaba.sls.otel.plugins.oss.instrumentation;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.model.CreateBucketRequest;

public class CreateBucket {

  public static void main(String[] args) throws Exception {
    // yourEndpoint填写Bucket所在地域对应的Endpoint。
    String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
    EnvironmentVariableCredentialsProvider credentialsProvider =
        CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
    // 填写Bucket名称。
    String bucketName = "examplebucket";
    // 填写资源组ID。如果不填写资源组ID，则创建的Bucket属于默认资源组。
    // String rsId = "rg-aek27tc****";

    // 创建OSSClient实例。
    OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

    try {
      // 创建CreateBucketRequest对象。
      CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);

      // 如果创建存储空间的同时需要指定存储类型、存储空间的读写权限、数据容灾类型, 请参考如下代码。
      // 此处以设置存储空间的存储类型为标准存储为例介绍。
      // createBucketRequest.setStorageClass(StorageClass.Standard);
      // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
      // createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
      // 设置存储空间读写权限为公共读，默认为私有。
      // createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);

      // 在支持资源组的地域创建Bucket时，您可以为Bucket配置资源组。
      // createBucketRequest.setResourceGroupId(rsId);

      // 创建存储空间。
      ossClient.createBucket(createBucketRequest);
    } catch (OSSException oe) {
      System.out.println(
          "Caught an OSSException, which means your request made it to OSS, "
              + "but was rejected with an error response for some reason.");
      System.out.println("Error Message:" + oe.getErrorMessage());
      System.out.println("Error Code:" + oe.getErrorCode());
      System.out.println("Request ID:" + oe.getRequestId());
      System.out.println("Host ID:" + oe.getHostId());
    } catch (ClientException ce) {
      System.out.println(
          "Caught an ClientException, which means the client encountered "
              + "a serious internal problem while trying to communicate with OSS, "
              + "such as not being able to access the network.");
      System.out.println("Error Message:" + ce.getMessage());
    } finally {
      if (ossClient != null) {
        ossClient.shutdown();
      }
    }
  }
}
