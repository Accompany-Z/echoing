<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="是否子订单" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-model="model.isRoot">
                <a-radio :value="1">是</a-radio>
                <a-radio :value="2">否</a-radio>
              </a-radio-group> </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item v-if="model.isRoot === 1" label="上级订单" :labelCol="labelCol" :wrapperCol="wrapperCol"
              prop="parentId" required>
              <j-dict-select-tag v-model="model.parentId" dictCode="tally_order,id, order_name,del_flag = '0'"
                placeholder="请选择上级订单" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="客户姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clientName" required>
              <j-category-select v-model="model.clientName" pcode="A01A01" placeholder="请选择客户姓名" :multiple="false" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="订单名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderName" required>
              <a-input v-model="model.orderName" placeholder="请输入订单名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="订单类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderType" required>
              <j-dict-select-tag v-model="model.orderType" dictCode="tally_type" placeholder="请选择订单类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="订单总量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderTotal" required>
              <a-input-number v-model="model.orderTotal" placeholder="请输入订单总量" style="width: 100%"
                :formatter="value => `${value} 吨`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\吨\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="订单余量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderMargin">
              <a-input-number v-model="model.orderMargin" placeholder="请输入订单余量" style="width: 100%"
                :formatter="value => `${value} 吨`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\吨\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="diameter" required>
              <a-input-number v-model="model.diameter" placeholder="请输入规格型号（直径）" style="width: 100%"
                :formatter="value => `Φ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\Φ\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="单价/吨" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="price" required>
              <a-input-number v-model="model.price" placeholder="请输入单价/吨" style="width: 100%"
                :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="总金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalMoney">
              <a-input-number v-model="model.totalMoney" placeholder="请输入总金额" style="width: 100%"
                :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="付款金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="paymentAmount">
              <a-input-number v-model="model.paymentAmount" placeholder="请输入付款金额" style="width: 100%"
                :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="付款方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="paymentMethod">
              <a-input v-model="model.paymentMethod" placeholder="请输入付款方式"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="税款" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tax">
              <a-input-number v-model="model.tax" placeholder="请输入税款" style="width: 100%"
                :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="税点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taxPoint">
              <a-input-number v-model="model.taxPoint" placeholder="请输入税点" style="width: 100%" :step="0.1" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="!isMaintain">
            <a-form-model-item label="开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startTime" required>
              <j-date placeholder="请选择开始日期" v-model="model.startTime" :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-show="isMaintain">
            <a-form-model-item label="完结日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="finishTime">
              <j-date placeholder="请选择完结日期" v-model="model.finishTime" :show-time="true"
                date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'

export default {
  name: 'TallyOrderForm',
  components: {
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    //维护字段
    isMaintain: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      model: {
        isRoot: 1,
        orderTotal: 0.0,
        orderMargin: 0.0,
        diameter: 0.0,
        price: 0.0,
        totalMoney: 0.0,
        paymentAmount: 0.0,
        tax: 0.0,
        taxPoint: 0.0,
        startTime: ''
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        parentId: { required: true, message: '请选择父订单', trigger: ['change', 'blur'] },
        clientName: { required: true, message: '请选择客户', trigger: ['change', 'blur'] },
        orderName: { required: true, message: '请输入订单名称', trigger: ['change', 'blur'] },
        orderType: { required: true, message: '请选择订单类型', trigger: ['change', 'blur'] },
        orderTotal: { required: true, message: '请输入订单总量', trigger: ['change', 'blur'] },
        diameter: { required: true, message: '请输入规格型号（直径）', trigger: ['change', 'blur'] },
        price: { required: true, message: '请输入单价/吨', trigger: ['change', 'blur'] },
        startTime: { required: true, message: '请选择开始日期', trigger: ['change', 'blur'] },
      },
      url: {
        add: "/order/add",
        edit: "/order/edit",
        queryById: "/order/queryById"
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
    this.initData();
  },
  methods: {
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;

      console.log('isMaintain', this.isMaintain);

      if (record.parentId && record.parentId !== '0') {
        this.model.isRoot = 1
      } else {
        this.model.isRoot = 2
      }
    },
    initData() {
      this.model.startTime = this.getDateTime()
    },
    getDateTime() {
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      let day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      let hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
      let minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      let second = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()

      return year + '-' + month + '-' + day + ' ' + hours + ':' + minute + ':' + second
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
          })
        }

      })
    },
  }
}
</script>