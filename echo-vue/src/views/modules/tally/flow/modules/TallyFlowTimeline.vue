<template>
  <a-drawer :title="title" placement="right" :closable="false" :visible="visible" @close="close" :width="width">
    <a-list class="demo-loadmore-list" :loading="loading" item-layout="horizontal" :data-source="recordList">
      <div v-if="showLoadingMore" slot="loadMore"
        :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }">
        <a-spin v-if="loadingMore" />
        <a-button v-else @click="onLoadMore">
          加载更多
        </a-button>
      </div>
      <a-list-item slot="renderItem" slot-scope="item, index">
        <a-list-item-meta>
          <a slot="title">{{ item.operator }}</a>

          <div slot="description">
            <p class="item-description">时间: {{ item.dateTime }}</p>
            <p class="item-description">类型: {{ item.operatorType_dictText }}</p>
            <p class="item-description">重量: {{ item.weight }}</p>
            <p class="item-description">备注: {{ item.remark }}</p>
          </div>

          <a-avatar v-if="item.operatorType === 1" slot="avatar"
            :src="require('@/assets/tally/wired-wooden-box.png')" />
          <a-avatar v-if="item.operatorType === 2" slot="avatar"
            :src="require('@/assets/tally/wired-truck-delivery.png')" />

        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </a-drawer>
</template>
<script>

export default {
  name: 'TallyFlowTimeline',
  components: {
  },
  data() {
    return {
      title: '',
      width: 520,
      record: {},
      recordList: [],
      loading: true,
      visible: false,
      loadingMore: false,
      showLoadingMore: true,
    }
  },
  mounted() {
  },
  methods: {
    list(record) {
      this.visible = true
      this.loading = false;

      this.record = record;

      this.recordList = [
        { operatorType: 1, operator: '张某', dateTime: "2024-08-08 10:00:00", weight: 20, operatorType_dictText: "提货", remark: "" },
        { operatorType: 1, operator: '张某', dateTime: "2024-08-08 11:00:00", weight: 20, operatorType_dictText: "提货", remark: "订单名称客户姓名订单类型规格型号（直径）" },
        { operatorType: 2, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 1, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 2, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 1, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 2, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 1, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 2, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        { operatorType: 1, operator: '张某', dateTime: "2024-08-08 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
      ];
    },
    onLoadMore() {
      this.loadingMore = true;

      setTimeout(() => {
        this.recordList.push(...[
          { operatorType: 1, operator: '张某', dateTime: "2024-08-09 10:00:00", weight: 20, operatorType_dictText: "提货", remark: "" },
          { operatorType: 2, operator: '张某', dateTime: "2024-08-09 11:00:00", weight: 20, operatorType_dictText: "出货", remark: "订单名称客户姓名订单类型规格型号（直径）" },
          { operatorType: 2, operator: '张某', dateTime: "2024-08-09 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
          { operatorType: 1, operator: '张某', dateTime: "2024-08-09 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
          { operatorType: 2, operator: '张某', dateTime: "2024-08-09 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
          { operatorType: 2, operator: '张某', dateTime: "2024-08-09 12:00:00", weight: 20, operatorType_dictText: "出货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
          { operatorType: 1, operator: '张某', dateTime: "2024-08-09 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
          { operatorType: 1, operator: '张某', dateTime: "2024-08-09 12:00:00", weight: 20, operatorType_dictText: "提货", remark: "张经理20吨光球客户姓名订单类型规格型号（直径）开始日期完结日期" },
        ]);

        this.loadingMore = false;
        this.$nextTick(() => {
          window.dispatchEvent(new Event('resize'));
        });
      }, 1000)
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';

.item-description {
  margin-bottom: 1%;
}
</style>