package com.chaos.eurekaproducer.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright (c) 2017<br>
 */
@Data
public class StoreTransactionLogQuery implements Serializable {
    private static final long serialVersionUID = 5454155825314635342L;

    /**
     * 主键ID  db_column: id
     */
    private Long id;
    /**
     * 集团ID  db_column: groupID
     */
    private Long groupID;
    /**
     * 所属仓库ID  db_column: houseId
     */
    private Long houseId;
    /**
     * 仓库名称  db_column: houseName
     */
    private String houseName;
    /**
     * 业务单号
     */
    private String businessNo;
    /**
     * 品项条形码  db_column: barcode
     */
    private String barcode;
    /**
     * 品项名称  db_column: goodsName
     */
    private String goodsName;
    /**
     * 品项规格  db_column: goodsDesc
     */
    private String goodsDesc;
    /**
     * 品项总数量  db_column: totalNum
     */
    private BigDecimal totalNum;
    /**
     * 初始批次数量  db_column: beforeNum
     */
    private BigDecimal beforeNum;
    /**
     * 初始总数量  db_column: beforeTotalNum
     */
    private BigDecimal beforeTotalNum;
    /**
     * 剩余批次数量  db_column: afterNum
     */
    private BigDecimal afterNum;
    /**
     * 剩余总数量  db_column: afterTotalNum
     */
    private BigDecimal afterTotalNum;
    /**
     * 品项标准单位  db_column: standardUnit
     */
    private String standardUnit;
    /**
     * 生产批次  db_column: batch
     */
    private String batch;
    /**
     * 系统批次
     */
    private String batchSys;
    /**
     * 生产日期  db_column: productionDate
     */
    private Long productionDate;
    /**
     * 保质期  db_column: shelfLife
     */
    private Long shelfLife;
    /**
     * 货位号/货位名称 由区域+排+层+位号组成  db_column: location
     */
    private String location;
    /**
     * 交易类型 1、收货 2、上架 3、上架盘亏 4、拣货 5、分拣 6、发货 7、预占 8、解预占 9、拣货盘亏 10、分拣盘亏 11、盘点盘盈 12、盘点盘亏 16、订单加工打包 18、预加工打包
     * 21、订单加工盘盈 24、核货盘亏 26、越库分拣盘亏 28、报残盘亏 30、报损盘亏 31、报损出库 41、退供拣货下架 43、退供拣货上架 45、退供出库 47、退供盘亏'
     * 51、批属性变更 61、客退收货 63、客退收货上架 71、移位下架 73、移位上架 81、调拨拣货 82、调拨出库 83、调拨核货盘亏 84、调拨收货 85、调拨收货上架 86、调拨上架盘亏'
     * 87、品项期初 91、残转正 92、补货下架  93、补货上架 94、补货盘亏'
     * db_column: status
     */
    private Integer status;
    /**
     * 交易类型编码名称  db_column: statusStr
     */
    private String statusStr;
    /**
     * 库存交易ID 一次订单可能拆分成多个子订单  db_column: parentId
     */
    private Long parentId;
    /**
     * 删除标志 1、未删除 2、删除  db_column: action
     */
    private Integer action = 1;
    /**
     * 操作人  db_column: actionBy
     */
    private String actionBy;
    /**
     * 操作时间  db_column: actionTime
     */
    private Long actionTime;
    /**
     * 创建人  db_column: createBy
     */
    private String createBy;
    /**
     * 创建时间  db_column: createTime
     */
    private Long createTime;
    /**
     * 货主id
     */
    private Long ownerId;
    /**
     * 货主名称
     */
    private String ownerName;
    /**
     * 品项三级分类id  db_column: goodsCategoryId
     */
    private Long goodsCategoryId;
    /**
     * 品项三级分类名称  db_column: goodsCategoryName
     */
    private String goodsCategoryName;
    /**
     * 品项一级分类id  db_column: goodsCategory1Id
     */
    private Long goodsCategory1Id;
    /**
     * 品项一级分类名称  db_column: goodsCategory1Name
     */
    private String goodsCategory1Name;
    /**
     * 品项二级分类id  db_column: goodsCategory2Id
     */
    private Long goodsCategory2Id;
    /**
     * 品项二级分类名称  db_column: goodsCategory2Name
     */
    private String goodsCategory2Name;

    /**
     * 是否越库品，0不是，1是
     */
    private Integer isCross;
    /**
     * 是否合格品 1、正品 2、残品 3、报损 db_column: isQualified
     */
    private Integer isQualified;
    
    private String isQualifiedStr;

    private String inboundOrg;
    /**
     * 重量 kg db_column: weight
     */
    private BigDecimal weight = BigDecimal.ZERO;
    /**
     * 体积 cm³ db_column: volume
     */
    private BigDecimal volume = BigDecimal.ZERO;
    /**
     * 重泡比 kg/m³ db_column: ratio
     */
    private BigDecimal ratio = BigDecimal.ZERO;

    private int pageNo;

    private int pageSize;

}

