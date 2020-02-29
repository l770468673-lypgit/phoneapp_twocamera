package com.estone.bank.estone_appsmartlock.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * OkHttp的工具类.
 */
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;
    private static Handler handler = new Handler();
    private static boolean loginFlag = true;

    private static String platform = "Android";//平台.
    private static String systemVersion;//系统版本.
    private static String phoneType;//机型.
    private static String appVersion;//app版本.

    /**
     * 初始化OkHttp.
     *
     * @param context
     */
    public static void initOkHttp(Context context) {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();


    }

    /**
     * POST请求. 提交表单.
     */
    public static void upDataForPost(final String url,
                                     Map<String, String> params,
                                     final OnDownDataListener onDownDataListener) {

        if (params.size() <= 0) {
            return;
        }

        FormBody.Builder builder = new FormBody.Builder();

        for (String key : params.keySet()) {
            String value = params.get(key);
            builder.add(key, value);
            LUtils.d("SplashActivity", value);
        }
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                //                .addHeader("platform", platform)
                //                .addHeader("systemVersion", systemVersion)
                //                .addHeader("phoneType", phoneType)
                //                .addHeader("appVersion", appVersion)
                .url(url)
                .post(formBody)
                .build();

        okHttpClient.newCall(request)
                .enqueue(new ResponseCallback(onDownDataListener, url));
    }

    /**
     * 执行请求完成之后的回调.
     */
    public static class ResponseCallback implements Callback {

        private OnDownDataListener cb;
        private String url;

        /**
         * @param cb  请求完成后的回调
         * @param url 请求 URL
         */
        public ResponseCallback(OnDownDataListener cb, String url) {
            this.cb = cb;
            this.url = url;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            if (response.code() == 200) {
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cb == null) {
                            return;
                        }
                        try {
                            cb.onResponse(url, str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Log.e("print", "数据请求失败：" + response.code());
            }
        }
    }

    /**
     * post请求.提交字符串请求.
     */
    public static void upStrForPost(final String url,
                                    String string,
                                    final OnDownDataListener onDownDataListener) {
        MediaType mediatype = MediaType.parse("application/json; charset=utf-8");//text/x-markdown; charset=utf-8

        Request request = new Request.Builder()
                .addHeader("platform", platform)
                .addHeader("systemVersion", systemVersion)
                .addHeader("phoneType", phoneType)
                .addHeader("appVersion", appVersion)
                .url(url)
                .post(RequestBody.create(mediatype, string))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                Log.e("print", "登录失败：==");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDownDataListener != null) {
                            onDownDataListener.onFailure(url, e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {//成功.
                    final String str = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onDownDataListener != null) {
                                try {
                                    onDownDataListener.onResponse(url, str);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    Log.e("print", "请求失败：" + response.code());
                }

            }
        });
    }

    /**
     * post请求.提交字符串携带Token.
     */
    public static void upStrForPostWithToken(final String url,
                                             String json,
                                             String token,
                                             final Context context,
                                             final OnDownDataListener onDownDataListener) {
        MediaType mediatype = MediaType.parse("application/json; charset=utf-8");//text/x-markdown; charset=utf-8

        Request request = new Request.Builder()
                .addHeader("USER_TOKEN", token)
                .addHeader("platform", platform)
                .addHeader("systemVersion", systemVersion)
                .addHeader("phoneType", phoneType)
                .addHeader("appVersion", appVersion)
                .url(url)
                .post(RequestBody.create(mediatype, json))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (onDownDataListener != null) {
                    onDownDataListener.onFailure(url, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code() == 200) {//成功.

                    loginFlag = true;

                    final String str = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onDownDataListener != null) {
                                try {
                                    onDownDataListener.onResponse(url, str);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else if (response.code() == 401) {//token 过期 重新登录.

                    if (loginFlag) {
                        //                        Intent intent = new Intent(context, LoginAct.class);
                        //                        context.startActivity(intent);
                        //                        MainAct.finishAct();
                        //                        SharedPreferencesUtil.putInt("loginState", 0);

                        loginFlag = false;
                    }

                } else {
                    Log.e("print", "请求错误： " + response.code());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "请求错误", Toast.LENGTH_SHORT).show();

                            if (onDownDataListener != null) {
                                onDownDataListener.onFailure(url, response.message());
                            }
                        }
                    });
                }

            }
        });
    }

    /**
     * 回调.
     */
    public interface OnDownDataListener {
        void onResponse(String url, String json) throws JSONException;

        void onFailure(String url, String error);
    }

    /**
     *       Map<String,String> map= new HashMap<>();
     map.put("actionId",Contants.ACTIONID94);
     map.put("adminId",mMUnionid1);
     map.put("imageBase64","123123123123");
     //                        map.put("imageBase64", ("data:image/jpeg;base64," + s));
     OkHttpUtil.upDataForPost("http://39.104.83.195:8080/guard/entranceGuard", map, new OkHttpUtil.OnDownDataListener() {
    @Override
    public void onResponse(String url, String json) throws JSONException {
    Gson gson = new Gson();
    bean_WX_XMlogin bean_wx_xMlogin = gson.fromJson(json, bean_WX_XMlogin.class);
    int errorCode = bean_wx_xMlogin.getErrorCode();
    if (errorCode ==0) {
    bean_WX_XMlogin.ExtraBean status = bean_wx_xMlogin.getExtra();
    String imageUrl = status.getImageUrl();

    LUtils.d(TAG, "  imageUrl ==" + imageUrl);
    }
    }

    @Override
    public void onFailure(String url, String error) {
    LUtils.d(TAG, "  onFailureonFailureonFailure  onFailure ==" + error);
    }
    });
     */
    /**
     * PUT请求. 提交json字符串.
     *
     * @param url
     * @param token
     * @param json
     * @param onDownDataListener
     */
    //    public static void upDataForPutWithToken(final String url,
    //                                             String token,
    //                                             String json,
    //                                             final Context context,
    //                                             final OnDownDataListener onDownDataListener) {
    //        MediaType mediatype = MediaType.parse("application/json; charset=utf-8");
    //
    //        Request request = new Request.Builder()
    //                .addHeader("USER_TOKEN", token)
    //                .addHeader("platform", platform)
    //                .addHeader("systemVersion", systemVersion)
    //                .addHeader("phoneType", phoneType)
    //                .addHeader("appVersion", appVersion)
    //                .url(url)
    //                .put(RequestBody.create(mediatype, json))
    //                .build();
    //
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                if (onDownDataListener != null) {
    //                    onDownDataListener.onFailure(url, e.getMessage());
    //                }
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, final Response response) throws IOException {
    //                if (response.code() == 200) {//成功.
    //
    //                    loginFlag = true;
    //
    //                    final String str = response.body().string();
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                try {
    //                                    onDownDataListener.onResponse(url, str);
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                            }
    //                        }
    //                    });
    //                } else if (response.code() == 401) {//token 过期 重新登录.
    //
    //                    if (loginFlag) {
    //                        Intent intent = new Intent(context, LoginAct.class);
    //                        context.startActivity(intent);
    //                        MainAct.finishAct();
    //                        SharedPreferencesUtil.putInt("loginState", 0);
    //
    //                        loginFlag = false;
    //                    }
    //
    //                } else {
    //                    Log.e("print", "请求错误： " + response.code());
    //
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                onDownDataListener.onFailure(url, response.message());
    //                            }
    //                        }
    //                    });
    //                }
    //
    //            }
    //        });
    //
    //
    //    }

    /**
     * PUT请求. 提交表单.
     *
     * @param url
     * @param token
     * @param hashMap
     * @param onDownDataListener
     */
    //    public static void upFormForPutWithToken(final String url,
    //                                             String token,
    //                                             HashMap<String, String> hashMap,
    //                                             final Context context,
    //                                             final OnDownDataListener onDownDataListener) {
    //
    //        if (hashMap.size() <= 0) {
    //            return;
    //        }
    //
    //        FormBody.Builder builder = new FormBody.Builder();
    //
    //        for (String key : hashMap.keySet()) {
    //            String value = hashMap.get(key);
    //            builder.add(key, value);
    //        }
    //        FormBody formBody = builder.build();
    //
    //        Request request = new Request.Builder()
    //                .addHeader("USER_TOKEN", token)
    //                .addHeader("platform", platform)
    //                .addHeader("systemVersion", systemVersion)
    //                .addHeader("phoneType", phoneType)
    //                .addHeader("appVersion", appVersion)
    //                .url(url)
    //                .put(formBody)
    //                .build();
    //
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, final IOException e) {
    //                handler.post(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        if (onDownDataListener != null) {
    //                            onDownDataListener.onFailure(url, e.getMessage());
    //                        }
    //                    }
    //                });
    //
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, final Response response) throws IOException {
    //                if (response.code() == 200) {//成功.
    //
    //                    loginFlag = true;
    //
    //                    final String str = response.body().string();
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                try {
    //                                    onDownDataListener.onResponse(url, str);
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                            }
    //                        }
    //                    });
    //                } else if (response.code() == 401) {//token 过期 重新登录.
    //
    //                    if (loginFlag) {
    //                        Intent intent = new Intent(context, LoginAct.class);
    //                        context.startActivity(intent);
    //                        MainAct.finishAct();
    //                        SharedPreferencesUtil.putInt("loginState", 0);
    //
    //                        loginFlag = false;
    //                    }
    //
    //                } else {
    //                    Log.e("print", "请求错误： " + response.code());
    //
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                onDownDataListener.onFailure(url, response.message());
    //                            }
    //                        }
    //                    });
    //                }
    //
    //            }
    //        });
    //
    //    }

    /**
     * PUT请求. 提交文件.
     *
     * @param url
     * @param token
     * @param params
     * @param onDownDataListener
     */
    //    public static void upFileForPutWithToken(final String url,
    //                                             String token,
    //                                             HashMap<String, Object> params,
    //                                             final Context context,
    //                                             final OnDownDataListener onDownDataListener) {
    //
    //        if (params.size() <= 0) {
    //            return;
    //        }
    //
    //        MultipartBody.Builder builder = new MultipartBody.Builder();
    //        builder.setType(MultipartBody.FORM);
    //
    //        for (String key : params.keySet()) {
    //            Object value = params.get(key);
    //            if (value instanceof File) {
    //                File file = (File) value;
    //                builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
    //                continue;
    //            }
    //            builder.addFormDataPart(key, value + "");
    //        }
    //
    //        RequestBody formBody = builder.build();
    //
    //        Request request = new Request.Builder()
    //                .addHeader("USER_TOKEN", token)
    //                .addHeader("platform", platform)
    //                .addHeader("systemVersion", systemVersion)
    //                .addHeader("phoneType", phoneType)
    //                .addHeader("appVersion", appVersion)
    //                .addHeader("Content-Type","multipart/form-data")
    //                .url(url)
    //                .put(formBody)
    //                .build();
    //
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, final IOException e) {
    //                handler.post(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        if (onDownDataListener != null) {
    //                            onDownDataListener.onFailure(url, e.getMessage());
    //                        }
    //                    }
    //                });
    //
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, final Response response) throws IOException {
    //                if (response.code() == 200) {//成功.
    //
    //                    loginFlag = true;
    //
    //                    final String str = response.body().string();
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                try {
    //                                    onDownDataListener.onResponse(url, str);
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                            }
    //                        }
    //                    });
    //                } else if (response.code() == 401) {//token 过期 重新登录.
    //
    //                    if (loginFlag) {
    //                        Intent intent = new Intent(context, LoginAct.class);
    //                        context.startActivity(intent);
    //                        MainAct.finishAct();
    //                        SharedPreferencesUtil.putInt("loginState", 0);
    //
    //                        loginFlag = false;
    //                    }
    //
    //                } else {
    //                    Log.e("print", "请求错误： " + response.code());
    //
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                onDownDataListener.onFailure(url, response.message());
    //                            }
    //                        }
    //                    });
    //                }
    //
    //            }
    //        });
    //
    //    }

    /**
     * PUT请求. 提交字符串.
     *
     * @param url
     * @param json
     * @param onDownDataListener
     */
    //    public static void upStrForPut(final String url,
    //                                   String json,
    //                                   final Context context,
    //                                   final OnDownDataListener onDownDataListener) {
    //        MediaType mediatype = MediaType.parse("application/json; charset=utf-8");
    //
    //        Request request = new Request.Builder()
    //                .addHeader("platform", platform)
    //                .addHeader("systemVersion", systemVersion)
    //                .addHeader("phoneType", phoneType)
    //                .addHeader("appVersion", appVersion)
    //                .url(url)
    //                .put(RequestBody.create(mediatype, json))
    //                .build();
    //
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                if (onDownDataListener != null) {
    //                    onDownDataListener.onFailure(url, e.getMessage());
    //                }
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, final Response response) throws IOException {
    //                if (response.code() == 200) {//成功.
    //
    //                    loginFlag = true;
    //
    //                    final String str = response.body().string();
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                try {
    //                                    onDownDataListener.onResponse(url, str);
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                            }
    //                        }
    //                    });
    //                } else if (response.code() == 401) {//token 过期 重新登录.
    //
    //                    if (loginFlag) {
    //                        Intent intent = new Intent(context, LoginAct.class);
    //                        context.startActivity(intent);
    //                        MainAct.finishAct();
    //                        SharedPreferencesUtil.putInt("loginState", 0);
    //
    //                        loginFlag = false;
    //                    }
    //
    //                } else {
    //                    Log.e("print", "请求错误： " + response.code());
    //
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                onDownDataListener.onFailure(url, response.message());
    //                            }
    //                        }
    //                    });
    //                }
    //
    //            }
    //        });
    //
    //
    //    }

    /**
     * DELETE请求.
     *
     * @param url
     * @param onDownDataListener
     */
    //    public static void upDataForDelete(final String url,
    //                                       String token,
    //                                       final Context context,
    //                                       final OnDownDataListener onDownDataListener) {
    //
    //        MediaType mediatype = MediaType.parse("application/json; charset=utf-8");
    //
    //        Request.Builder builder1 = new Request.Builder()
    //                .addHeader("USER_TOKEN", token)
    //                .addHeader("platform", platform)
    //                .addHeader("systemVersion", systemVersion)
    //                .addHeader("phoneType", phoneType)
    //                .addHeader("appVersion", appVersion)
    //                .url(url)
    //                .delete(RequestBody.create(mediatype, ""));
    //        Request request = builder1.build();
    //
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                if (onDownDataListener != null) {
    //                    onDownDataListener.onFailure(url, e.getMessage());
    //                }
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, final Response response) throws IOException {
    //                if (response.code() == 200) {//成功.
    //
    //                    loginFlag = true;
    //
    //                    final String str = response.body().string();
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                try {
    //                                    onDownDataListener.onResponse(url, str);
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                            }
    //                        }
    //                    });
    //                } else if (response.code() == 401) {//token 过期 重新登录.
    //
    //                    if (loginFlag) {
    //                        Intent intent = new Intent(context, LoginAct.class);
    //                        context.startActivity(intent);
    //                        MainAct.finishAct();
    //                        SharedPreferencesUtil.putInt("loginState", 0);
    //
    //                        loginFlag = false;
    //                    }
    //
    //                } else {
    //                    Log.e("print", "请求错误： " + response.code());
    //
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                onDownDataListener.onFailure(url, response.message());
    //                            }
    //                        }
    //                    });
    //                }
    //            }
    //        });
    //
    //    }

    /**
     * DELETE请求.
     *
     * @param url
     * @param onDownDataListener
     */
    //    public static void upStrForDelete(final String url,
    //                                      String token,
    //                                      String json,
    //                                      final Context context,
    //                                      final OnDownDataListener onDownDataListener) {
    //
    //        MediaType mediatype = MediaType.parse("application/json; charset=utf-8");
    //
    //        Request.Builder builder1 = new Request.Builder()
    //                .addHeader("USER_TOKEN", token)
    //                .addHeader("platform", platform)
    //                .addHeader("systemVersion", systemVersion)
    //                .addHeader("phoneType", phoneType)
    //                .addHeader("appVersion", appVersion)
    //                .url(url)
    //                .delete(RequestBody.create(mediatype, json));
    //        Request request = builder1.build();
    //
    //        okHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                if (onDownDataListener != null) {
    //                    onDownDataListener.onFailure(url, e.getMessage());
    //                }
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, final Response response) throws IOException {
    //                if (response.code() == 200) {//成功.
    //
    //                    loginFlag = true;
    //
    //                    final String str = response.body().string();
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                try {
    //                                    onDownDataListener.onResponse(url, str);
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                            }
    //                        }
    //                    });
    //                } else if (response.code() == 401) {//token 过期 重新登录.
    //
    //                    if (loginFlag) {
    //                        Intent intent = new Intent(context, LoginAct.class);
    //                        context.startActivity(intent);
    //                        MainAct.finishAct();
    //                        SharedPreferencesUtil.putInt("loginState", 0);
    //
    //                        loginFlag = false;
    //                    }
    //
    //                } else {
    //                    Log.e("print", "请求错误： " + response.code());
    //
    //                    handler.post(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            if (onDownDataListener != null) {
    //                                onDownDataListener.onFailure(url, response.message());
    //                            }
    //                        }
    //                    });
    //                }
    //            }
    //        });
    //
    //    }

    /**
     * 保存Bitmap到sdcard.
     *
     * @param b
     * @return
     */
    public static String saveBitmap(Bitmap b) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用.
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        File outfile = new File("/sdcard/TogWork");
        // 如果文件不存在，则创建一个新文件.
        if (!outfile.isDirectory()) {
            try {
                outfile.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String fname = outfile + "/" + sdf.format(new Date()) + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fname);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fname;
    }

}