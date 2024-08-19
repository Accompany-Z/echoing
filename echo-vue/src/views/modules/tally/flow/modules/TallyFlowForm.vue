<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="订单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderId">
              <j-tree-select ref="treeSelect" placeholder="请选择订单" v-model="model.orderId"
                dict="tally_order,order_name,id" pidField="pid" pidValue="0">
              </j-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流水类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderType">
              <j-dict-select-tag type="list" v-model="model.orderType" dictCode="tally_flow_type"
                placeholder="请选择流水类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="operator">
              <j-category-select placeholder="请选择姓名" v-model="model.operator" pcode="" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="操作类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="operatorType">
              <j-dict-select-tag type="list" v-model="model.operatorType" dictCode="operator_type"
                placeholder="请选择操作类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="重量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weight">
              <a-input-number v-model="model.weight" placeholder="请输入重量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dateTime">
              <j-date placeholder="请选择日期" v-model="model.dateTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号（Φ）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="diameter">
              <a-input-number v-model="model.diameter" placeholder="请输入规格型号（Φ）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" rows="4" placeholder="请输入备注" />
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
  name: 'TallyFlowForm',
  components: {
  },
  props: {
    info: {
      type: Object,
      default: {},
      required: false
    },
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
        orderId: '',
        orderType: '',
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
        add: "/flow/add",
        edit: "/flow/edit",
        queryById: "/flow/queryById"
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
    initData() {
      console.log('info', this.info);
      if (this.info) {
        this.model.orderId = this.info.id;
        this.model.orderType = this.info.type;
        this.model.dateTime = this.info.dateTime;
      }
    },
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      console.log('record', record);
      
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