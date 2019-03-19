package rest.test;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * RESTMan
 * rest.test
 *
 * @author BlueDriver
 * @email cpwu@foxmail.com
 * @date 2019/03/18 18:41 Monday
 */
public class Test {
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
    MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
    MediaType xmlType = MediaType.parse("application/xml; charset=utf-8");

    public Response post(String url, String data, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    @org.junit.Test
    public void test() {
//        ApplicationManager.getApplication().invokeLater(() -> {
//        });
//        RESTWindow dialog = new RESTWindow();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);

        try {
            Response resp = post("http://baidu.com", "", jsonType);
            System.out.println(resp.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
