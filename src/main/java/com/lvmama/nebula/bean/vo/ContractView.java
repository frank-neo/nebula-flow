package com.lvmama.nebula.bean.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * create by adolph on 2018/2/1
 */
@Data
public class ContractView
{
    /**
     * 合同ID
     */
    private Integer id;

    /**
     * 分销商ID
     */
    private Long merchantId;

    /**
     * 分销商名
     */
    private String merchantName;

    /**
     * 商家账号
     */
    private String userName;

    /**
     * 合同类型：TANDARD,标准，STANDARD_INTEL，标准国际版，CATEGORY_SUPPLY，品类补充协议
     */
    private String type;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 签订日期
     */
    private String signTime;

    /**
     * 甲方名称
     */
    private String partyaName;

    /**
     * 地址
     */
    private String address;

    private String partyaServiceName;

    private String partyaServicePhone;

    private String partybServiceName;

    private String partybServicePhone;

    private String operateCategory;

    private String operateType;

    private String partyaTradePlatform;

    private String customerService;

    private double deposit;

    private String accountName;

    private String bankName;

    private String accountNum;

    private String invoiceTitle;

    private String invoiceBank;

    private String invoiceNum;

    private String invoiceAddress;

    private String tbshopName;

    private String tbshopUrl;

    private String startDate;


    private String endDate;


    private String kkContract;


    private String swift;


    private String valid;


    private String status;


    private Date createTime;


    private Date updateTime;


    private Long genetateFileId;


    private Long uploadFileId;


    private Long approvedFileId;


    private String uploadFileName;


    private String approvedFileName;

    private String attachs;

    private String invoiceAccountNum;

    /**
     * 补充协议列表
     */
    private List<ContractAttach> contractAttachList;
}
