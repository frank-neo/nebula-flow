package com.lvmama.nebula.bean.vo;

import lombok.Data;

import java.util.Date;

/**
 * create by adolph on 2018/2/2
 */
@Data
public class ContractAttach
{
    /**
     * ID
     */
    private int id;

    /**
     * 附件类型
     */
    private String type;

    /**
     * 结算类型
     */
    private String settlementType;

    /**
     * 发票内容
     */
    private String invoiceContent;

    /**
     * 甲方签约时间
     */
    private String partyaSignTime;

    /**
     * 乙方签约时间
     */
    private String partybSignTime;

    /**
     * 甲方邮箱
     */
    private String partyaEmail;

    /**
     * 乙方邮箱
     */
    private String partybEmail;

    /**
     * 甲方QQ
     */
    private String partyaQq;

    /**
     * 乙方QQ
     */
    private String partybQq;
}
