<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="客户姓名">
              <j-category-select placeholder="请选择客户姓名" v-model="queryParam.clientName" pcode="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单名称">
              <a-input placeholder="请输入订单名称" v-model="queryParam.orderName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="订单类型">
              <j-dict-select-tag type="list" v-model="queryParam.orderType" dictCode="tally_type"
                placeholder="请选择订单类型" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="规格型号（Φ）">
              <a-input placeholder="请输入规格型号（Φ）" v-model="queryParam.diameter"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开始日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始日期"
                v-model="queryParam.startTime" style="width: 100%"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="完结日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择完结日期"
                v-model="queryParam.finishTime" style="width: 100%"></j-date>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" icon="plus" style="margin-left: 8px">新增</a-button>
              <a-button type="primary" icon="download" @click="handleExportXls('订单')"
                style="margin-left: 8px">导出</a-button>
              <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                :action="importExcelUrl" @change="handleImportExcel" style="margin-left: 8px">
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
          selectedRowKeys.length
          }}</a> 项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table ref="table" size="middle" rowKey="id" class="j-table-force-nowrap" :scroll="{ x: true }"
        :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :expandedRowKeys="expandedRowKeys" @change="handleTableChange" @expand="handleExpand" v-bind="tableProps">
        <span v-if="record.remark" slot="remark" slot-scope="text, record" :title="record.remark">
          {{ record.remark.length > 15 ? record.remark.substr(0, 15) + '...' : record.remark }}
        </span>
        <span slot="status" slot-scope="text, record">
          <a-badge v-if="!record.finishTime" status="processing" text="进行中" />
          <a-badge v-else status="success" text="已完成" />
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="handleRecord(record)">记录</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleAddChild(record)">添加下级</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleEdit(record)">编辑</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleEdit(record, 'edit')">维护</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteNode(record.id)" placement="topLeft">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <tallyOrder-detial ref="detialForm"></tallyOrder-detial>
    <tallyOrder-modal ref="modalForm" @ok="modalFormOk"></tallyOrder-modal>

    <tallyFlow-timeline ref="timelineList"></tallyFlow-timeline>
  </a-card>
</template>

<script>

import { getAction, deleteAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TallyOrderModal from './modules/TallyOrderModal'
import TallyOrderDetial from './modules/TallyOrderDetial'
import TallyFlowTimeline from '../flow/modules/TallyFlowTimeline'

import { filterObj } from '@/utils/util';

export default {
  name: "TallyOrderList",
  mixins: [JeecgListMixin],
  components: {
    TallyOrderModal,
    TallyOrderDetial,
    TallyFlowTimeline
  },
  data() {
    return {
      description: '订单管理页面',
      // 表头
      columns: [
        {
          title: '订单名称',
          align: "left",
          dataIndex: 'orderName'
        },
        {
          title: '状态',
          align: "left",
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
        },
        {
          title: '客户姓名',
          align: "left",
          dataIndex: 'clientName_dictText',
        },
        {
          title: '订单类型',
          align: "left",
          dataIndex: 'orderType_dictText'
        },
        {
          title: '规格型号（Φ）',
          align: "left",
          dataIndex: 'diameter'
        },
        {
          title: '订单总量',
          align: "left",
          dataIndex: 'orderTotal'
        },
        {
          title: '订单余量',
          align: "left",
          dataIndex: 'orderMargin'
        },
        {
          title: '开始日期',
          align: "left",
          dataIndex: 'startTime'
        },
        {
          title: '完结日期',
          align: "left",
          dataIndex: 'finishTime'
        },
        {
          title: '备注',
          align: "left",
          dataIndex: 'remark',
          scopedSlots: { customRender: 'remark' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: {
        list: "/order/rootList",
        childList: "/order/childList",
        getChildListBatch: "/order/getChildListBatch",
        delete: "/order/delete",
        deleteBatch: "/order/deleteBatch",
        exportXlsUrl: "/order/exportXls",
        importExcelUrl: "order/importExcel",
      },
      expandedRowKeys: [],
      hasChildrenField: "hasChild",
      pidField: "pid",
      dictOptions: {},
      loadParent: false,
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
    tableProps() {
      let _this = this
      return {
        // 列表项是否可选择
        rowSelection: {
          selectedRowKeys: _this.selectedRowKeys,
          onChange: (selectedRowKeys) => _this.selectedRowKeys = selectedRowKeys
        }
      }
    }
  },
  methods: {
    loadData(arg) {
      if (arg == 1) {
        this.ipagination.current = 1
      }
      this.loading = true
      let params = this.getQueryParams()
      params.hasQuery = 'true'
      getAction(this.url.list, params).then(res => {
        if (res.success) {
          let result = res.result
          if (Number(result.total) > 0) {
            this.ipagination.total = Number(result.total)
            this.dataSource = this.getDataByResult(res.result.records)
            return this.loadDataByExpandedRows(this.dataSource)
          } else {
            this.ipagination.total = 0
            this.dataSource = []
          }
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
      })
    },
    // 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
    loadDataByExpandedRows(dataList) {
      if (this.expandedRowKeys.length > 0) {
        return getAction(this.url.getChildListBatch, { parentIds: this.expandedRowKeys.join(',') }).then(res => {
          if (res.success && res.result.records.length > 0) {
            //已展开的数据批量子节点
            let records = res.result.records
            const listMap = new Map();
            for (let item of records) {
              let pid = item[this.pidField];
              if (this.expandedRowKeys.join(',').includes(pid)) {
                let mapList = listMap.get(pid);
                if (mapList == null) {
                  mapList = [];
                }
                mapList.push(item);
                listMap.set(pid, mapList);
              }
            }
            let childrenMap = listMap;
            let fn = (list) => {
              if (list) {
                list.forEach(data => {
                  if (this.expandedRowKeys.includes(data.id)) {
                    data.children = this.getDataByResult(childrenMap.get(data.id))
                    fn(data.children)
                  }
                })
              }
            }
            fn(dataList)
          }
        })
      } else {
        return Promise.resolve()
      }
    },
    getQueryParams(arg) {
      //获取查询条件
      let sqp = {}
      let param = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      if (arg) {
        param = Object.assign(sqp, this.isorter, this.filters);
      } else {
        param = Object.assign(sqp, this.queryParam, this.isorter, this.filters);
      }
      if (JSON.stringify(this.queryParam) === "{}" || arg) {
        param.hasQuery = 'false'
      } else {
        param.hasQuery = 'true'
      }
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    searchReset() {
      //重置
      this.expandedRowKeys = []
      this.queryParam = {}
      this.loadData(1);
    },
    getDataByResult(result) {
      if (result) {
        return result.map(item => {
          //判断是否标记了带有子节点
          if (item[this.hasChildrenField] == '1') {
            let loadChild = { id: item.id + '_loadChild', name: 'loading...', isLoading: true }
            item.children = [loadChild]
          }
          return item
        })
      }
    },
    handleExpand(expanded, record) {
      // 判断是否是展开状态
      if (expanded) {
        this.expandedRowKeys.push(record.id)
        if (record.children.length > 0 && record.children[0].isLoading === true) {
          let params = this.getQueryParams(1);//查询条件
          params[this.pidField] = record.id
          params.hasQuery = 'false'
          params.superQueryParams = ""
          getAction(this.url.childList, params).then((res) => {
            if (res.success) {
              if (res.result.records) {
                record.children = this.getDataByResult(res.result.records)
                this.dataSource = [...this.dataSource]
              } else {
                record.children = ''
                record.hasChildrenField = '0'
              }
            } else {
              this.$message.warning(res.message)
            }
          })
        }
      } else {
        let keyIndex = this.expandedRowKeys.indexOf(record.id)
        if (keyIndex >= 0) {
          this.expandedRowKeys.splice(keyIndex, 1);
        }
      }
    },
    handleRecord(record) {
      console.log('record', record);
      let info = {
        id: record.id,
        type: record.orderType
      }
      this.$refs.timelineList.list(record);
      this.$refs.timelineList.title = "流水记录";
    },
    handleDetail(record) {
      this.$refs.detialForm.list(record);
    },
    handleEdit(record, type) {
      let title = "编辑";
      if (type) {
        title = "维护";
      }
      this.$refs.modalForm.edit(record, type);
      this.$refs.modalForm.title = title;
      this.$refs.modalForm.disableSubmit = false;
    },
    handleAddChild(record) {
      this.loadParent = true
      let obj = {}
      obj[this.pidField] = record['id']
      this.$refs.modalForm.add(obj);
    },
    handleDeleteNode(id) {
      if (!this.url.delete) {
        this.$message.error("请设置url.delete属性!")
        return
      }
      var that = this;
      deleteAction(that.url.delete, { id: id }).then((res) => {
        if (res.success) {
          that.loadData(1)
        } else {
          that.$message.warning(res.message);
        }
      });
    },
    batchDel() {
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
        return false;
      } else {
        let ids = "";
        let that = this;
        that.selectedRowKeys.forEach(function (val) {
          ids += val + ",";
        });
        that.$confirm({
          title: "确认删除",
          content: "是否删除选中数据?",
          onOk: function () {
            that.handleDeleteNode(ids)
            that.onClearSelected();
          }
        });
      }
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'pid', text: '父id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'clientName', text: '客户姓名' })
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
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>