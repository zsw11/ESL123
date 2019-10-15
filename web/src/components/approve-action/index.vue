<template>
  <span>
    <el-button v-bind="$attrs" @click="show"><slot>审批</slot></el-button>
    <el-dialog class="approve-dialog"
      append-to-body
      :title="'审批'"
      :close-on-click-modal="false"
      :visible.sync="visible">
      <el-form ref="dataForm" :model="formData" :rules="rules">
        <el-radio-group v-model="formData.status" size="mini">
          <el-radio label="approved" border>通过</el-radio>
          <el-radio label="rejected" border>拒绝</el-radio>
        </el-radio-group>
        <el-form-item prop="approveRemark" label="审批备注">
          <el-input type="textarea" :rows="5" v-model="formData.approveRemark"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirm()">确定</el-button>
        <el-button @click="hide">取消</el-button>
      </span>
    </el-dialog>
  </span>
</template>

<script>
export default {
  name: 'ApproveAction',
  props: {
    object: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      visible: false,
      formData: {
        status: 'approved',
        approveRemark: null
      }
    }
  },
  computed: {
    rules () {
      return this.formData.status === 'rejected' ? {
        approveRemark: [
          { required: true, message: '请输入备注', trigger: 'blur' }
        ]
      } : {}
    }
  },
  methods: {
    show () {
      this.visible = true
    },
    hide () {
      this.visible = false
    },
    confirm () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$emit('confirm', this.object, this.formData)
          this.hide()
        }
      })
    }
  }
}
</script>
