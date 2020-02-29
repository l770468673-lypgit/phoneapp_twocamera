package com.estone.bank.estone_appsmartlock.https;

import com.estone.bank.estone_appsmartlock.https.beans.AppUpdate;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_AllDevices;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_GateWayBind;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_GateWayLine;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_HeadPic;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_login;
import com.estone.bank.estone_appsmartlock.https.beans.Bean_userInfo;
import com.estone.bank.estone_appsmartlock.https.beans.ChangeRoomName;
import com.estone.bank.estone_appsmartlock.https.beans.ResponsHead;
import com.estone.bank.estone_appsmartlock.https.beans.User_AllDevices;
import com.estone.bank.estone_appsmartlock.https.beans.User_AllDevices2;
import com.estone.bank.estone_appsmartlock.https.beans.bean_Alllookinfor;
import com.estone.bank.estone_appsmartlock.https.beans.bean_WX_XMlogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Define all rest API with server here Use open source Retrofit for http access
 * http://square.github.io/retrofit/
 */
public interface ClientRestAPI {


    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Bean_AllDevices> queryListHome(@Field("actionId") String actionId,
                                        @Field("adminId") String adminId);

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<User_AllDevices2> queryUserdevice(@Field("actionId") String actionId,
                                           @Field("adminId") String adminId,
                                           @Field("start") long starttime,
                                           @Field("end") long endtime,
                                           @Field("devId") String devId
    );


    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_Alllookinfor> querylookinformation(@Field("actionId") String actionId,
                                                 @Field("devId") String devId,
                                                 @Field("start") long starttime,
                                                 @Field("end") long endtime);

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResponsHead> addBookinformation(@Field("actionId") String actionId,
                                         @Field("adminId") String adminId,
                                         @Field("devId") String devId,
                                         @Field("idNumber") String idNumber,
                                         @Field("checkInStart") long checkInStart,
                                         @Field("checkInEnd") long checkInEnd,
                                         @Field("mobile") String mobile,
                                         @Field("reserveName") String reserveName,
                                         @Field("avatarBase64") String avatarBase64
    );

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_WX_XMlogin> registToXM(@Field("actionId") String actionId,
                                     @Field("adminId") String adminId,//用户名，微信UUID
                                     @Field("imageBase64") String imageBase64 //用户头像的base64
    );

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResponsHead> bindDevices(@Field("actionId") String actionId,
                                  @Field("devId") String devId,
                                  @Field("cameraId") String cameraId,
                                  @Field("gatewayId") String gatewayId,
                                  @Field("adminId") String adminId,
                                  @Field("roomId") String roomId,
                                  @Field("roomName") String roomName,
                                  @Field("desc") String desc,
                                  @Field("imageBase64") String imageBase64
    );


    // 移除设备
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResponsHead> remooveDevices(@Field("actionId") String actionId,
                                     @Field("devId") String devId//用户名，微信UUID

    );

    // 删除预定信息
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResponsHead> removebookinformation(@Field("actionId") String actionId,
                                            @Field("devId") String devId,
                                            @Field("idNumber") String idNumber,
                                            @Field("checkInStart") long checkInStart,
                                            @Field("checkInEnd") long checkInEnd

    );

    // 远程开门
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResponsHead> remountiopendoor(@Field("actionId") String actionId,
                                       @Field("devId") String devId,
                                       @Field("adminId") String adminId
    );


    //改变用户头像
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    Call<Bean_HeadPic> changeUserHeadPic(@Field("actionId") String actionId,
                                         @Field("adminId") String adminId,
                                         @Field("imageBase64") String imageBase64
    );

    // 登录服务器
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Bean_login> loginESTServer(@Field("actionId") String actionId,
                                    @Field("adminId") String adminId);

    //改变房间图片
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Bean_HeadPic> changeRoompic(@Field("actionId") String actionId,
                                     @Field("devId") String devId,
                                     @Field("roomId") String roomId,
                                     @Field("imageBase64") String imageBase64

    );

    //查询绑定关系
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Bean_GateWayBind> queryGateWayBind(@Field("actionId") String actionId,
                                            @Field("cameraId") String cameraId);//查询绑定关系

    // 获取身份信息
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Bean_userInfo> queryInfoUser(@Field("actionId") String actionId,
                                      @Field("sn") String sn);

    // 获取网管状态
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Bean_GateWayLine> queryGtewayIdStutstion(@Field("actionId") String actionId,
                                                  @Field("gatewayId") String gatewayId);

    // 获取网管状态
    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ChangeRoomName> changeRoomName(@Field("actionId") String actionId,
                                        @Field("devId") String devId,
                                        @Field("roomId") String roomId,
                                        @Field("roomName") String roomName);


    //提前退房

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResponsHead> checkOut(@Field("actionId") String actionId,
                               @Field("devId") String devId,
                               @Field("idNumber") String idNumber,
                               @Field("checkInEnd") long checkInEnd);

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<AppUpdate> upDateApp(@Field("actionId") String actionId,
                              @Field("versionId") String versionId);


}
