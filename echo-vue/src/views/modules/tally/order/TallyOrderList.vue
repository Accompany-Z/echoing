<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="客户姓名">
              <j-category-select v-model="queryParam.clientName" pcode="A01A01" placeholder="请选择客户姓名"
                :multiple="false" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单名称">
              <a-input placeholder="请输入订单名称" v-model="queryParam.orderName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单类型">
              <j-dict-select-tag v-model="queryParam.orderType" dictCode="tally_type" placeholder="请选择订单类型" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="规格型号（直径）">
              <j-dict-select-tag v-model="queryParam.diameter"
                dictCode="tally_order,diameter,diameter,group by diameter" placeholder="请选择规格型号" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开始日期">
              <a-range-picker :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" class="query-group-cust"
                v-model="queryParam.ksrq" style="width: 100%;" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="完结日期">
              <a-range-picker :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" class="query-group-cust"
                v-model="queryParam.wjrq" style="width: 100%;" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('订单')">导出</a-button>
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
          <a @click="handleMaintain(record)">编辑</a>

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
      description: '订单管理页面',
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
          dataIndex: 'orderName'
        },
        {
          title: '客户姓名',
          align: "center",
          dataIndex: 'clientName'
        },
        {
          title: '订单类型',
          align: "center",
          dataIndex: 'orderType'
        },
        {
          title: '规格型号（直径）',
          align: "center",
          dataIndex: 'diameter'
        },
        {
          title: '开始日期',
          align: "center",
          dataIndex: 'startTime'
        },
        {
          title: '完结日期',
          align: "center",
          dataIndex: 'finishTime'
        },
        {
          title: '备注',
          align: "center",
          dataIndex: 'remark'
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
    handleMaintain(record) {
      let that = this
      this.$confirm({
        title: "操作",
        content: "请选择所要进行的操作。",
        okText: "维护",
        cancelText: "编辑",
        onOk: function () {
          that.$refs.modalForm.edit(record);
          that.$refs.modalForm.title = "维护";
          that.$refs.modalForm.isMaintain = true;
          that.$refs.modalForm.disableSubmit = false;
        },
        onCancel: function () {
          that.$refs.modalForm.edit(record);
          that.$refs.modalForm.title = "维护";
          that.$refs.modalForm.isMaintain = false;
          that.$refs.modalForm.disableSubmit = false;
        }
      });
    },
    handleDetail(record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.isMaintain = true;
      this.$refs.modalForm.disableSubmit = true;
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'parentId', text: '父id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'clientName', text: '客户姓名', dictCode: '' })
      fieldList.push({ type: 'string', value: 'orderName', text: '订单名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'orderType', text: '订单类型', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'orderTotal', text: '订单总量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'orderMargin', text: '订单余量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'diameter', text: '规格型号（直径）', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'price', text: '单价/吨', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'totalMoney', text: '总金额', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'paymentAmount', text: '付款金额', dictCode: '' })
      fieldList.push({ type: 'string', value: 'paymentMethod', text: '付款方式', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'tax', text: '税款', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'taxPoint', text: '税点', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'startTime', text: '开始日期' })
      fieldList.push({ type: 'datetime', value: 'finishTime', text: '完结日期' })
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