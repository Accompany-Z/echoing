package org.jeecg.modules.order.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.UnsupportedEncodingException;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2024-08-12
 * @Version: V1.0
 */
@Data
@TableName("tally_order")
@ApiModel(value = "tally_order对象", description = "订单")
public class TallyOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 父id
     */
    @Excel(name = "父id", width = 15)
    @ApiModelProperty(value = "父id")
    private java.lang.String pid;
    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名", width = 15)
    @ApiModelProperty(value = "客户姓名")
    @Dict(dictTable = "sys_category", dicCode = "id", dicText = "name")
    private java.lang.String clientName;
    /**
     * 订单名称
     */
    @Excel(name = "订单名称", width = 15)
    @ApiModelProperty(value = "订单名称")
    private java.lang.String orderName;
    /**
     * 订单类型
     */
    @Excel(name = "订单类型", width = 15)
    @ApiModelProperty(value = "订单类型")
    @Dict(dicCode = "tally_type")
    private java.lang.String orderType;
    /**
     * 订单总量
     */
    @Excel(name = "订单总量", width = 15)
    @ApiModelProperty(value = "订单总量")
    private java.math.BigDecimal orderTotal;
    /**
     * 订单余量
     */
    @Excel(name = "订单余量", width = 15)
    @ApiModelProperty(value = "订单余量")
    private java.math.BigDecimal orderMargin;
    /**
     * 规格型号（直径）
     */
    @Excel(name = "规格型号（直径）", width = 15)
    @ApiModelProperty(value = "规格型号（直径）")
    private java.math.BigDecimal diameter;
    /**
     * 单价/吨
     */
    @Excel(name = "单价/吨", width = 15)
    @ApiModelProperty(value = "单价/吨")
    private java.math.BigDecimal price;
    /**
     * 总金额
     */
    @Excel(name = "总金额", width = 15)
    @ApiModelProperty(value = "总金额")
    private java.math.BigDecimal totalMoney;
    /**
     * 付款金额
     */
    @Excel(name = "付款金额", width = 15)
    @ApiModelProperty(value = "付款金额")
    private java.math.BigDecimal paymentAmount;
    /**
     * 付款方式
     */
    @Excel(name = "付款方式", width = 15)
    @ApiModelProperty(value = "付款方式")
    private java.lang.String paymentMethod;
    /**
     * 税款
     */
    @Excel(name = "税款", width = 15)
    @ApiModelProperty(value = "税款")
    private java.math.BigDecimal tax;
    /**
     * 税点
     */
    @Excel(name = "税点", width = 15)
    @ApiModelProperty(value = "税点")
    private java.math.BigDecimal taxPoint;
    /**
     * 开始日期
     */
    @Excel(name = "开始日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date startTime;
    /**
     * 完结日期
     */
    @Excel(name = "完结日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完结日期")
    private java.util.Date finishTime;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**
     * 是否删除
     */
    @Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private java.lang.String delFlag;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否有子节点")
    private java.lang.String hasChild;
}
