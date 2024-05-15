package org.jeecg.modules.order.entity;

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
 * @Description: 订单信息
 * @Author: jeecg-boot
 * @Date:   2024-05-15
 * @Version: V1.0
 */
@Data
@TableName("tally_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="tally_order对象", description="订单信息")
public class TallyOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**订单名称*/
	@Excel(name = "订单名称", width = 15)
    @ApiModelProperty(value = "订单名称")
    private java.lang.String name;
	/**开始日期*/
	@Excel(name = "开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date startDate;
	/**完结日期*/
	@Excel(name = "完结日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "完结日期")
    private java.util.Date endDate;
	/**规格型号*/
	@Excel(name = "规格型号", width = 15)
    @ApiModelProperty(value = "规格型号")
    private java.lang.Double type;
	/**单价/顿*/
	@Excel(name = "单价/顿", width = 15)
    @ApiModelProperty(value = "单价/顿")
    private java.lang.Double price;
	/**订单总量*/
	@Excel(name = "订单总量", width = 15)
    @ApiModelProperty(value = "订单总量")
    private java.lang.Double orderTotal;
	/**订单余量*/
	@Excel(name = "订单余量", width = 15)
    @ApiModelProperty(value = "订单余量")
    private java.lang.Double orderMargin;
	/**总金额*/
	@Excel(name = "总金额", width = 15)
    @ApiModelProperty(value = "总金额")
    private java.lang.Double totalMoney;
	/**付款金额*/
	@Excel(name = "付款金额", width = 15)
    @ApiModelProperty(value = "付款金额")
    private java.lang.Double payMent;
	/**付款方式*/
	@Excel(name = "付款方式", width = 15)
    @ApiModelProperty(value = "付款方式")
    private java.lang.String payType;
	/**税款*/
	@Excel(name = "税款", width = 15)
    @ApiModelProperty(value = "税款")
    private java.lang.Double tax;
	/**税点*/
	@Excel(name = "税点", width = 15)
    @ApiModelProperty(value = "税点")
    private java.lang.Double taxPoint;
	/**客户姓名*/
	@Excel(name = "客户姓名", width = 15)
    @ApiModelProperty(value = "客户姓名")
    private java.lang.String clientName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**是否删除*/
	@Excel(name = "是否删除", width = 15)
    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private java.lang.String delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
}
