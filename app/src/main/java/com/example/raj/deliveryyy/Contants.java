package com.example.raj.deliveryyy;

/**
 * Created by uma on 6/2/2017.
 */
public class Contants {

    public static final String KEY_STATUS_CODE="statusCode";
    public static final String KEY_STATUS_DESC="statusDesc";
    public static final String KEY_ID_DESCRIPTION="idDescription";

    public static final String KEY_AWB_LIVEID="liveId";
    public static final String KEY_AWB_AWBNO="awbNo";
    public static final String KEY_AWB_CITYCODE="cityCode";
    public static final String KEY_AWB_SVCCODE="svcCode";
    public static final String KEY_AWB_SCANDTTIME="scanDtTime";
    public static final String KEY_AWB_STATUSCODE="statusCode";
    public static final String KEY_AWB_EMPCODE="empCode";
    public static final String KEY_AWB_EEMPCODE="eempCode";
    public static final String KEY_AWB_DELIVERYPRINTNO="deliveryPrintNo";
    public static final String KEY_AWB_PRINTORDER="printOrder";


    public static final  String BASE_URL="http://192.168.17.212:8080/backend-delivery/delivery/";
    public static String getBaseUrl()
    {
        return BASE_URL;
    }
    public static final String LOGIN_URL = "user/login";
    public static String getLoginUrl()
    {
        return getBaseUrl()+LOGIN_URL;
    }
    public static final String STATUS_URL = "ops/get/status/list";
    public static String getStatusUrl()
    {
        return getBaseUrl()+STATUS_URL;
    }
    public static final String AWB_URL = "ops/get/awbno/list/";
    public static String getAwbUrl()
    {
        return getBaseUrl()+AWB_URL ;
    }
    public static final String RELATION_URL ="user/get/relation/list";
    public static String getRelationUrl()
    {
        return getBaseUrl()+RELATION_URL;
    }
    public static final String ID_URL="user/get/id/list";
    public static String getIdUrl()
    {
        return getBaseUrl()+ID_URL;
    }
    public  static final String AWBDETAILS_URL="ops/delivery/awbno/";
    public static  String getAwbDetailsUrl()
    {
        return getBaseUrl()+AWBDETAILS_URL;
    }

    public  static final String SAVEALL_DETAILS_URL="ops/delivery/status";
    public static  String getSaveallDetailsUrl()
    {
        return getBaseUrl()+SAVEALL_DETAILS_URL;
    }
    public  static final String DELIVERED_REPORT_URL="ops/report/delivered/";
    public static  String getDeliveredReportUrl()
    {
        return getBaseUrl()+DELIVERED_REPORT_URL;
    }
    public  static final String UNDELIVERED_REPORT_URL="ops/report/undelivered/";
    public static  String getUndeliveredReportUrl()
    {
        return getBaseUrl()+UNDELIVERED_REPORT_URL;
    }
}
