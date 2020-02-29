package com.estone.bank.estone_appsmartlock.yiding

/**
 *   Created by Bingo on 2018/4/13.
 *
 *   下列参数为连接设备的必须参数,请联系厂商获取参数
 *   sn:HCYB04YB00001454455F9
     apptype:ybell
     did:SZHCB-006346-FGPDW
     address:120.79.143.73
     initString:EFGBFFBJKDJAGMJDEGGAFCENHBNLHMNAHAFKBFCHABJGLILGDKAGCEPMGDLBIBLGADNJKKDHOMNMBJCNJNMJ
 * 设备相关的必要信息
 * 每个设备都具有不同的参数
 */
class DevConfig {
//    companion object {
//        //        、、1111111111111111111111111
//        // 请联系厂商获取参数
//          val deviceSn = "HCYB04YB00001454"             // 替换成你获取的参数
//          val deviceDid = "SZHCB-006346-FGPDW"          // 1454替换成你获取的参数
//          val deviceInitString = "EFGBFFBJKDJKGGJJEEGIFKEAHDNJDONFCLABAMCDEEIMPHLEHDAEDAKHGCOPNPLOFGIOKJHGOEMIEMGAILJNJOEBNKKLFBDGFDDMCIEHNDPD"  // 1454替换成你获取的参数
//          val deviceAppType = "ybell"        // 替换成你获取的参数
//          val deviceAddress = "120.79.143.73"  // 替换成你获取的参数
//
//    }


    companion object {
        var deviceSn = ""
        var deviceAppType = ""

        // 下面三个参数是,不能对外公开的
        var deviceInitString = ""
        var deviceTMSAddress = ""
        var deviceDid = ""
    }



}