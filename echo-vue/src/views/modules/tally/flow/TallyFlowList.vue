<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="姓名">
              <j-category-select placeholder="请选择姓名" v-model="queryParam.operator" pcode="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属订单">
              <j-tree-select ref="treeSelect" placeholder="请选择所属订单" v-model="queryParam.orderId"
                dict="tally_order,order_name,id" pidField="pid" pidValue="0">
              </j-tree-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="流水类型">
              <j-dict-select-tag type="list" v-model="queryParam.orderType" dictCode="tally_flow_type"
                placeholder="请选择流水类型" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="操作类型">
              <j-dict-select-tag type="list" v-model="queryParam.operatorType" dictCode="operator_type"
                placeholder="请选择操作类型" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择日期"
                v-model="queryParam.dateTime" style="width: 100%"></j-date>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" icon="plus" style="margin-left: 8px">新增</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('订单流水')"
                style="margin-left: 8px">导出</a-button>
              <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                style="margin-left: 8px" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
              </a-upload>
              <a-dropdown v-if="selectedRowKeys.length > 0" style="margin-left: 8px">
                <a-menu slot="overlay">
                  <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
              </a-dropdown>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
          selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" size="middle" :scroll="{ x: true }" bordered rowKey="id" :columns="columns"
        :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" class="j-table-force-nowrap"
        @change="handleTableChange">

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

    <tally-flow-modal ref="modalForm" @ok="modalFormOk"></tally-flow-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TallyFlowModal from './modules/TallyFlowModal'

export default {
  name: 'TallyFlowList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TallyFlowModal
  },
  data() {
    return {
      description: '订单流水管理页面',
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
          title: '流水类型',
          align: "center",
          dataIndex: 'orderType_dictText'
        },
        {
          title: '操作类型',
          align: "center",
          dataIndex: 'operatorType_dictText'
        },
        {
          title: '操作人',
          align: "center",
          dataIndex: 'operator_dictText'
        },
        {
          title: '规格型号（Φ）',
          align: "center",
          dataIndex: 'diameter'
        },
        {
          title: '重量',
          align: "center",
          dataIndex: 'weight'
        },
        {
          title: '日期',
          align: "center",
          dataIndex: 'dateTime'
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
        list: "/flow/list",
        delete: "/flow/delete",
        deleteBatch: "/flow/deleteBatch",
        exportXlsUrl: "/flow/exportXls",
        importExcelUrl: "flow/importExcel",
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
      fieldList.push({ type: 'string', value: 'orderId', text: '订单', dictCode: '' })
      fieldList.push({ type: 'double', value: 'orderType', text: '流水类型（1：拔丝；2:冷镦）', dictCode: '' })
      fieldList.push({ type: 'string', value: 'operator', text: '操作人', dictCode: '' })
      fieldList.push({ type: 'double', value: 'operatorType', text: '操作类型（1：提货/2：出货）', dictCode: '' })
      fieldList.push({ type: 'datetime', value: 'dateTime', text: '日期' })
      fieldList.push({ type: 'double', value: 'diameter', text: '规格型号（直径）', dictCode: '' })
      fieldList.push({ type: 'double', value: 'weight', text: '重量', dictCode: '' })
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