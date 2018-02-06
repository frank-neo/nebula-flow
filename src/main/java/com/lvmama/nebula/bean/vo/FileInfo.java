package com.lvmama.nebula.bean.vo;

import lombok.Data;

import java.sql.Blob;

/**
 * create by adolph on 2018/2/5
 */
@Data
public class FileInfo
{
    private String fileName;
    private String fileType;
    private Blob fileContent;
}
