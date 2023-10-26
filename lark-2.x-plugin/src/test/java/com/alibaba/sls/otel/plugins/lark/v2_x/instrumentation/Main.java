package com.alibaba.sls.otel.plugins.lark.v2_x.instrumentation;

import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReq;
import com.lark.oapi.service.docx.v1.model.CreateDocumentReqBody;
import com.lark.oapi.service.docx.v1.model.CreateDocumentResp;

public class Main {
  public static void main(String arg[]) throws Exception {
    // 构建client
    Client client = Client.newBuilder("appId", "appSecret").build();

    // 发起请求
    CreateDocumentResp resp =
        client
            .docx()
            .document()
            .create(
                CreateDocumentReq.newBuilder()
                    .createDocumentReqBody(
                        CreateDocumentReqBody.newBuilder()
                            .title("title")
                            .folderToken("fldcniHf40Vcv1DoEc8SXeuA0Zd")
                            .build())
                    .build());

    // 处理服务端错误
    if (!resp.success()) {
      System.out.println(
          String.format(
              "code:%s,msg:%s,reqId:%s", resp.getCode(), resp.getMsg(), resp.getRequestId()));
      return;
    }

    // 业务数据处理
    System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
  }
}
