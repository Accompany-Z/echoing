package org.jeecg.modules.flow.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 订单流水
 * @Author: jeecg-boot
 * @Date: 2024-08-08
 * @Version: V1.0
 */
@Data
@TableName("tally_flow")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "tally_flow对象", description = "订单流水")
public class TallyFlow implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 订单
     */
    @Excel(name = "订单", width = 15)
    @ApiModelProperty(value = "订单")
    private String orderId;
    /**
     * 流水类型（1：拔丝；2:冷镦）
     */
    @Excel(name = "流水类型", width = 15)
    @ApiModelProperty(value = "流水类型")
    @Dict(dicCode = "tally_flow_type")
    private String orderType;
    /**
     * 操作人
     */
    @Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    @Dict(dictTable = "sys_category", dicCode = "id", dicText = "name")
    private String operator;
    /**
     * 操作类型（1：提货/2：出货）
     */
    @Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    @Dict(dicCode = "operator_type")
    private String operatorType;
    /**
     * 日期
     */
    @Excel(name = "日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "日期")
    private Date dateTime;
    /**
     * 规格型号（直径）
     */
    @Excel(name = "规格型号（直径）", width = 15)
    @ApiModelProperty(value = "规格型号（直径）")
    private Double diameter;
    /**
     * 重量
     */
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private Double weight;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 是否删除
     */
    @Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private String delFlag;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
