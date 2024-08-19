<template>
  <j-modal :title="title" :width="width" :visible="visible" :confirmLoading="confirmLoading" switchFullscreen
    @ok="handleOk" @cancel="handleCancel" cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item label="上级订单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pid" v-if="isPidField">
          <j-tree-select ref="treeSelect" placeholder="请选择上级订单" v-model="model.pid" dict="tally_order,order_name,id"
            pidField="pid" pidValue="0" hasChildField="has_child">
          </j-tree-select>
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="订单类型" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="orderType">
          <j-dict-select-tag type="list" v-model="model.orderType" dictCode="tally_type" placeholder="请选择订单类型" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="客户姓名" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="clientName">
          <j-category-select v-model="model.clientName" pcode="" placeholder="请选择客户姓名" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="订单名称" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="orderName">
          <a-input v-model="model.orderName" placeholder="请输入订单名称"></a-input>
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="订单总量" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="orderTotal">
          <a-input-number v-model="model.orderTotal" placeholder="请输入订单总量" style="width: 100%"
            :formatter="value => `${value} 吨`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\吨\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="订单余量" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="orderMargin">
          <a-input-number v-model="model.orderMargin" placeholder="请输入订单余量" style="width: 100%"
            :formatter="value => `${value} 吨`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\吨\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="规格型号" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="diameter">
          <a-input-number v-model="model.diameter" placeholder="请输入规格型号（直径）" style="width: 100%"
            :formatter="value => `Φ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\Φ\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="单价/吨" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="price">
          <a-input-number v-model="model.price" placeholder="请输入单价/吨" style="width: 100%"
            :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="总金额" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="totalMoney">
          <a-input-number v-model="model.totalMoney" placeholder="请输入总金额" style="width: 100%"
            :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="付款金额" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="paymentAmount">
          <a-input-number v-model="model.paymentAmount" placeholder="请输入付款金额" style="width: 100%"
            :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="付款方式" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="paymentMethod">
          <a-input v-model="model.paymentMethod" placeholder="请输入付款方式"></a-input>
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="税款" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="tax">
          <a-input-number v-model="model.tax" placeholder="请输入税款" style="width: 100%"
            :formatter="value => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
            :parser="value => value.replace(/\￥\s?|(,*)/g, '')" :step="0.1" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="税点" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="taxPoint">
          <a-input-number v-model="model.taxPoint" placeholder="请输入税点" style="width: 100%" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == '' || modeOperation == 'detail'" label="开始日期" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="startTime">
          <j-date placeholder="请选择开始日期" v-model="model.startTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%" />
        </a-form-model-item>
        <a-form-model-item v-if="modeOperation == 'edit' || modeOperation == 'detail'" label="完结日期" :labelCol="labelCol"
          :wrapperCol="wrapperCol" prop="finishTime">
          <j-date placeholder="请选择完结日期" v-model="model.finishTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%" />
        </a-form-model-item>
        <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
          <a-textarea v-model="model.remark" rows="4" placeholder="请输入备注" />
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </j-modal>
</template>

<script>

import { httpAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'
export default {
  name: "TallyOrderModal",
  components: {
  },
  data() {
    return {
      title: "操作",
      width: 800,
      visible: false,
      disableSubmit: false,
      model: {
        orderTotal: 0.0,
        orderMargin: 0.0,
        diameter: 0.0,
        price: 0.0,
        totalMoney: 0.0,
        paymentAmount: 0.0,
        tax: 0.0,
        taxPoint: 0.0,
        startTime: '',
        clientName: '',
        orderType: ''
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
      },
      expandedRowKeys: [],
      pidField: "pid",
      isPidField: false,
      modeOperation: ''
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add(obj) {
      if (obj) {
        this.isPidField = true
      }
      console.log('obj', this.isPidField);

      this.modelDefault.pid = ''
      this.edit(Object.assign(this.modelDefault, obj));
    },
    edit(record, type) {
      if (type) {
        this.modeOperation = type
      }
      if (record.pid && record.pid != 0) {
        console.log('pid', record.pid);

        this.isPidField = true
      }
      this.model = Object.assign({}, record);
      this.visible = true;
    },
    close() {
      this.$emit('close');
      this.visible = false;
      this.modeOperation = ''
      this.disableSubmit = false;
      this.$refs.form.clearValidate()
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            this.model.orderMargin = this.model.orderTotal;
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          if (this.model.id && this.model.id === this.model[this.pidField]) {
            that.$message.warning("父级节点不能选择自己");
            that.confirmLoading = false;
            return;
          }
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              this.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
            that.close();
          })
        } else {
          return false
        }
      })
    },
    handleCancel() {
      this.close()
    },
    handleCategoryChange(value, backObj) {
      this.model = Object.assign(this.model, backObj);
    },
    submitSuccess(formData, flag) {
      if (!formData.id) {
        let treeData = this.$refs.treeSelect.getCurrTreeData()
        this.expandedRowKeys = []
        this.getExpandKeysByPid(formData[this.pidField], treeData, treeData)
        this.$emit('ok', formData, this.expandedRowKeys.reverse());
      } else {
        this.$emit('ok', formData, flag);
      }
    },
    getExpandKeysByPid(pid, arr, all) {
      if (pid && arr && arr.length > 0) {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].key == pid) {
            this.expandedRowKeys.push(arr[i].key)
            this.getExpandKeysByPid(arr[i]['parentId'], all, all)
          } else {
            this.getExpandKeysByPid(pid, arr[i].children, all)
          }
        }
      }
    }


  }
}
</script>