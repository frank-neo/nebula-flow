package com.lvmama.nebula.flow;

import com.alibaba.fastjson.JSONObject;
import com.lvmama.nebula.bean.dto.StartMerchantContractDTO;
import com.lvmama.nebula.bean.vo.*;
import com.lvmama.utils.DateUtils;
import com.lvmama.utils.http.ApiService;
import com.lvmama.utils.http.HttpResult;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * create by adolph on 2018/1/26
 */
@RestController
@RequestMapping("/flow/contract/merchant")
public class FlowService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ApiService apiService;

    private static final String ACTIVITI_URL = "";

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public String startMerchantContractFlow(@RequestBody StartMerchantContractDTO startMerchantContract) throws IOException {

        JSONObject bizJson = new JSONObject();

        String deploymentId = "";
        String businessKey = "merchantContract#" + startMerchantContract.getContractId();
        String bizVal = bizJson.toJSONString();
        String starter = startMerchantContract.getStarter();


        // 组装参数，发起流程
        Map<String, Object> mapParameter = new HashMap<>();
        mapParameter.put("deploymentId", deploymentId);
        mapParameter.put("businessKey", businessKey);
        mapParameter.put("bizVal", bizVal);
        mapParameter.put("starter", starter);

        HttpResult result = apiService.doPost(ACTIVITI_URL, mapParameter);

        if (result.getStatus() == 200)
            return "success";
        return "fail";
    }


    /**
     * 获取合同文件
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public void getMerchantContractFile(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String merchantId = request.getParameter("merchantId");
        String contractId = request.getParameter("contractId");

        if (contractId == null) {
            response.setStatus(404);
            return;
        }

        String sql = "select pic_name, pic_type, content from scm_corp_qual_pic where pic_id=" + contractId;

        FileInfo fileInfo = jdbcTemplate.query(sql, new ResultSetExtractor<FileInfo>() {
            @Override
            public FileInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    FileInfo file = new FileInfo();
                    file.setFileName(rs.getNString(1));
                    file.setFileType(rs.getString(2));
                    file.setFileContent(rs.getBlob(3));
                    return file;
                }
                return null;
            }
        });

        if (fileInfo != null) {
            response.setHeader("Content-Type", fileInfo.getFileType());
            response.setHeader("Content-Disposition", "attachment:filename=" + fileInfo.getFileName());
            response.setCharacterEncoding("utf-8");

            BufferedInputStream inputStream = new BufferedInputStream(fileInfo.getFileContent().getBinaryStream());

            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } else {
            response.setStatus(404);
        }
    }

    private static final String SELECT_CONTRACT_ATTACH = "SELECT id, settlement_type, invoice_content, partya_sign_time, partyb_sign_time, partya_email, partyb_email, partya_qq, partyb_qq, TYPE " +
            " FROM contract_attach WHERE contract_id=";

    private static final String SELECT_CONTRACT_SQL = "SELECT c.id, c.contract_no, c.user_name, c.type, tm.id, c.sign_time, " +
            " c.partya_name, c.operate_category, c.operate_type, c.account_name, " +
            " c.bank_name, c.account_num, c.invoice_title, c.invoice_bank, " +
            " c.deposit, c.invoice_address, c.invoice_num, c.start_date, c.end_date, c.invoice_account_num, c.operate_category, c.operate_type,  " +
            " tm.company_name " +
            " FROM contract c LEFT JOIN tnt_merchant tm ON c.merchant_id = tm.id" +
            " where c.id=";

    @RequestMapping(value = "/checkContractView", method = RequestMethod.GET)
    public ContractView checkContractView(int contractId) {
        if (contractId <= 0)
            return null;

        String sql = SELECT_CONTRACT_SQL + contractId;

        List<ContractView> contractViews = jdbcTemplate.query(sql, new ResultSetExtractor<List<ContractView>>() {
            @Override
            public List<ContractView> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<ContractView> list = new ArrayList<>();

                if (rs.next()) {
                    ContractView cv = new ContractView();

                    // TODO

                    cv.setId(rs.getInt("id"));
                    cv.setContractNo(rs.getString("contract_no"));
                    cv.setUserName(rs.getString("user_name"));
                    cv.setType(rs.getString("type"));
                    cv.setSignTime(DateUtils.dateFormatYYYYMMDDHHMMSS(rs.getTimestamp("sign_time")));
                    cv.setPartyaName(rs.getString("partya_name"));
                    cv.setOperateCategory(rs.getString("operate_category"));
                    cv.setOperateType(rs.getString("operate_type"));
                    cv.setAccountName(rs.getString("account_name"));
                    cv.setBankName(rs.getString("bank_name"));
                    cv.setAccountNum(rs.getString("account_num"));
                    cv.setInvoiceTitle(rs.getString("invoice_title"));
                    cv.setInvoiceBank(rs.getString("invoice_bank"));
                    cv.setDeposit(rs.getDouble("deposit"));
                    cv.setInvoiceAddress(rs.getString("invoice_address"));
                    cv.setInvoiceNum(rs.getString("invoice_num"));
                    cv.setStartDate(DateUtils.dateFormatYYYYMMDDHHMMSS(rs.getTimestamp("start_date")));
                    cv.setEndDate(DateUtils.dateFormatYYYYMMDDHHMMSS(rs.getTimestamp("end_date")));
                    cv.setInvoiceAccountNum(rs.getString("invoice_account_num"));
                    cv.setOperateCategory(rs.getString("operate_category"));
                    cv.setOperateType(rs.getString("operate_type"));
                    list.add(cv);
                }

                return list;
            }
        });

        if (contractViews.size() > 0) {
            ContractView contractView = contractViews.get(0);

            sql = SELECT_CONTRACT_ATTACH + contractView.getId();

            List<ContractAttach> contractAttaches = jdbcTemplate.query(sql, new ResultSetExtractor<List<ContractAttach>>() {
                @Override
                public List<ContractAttach> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<ContractAttach> list = new ArrayList<>();

                    while (rs.next()) {
                        ContractAttach ca = new ContractAttach();
                        ca.setId(rs.getInt("id"));
                        ca.setType(rs.getString("type"));

                        //处理settlement_type，invoice_content
                        if (rs.getString("settlement_type").equals(Settlement_type.NORMAL.getIndex())) {
                            ca.setSettlementType(Settlement_type.NORMAL.getName());
                        } else if (rs.getString("settlement_type").equals(Settlement_type.ORDER.getIndex())) {
                            ca.setSettlementType(Settlement_type.ORDER.getName());
                        } else {
                            ca.setSettlementType(Settlement_type.API.getName());
                        }

                        if (rs.getString("invoice_content").equals(Invoice_content.TICKET.getIndex())) {
                            ca.setInvoiceContent(Invoice_content.TICKET.getName());
                        } else if (rs.getString("invoice_content").equals(Invoice_content.TOUR.getIndex())) {
                            ca.setInvoiceContent(Invoice_content.TOUR.getName());
                        } else {
                            ca.setInvoiceContent(Invoice_content.HOTEL.getName());
                        }
                        ca.setPartyaSignTime(DateUtils.dateFormatYYYYMMDDHHMMSS(rs.getTimestamp("partya_sign_time")));
                        ca.setPartybSignTime(DateUtils.dateFormatYYYYMMDDHHMMSS(rs.getTimestamp("partyb_sign_time")));
                        ca.setPartyaEmail(rs.getString("partya_email"));
                        ca.setPartybEmail(rs.getString("partyb_email"));
                        ca.setPartyaQq(rs.getString("partya_qq"));
                        ca.setPartybQq(rs.getString("partyb_qq"));

                        list.add(ca);
                    }

                    return list;
                }
            });

            contractView.setContractAttachList(contractAttaches);

            return contractView;
        }

        return null;
    }

    // 结算方式
    enum Settlement_type {
        NORMAL("一般模式", "NORMAL"), API("API", "API"), ORDER("按单结算", "ORDER");

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        private String name;
        private String index;

        private Settlement_type(String name, String index) {
            this.name = name;
            this.index = index;
        }

    }

    // 发票内容
    enum Invoice_content {
        TICKET("门票服务费", "TICKET"), TOUR("旅游费", "TOUR"), HOTEL("住宿代理费", "HOTEL");

        private String name;
        private String index;
        private Invoice_content(String name, String index) {
            this.name = name;
            this.index = index;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }
    }

        private static final String H_FileAtt_SQL = "SELECT ID,MERCHANT_ID FROM CONTRACT WHERE ID =";
        private static final String Z_FileAtt_SQL = "SELECT MATERIAL_NAME,MATERIAL_URLL FROM TNT_USER_MATERIAL WHERE MERCHANT_ID =";

        @RequestMapping(value = "/FileAttDownload",method = RequestMethod.GET)
        public FileAtt FileAttDownload(int contractId){

            if (contractId <= 0)
                return null;

            String sql = H_FileAtt_SQL + contractId;

            List<FileAtt> fileAtts = jdbcTemplate.query(sql,new ResultSetExtractor<List<FileAtt>>(){

                @Override
                public List<FileAtt> extractData(ResultSet rs) throws SQLException, DataAccessException {

                    List<FileAtt> list = new ArrayList<>();

                    if(rs.next()){

                        FileAtt fileAtt = new FileAtt();

                        fileAtt.setContractId(rs.getInt("ID"));
                        fileAtt.setMerchantId(rs.getInt("MERCHANT_ID"));
                        list.add(fileAtt);
                    }

                    return list;
                }
            });

            if(fileAtts.size()>0){

                FileAtt fileAtt = fileAtts.get(0);

                sql = Z_FileAtt_SQL + fileAtt.getMerchantId();
                List<FileQul> FileQul = jdbcTemplate.query(sql,new ResultSetExtractor<List<FileQul>>(){

                    @Override
                    public List<FileQul> extractData(ResultSet rs) throws SQLException, DataAccessException {

                        List<FileQul> listh = new ArrayList<>();

                        while (rs.next()){
                            FileQul fileQul = new FileQul();
                            fileQul.setMaterialName(rs.getString("MATERIAL_NAME"));
                            fileQul.setMaterialUrll(rs.getString("MATERIAL_URLL"));
                            listh.add(fileQul);
                        }

                        return listh;
                    }

                });

                fileAtt.setFileQulList(FileQul);

                return fileAtt;

            }

            return null;
        }


}
