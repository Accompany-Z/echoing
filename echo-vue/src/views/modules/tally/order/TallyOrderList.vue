<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单名称">
              <a-input placeholder="请输入订单名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开始日期">
              <j-date placeholder="请选择开始日期" v-model="queryParam.startDate"></j-date>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="完结日期">
                <j-date placeholder="请选择完结日期" v-model="queryParam.endDate"></j-date>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="规格型号">
                <j-dict-select-tag v-model="queryParam.type" dictCode="tally_order,type, type,del_flag = '0'"
                  placeholder="请选择规格型号" />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="客户姓名">
                <j-category-select v-model="queryParam.clientName" pcode="A01A01" placeholder="请选择客户姓名"
                  :multiple="false" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('订单信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
        @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
    selectedRowKeys.length
  }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns"
        :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt=""
            style="max-width:80px;font-size: 12px;font-style: italic;" />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <tally-order-modal ref="modalForm" @ok="modalFormOk"></tally-order-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TallyOrderModal from './modules/TallyOrderModal'

export default {
  name: 'TallyOrderList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TallyOrderModal
  },
  data() {
    return {
      description: '订单信息管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '订单名称',
          align: "center",
          dataIndex: 'name'
        },
        {
          title: '开始日期',
          align: "center",
          dataIndex: 'startDate',
          customRender: function (text) {
            return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '完结日期',
          align: "center",
          dataIndex: 'endDate',
          customRender: function (text) {
            return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '规格型号',
          align: "center",
          dataIndex: 'type_dictText'
        },
        {
          title: '单价/顿',
          align: "center",
          dataIndex: 'price'
        },
        {
          title: '订单总量',
          align: "center",
          dataIndex: 'orderTotal'
        },
        {
          title: '订单余量',
          align: "center",
          dataIndex: 'orderMargin'
        },
        {
          title: '总金额',
          align: "center",
          dataIndex: 'totalMoney'
        },
        {
          title: '付款金额',
          align: "center",
          dataIndex: 'payMent'
        },
        {
          title: '付款方式',
          align: "center",
          dataIndex: 'payType'
        },
        {
          title: '税款',
          align: "center",
          dataIndex: 'tax'
        },
        {
          title: '税点',
          align: "center",
          dataIndex: 'taxPoint'
        },
        {
          title: '客户姓名',
          align: "center",
          dataIndex: 'clientName_dictText'
        },
        {
          title: '备注',
          align: "center",
          dataIndex: 'remark'
        },
        {
          title: '是否删除',
          align: "center",
          dataIndex: 'delFlag'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: "/order/list",
        delete: "/order/delete",
        deleteBatch: "/order/deleteBatch",
        exportXlsUrl: "/order/exportXls",
        importExcelUrl: "order/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'name', text: '订单名称', dictCode: '' })
      fieldList.push({ type: 'date', value: 'startDate', text: '开始日期' })
      fieldList.push({ type: 'date', value: 'endDate', text: '完结日期' })
      fieldList.push({ type: 'double', value: 'type', text: '规格型号', dictCode: '' })
      fieldList.push({ type: 'double', value: 'price', text: '单价/顿', dictCode: '' })
      fieldList.push({ type: 'double', value: 'orderTotal', text: '订单总量', dictCode: '' })
      fieldList.push({ type: 'double', value: 'orderMargin', text: '订单余量', dictCode: '' })
      fieldList.push({ type: 'double', value: 'totalMoney', text: '总金额', dictCode: '' })
      fieldList.push({ type: 'double', value: 'payMent', text: '付款金额', dictCode: '' })
      fieldList.push({ type: 'string', value: 'payType', text: '付款方式', dictCode: '' })
      fieldList.push({ type: 'double', value: 'tax', text: '税款', dictCode: '' })
      fieldList.push({ type: 'double', value: 'taxPoint', text: '税点', dictCode: '' })
      fieldList.push({ type: 'string', value: 'clientName', text: '客户姓名', dictCode: '' })
      fieldList.push({ type: 'Text', value: 'remark', text: '备注', dictCode: '' })
      fieldList.push({ type: 'string', value: 'delFlag', text: '是否删除', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>