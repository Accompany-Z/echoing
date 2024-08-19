<template>
  <a-modal :title="title" :width="width" :visible="visible" switchFullscreen @cancel="close" cancelText="关闭"
    :footer="null">
    <a-descriptions bordered>
      <a-descriptions-item label="订单名称" :span="4">
        {{ model.orderName }}
      </a-descriptions-item>
      <a-descriptions-item label="订单类型" :span="2">
        {{ model.orderType_dictText }}
      </a-descriptions-item>
      <a-descriptions-item label="订单状态" :span="2">
        <a-badge v-if="!model.finishTime" status="processing" text="进行中" />
        <a-badge v-else status="success" text="已完成" />
      </a-descriptions-item>
      <a-descriptions-item label="客户姓名" :span="2">
        {{ model.clientName_dictText }}
      </a-descriptions-item>
      <a-descriptions-item label="订单总量" :span="2">
        {{ model.orderTotal }}
      </a-descriptions-item>
      <a-descriptions-item label="订单余量" :span="2">
        {{ model.orderMargin }}
      </a-descriptions-item>
      <a-descriptions-item label="规格型号" :span="2">
        {{ model.diameter }}
      </a-descriptions-item>
      <a-descriptions-item label="单价/吨" :span="2">
        {{ model.price }}
      </a-descriptions-item>
      <a-descriptions-item label="总金额" :span="2">
        {{ model.totalMoney }}
      </a-descriptions-item>
      <a-descriptions-item label="付款金额" :span="2">
        {{ model.paymentAmount }}
      </a-descriptions-item>
      <a-descriptions-item label="付款方式" :span="2">
        {{ model.paymentMethod }}
      </a-descriptions-item>
      <a-descriptions-item label="税款" :span="2">
        {{ model.tax }}
      </a-descriptions-item>
      <a-descriptions-item label="税点" :span="2">
        {{ model.taxPoint }}
      </a-descriptions-item>
      <a-descriptions-item label="开始日期" :span="2">
        {{ model.startTime }}
      </a-descriptions-item>
      <a-descriptions-item label="完结日期" :span="2">
        {{ model.finishTime }}
      </a-descriptions-item>
      <a-descriptions-item label="备注" :span="4">
        <span v-if="model.remark" :title="model.remark">{{ model.remark.length > 45 ? model.remark.substr(0, 45) + '...'
          : model.remark
          }}</span>
      </a-descriptions-item>
    </a-descriptions>
  </a-modal>
</template>

<script>

export default {
  name: "TallyOrderDetial",
  components: {
  },
  data() {
    return {
      title: "详情",
      width: 800,
      visible: false,
      model: {
      },
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    list(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
    },
    close() {
      this.$emit('close');
      this.visible = false;
    },
  }
}
</script>