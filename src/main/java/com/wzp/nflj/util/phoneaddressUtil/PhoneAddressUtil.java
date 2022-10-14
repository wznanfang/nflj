package com.wzp.nflj.util.phoneaddressUtil;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

/**
 * @author zp.wei
 * @date 2022/9/22 11:22
 */
public class PhoneAddressUtil {

    //找出直辖市
    private final static String[] MUNICIPALITY = {"北京市", "天津市", "上海市", "重庆市"};
    //找出自治区
    private final static String[] AUTONOMOUS_REGION = {"新疆", "内蒙古", "西藏", "宁夏", "广西"};

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    //提供与电话号码相关的运营商信息
    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
    // 提供与电话号码有关的地理信息
    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    //中国大陆区号
    private final static int COUNTRY_CODE = 86;


    /**
     * 根据手机号返回手机号归属地，有可能为空
     *
     * @param phoneNumber
     * @return
     */
    public static String getPhoneAddress(String phoneNumber) {
        PhoneModel phoneModel = getPhoneModel(phoneNumber);
        if (phoneModel != null) {
            return phoneModel.getProvinceName() + "" + phoneModel.getCityName();
        } else {
            return "未知";
        }
    }

    /**
     * 根据手机号 获取封裝信息
     */
    public static PhoneModel getPhoneModel(String phoneNumber) {
        if (checkPhoneNumber(phoneNumber)) {
            String geo = getGeo(phoneNumber);
            PhoneModel phoneModel = new PhoneModel();
            String carrier = getCarrier(phoneNumber);
            phoneModel.setCarrier(carrier);
            // 直辖市
            for (String val : MUNICIPALITY) {
                if (geo.equals(val)) {
                    phoneModel.setProvinceName(val.replace("市", ""));
                    phoneModel.setCityName(val);
                    return phoneModel;
                }
            }
            // 自治区
            for (String val : AUTONOMOUS_REGION) {
                if (geo.startsWith(val)) {
                    phoneModel.setProvinceName(val);
                    phoneModel.setCityName(geo.replace(val, ""));
                    return phoneModel;
                }
            }
            // 其它
            String[] splitArr = geo.split("省");
            if (splitArr.length == 2) {
                phoneModel.setProvinceName(splitArr[0]);
                phoneModel.setCityName(splitArr[1]);
                return phoneModel;
            }
        }
        return null;
    }


    /**
     * 根据手机号 判断手机号是否有效
     *
     * @param phoneNumber
     * @return true-有效 false-无效
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        long phone = Long.parseLong(phoneNumber);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(COUNTRY_CODE);
        pn.setNationalNumber(phone);
        return phoneNumberUtil.isValidNumber(pn);
    }


    /**
     * 根据手机号 获取手机归属地
     */
    public static String getGeo(String phoneNumber) {
        long phone = Long.parseLong(phoneNumber);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(COUNTRY_CODE);
        pn.setNationalNumber(phone);
        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }


    /**
     * 根据手机号 判断手机运营商
     *
     * @param phoneNumber
     */
    public static String getCarrier(String phoneNumber) {
        long phone = Long.parseLong(phoneNumber);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(COUNTRY_CODE);
        pn.setNationalNumber(phone);
        // 返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }


}
