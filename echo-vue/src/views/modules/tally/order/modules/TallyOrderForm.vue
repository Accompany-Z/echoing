<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="订单名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入订单名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startDate">
              <j-date placeholder="请选择开始日期" v-model="model.startDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="完结日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endDate">
              <j-date placeholder="请选择完结日期" v-model="model.endDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <a-input-number v-model="model.type" placeholder="请输入规格型号" style="width: 100%" :min="0" :max="1000"
                :step="0.1" :formatter="value => `${value} Φ`" :parser="value => value.replace(' Φ', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="单价/吨" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="price">
              <a-input-number v-model="model.price" placeholder="请输入单价/吨" style="width: 100%" :min="0" :max="1000"
                :step="0.1" :formatter="value => `${value} 吨`" :parser="value => value.replace(' 吨', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="订单总量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderTotal">
              <a-input-number v-model="model.orderTotal" placeholder="请输入订单总量" style="width: 100%" :step="0.1"
                :defaultValue="0" :formatter="value => `${value} 吨`" :parser="value => value.replace(' 吨', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="订单余量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderMargin">
              <a-input-number v-model="model.orderMargin" placeholder="请输入订单余量" style="width: 100%" :step="0.1"
                :formatter="value => `${value} 吨`" :parser="value => value.replace(' 吨', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="总金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalMoney">
              <a-input-number v-model="model.totalMoney" placeholder="请输入总金额" style="width: 100%" :step="0.1"
                :formatter="value => `${value} 万元`" :parser="value => value.replace(' 万元', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="付款金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payMent">
              <a-input-number v-model="model.payMent" placeholder="请输入付款金额" style="width: 100%" :step="0.1"
                :formatter="value => `${value} 万元`" :parser="value => value.replace(' 万元', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="付款方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payType">
              <a-input v-model="model.payType" placeholder="请输入付款方式"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="税点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taxPoint">
              <a-input-number v-model="model.taxPoint" placeholder="请输入税点" style="width: 100%" :step="0.1"
                :formatter="value => `${value} %`" :parser="value => value.replace(' %', '')" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="客户姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clientName">
              <j-category-select v-model="model.clientName" pcode="A01A01" placeholder="请选择客户姓名" :multiple="false" />
            </a-form-model-item>
          </a-col>

        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-model-item prop="remark">
              <a-textarea v-model="model.remark" rows="5" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'

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
    }
  },
  data() {
    return {
      model: {
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
  },
  methods: {
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
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